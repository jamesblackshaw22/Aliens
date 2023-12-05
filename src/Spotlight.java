import gmaths.*;

import java.nio.*;
import java.util.HashMap;

import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.awt.*;
import com.jogamp.opengl.util.glsl.*;
import com.jogamp.opengl.util.texture.*;
import com.jogamp.opengl.util.texture.awt.*;
import com.jogamp.opengl.util.texture.spi.JPEGImage;


public class Spotlight {
    private Camera camera;
    private Light[] lights;

    private double timeElapsed = 0;
    private ModelMultipleLights sphere, lightSphere;
    private SGNode spotlightRoot;

    private Vec3 lightPosition;
    private float xPosition = 0;

    private ModelNode[] allModels = new ModelNode[3];

    private TransformNode translateX, spotlightMoveTranslate;


    public Spotlight(GL3 gl, Boolean isSpotlightOn, Camera cameraIn, Light[] lightsIn, Texture t1, float xPos){
        this.camera = cameraIn;
        this.lights = lightsIn;
        this.xPosition = xPos;

        sphere = makeSphere(gl, t1);
        lightSphere = makeSphere(gl, t1);

        float spotlightBaseHeight = 20f;
        float spotlightBaseWidth = 0.6f;

        spotlightRoot = new NameNode("root");
        spotlightMoveTranslate = new TransformNode("spotlight transform", Mat4Transform.translate(xPosition,spotlightBaseHeight /2,0));

        //Spotlight Base Shape
        NameNode base = new NameNode("base");
        Mat4 m = Mat4Transform.scale(spotlightBaseWidth,spotlightBaseHeight,spotlightBaseWidth);
        TransformNode spotlightBaseTransform = new TransformNode("spotlight base transform", m);
        ModelNode spotlightBaseShape = new ModelNode("Sphere(spotlight base)", sphere);
        allModels[0] = spotlightBaseShape;

        NameNode head = new NameNode("head");
        m = new Mat4(1);
        // Assuming the pole's height is spotlightBaseHeight and we want to translate the head on top of it
        m = Mat4.multiply(m, Mat4Transform.translate(0, spotlightBaseHeight/2, 0));
        TransformNode moveHeadToTopOfPole = new TransformNode("head translate", m);

        // Apply rotation to tilt the head downward
        TransformNode rotateHeadDown = new TransformNode("head rotate down", Mat4Transform.rotateAroundZ(-45));
        TransformNode rotateHeadAround = new TransformNode("head rotate around", Mat4Transform.rotateAroundY(0));
        rotateHeadAround.update();

        //Spotlight Attachment Shape
        NameNode attachment = new NameNode("attachment");
        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.scale(3f,0.5f,0.5f));
        TransformNode attachmentTransform = new TransformNode("attachment transform", m);
        ModelNode attachmentShape = new ModelNode("Sphere(attachment)", sphere);
        allModels[1] = attachmentShape;

        //Spotlight Light Shape
        NameNode light = new NameNode("light");
        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.scale(0.5f,0.5f,0.5f));
        TransformNode lightTransform = new TransformNode("light transform", m);
        ModelNode lightShape = new ModelNode("Sphere(light)", sphere);
        allModels[2] = lightShape;

        //Spotlight light positioning
        TransformNode moveLightTranslate = new TransformNode("light translate", Mat4Transform.translate(1.3f,0,0));

        Vec3 initialLightPosition = new Vec3(xPosition + 1.3f,spotlightBaseHeight / 2,0);
        setLightPosition(initialLightPosition);

        spotlightRoot.addChild(spotlightMoveTranslate);
            spotlightMoveTranslate.addChild(base);
                base.addChild(spotlightBaseTransform);
                    spotlightBaseTransform.addChild(spotlightBaseShape);
                base.addChild(moveHeadToTopOfPole);
                    moveHeadToTopOfPole.addChild(rotateHeadAround);
                        rotateHeadAround.addChild(rotateHeadDown);
                            rotateHeadDown.addChild(head);
                                head.addChild(moveLightTranslate);
                                    moveLightTranslate.addChild(light);
                                        light.addChild(lightTransform);
                                            lightTransform.addChild(lightShape);
                                head.addChild(attachment);
                                    attachment.addChild(attachmentTransform);
                                        attachmentTransform.addChild(attachmentShape);
        //alienRoot.print(1,false);
        spotlightRoot.update();

    }

    private ModelMultipleLights makeSphere(GL3 gl, Texture t1){
        String name = "sphere";
        Mesh mesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
        Shader shader;

        shader = new Shader(gl, "vs_standard.txt", "fs_standard_m_1t.txt");


        Material material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
        Mat4 modelMatrix = Mat4.multiply(Mat4Transform.scale(4,4,4), Mat4Transform.translate(0,0.5f,0));
        return new ModelMultipleLights(name, mesh, modelMatrix, shader, material, lights, camera, t1);
    }

    public void render(GL3 gl){
        spotlightRoot.draw(gl);
    }

    public void setLightPosition(Vec3 lightPosition) {
        this.lightPosition = lightPosition;
    }

    public Vec3 getLightPosition() {
        return lightPosition;
    }

    public void updateLights(Light[]lights){
        this.lights = lights;
        for (ModelNode node : allModels) {
            node.updateLights(lights);
        }
    }
}
