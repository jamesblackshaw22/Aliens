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
    private Light light;
    private Model sphere;
    private SGNode alienRoot;
    private float xPosition = 0;

    private TransformNode translateX, alienMoveTranslate;

    public Alien(GL3 gl, Camera cameraIn, Light lightIn, Texture t1, float xPos){


        this.camera = cameraIn;
        this.light = lightIn;
        this.xPosition = xPos;

        sphere = makeSphere(gl, t1);

        float alienBodyScale = 5f;
        float alienHeadScale = 3.5f;
        float alienArmHeight = alienBodyScale * 0.5f;
        float alienArmWidth = 0.35f;
        float alienArmDepth = alienBodyScale * 0.2f;


        alienRoot = new NameNode("root");
        alienMoveTranslate = new TransformNode("alien transform", Mat4Transform.translate(xPosition,alienBodyScale/2,0));

        //TransformNode alienTranslate = new TransformNode("alien transform",Mat4Transform.translate(0,0,0));

        //Body Shape
        NameNode body = new NameNode("body");
        Mat4 m = Mat4Transform.scale(alienBodyScale,alienBodyScale,alienBodyScale);
        TransformNode bodyTransform = new TransformNode("body transform", m);
        ModelNode bodyShape = new ModelNode("Sphere(body)", sphere);

        //Right arm Shape
        NameNode rightArm = new NameNode("rightArm");
        m = new Mat4(1);
        m = Mat4.multiply(m,Mat4Transform.rotateAroundZ(-35));
        m = Mat4.multiply(m,Mat4Transform.scale(alienArmWidth,alienArmHeight,alienArmDepth));
        TransformNode rightArmTransform = new TransformNode("rightArm transform", m);
        ModelNode rightArmShape = new ModelNode("Sphere(rightArm)", sphere);

        //Right arm positioning
        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.translate(alienBodyScale * 0.6f, alienBodyScale * 0.35f, 0));
        TransformNode rightArmTranslate = new TransformNode("rightArm translate", m);

        //Left arm Shape
        NameNode leftArm = new NameNode("leftArm");
        m = new Mat4(1);
        m = Mat4.multiply(m,Mat4Transform.rotateAroundZ(35));
        m = Mat4.multiply(m,Mat4Transform.scale(alienArmWidth,alienArmHeight,alienArmDepth));
        TransformNode leftArmTransform = new TransformNode("leftArm transform", m);
        ModelNode leftArmShape = new ModelNode("Sphere(leftArm)", sphere);

        //Left arm positioning
        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.translate(-(alienBodyScale * 0.6f), alienBodyScale * 0.35f, 0));
        TransformNode leftArmTranslate = new TransformNode("leftArm translate", m);

        //Head Shape
        NameNode head = new NameNode("head");
        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.scale(alienHeadScale,alienHeadScale,alienHeadScale));
        TransformNode headTransform = new TransformNode("head transform", m);
        ModelNode headShape = new ModelNode("Sphere(head)", sphere);

        //Head Positioning
        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.translate(0,alienBodyScale /2 + alienHeadScale/2 , 0));
        TransformNode headTranslate = new TransformNode("head translate", m);

        //Right ear Shape
        NameNode rightEar = new NameNode("rightEar");
        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.scale(alienHeadScale / 11, alienHeadScale * 0.8f,alienHeadScale /2));
        TransformNode rightEarTransform = new TransformNode("right ear transform", m);
        ModelNode rightEarShape = new ModelNode("Sphere(right ear)", sphere);

        //Right ear positioning
        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.translate(alienHeadScale / 2, alienBodyScale / 4 , 0));
        TransformNode rightEarTranslate = new TransformNode("right ear translate", m);

        //Left ear Shape
        NameNode leftEar = new NameNode("leftEar");
        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.scale(alienHeadScale / 11, alienHeadScale * 0.8f,alienHeadScale /2));
        TransformNode leftEarTransform = new TransformNode("left ear transform", m);
        ModelNode leftEarShape = new ModelNode("Sphere(left ear)", sphere);

        //Right ear positioning
        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.translate(-(alienHeadScale / 2), alienBodyScale / 4 , 0));
        TransformNode leftEarTranslate = new TransformNode("left ear translate", m);

        //Right eye Shape
        NameNode rightEye = new NameNode("rightEye");
        m = new Mat4(1);
        m = Mat4.multiply(m,Mat4Transform.rotateAroundY(30));
        m = Mat4.multiply(m, Mat4Transform.scale(alienHeadScale / 5, alienHeadScale / 7,alienHeadScale / 10));
        TransformNode rightEyeTransform = new TransformNode("right eye transform", m);
        ModelNode rightEyeShape = new ModelNode("Sphere(right eye)", sphere);

        //Right eye positioning
        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.translate(0.7f, alienBodyScale / 5 , 1.1f));
        TransformNode rightEyeTranslate = new TransformNode("right eye translate", m);

        //Left eye Shape
        NameNode leftEye = new NameNode("leftEye");
        m = new Mat4(1);
        m = Mat4.multiply(m,Mat4Transform.rotateAroundY(-30));
        m = Mat4.multiply(m, Mat4Transform.scale(alienHeadScale / 5, alienHeadScale / 7,alienHeadScale / 10));
        TransformNode leftEyeTransform = new TransformNode("left eye transform", m);
        ModelNode leftEyeShape = new ModelNode("Sphere(left eye)", sphere);

        //left eye positioning
        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.translate(-0.7f, alienBodyScale / 5 , 1.1f));
        TransformNode leftEyeTranslate = new TransformNode("left eye translate", m);

        //Antenna

        NameNode antenna = new NameNode("antenna");
        //antenna positioning
        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.translate(0, 1.2f , 0));
        TransformNode antennaTranslate = new TransformNode("antenna translate", m);

        //Antenna Base Shape
        float antennaBaseHeight = alienHeadScale / 3;
        float antennaTopScale  = alienHeadScale / 11;
        float antennaBaseWidth = 0.2f;
        NameNode antennaBase = new NameNode("antennaBase");
        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.scale(antennaBaseWidth, antennaBaseHeight,antennaBaseWidth));
        TransformNode antennaBaseTransform = new TransformNode("antenna base transform", m);
        ModelNode antennaBaseShape = new ModelNode("Sphere(antennaBase)", sphere);

        //Antenna Base positioning
        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.translate(0, 1.1f , 0));
        TransformNode antennaBasePositioning = new TransformNode("antenna base translate", m);

        //Antenna top Shape
        NameNode antennaTop = new NameNode("antennaTop");
        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.scale(antennaTopScale, antennaTopScale,antennaTopScale));
        TransformNode antennaTopTransform = new TransformNode("antenna top transform", m);
        ModelNode antennaTopShape = new ModelNode("Sphere(antennaBase)", sphere);

        //Antenna Top positioning
        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.translate(0, 1.8f , 0));
        TransformNode antennaTopPositioning = new TransformNode("antenna top translate", m);




        alienRoot.addChild(alienMoveTranslate);
            alienMoveTranslate.addChild(body);
                body.addChild(bodyTransform);
                    bodyTransform.addChild(bodyShape);
                body.addChild(headTranslate);
                    headTranslate.addChild(head);
                        head.addChild(antennaTranslate);
                            antennaTranslate.addChild(antenna);
                                antenna.addChild(antennaBasePositioning);
                                    antennaBasePositioning.addChild(antennaBaseTransform);
                                        antennaBaseTransform.addChild(antennaBaseShape);
                                antenna.addChild(antennaTopPositioning);
                                    antennaTopPositioning.addChild(antennaTopTransform);
                                        antennaTopTransform.addChild(antennaTopShape);
                        head.addChild(rightEarTranslate);
                            rightEarTranslate.addChild(rightEar);
                                rightEar.addChild(rightEarTransform);
                                    rightEarTransform.addChild(rightEarShape);
                        head.addChild(leftEarTranslate);
                            leftEarTranslate.addChild(leftEar);
                                leftEar.addChild(leftEarTransform);
                                    leftEarTransform.addChild(leftEarShape);
                        head.addChild(rightEyeTranslate);
                            rightEyeTranslate.addChild(rightEye);
                                rightEye.addChild(rightEyeTransform);
                                    rightEyeTransform.addChild(rightEyeShape);
                        head.addChild(leftEyeTranslate);
                            leftEyeTranslate.addChild(leftEye);
                                leftEye.addChild(leftEyeTransform);
                                        leftEyeTransform.addChild(leftEyeShape);
                        head.addChild(headTransform);
                            headTransform.addChild(headShape);
                body.addChild(rightArmTranslate);
                    rightArmTranslate.addChild(rightArm);
                        rightArm.addChild(rightArmTransform);
                            rightArmTransform.addChild(rightArmShape);
                body.addChild(leftArmTranslate);
                    leftArmTranslate.addChild(leftArm);
                        leftArm.addChild(leftArmTransform);
                            leftArmTransform.addChild(leftArmShape);



        //alienRoot.print(1,false);
        alienRoot.update();

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
        alienRoot.draw(gl);
    }

}
