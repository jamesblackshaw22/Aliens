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

import javax.naming.Name;
import javax.xml.namespace.QName;

public class Alien {
    private Camera camera;
    private Light[] lights;
    private ModelMultipleLights sphere;
    private SGNode alienRoot;
    private float xPosition = 0;

    private ModelNode[] allModels = new ModelNode[10];

    private TransformNode translateX, alienMoveTranslate, alienRotate, headRoll;

    public Alien(GL3 gl, Camera cameraIn,  Light[] lightsIn, Texture t1, float xPos){


        this.camera = cameraIn;
        this.lights = lightsIn;
        this.xPosition = xPos;

        sphere = makeSphere(gl, t1, false);

        float alienBodyScale = 5f;
        float alienHeadScale = 3.5f;
        float alienArmHeight = alienBodyScale * 0.5f;
        float alienArmWidth = 0.35f;
        float alienArmDepth = alienBodyScale * 0.2f;
        float antennaBaseHeight = alienHeadScale / 3;
        float antennaTopScale  = alienHeadScale / 11;
        float antennaBaseWidth = 0.2f;


        alienRoot = new NameNode("root");
        alienMoveTranslate = new TransformNode("alien transform", Mat4Transform.translate(xPosition,alienBodyScale,0));
        alienRotate = new TransformNode("body transform", Mat4Transform.rotateAroundZ(0));


        //TransformNode alienTranslate = new TransformNode("alien transform",Mat4Transform.translate(0,0,0));

        /*BODY*/
        NameNode body = new NameNode("body");
        TransformNode bodyTranslate = new TransformNode("body transform", Mat4Transform.translate(0, 0, 0));
        TransformNode bodyTransform = new TransformNode("body transform", Mat4Transform.scale(alienBodyScale, alienBodyScale, alienBodyScale));
        ModelNode bodyShape = new ModelNode("Sphere(body)", sphere);
        allModels[0] = bodyShape;
        body.addChild(bodyTranslate);
        bodyTranslate.addChild(bodyTransform);
            bodyTransform.addChild(bodyShape);

        /*RIGHT ARM*/
        NameNode rightArm = new NameNode("rightArm");
        TransformNode rightArmTranslate = new TransformNode("rightArm translate", Mat4Transform.translate(alienBodyScale * 0.6f, alienBodyScale /2 * 0.6f, 0));
        TransformNode rightArmTransform = new TransformNode("rightArm transform", Mat4.multiply(Mat4Transform.rotateAroundZ(-35), Mat4Transform.scale(alienArmWidth, alienArmHeight, alienArmDepth)));
        ModelNode rightArmShape = new ModelNode("Sphere(rightArm)", sphere);
        allModels[1] = rightArmShape;
        rightArm.addChild(rightArmTranslate);
        rightArmTranslate.addChild(rightArmTransform);
        rightArmTransform.addChild(rightArmShape);

        /*LEFT ARM*/
        NameNode leftArm = new NameNode("leftArm");
        TransformNode leftArmTranslate = new TransformNode("leftArm translate", Mat4Transform.translate(-(alienBodyScale * 0.6f), alienBodyScale/2  * 0.6f, 0));
        TransformNode leftArmTransform = new TransformNode("leftArm transform", Mat4.multiply(Mat4Transform.rotateAroundZ(35), Mat4Transform.scale(alienArmWidth, alienArmHeight, alienArmDepth)));
        ModelNode leftArmShape = new ModelNode("Sphere(leftArm)", sphere);
        allModels[2] = leftArmShape;
        leftArm.addChild(leftArmTranslate);
        leftArmTranslate.addChild(leftArmTransform);
        leftArmTransform.addChild(leftArmShape);

        /*HEAD*/
        NameNode head = new NameNode("head");
        TransformNode headTranslate = new TransformNode("head transform", Mat4Transform.translate(0, alienBodyScale/2, 0));
        headRoll = new TransformNode("head transform", Mat4Transform.rotateAroundZ(0));

        NameNode headBase = new NameNode("headBase");
        TransformNode headBaseTranslate = new TransformNode("head translate", Mat4Transform.translate(0, alienHeadScale/2, 0));
        TransformNode headBaseTransform = new TransformNode("head transform", Mat4Transform.scale(alienHeadScale, alienHeadScale, alienHeadScale));
        ModelNode headBaseShape = new ModelNode("Sphere(head)", sphere);
        allModels[3] = headBaseShape;


        /*RIGHT EAR*/
        NameNode rightEar = new NameNode("rightEar");
        TransformNode rightEarTranslate = new TransformNode("right ear translate", Mat4Transform.translate(alienHeadScale / 2, alienBodyScale * 0.5f, 0));
        TransformNode rightEarTransform = new TransformNode("right ear transform", Mat4Transform.scale(alienHeadScale / 11, alienHeadScale * 0.8f, alienHeadScale / 2));
        ModelNode rightEarShape = new ModelNode("Sphere(right ear)", sphere);
        allModels[4] = rightEarShape;
        rightEar.addChild(rightEarTranslate);
        rightEarTranslate.addChild(rightEarTransform);
        rightEarTransform.addChild(rightEarShape);

        /*LEFT EAR*/
        NameNode leftEar = new NameNode("leftEar");
        TransformNode leftEarTranslate = new TransformNode("left ear translate", Mat4Transform.translate(-(alienHeadScale / 2), alienBodyScale * 0.5f, 0));
        TransformNode leftEarTransform = new TransformNode("left ear transform", Mat4Transform.scale(alienHeadScale / 11, alienHeadScale * 0.8f, alienHeadScale / 2));
        ModelNode leftEarShape = new ModelNode("Sphere(left ear)", sphere);
        allModels[5] = leftEarShape;
        leftEar.addChild(leftEarTranslate);
        leftEarTranslate.addChild(leftEarTransform);
        leftEarTransform.addChild(leftEarShape);

        /*RIGHT EYE*/
        NameNode rightEye = new NameNode("rightEye");
        TransformNode rightEyeTranslate = new TransformNode("right eye translate", Mat4Transform.translate(0.7f, alienHeadScale * 0.7f, 1.6f));
        TransformNode rightEyeTransform = new TransformNode("right eye transform", Mat4.multiply(Mat4Transform.rotateAroundZ(20), Mat4Transform.scale(alienHeadScale / 5, alienHeadScale / 7, alienHeadScale / 10)));
        ModelNode rightEyeShape = new ModelNode("Sphere(right eye)", sphere);
        allModels[6] = rightEyeShape;
        rightEye.addChild(rightEyeTranslate);
        rightEyeTranslate.addChild(rightEyeTransform);
        rightEyeTransform.addChild(rightEyeShape);

        /*LEFT EYE*/
        NameNode leftEye = new NameNode("leftEye");
        TransformNode leftEyeTranslate = new TransformNode("left eye translate", Mat4Transform.translate(-0.7f, alienHeadScale* 0.7f, 1.6f));
        TransformNode leftEyeTransform = new TransformNode("left eye transform", Mat4.multiply(Mat4Transform.rotateAroundZ(-20), Mat4Transform.scale(alienHeadScale / 5, alienHeadScale / 7, alienHeadScale / 10)));
        ModelNode leftEyeShape = new ModelNode("Sphere(left eye)", sphere);
        allModels[7] = leftEyeShape;
        leftEye.addChild(leftEyeTranslate);
        leftEyeTranslate.addChild(leftEyeTransform);
        leftEyeTransform.addChild(leftEyeShape);

        /*ANTENNA*/
        NameNode antenna = new NameNode("antenna");
        TransformNode antennaTranslate = new TransformNode("antenna translate", Mat4Transform.translate(0,alienHeadScale, 0));

        // Antenna Base
        NameNode antennaBase = new NameNode("antennaBase");
        TransformNode antennaBasePositioning = new TransformNode("antenna base translate", Mat4Transform.translate(0, antennaBaseHeight /2 , 0));
        TransformNode antennaBaseTransform = new TransformNode("antenna base transform", Mat4Transform.scale(antennaBaseWidth, antennaBaseHeight, antennaBaseWidth));
        ModelNode antennaBaseShape = new ModelNode("Sphere(antennaBase)", sphere);
        allModels[8] = antennaBaseShape;
        antennaBase.addChild(antennaBasePositioning);
        antennaBasePositioning.addChild(antennaBaseTransform);
        antennaBaseTransform.addChild(antennaBaseShape);

        // Antenna Top
        NameNode antennaTop = new NameNode("antennaTop");
        TransformNode antennaTopPositioning = new TransformNode("antenna top translate", Mat4Transform.translate(0, antennaTopScale /2 + antennaBaseHeight, 0));
        TransformNode antennaTopTransform = new TransformNode("antenna top transform", Mat4Transform.scale(antennaTopScale, antennaTopScale, antennaTopScale));
        ModelNode antennaTopShape = new ModelNode("Sphere(antennaBase)", sphere);
        allModels[9] = antennaTopShape;
        antennaTop.addChild(antennaTopPositioning);
        antennaTopPositioning.addChild(antennaTopTransform);
        antennaTopTransform.addChild(antennaTopShape);


        antenna.addChild(antennaTranslate);
        antennaTranslate.addChild(antennaBase);
        antennaTranslate.addChild(antennaTop);
        //ASSEMBLY
        alienRoot.addChild(alienMoveTranslate);
        alienMoveTranslate.addChild(alienRotate);
        alienRotate.addChild(body);
        alienRotate.addChild(rightArm);
        alienRotate.addChild(leftArm);

        body.addChild(head);
        head.addChild(headRoll);

        headRoll.addChild(headTranslate);
        headTranslate.addChild(headBase);
            headBase.addChild(headBaseTranslate);
                headBaseTranslate.addChild(headBaseTransform);
                    headBaseTransform.addChild(headBaseShape);
        headTranslate.addChild(antenna);
        headTranslate.addChild(rightEar);
        headTranslate.addChild(leftEar);
        headTranslate.addChild(rightEye);
        headTranslate.addChild(leftEye);



        //alienRoot.print(1,false);
        alienRoot.update();

    }

