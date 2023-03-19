package fr.univ_poitiers.dptinfo.algo3d;


import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import fr.univ_poitiers.dptinfo.algo3d.renderObject.Ball;
import fr.univ_poitiers.dptinfo.algo3d.renderObject.Icosphere;
import fr.univ_poitiers.dptinfo.algo3d.renderObject.LoaderOBJ;
import fr.univ_poitiers.dptinfo.algo3d.renderObject.Room;
import fr.univ_poitiers.dptinfo.algo3d.renderObject.Square;
import fr.univ_poitiers.dptinfo.algo3d.renderObject.Torus;

/**
 * Class to represent the scene. It includes all the objects to display, in this case a room
 * @author Philippe Meseure
 * @version 1.0
 */
public class Scene
{

    /**
     * Context of the application
     */
    Context context;

    boolean isHitboxesAreActivated;

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
    Ball ball1;
    Ball ball2;

    /**
     * OBJ loader
     */
    LoaderOBJ drk_sword;
    LoaderOBJ drk_sword_lux;
    LoaderOBJ cow;
    LoaderOBJ toy_chest;
    LoaderOBJ turtle;
    LoaderOBJ pig;

    /**
     * Square object
     */
    Square support;
    Square square;

    /**
     * Icosphere object
     */
    Icosphere icosphere_div0;
    Icosphere icosphere_div2;
    Icosphere icosphere_div4;

    /**
     * Torus object
     */
    Torus torus_ring;
    Torus torus_horn;
    Torus torus_self_intersecting_spindle;


    /**
     * Constructor : build each wall, the floor and the ceiling as quads
     */
    public Scene(Context c) {

        context = c;

        isHitboxesAreActivated = false;

        // Init observer's view angles
        angley = 0.F;
        anglex = 0.F;

        // Init observer's position
        posx = 0.F;
        posz = 0.F;

        // Set quarter an slice for all balls
        Ball.setQUARTER(20);
        Ball.setSLICE(20);

        // Construct objects
        room = new Room();
        ball1 = new Ball(1.F, 1.8F, -1.8F);
        ball2 = new Ball(0.5F, 1.8F, 0.F);
        drk_sword = new LoaderOBJ(context, "drk_sword.obj");
        drk_sword_lux = new LoaderOBJ(context, "drk_sword_lux.obj");
        cow = new LoaderOBJ(context, "cow.obj");
        toy_chest = new LoaderOBJ(context, "toy_chest.obj");
        turtle = new LoaderOBJ(context, "turtle.obj");
        pig = new LoaderOBJ(context, "pig.obj");
        support = new Square();
        square = new Square();
        icosphere_div0 = new Icosphere(0);
        icosphere_div2 = new Icosphere(2);
        icosphere_div4 = new Icosphere(4);
        torus_ring = new Torus(49, 49,2.F, 1.F);
        torus_horn = new Torus(49, 49,1.F, 1.F);
        torus_self_intersecting_spindle = new Torus(49, 49,1.F, 2.F);
    }


