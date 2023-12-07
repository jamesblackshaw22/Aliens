import gmaths.*;

import com.jogamp.opengl.*;
import com.jogamp.opengl.util.texture.*;

public class Skybox {

    private ModelMultipleLights[] wall;
    private Camera camera;
    private Light[] lights;
    private float size = 40f;
    private ModelNode[] allModels = new ModelNode[5];

    ModelNode negXModel;
    ModelNode posZModel;
    ModelNode posXModel;

    private SGNode skyboxRoot;

    TextureLibrary textures;
    public Skybox(GL3 gl, Camera camera, Light[] lights, TextureLibrary textures) {
        this.camera = camera;
        this.lights = lights;
        this.textures = textures;


        skyboxRoot = new NameNode("root");
        //FLOOR
        ModelNode negYModel = new ModelNode("Square(negy)", makeWall(gl, textures.get("negy")));
        TransformNode moveNegY = new TransformNode("moveNegY" , Mat4Transform.translate(0,0,0));
        TransformNode scaleNegY = new TransformNode("scaleNegy", Mat4Transform.scale(size, 1f, size));
        allModels[0] = negYModel;

        //LEFT
        negXModel = new ModelNode("Square(negx)", makeSnowyWall(gl, textures.get("negx"), textures.get("snow"), new Vec2(0,0)));
        TransformNode moveNegX = new TransformNode("moveNegX", Mat4Transform.translate(-size*0.5f,size*0.5f,0));
        TransformNode scaleNegX= new TransformNode("scaleNegX", Mat4Transform.scale(size, 1f, size));
        TransformNode rotateNegX= new TransformNode("rotateNegX", Mat4.multiply(Mat4Transform.rotateAroundZ(-90),Mat4Transform.rotateAroundY(90)));

        allModels[1] = negXModel;

        //Ceiling

        ModelNode posYModel = new ModelNode("Square(posY)", makeWall(gl, textures.get("posy")));
        TransformNode movePosY = new TransformNode("movePosY", Mat4Transform.translate(0,size,0));
        TransformNode scalePosY= new TransformNode("scalePosY", Mat4Transform.scale(size, 1f, size));
        TransformNode rotatePosY= new TransformNode("rotatePosY", Mat4Transform.rotateAroundZ(180));
        allModels[2] = posYModel;

        //RIGHT

        posXModel = new ModelNode("Square(PosX)", makeSnowyWall(gl, textures.get("posx"), textures.get("snow"),new Vec2(0,0)));
        TransformNode movePosX = new TransformNode("movePosX", Mat4Transform.translate(size*0.5f,size*0.5f,0));
        TransformNode scalePosX= new TransformNode("scalePosX", Mat4Transform.scale(size, 1f, size));
        TransformNode rotatePosX= new TransformNode("rotatePosX", Mat4.multiply(Mat4Transform.rotateAroundZ(90),Mat4Transform.rotateAroundY(-90)));
        allModels[3] = posXModel;


        //In Front
        posZModel = new ModelNode("Square(PosZ)", makeSnowyWall(gl, textures.get("posz"), textures.get("snow"), new Vec2(0,-30)));
        TransformNode movePosZ = new TransformNode("movePosZ", Mat4Transform.translate(0,size*0.5f,-size*0.5f));
        TransformNode scalePosZ= new TransformNode("scalePosZ", Mat4Transform.scale(size, 1f, size));
        TransformNode rotatePosZ= new TransformNode("rotatePosZ", Mat4Transform.rotateAroundX(90));
        allModels[4] = posZModel;

        skyboxRoot.addChild(moveNegY);
            moveNegY.addChild(scaleNegY);
                scaleNegY.addChild(negYModel);


        skyboxRoot.addChild(moveNegX);
            moveNegX.addChild(rotateNegX);
                rotateNegX.addChild(scaleNegX);
                    scaleNegX.addChild(negXModel);

        skyboxRoot.addChild(movePosY);
            movePosY.addChild(rotatePosY);
                rotatePosY.addChild(scalePosY);
                    scalePosY.addChild(posYModel);

        skyboxRoot.addChild(movePosX);
            movePosX.addChild(rotatePosX);
                rotatePosX.addChild(scalePosX);
                    scalePosX.addChild(posXModel);

        skyboxRoot.addChild(movePosZ);
            movePosZ.addChild(rotatePosZ);
                rotatePosZ.addChild(scalePosZ);
                    scalePosZ.addChild(posZModel);


        skyboxRoot.update();


        //wall[0] = negx(gl, textures.get("negx"));
        //wall[0] = negy(gl, textures.get("negy"));
       // wall[2] = negz(gl, textures.get("negz"));
       // wall[3] = posx(gl, textures.get("posx"));
       // wall[4] = posy(gl, textures.get("posy"));
       // wall[5] = posz(gl, textures.get("posz"));
    }

    // There is repetion in each of the following methods
    // An alternative would attempt to remove the repetition



    public void dispose(GL3 gl) {
        for (int i=0; i<3; i++) {
            wall[i].dispose(gl);
        }
    }

    public void updateLights(Light[]lights){
        this.lights = lights;
        for (ModelNode node : allModels) {
            node.updateLights(lights);
        }
    }

    private ModelMultipleLights makeWall(GL3 gl, Texture t1){
        String name = "wall";
        Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
        Shader shader = new Shader(gl, "vs_standard.txt", "fs_standard_m_1t.txt");
        Vec3 basecolor = new Vec3(0.5f, 0.5f, 0.5f); // grey
        Material material = new Material(basecolor, basecolor, new Vec3(0.3f, 0.3f, 0.3f), 4.0f);
        Mat4 modelMatrix = new Mat4(1);

        return new ModelMultipleLights(name, mesh, modelMatrix, shader, material, lights, camera, t1);
    }

    private ModelMultipleLights makeSnowyWall(GL3 gl, Texture t1, Texture snow, Vec2 offset){
        String name = "wall";
        Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
        Shader shader = new Shader(gl, "vs_walls.txt", "fs_walls.txt");
        Vec3 basecolor = new Vec3(0.5f, 0.5f, 0.5f); // grey
        Material material = new Material(basecolor, basecolor, new Vec3(0.3f, 0.3f, 0.3f), 4.0f);
        Mat4 modelMatrix = new Mat4(1);

        return new ModelMultipleLights(name, mesh, modelMatrix, shader, material, lights, camera, t1, snow, offset);
    }

    public void render(GL3 gl, double elapsedTime){
        double t = elapsedTime*0.1;  // *0.1 slows it down a bit
        float offsetY = (float)(t - Math.floor(t));
        float offsetX = 0.0f;
        Vec2 offset = new Vec2(offsetX,offsetY);
        posZModel.getModel().setOffset(offset);
        negXModel.getModel().setOffset(offset);
        posXModel.getModel().setOffset(offset);
        skyboxRoot.update();
        skyboxRoot.draw(gl);
    }
}