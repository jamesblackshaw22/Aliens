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

public class Alien {
    private Camera camera;
    private Light light;
    private Model sphere;
    private SGNode alienRoot;
    private float xPosition = 0;

    private TransformNode translateX, alienMoveTranslate;

    public Alien(GL3 gl, Camera cameraIn, Light lightIn, Texture t1){


        this.camera = cameraIn;
        this.light = lightIn;

        sphere = makeSphere(gl, t1);

        float alienBodyScale = 4f;
        float alienHeadScale = 3f;


        alienRoot = new NameNode("root");
        alienMoveTranslate = new TransformNode("alien transform", Mat4Transform.translate(xPosition,0,0));

        //TransformNode alienTranslate = new TransformNode("alien transform",Mat4Transform.translate(0,0,0));

        //Body Shape
        NameNode body = new NameNode("body");
        Mat4 m = Mat4Transform.scale(alienBodyScale,alienBodyScale,alienBodyScale);
        TransformNode bodyTransform = new TransformNode("body transform", m);
        ModelNode bodyShape = new ModelNode("Sphere(body)", sphere);

        //Head Shape
        NameNode head = new NameNode("head");
        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.scale(alienHeadScale,alienHeadScale,alienHeadScale));
        TransformNode headTransform = new TransformNode("head transform", m);
        ModelNode headShape = new ModelNode("Sphere(head)", sphere);

        //Head Move
        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.translate(0,alienBodyScale / 2, 0));
        TransformNode headTranslate = new TransformNode("head translate", m);





        /*//Eyes
        float eyeScale = 0.2f;
        NameNode eye1 = new NameNode("eye1");
        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.scale(eyeScale,eyeScale/2, eyeScale));
        m = Mat4.multiply(m, Mat4Transform.translate(0.8f,2.7f,2f));
        TransformNode eye1Transform = new TransformNode("eye1 transform", m);
        ModelNode eye1Shape = new ModelNode("Sphere(eye1)", sphere);

        NameNode eye2 = new NameNode("eye2");
        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.scale(eyeScale,eyeScale/2, eyeScale));
        m = Mat4.multiply(m, Mat4Transform.translate(-0.8f,2.7f,2f));
        TransformNode eye2Transform = new TransformNode("eye2 transform", m);
        ModelNode eye2Shape = new ModelNode("Sphere(eye2)", sphere);
        //Ears
        float earsScale = 0.8f;
        NameNode ear1 = new NameNode("ear1");
        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.scale(earsScale/10,earsScale, earsScale/4));
        m = Mat4.multiply(m, Mat4Transform.translate(6f,0.5f,0f));
        TransformNode ear1Transform = new TransformNode("ear1 transform", m);
        ModelNode ear1Shape = new ModelNode("Sphere(ear1)", sphere);

        NameNode ear2 = new NameNode("ear2");
        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.scale(earsScale/10, earsScale, earsScale/4));
        m = Mat4.multiply(m, Mat4Transform.translate(-6f,0.5f,0f));
        TransformNode ear2Transform = new TransformNode("ear1 transform", m);
        ModelNode ear2Shape = new ModelNode("Sphere(ear2)", sphere);

        //Arms
        float armsScale = 1f;
        NameNode arm1 = new NameNode("arm1");
        m = new Mat4(1);
        m = Mat4.multiply(m, Mat4Transform.rotateAroundZ(-45));
        m = Mat4.multiply(m, Mat4Transform.scale(armsScale/4, 2.5f, armsScale/3));
        m = Mat4.multiply(m, Mat4Transform.translate(0,1.4f,0f));

        TransformNode arm1Transform = new TransformNode("arm1 transform", m);
        ModelNode arm1Shape = new ModelNode("Sphere(arm1)", sphere);*/



        alienRoot.addChild(alienMoveTranslate);
            alienMoveTranslate.addChild(body);


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
