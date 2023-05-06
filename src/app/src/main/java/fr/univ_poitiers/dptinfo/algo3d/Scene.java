package fr.univ_poitiers.dptinfo.algo3d;


import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import fr.univ_poitiers.dptinfo.algo3d.renderObject.Ball;
import fr.univ_poitiers.dptinfo.algo3d.renderObject.Crossroad;
import fr.univ_poitiers.dptinfo.algo3d.renderObject.Glass;
import fr.univ_poitiers.dptinfo.algo3d.renderObject.Icosphere;
import fr.univ_poitiers.dptinfo.algo3d.renderObject.LoaderOBJ;
import fr.univ_poitiers.dptinfo.algo3d.renderObject.Room;
import fr.univ_poitiers.dptinfo.algo3d.renderObject.Cube;
import fr.univ_poitiers.dptinfo.algo3d.renderObject.Torus;
import fr.univ_poitiers.dptinfo.algo3d.shaders.BlinnPhongShaders;
import fr.univ_poitiers.dptinfo.algo3d.shaders.LightingShaders;

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

    /**
     * to activate detection of hit boxes
     */
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
     * Crossroad object
     */
    Crossroad crossroad;

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
    LoaderOBJ eden;

    /**
     * Square object
     */
    Cube support;
    Cube cube;

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
     * Shader
     */
    BlinnPhongShaders shaders;

    /**
     * Loaded textures
     */
    private int basketball_tex;
    private int beachball_tex;
    private int ceiling_tex;
    private int donut_tex;
    private int drk_sword_tex;
    private int eden_tex;
    private  int marble2_tex;
    private  int pig_tex;
    private  int rust_tex;
    private  int stone_tex;
    private  int toy_chest_tex;
    private  int turtle_tex;
    private  int wall_tex;

    /**
     * Specular light
     */
    final float[] SPECULAR_LIGHT = MyGLRenderer.white;

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
        Ball.setQUARTER(40);
        Ball.setSLICE(40);

        // Construct objects
        room = new Room();
        crossroad = new Crossroad();
        ball1 = new Ball(1.F, 1.8F, -1.8F);
        ball2 = new Ball(0.5F, 1.8F, 0.F);
        drk_sword = new LoaderOBJ(context, "drk_sword.obj", true);
        drk_sword_lux = new LoaderOBJ(context, "drk_sword_lux.obj", true);
        cow = new LoaderOBJ(context, "cow.obj", false);
        toy_chest = new LoaderOBJ(context, "toy_chest.obj", true);
        turtle = new LoaderOBJ(context, "turtle.obj", true);
        pig = new LoaderOBJ(context, "pig.obj", true);
        eden = new LoaderOBJ(context, "eden.obj", true);
        support = new Cube(true);
        cube = new Cube(true);
        icosphere_div0 = new Icosphere(0, false);
        icosphere_div2 = new Icosphere(2, true);
        icosphere_div4 = new Icosphere(4, true);
        torus_ring = new Torus(49, 49,2.F, 1.F, true);
        torus_horn = new Torus(49, 49,1.F, 1.F, true);
        torus_self_intersecting_spindle = new Torus(49, 49,1.F, 2.F, true);
    }


    /**
     * Init some OpenGL and shaders uniform data to render the simulation scene
     * @param renderer Renderer
     */
    public void initGraphics(MyGLRenderer renderer){
        // Initialize all specificities of current shaders
        shaders = (BlinnPhongShaders) renderer.getShaders();
        shaders.setAmbiantLight(MyGLRenderer.darkgray);
        shaders.setLightColor(MyGLRenderer.lightgray);
        shaders.setLightSpecular(SPECULAR_LIGHT);
        shaders.setLightAttenuation(.5f, .1f, .1f);
        shaders.setLightPosition(new float[]{0.f, 0.f, 0.f});
        shaders.setLighting(true);

        // Load all textures
        basketball_tex = MyGLRenderer.loadTexture(context, R.drawable.basketball);
        beachball_tex = MyGLRenderer.loadTexture(context, R.drawable.beachball);
        ceiling_tex = MyGLRenderer.loadTexture(context, R.drawable.ceiling);
        donut_tex = MyGLRenderer.loadTexture(context, R.drawable.donut);
        drk_sword_tex = MyGLRenderer.loadTexture(context, R.drawable.drk_sword);
        eden_tex = MyGLRenderer.loadTexture(context, R.drawable.eden);
        marble2_tex = MyGLRenderer.loadTexture(context, R.drawable.marble2);
        pig_tex = MyGLRenderer.loadTexture(context, R.drawable.pig);
        rust_tex = MyGLRenderer.loadTexture(context, R.drawable.rust);
        stone_tex = MyGLRenderer.loadTexture(context, R.drawable.stone);
        toy_chest_tex = MyGLRenderer.loadTexture(context, R.drawable.toy_chest);
        turtle_tex = MyGLRenderer.loadTexture(context, R.drawable.turtle);
        wall_tex = MyGLRenderer.loadTexture(context, R.drawable.wall);

        // Initialize all objects
        room.initGraphics();
        crossroad.initGraphics();
        ball1.initGraphics();
        ball2.initGraphics();
        drk_sword.initGraphics();
        drk_sword_lux.initGraphics();
        cow.initGraphics();
        toy_chest.initGraphics();
        turtle.initGraphics();
        pig.initGraphics();
        eden.initGraphics();
        support.initGraphics();
        cube.initGraphics();
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

        // Place viewer in the right position and orientation
        Matrix.setIdentityM(modelviewmatrix,0);

        // setRotateM instead of rotateM in the next instruction would avoid this initialization...
        Matrix.rotateM(modelviewmatrix,0,anglex,1.0F,0.0F,0.0F);
        Matrix.rotateM(modelviewmatrix,0,angley,0.F,1.0F,0.0F);
        Matrix.translateM(modelviewmatrix,0,posx,0.F,posz);
        Matrix.translateM(modelviewmatrix,0,0.F,-1.6F,0.F);


          /////////////////
         // Mirror draw //
        /////////////////

        GLES20.glFrontFace(GLES20.GL_CW);
        Matrix.scaleM(modelviewmatrix, 0, 1.F, -1.F, 1.F);
        drawScene(shaders, modelviewmatrix);


          /////////////////
         // Direct draw //
        /////////////////

        GLES20.glFrontFace(GLES20.GL_CCW);
        Matrix.scaleM(modelviewmatrix, 0, 1.F, -1.F, 1.F);
        drawScene(shaders, modelviewmatrix);

        MainActivity.log("Rendering terminated.");
    }

    public void reset_pos(){
        angley = 0.F;
        anglex = 0.F;
        posx = 0.F;
        posz = 0.F;
    }

    public void drawScene(LightingShaders shaders, float[] modelviewmatrix){

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
        eden.setModelView(modelviewmatrix);
        support.setModelView(modelviewmatrix);
        cube.setModelView(modelviewmatrix);
        icosphere_div0.setModelView(modelviewmatrix);
        icosphere_div2.setModelView(modelviewmatrix);
        icosphere_div4.setModelView(modelviewmatrix);
        torus_ring.setModelView(modelviewmatrix);
        torus_horn.setModelView(modelviewmatrix);
        torus_self_intersecting_spindle.setModelView(modelviewmatrix);
        crossroad.setModelView(modelviewmatrix);


        // 1st room
        room.draw(
                shaders,
                MyGLRenderer.white_alpha,
                MyGLRenderer.lightgray,
                MyGLRenderer.magenta,
                marble2_tex,
                ceiling_tex,
                wall_tex
        );

        // 2nd room
        room.translate(0.F,0.F,-6.F);
        room.rotate(180.F,0.0F,1.0F,0.0F);

        room.draw(
                shaders,
                MyGLRenderer.white_alpha,
                MyGLRenderer.lightgray,
                MyGLRenderer.yellow,
                marble2_tex,
                ceiling_tex,
                wall_tex
        );


        // Balls
        shaders.setMaterialColor(MyGLRenderer.lightgray);
        shaders.setMaterialSpecular(SPECULAR_LIGHT);
        shaders.setMaterialShininess(1000);
        ball1.draw(shaders, stone_tex);

        shaders.setMaterialColor(MyGLRenderer.lightgray);
        shaders.setMaterialSpecular(SPECULAR_LIGHT);
        shaders.setMaterialShininess(100);
        ball2.draw(shaders, basketball_tex);


        //OBJ
        shaders.setMaterialColor(MyGLRenderer.lightgray);
        shaders.setMaterialSpecular(SPECULAR_LIGHT);
        shaders.setMaterialShininess(100);
        drk_sword.translate(-2.0F,1.8F, 2.9F);
        drk_sword.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        drk_sword.draw(shaders, drk_sword_tex);

        shaders.setMaterialColor(MyGLRenderer.lightgray);
        shaders.setMaterialSpecular(SPECULAR_LIGHT);
        shaders.setMaterialShininess(1000);
        drk_sword_lux.translate(-1.0F,1.8F, 2.9F);
        drk_sword_lux.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        drk_sword_lux.draw(shaders, rust_tex);

        shaders.setMaterialColor(MyGLRenderer.lightgray);
        shaders.setMaterialSpecular(SPECULAR_LIGHT);
        shaders.setMaterialShininess(100);
        cow.translate(1.8F, 0.91F, 1.8F);
        cow.rotate(135.F, 0.F, 1.F, 0.F);
        cow.scale(0.25F, 0.25f, 0.25f);
        cow.draw(shaders);

        shaders.setMaterialColor(MyGLRenderer.lightgray);
        shaders.setMaterialSpecular(SPECULAR_LIGHT);
        shaders.setMaterialShininess(100);
        toy_chest.translate(-1.5F,0.5F, 2.65F);
        toy_chest.rotate(180.0F, 0.0F, 1.0F, 0.0F);
        toy_chest.draw(shaders, toy_chest_tex);

        shaders.setMaterialColor(MyGLRenderer.lightgray);
        shaders.setMaterialSpecular(SPECULAR_LIGHT);
        shaders.setMaterialShininess(100);
        turtle.translate(-2.65F, 1.96F, -3.35F);
        turtle.rotate(135.0F, 0.0F, 1.0F, 0.0F);
        turtle.draw(shaders, turtle_tex);

        shaders.setMaterialColor(MyGLRenderer.lightgray);
        shaders.setMaterialSpecular(SPECULAR_LIGHT);
        shaders.setMaterialShininess(100);
        pig.translate(1.925F, 0.F, -4.065F);
        pig.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
        pig.scale(4.F, 4.F, 4.F);
        pig.draw(shaders, pig_tex);

        shaders.setMaterialColor(MyGLRenderer.lightgray);
        shaders.setMaterialSpecular(SPECULAR_LIGHT);
        shaders.setMaterialShininess(100);
        eden.translate(0.F, 1.F, -3.F);
        eden.scale(0.2F, 0.2F, 0.2F);
        eden.draw(shaders, eden_tex);


        //Square
        shaders.setMaterialColor(MyGLRenderer.lightgray);
        shaders.setMaterialSpecular(SPECULAR_LIGHT);
        shaders.setMaterialShininess(100);
        support.translate(-1.75F, 0.0F, 2.4F);
        support.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
        support.scale(0.5F, 0.5F, 0.5F);
        support.draw(shaders, stone_tex);

        shaders.setMaterialColor(MyGLRenderer.blue);
        shaders.setMaterialSpecular(SPECULAR_LIGHT);
        shaders.setMaterialShininess(100);
        cube.translate(-2.9F, 0.0F, -3.1F);
        cube.scale(0.8F, 0.8F, 0.8F);
        cube.draw(shaders, wall_tex);

        shaders.setMaterialColor(MyGLRenderer.yellow);
        shaders.setMaterialSpecular(SPECULAR_LIGHT);
        shaders.setMaterialShininess(100);
        cube.translate(-0.0F, 1.0F, 0.0F);
        cube.scale(0.8F, 0.8F, 0.8F);
        cube.draw(shaders, wall_tex);

        shaders.setMaterialColor(MyGLRenderer.green);
        shaders.setMaterialSpecular(SPECULAR_LIGHT);
        shaders.setMaterialShininess(100);
        cube.translate(-0.0F, 1.0F, 0.0F);
        cube.scale(0.8F, 0.8F, 0.8F);
        cube.draw(shaders, wall_tex);


        //Icosphere
        shaders.setMaterialColor(MyGLRenderer.lightgray);
        shaders.setMaterialSpecular(SPECULAR_LIGHT);
        shaders.setMaterialShininess(100);
        icosphere_div0.translate(-2.F, 1.25F, -7.F);
        icosphere_div0.scale(0.75F, 0.75F, 0.75F);
        icosphere_div0.draw(shaders);

        shaders.setMaterialColor(MyGLRenderer.lightgray);
        shaders.setMaterialSpecular(SPECULAR_LIGHT);
        shaders.setMaterialShininess(100);
        icosphere_div2.translate(0.F, 1.25F, -7.F);
        icosphere_div2.scale(0.75F, 0.75F, 0.75F);
        icosphere_div2.draw(shaders, beachball_tex);

        shaders.setMaterialColor(MyGLRenderer.lightgray);
        shaders.setMaterialSpecular(SPECULAR_LIGHT);
        shaders.setMaterialShininess(100);
        icosphere_div4.translate(2.F, 1.25F, -7.F);
        icosphere_div4.scale(0.75F, 0.75F, 0.75F);
        icosphere_div4.draw(shaders, beachball_tex);


        //Torus
        shaders.setMaterialColor(MyGLRenderer.lightgray);
        shaders.setMaterialSpecular(SPECULAR_LIGHT);
        shaders.setMaterialShininess(100);
        torus_ring.translate(-2.2F, 0.25F, -2.F);
        torus_ring.scale(0.25F,0.25F, 0.25F);
        torus_ring.draw(shaders, donut_tex);

        shaders.setMaterialColor(MyGLRenderer.lightgray);
        shaders.setMaterialSpecular(SPECULAR_LIGHT);
        shaders.setMaterialShininess(100);
        torus_horn.translate(-2.2F, 0.25F, -0.5F);
        torus_horn.scale(0.25F,0.25F, 0.25F);
        torus_horn.draw(shaders, donut_tex);

        shaders.setMaterialColor(MyGLRenderer.lightgray);
        shaders.setMaterialSpecular(SPECULAR_LIGHT);
        shaders.setMaterialShininess(100);
        torus_self_intersecting_spindle.translate(-2.2F, 0.5F, 1.F);
        torus_self_intersecting_spindle.scale(0.25F,0.25F, 0.25F);
        torus_self_intersecting_spindle.draw(shaders, donut_tex);
    }
}
