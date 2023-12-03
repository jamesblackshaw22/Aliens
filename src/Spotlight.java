import gmaths.*;

import java.nio.*;
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
    private Light light;
    private Model sphere;
    private SGNode spotlightRoot;
    private float xPosition = 0;

    private TransformNode translateX, spotlightMoveTranslate;

    public Spotlight(GL3 gl, Camera cameraIn, Light lightIn, Texture t1, float xPos){


        this.camera = cameraIn;
        this.light = lightIn;
        this.xPosition = xPos;

        sphere = makeSphere(gl, t1);

        float spotlightBaseHeight = 20f;
        float spotlightBaseWidth = 0.6f;

        spotlightRoot = new NameNode("root");
        spotlightMoveTranslate = new TransformNode("spotlight transform", Mat4Transform.translate(xPosition,spotlightBaseHeight /2,0));


        //Spotlight Base Shape
        NameNode base = new NameNode("base");
        Mat4 m = Mat4Transform.scale(spotlightBaseWidth,spotlightBaseHeight,spotlightBaseWidth);
        TransformNode spotlightBaseTransform = new TransformNode("spotlight base transform", m);
        ModelNode spotlightBaseShape = new ModelNode("Sphere(spotlight base)", sphere);

        NameNode head = new NameNode("head");
        TransformNode moveHeadTranslation = new TransformNode("head translate", Mat4Transform.translate(1,spotlightBaseHeight /2,0));

        //Spotlight Attachment Shape
        NameNode attachment = new NameNode("attachment");
        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.scale(3f,0.5f,0.5f));
        TransformNode attachmentTransform = new TransformNode("attachment transform", m);
        ModelNode attachmentShape = new ModelNode("Sphere(attachment)", sphere);

        //Spotlight Light Shape
        NameNode light = new NameNode("light");
        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.scale(0.5f,0.5f,0.5f));
        TransformNode lightTransform = new TransformNode("light transform", m);
        ModelNode lightShape = new ModelNode("Sphere(light)", sphere);

        //Spotlight light positioning
        TransformNode moveLightTranslate = new TransformNode("light translate", Mat4Transform.translate(1.3f,0,0));

        spotlightRoot.addChild(spotlightMoveTranslate);
            spotlightMoveTranslate.addChild(base);
                base.addChild(spotlightBaseTransform);
                    spotlightBaseTransform.addChild(spotlightBaseShape);
                base.addChild(moveHeadTranslation);
                    moveHeadTranslation.addChild(head);
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

    private Model makeSphere(GL3 gl, Texture t1){
        String name = "sphere";
        Mesh mesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());

        Shader shader = new Shader(gl, "vs_standard.txt", "fs_standard_1t.txt");

        Material material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
        Mat4 modelMatrix = Mat4.multiply(Mat4Transform.scale(4,4,4), Mat4Transform.translate(0,0.5f,0));
        return new Model(name, mesh, modelMatrix, shader, material, light, camera, t1);
    }

    public void render(GL3 gl){
        spotlightRoot.draw(gl);
    }

}