    private ModelMultipleLights makeSphere(GL3 gl, Texture t1, Boolean isLight){
        String name = "sphere";
        Mesh mesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());

        //change to multiple lights
        Shader shader = new Shader(gl, "vs_standard.txt", "fs_standard_m_1t.txt");

        Material material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
        Mat4 modelMatrix = Mat4.multiply(Mat4Transform.scale(4,4,4), Mat4Transform.translate(0,0.5f,0));
        return new ModelMultipleLights(name, mesh, modelMatrix, shader, material, lights, camera, t1);
    }



    public void render(GL3 gl){
        alienRoot.draw(gl);
    }

    public void dispose(GL3 gl) {
        sphere.dispose(gl);
    }

    public void updateLights(Light[]lights){
        this.lights = lights;
        for (ModelNode node : allModels){
            node.updateLights(lights);
        }
    }

    public void rock(double elapsedTime){
        float maxAngle = 30.0f;
        float speed = 1f;
        float newAngle = (float) (maxAngle * Math.sin(elapsedTime * speed * 2.0f * Math.PI));
        alienRotate.setTransform(Mat4Transform.rotateAroundZ(newAngle));
        alienRotate.update();
    }

    public void roll(double elapsedTime){
        float maxAngle = 30.0f;
        float speed = 1f;
        float newAngle = (float) (maxAngle * Math.sin(elapsedTime * speed * 2.0f * Math.PI));
        headRoll.setTransform(Mat4Transform.rotateAroundZ(newAngle));
        headRoll.update();
    }

    public void stopRock(){
        alienRotate.setTransform(Mat4Transform.rotateAroundZ(0));
        alienRotate.update();
    }

    public void stopRoll(){
        headRoll.setTransform(Mat4Transform.rotateAroundZ(0));
        headRoll.update();
    }

}
