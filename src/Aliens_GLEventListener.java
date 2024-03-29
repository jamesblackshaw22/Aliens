import gmaths.*;

import com.jogamp.opengl.*;

import java.util.HashMap;

public class Aliens_GLEventListener implements GLEventListener{

    public Aliens_GLEventListener(Camera camera) {
        this.camera = camera;
        this.camera.setPosition(new Vec3(0f,20,35));
    }

    // ***************************************************
    /*
     * METHODS DEFINED BY GLEventListener
     */

    /* Initialisation */
    public void init(GLAutoDrawable drawable) {
        GL3 gl = drawable.getGL().getGL3();
        System.err.println("Chosen GLCapabilities: " + drawable.getChosenGLCapabilities());
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        gl.glClearDepth(1.0f);
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glDepthFunc(GL.GL_LESS);
        gl.glFrontFace(GL.GL_CCW);    // default is 'CCW'
        gl.glEnable(GL.GL_CULL_FACE); // default is 'not enabled'
        gl.glCullFace(GL.GL_BACK);   // default is 'back', assuming CCW
        initialise(gl);
        startTime = getSeconds();
    }

    /* Called to indicate the drawing surface has been moved and/or resized  */
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL3 gl = drawable.getGL().getGL3();
        gl.glViewport(x, y, width, height);
        float aspect = (float)width/(float)height;
        camera.setPerspectiveMatrix(Mat4Transform.perspective(45, aspect));
    }

    /* Draw */
    public void display(GLAutoDrawable drawable) {
        GL3 gl = drawable.getGL().getGL3();
        render(gl);
    }

    /* Clean up memory, if necessary */
    public void dispose(GLAutoDrawable drawable) {
        GL3 gl = drawable.getGL().getGL3();
        alien1.dispose(gl);
    }

    // ***************************************************
    /* THE SCENE
     * Now define all the methods to handle the scene.
     * This will be added to in later examples.
     */

    //Below is my code
    private Camera camera;

    private Alien alien1, alien2;

    private Environment skybox;

    private Spotlight spotlight;

    private Boolean light1On = true;

    private  Boolean light2On = true;

    private Boolean spotlightOn = true;

    private Boolean toggleRock = false;

    private Boolean toggleRoll = false;

    private Boolean lightsHaveChanged = true;

    private double startRockTime;


    private final HashMap<String,Light> lightsMap = new HashMap<>();

    private Light[] lights;

    private void initialise(GL3 gl) {
        // textures
        TextureLibrary texturesSpotlightRoom = new TextureLibrary();
        texturesSpotlightRoom.add(gl, "chequerboard", "chequerboard.jpg");
        texturesSpotlightRoom.add(gl, "negx", "negx.jpg");
        texturesSpotlightRoom.add(gl, "posx", "posx.jpg");
        texturesSpotlightRoom.add(gl, "posy", "posy.jpg");
        texturesSpotlightRoom.add(gl, "negy", "negy.jpg");
        texturesSpotlightRoom.add(gl, "negz", "negz.jpg");
        texturesSpotlightRoom.add(gl, "posz", "posz.jpg");
        texturesSpotlightRoom.add(gl, "attachment", "attachment.jpg");
        texturesSpotlightRoom.add(gl, "spotBase", "spotBase.jpg");
        texturesSpotlightRoom.addSnowTexture(gl,"snow", "snow.png");

        TextureLibrary texturesAlien1 = new TextureLibrary();
        texturesAlien1.add(gl, "a", "alien1a.jpg");
        texturesAlien1.add(gl, "b", "alien1b.jpg");
        texturesAlien1.add(gl, "c", "alien1c.jpg");
        texturesAlien1.add(gl, "d", "alien1d.jpg");

        TextureLibrary texturesAlien2 = new TextureLibrary();
        texturesAlien2.add(gl, "a", "alien2a.jpg");
        texturesAlien2.add(gl, "b", "alien2b.jpg");
        texturesAlien2.add(gl, "c", "alien2c.jpg");
        texturesAlien2.add(gl, "d", "alien2d.jpg");


        startTime = getSeconds();
        //Top left general light
        lightsMap.put("light1",new Light(gl));
        lightsMap.get("light1").setCamera(camera);
        lightsMap.get("light1").setPosition(new Vec3(10f,25f,0f));
        //Spotlight
        lightsMap.put("light2",new Light(gl));
        lightsMap.get("light2").setCamera(camera);
        lightsMap.get("light2").setPosition(new Vec3(-10f,25f,0f));

        lightsMap.put("spotlight",new Light(gl));
        lightsMap.get("spotlight").setCamera(camera);
        lights = lightsMap.values().toArray(new Light[0]);

        alien1 = new Alien(gl, camera, lights, texturesAlien1,-4f, true);
        alien2 = new Alien(gl, camera, lights, texturesAlien2,4f, false);
        spotlight = new Spotlight(gl, camera, lights, texturesSpotlightRoom,-15f);
        skybox = new Environment(gl, camera, lights, texturesSpotlightRoom);
        lightsMap.get("spotlight").setPosition(spotlight.getLightPosition());

    }

    private void render(GL3 gl) {
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        if (lightsHaveChanged) updateLights(gl);
        lights = lightsMap.values().toArray(new Light[0]);
        skybox.updateLights(lights);
        alien1.updateLights(lights);
        alien2.updateLights(lights);
        spotlight.updateLights(lights);
        spotlight.updateRotation(getSeconds()-startTime);

        if (toggleRock){
            alien1.rock(getSeconds() - startRockTime);
            alien2.rock(getSeconds() - startRockTime);
        }else {
            alien1.stopRock();
            alien2.stopRock();
        }

        if (toggleRoll){
            alien1.roll(getSeconds() - startRockTime);
            alien2.roll(getSeconds() - startRockTime);
        }else {
            alien1.stopRoll();
            alien2.stopRoll();
        }

        // changing light position each frame

        //floor.render(gl)
        skybox.render(gl,getSeconds()-startTime);
        alien1.render(gl);
        alien2.render(gl);
        spotlight.render(gl);
    }


    // ***************************************************
    /* TIME
     */

    private double startTime;

    private double getSeconds() {
        return System.currentTimeMillis()/1000.0;
    }

    public void toggleLight1(){
        light1On = !light1On;
    }

    public void toggleLight2(){
        light2On = !light2On;
    }

    public void toggleSpotlight(){
        spotlightOn = !spotlightOn;
    }
    public void toggleRock(){
        if (!toggleRock){
            startRockTime = getSeconds();
            toggleRock = true;
        }else {
            toggleRock = false;
        }
    }
    public void toggleRoll(){
        toggleRoll = !toggleRoll;
    }

    public void lightsHaveChanged(){
        lightsHaveChanged = true;
    }

    private void updateLights(GL3 gl) {
        if (light1On) {
            if (!lightsMap.containsKey("light1")) {
                lightsMap.put("light1",new Light(gl));
                lightsMap.get("light1").setCamera(camera);
                lightsMap.get("light1").setPosition(new Vec3(10,20,0));
            }
        } else {
            lightsMap.remove("light1");
        }

        if (light2On) {
            if (!lightsMap.containsKey("light2")) {
                lightsMap.put("light2",new Light(gl));
                lightsMap.get("light2").setCamera(camera);
                lightsMap.get("light2").setPosition(new Vec3(-10f,20f,0f));
            }
        } else {
            lightsMap.remove("light2");
        }

        if (spotlightOn) {
            if (!lightsMap.containsKey("spotlight")) {
                lightsMap.put("spotlight",new Light(gl));
                lightsMap.get("spotlight").setCamera(camera);
                lightsMap.get("spotlight").setPosition(spotlight.getLightPosition());
            }
        } else {
            lightsMap.remove("spotlight");
        }

        lights = lightsMap.values().toArray(new Light[0]); // Update the lights array
        lightsHaveChanged = false;
    }
}