    /**
     * Init some OpenGL and shaders uniform data to render the simulation scene
     * @param renderer Renderer
     */
    public void initGraphics(MyGLRenderer renderer)
    {
        room.initGraphics();
        ball1.initGraphics();
        ball2.initGraphics();
        drk_sword.initGraphics();
        drk_sword_lux.initGraphics();
        cow.initGraphics();
        toy_chest.initGraphics();
        turtle.initGraphics();
        pig.initGraphics();
        support.initGraphics();
        square.initGraphics();
        icosphere_div0.initGraphics();
        icosphere_div2.initGraphics();
        icosphere_div4.initGraphics();
        torus_ring.initGraphics();
        torus_horn.initGraphics();
        torus_self_intersecting_spindle.initGraphics();

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
        ball1.setModelView(modelviewmatrix);
        ball2.setModelView(modelviewmatrix);
        drk_sword.setModelView(modelviewmatrix);
        drk_sword_lux.setModelView(modelviewmatrix);
        cow.setModelView(modelviewmatrix);
        toy_chest.setModelView(modelviewmatrix);
        turtle.setModelView(modelviewmatrix);
        pig.setModelView(modelviewmatrix);
        support.setModelView(modelviewmatrix);
        square.setModelView(modelviewmatrix);
        icosphere_div0.setModelView(modelviewmatrix);
        icosphere_div2.setModelView(modelviewmatrix);
        icosphere_div4.setModelView(modelviewmatrix);
        torus_ring.setModelView(modelviewmatrix);
        torus_horn.setModelView(modelviewmatrix);
        torus_self_intersecting_spindle.setModelView(modelviewmatrix);


        // 1st room
        room.draw(
                shaders,
                MyGLRenderer.white,
                MyGLRenderer.darkgray,
                MyGLRenderer.magenta
        );


        // 2nd room
        room.translate(0.F,0.F,-6.F);
        room.rotate(180.F,0.0F,1.0F,0.0F);

        room.draw(
                shaders,
                MyGLRenderer.white,
                MyGLRenderer.darkgray,
                MyGLRenderer.gray
        );


        // Balls
        shaders.setColor(MyGLRenderer.cyan);
        ball1.draw(shaders);
        shaders.setColor(MyGLRenderer.orange);
        ball2.draw(shaders);


        //OBJ
        shaders.setColor(MyGLRenderer.lightgray);
        drk_sword.translate(-2.0F,1.8F, 2.9F);
        drk_sword.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        drk_sword.draw(shaders);

        shaders.setColor(MyGLRenderer.lightgray);
        drk_sword_lux.translate(-1.0F,1.8F, 2.9F);
        drk_sword_lux.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        drk_sword_lux.draw(shaders);

        shaders.setColor(MyGLRenderer.lightgray);
        cow.translate(1.8F, 0.91F, 1.8F);
        cow.rotate(135.F, 0.F, 1.F, 0.F);
        cow.scale(0.25F, 0.25f, 0.25f);
        cow.draw(shaders);

        shaders.setColor(MyGLRenderer.lightgray);
        toy_chest.translate(-1.5F,0.5F, 2.65F);
        toy_chest.rotate(180.0F, 0.0F, 1.0F, 0.0F);
        toy_chest.draw(shaders);

        shaders.setColor(MyGLRenderer.lightgray);
        turtle.translate(-2.65F, 1.96F, -3.35F);
        turtle.rotate(135.0F, 0.0F, 1.0F, 0.0F);
        turtle.draw(shaders);

        shaders.setColor(MyGLRenderer.lightgray);
        pig.translate(1.925F, 0.F, -4.065F);
        pig.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
        pig.scale(4.F, 4.F, 4.F);
        pig.draw(shaders);


        //Square
        shaders.setColor(MyGLRenderer.lightgray);
        support.translate(-1.75F, 0.0F, 2.4F);
        support.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
        support.scale(0.5F, 0.5F, 0.5F);
        support.draw(shaders);

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


        //Icosphere
        shaders.setColor(MyGLRenderer.yellow);
        icosphere_div0.translate(-2.F, 1.25F, -7.F);
        icosphere_div0.scale(0.75F, 0.75F, 0.75F);
        icosphere_div0.draw(shaders);

        shaders.setColor(MyGLRenderer.yellow);
        icosphere_div2.translate(0.F, 1.25F, -7.F);
        icosphere_div2.scale(0.75F, 0.75F, 0.75F);
        icosphere_div2.draw(shaders);

        shaders.setColor(MyGLRenderer.yellow);
        icosphere_div4.translate(2.F, 1.25F, -7.F);
        icosphere_div4.scale(0.75F, 0.75F, 0.75F);
        icosphere_div4.draw(shaders);


        //Torus
        shaders.setColor(MyGLRenderer.red);
        torus_ring.translate(-2.2F, 0.25F, -2.F);
        torus_ring.scale(0.25F,0.25F, 0.25F);
        torus_ring.draw(shaders);

        shaders.setColor(MyGLRenderer.white);
        torus_horn.translate(-2.2F, 0.25F, -0.5F);
        torus_horn.scale(0.25F,0.25F, 0.25F);
        torus_horn.draw(shaders);

        shaders.setColor(MyGLRenderer.blue);
        torus_self_intersecting_spindle.translate(-2.2F, 0.5F, 1.F);
        torus_self_intersecting_spindle.scale(0.25F,0.25F, 0.25F);
        torus_self_intersecting_spindle.draw(shaders);


        MainActivity.log("Rendering terminated.");
    }

    public void reset_pos(){
        angley = 0.F;
        anglex = 0.F;
        posx = 0.F;
        posz = 0.F;
    }
}
