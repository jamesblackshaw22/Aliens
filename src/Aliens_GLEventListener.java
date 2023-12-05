import gmaths.*;

import com.jogamp.opengl.*;

import java.util.HashMap;

public class Aliens_GLEventListener implements GLEventListener{

    private static final boolean DISPLAY_SHADERS = false;

    public Aliens_GLEventListener(Camera camera) {
        this.camera = camera;
        this.camera.setPosition(new Vec3(4f,20f,40f));
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
        //lights[0].dispose(gl);
        //lights[1].dispose(gl);
        //lights[2].dispose(gl);
        //floor.dispose(gl);
        alien1.dispose(gl);
    }

    // ***************************************************
    /* THE SCENE
     * Now define all the methods to handle the scene.
     * This will be added to in later examples.
     */

    // textures
    private TextureLibrary textures;

    private Camera camera;
    private Mat4 perspective;
    private Model floor;
    private SGNode alienRoot;

    private Alien alien1, alien2;

    private Spotlight spotlight;

    private Boolean light1On = true;

    private  Boolean light2On = true;

    private Boolean spotlightOn = true;

    private Boolean lightsHaveChanged = true;

    private HashMap<String,Light> lightsMap = new HashMap<>();

    private Light[] lights;

    private void initialise(GL3 gl) {
        createRandomNumbers();

        textures = new TextureLibrary();
        textures.add(gl, "chequerboard", "textures/chequerboard.jpg");

        startTime = getSeconds();
        //Top left general light
        // floor
        /*String name = "floor";
        Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
        Shader shader = new Shader(gl, "vs_standard.txt", "fs_standard_1t.txt");
        Material material = new Material(new Vec3(0.0f, 0.5f, 0.81f), new Vec3(0.0f, 0.5f, 0.81f), new Vec3(0.3f, 0.3f, 0.3f), 32.0f);
        Mat4 modelMatrix = Mat4Transform.scale(16,1f,16);
        floor = new Model(name, mesh, modelMatrix, shader, material, light, camera, textures.get("chequerboard"));*/

        alien1 = new Alien(gl, camera, lights, textures.get("chequerboard"),-4f);
        alien2 = new Alien(gl, camera, lights, textures.get("chequerboard"),4f);
        spotlight = new Spotlight(gl, getSpotlightOn(), camera, lights, textures.get("chequerboard"),-15f);
    }

    private void render(GL3 gl) {
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        if (lightsHaveChanged) updateLights(gl);
        lights = lightsMap.values().toArray(new Light[0]);
        alien1.updateLights(lights);
        alien2.updateLights(lights);
        spotlight.updateLights(lights);
        // changing light position each frame
        //floor.render(gl);
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

    // ***************************************************
    /* An array of random numbers
     */

    private int NUM_RANDOMS = 1000;
    private float[] randoms;

    private void createRandomNumbers() {
        randoms = new float[NUM_RANDOMS];
        for (int i=0; i<NUM_RANDOMS; ++i) {
            randoms[i] = (float)Math.random();
        }
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

    public void lightsHaveChanged(){
        lightsHaveChanged = true;
    }

    public Boolean getSpotlightOn(){
        return spotlightOn;
    }
    private float getRotationAngle() {
        double elapsedTime = getSeconds() - startTime;  // getSeconds() should return the current time in seconds
        double rotationPeriod = 2.0;  // period of rotation in seconds
        double anglePerSecond = 360.0 / rotationPeriod;  // how much the angle changes per second

        // Calculate current angle
        double currentAngle = (elapsedTime * anglePerSecond) % 360.0;  // Use modulus to loop the angle
        return (float) currentAngle;
    }

    private void updateLights(GL3 gl) {
        if (light1On) {
            if (!lightsMap.containsKey("light1")) {
                lightsMap.put("light1",new Light(gl));
                lightsMap.get("light1").setCamera(camera);
                lightsMap.get("light1").setPosition(new Vec3(50f,15f,0f));
            }
        } else {
            lightsMap.remove("light1");
        }

        if (light2On) {
            if (!lightsMap.containsKey("light2")) {
                lightsMap.put("light2",new Light(gl));
                lightsMap.get("light2").setCamera(camera);
                lightsMap.get("light2").setPosition(new Vec3(-50f,-15f,0f));
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
