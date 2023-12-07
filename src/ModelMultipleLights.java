import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.util.texture.Texture;
import gmaths.Mat4;
import gmaths.Vec2;
import gmaths.Vec3;

//Taken from lab classes modified some parts.
public class ModelMultipleLights {

  private String name;
  private Mesh mesh;
  private Mat4 modelMatrix;
  private Shader shader;
  private Material material;
  private Camera camera;
  private Light[] lights;
  private Texture diffuse;
  private Texture specular;
  private Texture snow;


  private ModelMultipleLights spotlight;
  private Vec3 position;

  private Vec2 offset;

  public ModelMultipleLights(String name, Mesh mesh, Mat4 modelMatrix, Shader shader, Material material, Light[] lights,
                             Camera camera, Texture diffuse, Texture specular) {
    this.name = name;
    this.mesh = mesh;
    this.modelMatrix = modelMatrix;
    this.shader = shader;
    this.material = material;
    this.lights = lights;
    this.camera = camera;
    this.diffuse = diffuse;
    this.specular = specular;
  }

  public ModelMultipleLights(String name, Mesh mesh, Mat4 modelMatrix, Shader shader, Material material, Light[] lights,
                             Camera camera, Texture diffuse) {
    this(name, mesh, modelMatrix, shader, material, lights, camera, diffuse, null);
  }

  public ModelMultipleLights(String name, Mesh mesh, Mat4 modelMatrix, Shader shader, Material material, Light[] lights,
                             Camera camera) {
    this(name, mesh, modelMatrix, shader, material, lights, camera, null, null);
  }

  public ModelMultipleLights(String name, Mesh mesh, Mat4 modelMatrix, Shader shader, Material material, Light[] lights,
                             Camera camera,Texture wall, Texture snow, Vec2 offset ) {
    this(name, mesh, modelMatrix, shader, material, lights, camera);
    this.diffuse = wall;
    this.snow = snow;
    this.offset = offset;
  }

  public void setName(String s) {
    this.name = s;
  }

  public void setLights(Light[] lights) {
    this.lights = lights;
  }

  public void setPosition(Vec3 position){
    this.position = position;
  }

  public Vec3 getPosition(){
    return position;
  }

  // second version of render is so that modelMatrix can be overriden with a new parameter
  public void render(GL3 gl, Mat4 modelMatrix) {
    if (mesh_null()) {
      System.out.println("Error: null in model render");
      return;
    }

    Mat4 mvpMatrix = Mat4.multiply(camera.getPerspectiveMatrix(), Mat4.multiply(camera.getViewMatrix(), modelMatrix));
    shader.use(gl);
    shader.setFloatArray(gl, "model", modelMatrix.toFloatArrayForGLSL());
    shader.setFloatArray(gl, "mvpMatrix", mvpMatrix.toFloatArrayForGLSL());

    shader.setVec3(gl, "viewPos", camera.getPosition());

    shader.setInt(gl,"numLights", lights.length);


    for (int i=0; i<lights.length; i++) {

      shader.setVec3(gl, "lights["+i+"].position", lights[i].getPosition());
      shader.setVec3(gl, "lights["+i+"].ambient", lights[i].getMaterial().getAmbient());
      shader.setVec3(gl, "lights["+i+"].diffuse", lights[i].getMaterial().getDiffuse());
      shader.setVec3(gl, "lights["+i+"].specular", lights[i].getMaterial().getSpecular());
    }



    shader.setVec3(gl, "material.ambient", material.getAmbient());
    shader.setVec3(gl, "material.diffuse", material.getDiffuse());
    shader.setVec3(gl, "material.specular", material.getSpecular());
    shader.setFloat(gl, "material.shininess", material.getShininess());

    if (diffuse!=null) {
      shader.setInt(gl, "first_texture", 0);  // be careful to match these with GL_TEXTURE0 and GL_TEXTURE1
      gl.glActiveTexture(GL.GL_TEXTURE0);
      diffuse.bind(gl);
    }
    if (specular!=null) {
      shader.setInt(gl, "second_texture", 1);
      gl.glActiveTexture(GL.GL_TEXTURE1);
      specular.bind(gl);
    }

    //added
    if (snow!= null){
      shader.setInt(gl, "snow_texture", 1);
      shader.setFloat(gl,"offset", offset.x, offset.y);
      gl.glActiveTexture(GL.GL_TEXTURE1);
      snow.bind(gl);
    }

    // then render the mesh
    mesh.render(gl);
  }

  private boolean mesh_null() {
    return (mesh==null);
  }

  public void dispose(GL3 gl) {
    mesh.dispose(gl);  // only need to dispose of mesh
  }

  //added
  public void setOffset(Vec2 offset) {
    this.offset = offset;
  }
}