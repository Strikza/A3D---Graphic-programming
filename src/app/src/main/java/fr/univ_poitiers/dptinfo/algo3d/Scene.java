package fr.univ_poitiers.dptinfo.algo3d;


import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

/**
 * Class to represent the scene. It includes all the objects to display, in this case a room
 * @author Philippe Meseure
 * @version 1.0
 */
public class Scene
{
    /**
     * A constant for the size of the wall
     */
    static final float wallsize=3.F;
    /**
     * 4 quads to represent the walls of the room
     */
    Quad wall1,wall2,wall3,wall4;

    /**
     * A quad to represent a floor
     */
    Quad floor;

    /**
     * A quad to represent the ceiling of the room
     */
    Quad ceiling;

    /**
     * An angle used to animate the viewer
     */
    float anglex,angley;

    /**
     * Current observer's position
     */
    float posx, posz;

    /**
     * Room object
     */
    Room room;

    /**
     * Sphere object
     */
    Sphere yellow_sphere;


    /**
     * Constructor : build each wall, the floor and the ceiling as quads
     */
    public Scene() {
        // Init observer's view angles
        angley = 0.F;
        anglex = 0.F;

        // Init observer's position
        posx = 0.F;
        posz = 0.F;


        Vec3f P0_b = new Vec3f(-3.f, 0.f, 3.f);
        Vec3f P1_b = new Vec3f(3.f, 0.f, 3.f);
        Vec3f P2_b = new Vec3f(3.f, 0.f, -3.f);
        Vec3f P3_b = new Vec3f(-3.f, 0.f, -3.f);

        Vec3f P0_t = new Vec3f(-3.f, 2.5f, 3.f);
        Vec3f P1_t = new Vec3f(3.f, 2.5f, 3.f);
        Vec3f P2_t = new Vec3f(3.f, 2.5f, -3.f);
        Vec3f P3_t = new Vec3f(-3.f, 2.5f, -3.f);


        // Create the front wall
        this.wall1 = new Quad(P3_b, P2_b, P2_t, P3_t);

        // Create the right wall
        this.wall2 = new Quad(P2_b, P1_b, P1_t, P2_t);

        // Create the left wall
        this.wall3 = new Quad(P1_b, P0_b, P0_t, P1_t);

        // create the back wall
        this.wall4 = new Quad(P0_b, P3_b, P3_t, P0_t);

        // Create the floor of the room
        this.floor = new Quad(P0_b, P1_b, P2_b, P3_b);

        // Create the ceiling of the room
        this.ceiling = new Quad(P3_t, P2_t, P1_t, P0_t);

        room = new Room();
        yellow_sphere = new Sphere((short) 20, (short) 20);
    }


    /**
     * Init some OpenGL and shaders uniform data to render the simulation scene
     * @param renderer Renderer
     */
    public void initGraphics(MyGLRenderer renderer)
    {
        room.initGraphics();
        yellow_sphere.initGraphics();

        MainActivity.log("Initializing graphics");
        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        // Allow back face culling !!
        GLES20.glEnable(GLES20.GL_CULL_FACE);

        GLES20.glDepthFunc(GLES20.GL_LESS);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        MainActivity.log("Graphics initialized");
    }

    public void step()
    {
        yellow_sphere.step();
    }


    /**
     * Draw the current simulation state
     * @param renderer Renderer
     */
    public void draw(MyGLRenderer renderer)
    {
        float[] modelviewmatrix=new float[16];

        MainActivity.log("Starting rendering");

        // Get shader to send uniform data
        NoLightShaders shaders=renderer.getShaders();

        // Place viewer in the right position and orientation
        Matrix.setIdentityM(modelviewmatrix,0);
        // setRotateM instead of rotateM in the next instruction would avoid this initialization...

        Matrix.rotateM(modelviewmatrix,0,anglex,1.0F,0.0F,0.0F);
        Matrix.rotateM(modelviewmatrix,0,angley,0.F,1.0F,0.0F);
        Matrix.translateM(modelviewmatrix,0,posx,0.F,posz);
        Matrix.translateM(modelviewmatrix,0,0.F,-1.6F,0.F);

        shaders.setColor(MyGLRenderer.blue);

        shaders.setModelViewMatrix(modelviewmatrix);

        // Draw walls, floor and ceil in selected colors
        shaders.setColor(MyGLRenderer.white);
        room.drawFloor(shaders);
        shaders.setColor(MyGLRenderer.darkgray);
        room.drawCeiling(shaders);
        shaders.setColor(MyGLRenderer.magenta);
        room.drawWall(shaders);

        shaders.setColor(MyGLRenderer.yellow);
        yellow_sphere.translate(modelviewmatrix, 0.F, 1.F, -6.F);
        yellow_sphere.scale(modelviewmatrix, 0, 1F, 1F, 1F);
        //yellow_sphere.scale(modelviewmatrix, 0, 4.F, 4.F, 4.F);
        yellow_sphere.rotate(modelviewmatrix, 0, 90, 1.F, 0.F, 0.F);
        yellow_sphere.draw(shaders, modelviewmatrix);

        // 2nd room
        Matrix.rotateM(modelviewmatrix,0,180.F,0.0F,1.0F,0.0F);
        Matrix.translateM(modelviewmatrix,0,0.F,0.F,6.F);

        shaders.setModelViewMatrix(modelviewmatrix);

        shaders.setColor(MyGLRenderer.white);
        room.drawFloor(shaders);
        shaders.setColor(MyGLRenderer.darkgray);
        room.drawCeiling(shaders);
        shaders.setColor(MyGLRenderer.gray);
        room.drawWall(shaders);

        MainActivity.log("Rendering terminated.");
    }

    public void reset_pos(){
        angley = 0.F;
        anglex = 0.F;
        posx = 0.F;
        posz = 0.F;
    }
}
