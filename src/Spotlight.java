import gmaths.*;

import com.jogamp.opengl.*;
import com.jogamp.opengl.util.texture.*;


public class Spotlight {
    private Camera camera;
    private Light[] lights;

    private ModelMultipleLights sphere, lightSphere;
    private SGNode spotlightRoot;

    private Vec3 lightPosition;
    private float xPosition = 0;

    private ModelNode[] allModels = new ModelNode[3];

    private TransformNode translateX, spotlightMoveTranslate, rotateHeadAround;


    public Spotlight(GL3 gl, Boolean isSpotlightOn, Camera cameraIn, Light[] lightsIn, TextureLibrary textures, float xPos){
        this.camera = cameraIn;
        this.lights = lightsIn;
        this.xPosition = xPos;

        Material spotlightBase = new Material(new Vec3(0.3f,0.3f,0.3f),new Vec3(0.2f, 0.2f, 0.2f),new Vec3(0.7f, 0.7f, 0.7f), 140.0f);
        Material lightTex = new Material(new Vec3(0.1f,0.1f,0.1f),new Vec3(0.6f, 0.6f, 0.6f),new Vec3(0.2f, 0.2f, 0.2f), 30f);

        sphere = makeSphere(gl, textures.get("spotBase"), spotlightBase);

        float spotlightBaseHeight = 20f;
        float spotlightBaseWidth = 0.6f;

        spotlightRoot = new NameNode("root");
        TransformNode spotlightMoveTranslate = new TransformNode("spotlight transform", Mat4Transform.translate(xPosition, 2f , 0));

        /*BASE*/
        NameNode base = new NameNode("base");
        TransformNode spotlightBaseTranslate = new TransformNode("spotlight base transform", Mat4Transform.translate(0,spotlightBaseHeight/2,0));
        TransformNode spotlightBaseTransform = new TransformNode("spotlight base transform", Mat4Transform.scale(spotlightBaseWidth, spotlightBaseHeight, spotlightBaseWidth));
        ModelNode spotlightBaseShape = new ModelNode("Sphere(spotlight base)", sphere);
        allModels[0] = spotlightBaseShape;



        /*HEAD*/
        NameNode head = new NameNode("head");
        TransformNode moveHeadToTopOfPole = new TransformNode("head translate", Mat4Transform.translate(0, spotlightBaseHeight, 0));
        TransformNode rotateHeadDown = new TransformNode("head rotate down", Mat4Transform.rotateAroundZ(-45));
        rotateHeadAround = new TransformNode("head rotate around", Mat4Transform.rotateAroundY(0));


        /*ATTACHMENT*/
        NameNode attachment = new NameNode("attachment");
        TransformNode attachmentTransform = new TransformNode("attachment transform", Mat4Transform.scale(3f, 0.5f, 0.5f));
        ModelNode attachmentShape = new ModelNode("Sphere(attachment)", sphere);
        allModels[1] = attachmentShape;


        sphere = makeSphere(gl, textures.get("attachment"), lightTex);
        NameNode light = new NameNode("light");
        TransformNode moveLightTranslate = new TransformNode("light translate", Mat4Transform.translate(1.5f, 0, 0));
        TransformNode lightTransform = new TransformNode("light transform", Mat4Transform.scale(0.75f, 0.75f, 0.75f));
        ModelNode lightShape = new ModelNode("Sphere(light)", sphere);
        allModels[2] = lightShape;

        double lengthOfLight = 1.5;
        Vec3 initialLightPosition = new Vec3(xPosition + 4.5f, 22 - (float) (Math.tan(lengthOfLight)*1.5), 0);
        setLightPosition(initialLightPosition);

        spotlightRoot.addChild(spotlightMoveTranslate);
            spotlightMoveTranslate.addChild(base);
                base.addChild(spotlightBaseTranslate);
                    spotlightBaseTranslate.addChild(spotlightBaseTransform);
                        spotlightBaseTransform.addChild(spotlightBaseShape);
                base.addChild(moveHeadToTopOfPole);
                    moveHeadToTopOfPole.addChild(rotateHeadAround);
                        rotateHeadAround.addChild(rotateHeadDown);
                            rotateHeadDown.addChild(head);
                                head.addChild(attachment);
                                    attachment.addChild(attachmentTransform);
                                        attachmentTransform.addChild(attachmentShape);
                                head.addChild(light);
                                    light.addChild(moveLightTranslate);
                                        moveLightTranslate.addChild(lightTransform);
                                            lightTransform.addChild(lightShape);

        // Make sure to update the root of the scene graph
        spotlightRoot.update();

    }

    private ModelMultipleLights makeSphere(GL3 gl, Texture t1, Material material){
        String name = "sphere";
        Mesh mesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
        Shader shader;

        shader = new Shader(gl, "vs_standard.txt", "fs_standard_m_1t.txt");
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

    public void updateRotation(double elapsedTime){
        float rotationSpeed = 360.0f / 4.0f; // degrees per second
        float angle = (float) ((elapsedTime) * rotationSpeed);
        rotateHeadAround.setTransform(Mat4Transform.rotateAroundY(angle));
        rotateHeadAround.update();
    }
}
