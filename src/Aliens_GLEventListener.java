import gmaths.*;

import com.jogamp.opengl.*;

public class Aliens_GLEventListener implements GLEventListener{

    private static final boolean DISPLAY_SHADERS = false;

    public Aliens_GLEventListener(Camera camera) {
        this.camera = camera;
        this.camera.setPosition(new Vec3(4f,12f,18f));
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
        light.dispose(gl);
        floor.dispose(gl);
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
    private Light light;

    private SGNode alienRoot;

    private Alien alien1, alien2;

    private void initialise(GL3 gl) {
        createRandomNumbers();

        textures = new TextureLibrary();
        textures.add(gl, "chequerboard", "textures/chequerboard.jpg");

        light = new Light(gl);
        light.setCamera(camera);

        // floor
        /*String name = "floor";
        Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
        Shader shader = new Shader(gl, "vs_standard.txt", "fs_standard_1t.txt");
        Material material = new Material(new Vec3(0.0f, 0.5f, 0.81f), new Vec3(0.0f, 0.5f, 0.81f), new Vec3(0.3f, 0.3f, 0.3f), 32.0f);
        Mat4 modelMatrix = Mat4Transform.scale(16,1f,16);
        floor = new Model(name, mesh, modelMatrix, shader, material, light, camera, textures.get("chequerboard"));*/

        alien1 = new Alien(gl, camera, light, textures.get("chequerboard"));

    }

    private void render(GL3 gl) {
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        light.setPosition(getLightPosition());  // changing light position each frame
        light.render(gl);
        //floor.render(gl);
        alien1.render(gl);
    }



    // The light's postion is continually being changed, so needs to be calculated for each frame.
    private Vec3 getLightPosition() {
        double elapsedTime = getSeconds()-startTime;
        float x = 5.0f*(float)(Math.sin(Math.toRadians(elapsedTime*50)));
        float y = 2.7f;
        float z = 5.0f*(float)(Math.cos(Math.toRadians(elapsedTime*50)));
        return new Vec3(x,y,z);
        //return new Vec3(5f,3.4f,5f);
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

}
