import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import gmaths.Mat4;
import gmaths.Mat4Transform;
import gmaths.Vec3;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Light {
  
  private Material material;
  private Vec3 position;
  private Mat4 model;
  private Shader shader;
  private Camera camera;
    
  public Light(GL3 gl) {
    material = new Material();
    material.setAmbient(0.5f, 0.5f, 0.5f);
    material.setDiffuse(0.8f, 0.8f, 0.8f);
    material.setSpecular(0.8f, 0.8f, 0.8f);
    position = new Vec3(3f,2f,1f);
    model = new Mat4(1);
    shader = new Shader(gl, "vs_light_01.txt", "fs_light_01.txt");
  }
  
  public void setPosition(Vec3 v) {
    position.x = v.x;
    position.y = v.y;
    position.z = v.z;
  }
  
  public void setPosition(float x, float y, float z) {
    position.x = x;
    position.y = y;
    position.z = z;
  }
  
  public Vec3 getPosition() {
    return position;
  }
  
  public void setMaterial(Material m) {
    material = m;
  }
  
  public Material getMaterial() {
    return material;
  }
  
  public void setCamera(Camera camera) {
    this.camera = camera;
  }

  public void dispose(GL3 gl) {
  }

    // ***************************************************
  /* THE DATA
   */
  // anticlockwise/counterclockwise ordering



}