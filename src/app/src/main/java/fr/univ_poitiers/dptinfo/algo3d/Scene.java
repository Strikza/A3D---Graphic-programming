package fr.univ_poitiers.dptinfo.algo3d;


import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import fr.univ_poitiers.dptinfo.algo3d.object.Ball;
import fr.univ_poitiers.dptinfo.algo3d.object.LoaderOBJ;
import fr.univ_poitiers.dptinfo.algo3d.object.Room;
import fr.univ_poitiers.dptinfo.algo3d.object.Square;

/**
 * Class to represent the scene. It includes all the objects to display, in this case a room
 * @author Philippe Meseure
 * @version 1.0
 */
public class Scene
{

    Context context;

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
    Ball ball;
    Ball ball2;
    Ball ball3;

    /**
     * OBJ loader
     */
    LoaderOBJ ldOBJ;

    /**
     * Square object
     */
    Square square;


    /**
     * Constructor : build each wall, the floor and the ceiling as quads
     */
    public Scene(Context c) {

        context = c;

        // Init observer's view angles
        angley = 0.F;
        anglex = 0.F;

        // Init observer's position
        posx = 0.F;
        posz = 0.F;

        // Set quarter an slice for all balls
        Ball.setQUARTER(50);
        Ball.setSLICE(50);

        // Construct objects
        room = new Room();
        ball = new Ball(1.2F, 0.F, -6.F);
        ball2 = new Ball(1.F, -1.8F, -1.8F);
        ball3 = new Ball(0.5F, 1.8F, -1.8F);
        ldOBJ = new LoaderOBJ(context, "cow.obj");
        square = new Square();
    }


    /**
     * Init some OpenGL and shaders uniform data to render the simulation scene
     * @param renderer Renderer
     */
    public void initGraphics(MyGLRenderer renderer)
    {
        room.initGraphics();
        ball.initGraphics();
        ball2.initGraphics();
        ball3.initGraphics();
        ldOBJ.initGraphics();
        square.initGraphics();

        MainActivity.log("Initializing graphics");
        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        // Allow back face culling !!
        GLES20.glEnable(GLES20.GL_CULL_FACE);

        GLES20.glDepthFunc(GLES20.GL_LESS);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        MainActivity.log("Graphics initialized");
    }

    public void step() {}


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


        // Set ModelView for objects
        room.setModelView(modelviewmatrix);
        ball.setModelView(modelviewmatrix);
        ball2.setModelView(modelviewmatrix);
        ball3.setModelView(modelviewmatrix);
        ldOBJ.setModelView(modelviewmatrix);
        square.setModelView(modelviewmatrix);


        // 1st room
        shaders.setColor(MyGLRenderer.white);
        room.drawFloor(shaders);
        shaders.setColor(MyGLRenderer.darkgray);
        room.drawCeiling(shaders);
        shaders.setColor(MyGLRenderer.magenta);
        room.drawWall(shaders);


        // 2nd room
        room.translate(0.F,0.F,-6.F);
        room.rotate(180.F,0.0F,1.0F,0.0F);

        shaders.setColor(MyGLRenderer.white);
        room.drawFloor(shaders);
        shaders.setColor(MyGLRenderer.darkgray);
        room.drawCeiling(shaders);
        shaders.setColor(MyGLRenderer.gray);
        room.drawWall(shaders);

        // Balls
        shaders.setColor(MyGLRenderer.yellow);
        ball.draw(shaders);
        shaders.setColor(MyGLRenderer.cyan);
        ball2.draw(shaders);
        shaders.setColor(MyGLRenderer.orange);
        ball3.draw(shaders);

        //OBJ
        shaders.setColor(MyGLRenderer.lightgray);
        ldOBJ.translate(0.F,1.F, 1.F);
        ldOBJ.scale(0.25F,0.25F, 0.25F);
        ldOBJ.draw(shaders);

        //Square
        shaders.setColor(MyGLRenderer.blue);
        square.translate(-2.9F, 0.0F, -3.1F);
        square.scale(0.8F, 0.8F, 0.8F);
        square.draw(shaders);

        shaders.setColor(MyGLRenderer.yellow);
        square.translate(-0.0F, 1.0F, 0.0F);
        square.scale(0.8F, 0.8F, 0.8F);
        square.draw(shaders);

        shaders.setColor(MyGLRenderer.green);
        square.translate(-0.0F, 1.0F, 0.0F);
        square.scale(0.8F, 0.8F, 0.8F);
        square.draw(shaders);

        shaders.setColor(MyGLRenderer.red);
        square.translate(-0.0F, 1.0F, 0.0F);
        square.scale(0.8F, 0.8F, 0.8F);
        square.draw(shaders);

        MainActivity.log("Rendering terminated.");
    }

    public void reset_pos(){
        angley = 0.F;
        anglex = 0.F;
        posx = 0.F;
        posz = 0.F;
    }
}
