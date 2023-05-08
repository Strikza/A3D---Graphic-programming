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
     * An angle used to animate the viewer
     */
    float anglex,angley;
    float dynamic_angle;

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

    /**
     * Glass object
     */
    Glass glass;

    /**
     * Shader
     */
    BlinnPhongShaders shaders;

    /**
     * Loaded textures
     */
    private int autumn_leaf_tex;
    private int basketball_tex;
    private int beach_tex;
    private int beachball_tex;
    private int buoy_tex;
    private int ceiling_tex;
    private int default_tex;
    private int donut_tex;
    private int drk_sword_tex;
    private int earth_tex;
    private int eden_tex;
    private int forest_tex;
    private int jupiter_tex;
    private  int marble1_tex;
    private  int marble2_tex;
    private  int pig_tex;
    private  int ruin_tex;
    private  int rust_tex;
    private  int sky_tex;
    private  int sky_above_tex;
    private  int space_tex;
    private  int stone_tex;
    private  int tiles1_tex;
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

        // Init observer's view angles
        angley = 0.F;
        anglex = 0.F;
        dynamic_angle = 0.F;
        // Init observer's position
        posx = 0.F;
        posz = 0.F;

        // Set quarter an slice for all balls
        Ball.setQUARTER(40);
        Ball.setSLICE(40);

        // Construct objects
        room = new Room();
        crossroad = new Crossroad();
        ball1 = new Ball(0.5F, 8.F, 0.F);
        ball2 = new Ball(0.3F, 6.5F, 1.8F);
        drk_sword = new LoaderOBJ(context, "drk_sword.obj");
        drk_sword_lux = new LoaderOBJ(context, "drk_sword_lux.obj");
        toy_chest = new LoaderOBJ(context, "toy_chest.obj");
        turtle = new LoaderOBJ(context, "turtle.obj");
        pig = new LoaderOBJ(context, "pig.obj");
        eden = new LoaderOBJ(context, "eden.obj");
        support = new Cube();
        cube = new Cube();
        icosphere_div0 = new Icosphere(0);
        icosphere_div2 = new Icosphere(2);
        icosphere_div4 = new Icosphere(4);
        torus_ring = new Torus(49, 49,2.F, 1.F);
        glass = new Glass();
    }


    /**
     * Init some OpenGL and shaders uniform data to render the simulation scene
     * @param renderer Renderer
     */
    public void initGraphics(MyGLRenderer renderer){
        // Initialize all specificities of current shaders
        shaders = (BlinnPhongShaders) renderer.getShaders();
        shaders.setAmbiantLight(MyGLRenderer.gray);
        shaders.setLightColor(MyGLRenderer.lightgray);
        shaders.setLightSpecular(SPECULAR_LIGHT);
        shaders.setLightAttenuation(.5f, .1f, .1f);
        shaders.setLightPosition(new float[]{0.f, 0.f, 0.f});
        shaders.setLighting(true);

        // Load all textures
        autumn_leaf_tex = MyGLRenderer.loadTexture(context, R.drawable.autumn_leaf);
        basketball_tex = MyGLRenderer.loadTexture(context, R.drawable.basketball);
        beach_tex = MyGLRenderer.loadTexture(context, R.drawable.beach);
        beachball_tex = MyGLRenderer.loadTexture(context, R.drawable.beachball);
        buoy_tex = MyGLRenderer.loadTexture(context, R.drawable.buoy);
        ceiling_tex = MyGLRenderer.loadTexture(context, R.drawable.ceiling);
        default_tex = MyGLRenderer.loadTexture(context, R.drawable.default_texture);
        donut_tex = MyGLRenderer.loadTexture(context, R.drawable.donut);
        drk_sword_tex = MyGLRenderer.loadTexture(context, R.drawable.drk_sword);
        earth_tex = MyGLRenderer.loadTexture(context, R.drawable.earth);
        eden_tex = MyGLRenderer.loadTexture(context, R.drawable.eden);
        forest_tex = MyGLRenderer.loadTexture(context, R.drawable.forest);
        jupiter_tex = MyGLRenderer.loadTexture(context, R.drawable.jupiter);
        marble1_tex = MyGLRenderer.loadTexture(context, R.drawable.marble1);
        marble2_tex = MyGLRenderer.loadTexture(context, R.drawable.marble2);
        pig_tex = MyGLRenderer.loadTexture(context, R.drawable.pig);
        ruin_tex = MyGLRenderer.loadTexture(context, R.drawable.ruin);
        rust_tex = MyGLRenderer.loadTexture(context, R.drawable.rust);
        sky_tex = MyGLRenderer.loadTexture(context, R.drawable.sky);
        sky_above_tex = MyGLRenderer.loadTexture(context, R.drawable.sky_above);
        space_tex = MyGLRenderer.loadTexture(context, R.drawable.space);
        stone_tex = MyGLRenderer.loadTexture(context, R.drawable.stone);
        tiles1_tex = MyGLRenderer.loadTexture(context, R.drawable.tiles1);
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
        glass.initGraphics();

        MainActivity.log("Initializing graphics");
        // Set the background frame color
        GLES20.glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
        // Allow back face culling !!
        GLES20.glEnable(GLES20.GL_CULL_FACE);

        GLES20.glDepthFunc(GLES20.GL_LESS);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        MainActivity.log("Graphics initialized");
    }

    public void step() {
        dynamic_angle = (dynamic_angle + 0.5f) % 360.f;
    }


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
        crossroad.setModelView(modelviewmatrix);
        ball1.setModelView(modelviewmatrix);
        ball2.setModelView(modelviewmatrix);
        drk_sword.setModelView(modelviewmatrix);
        drk_sword_lux.setModelView(modelviewmatrix);
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
        glass.setModelView(modelviewmatrix);

        // Crossroad //
        crossroad.draw(
                shaders,
                MyGLRenderer.white_alpha,
                MyGLRenderer.lightgray,
                MyGLRenderer.yellow,
                marble1_tex,
                ceiling_tex,
                wall_tex
        );


        // Room 1 - Nature //
        room.translate(0.f,0.f, 6.f);

        room.draw(
                shaders,
                MyGLRenderer.white_alpha,
                MyGLRenderer.lightgray,
                MyGLRenderer.lightgray,
                autumn_leaf_tex,
                sky_above_tex,
                forest_tex
        );

        shaders.setMaterialColor(MyGLRenderer.lightgray);
        shaders.setMaterialSpecular(SPECULAR_LIGHT);
        shaders.setMaterialShininess(100);
        pig.translate(-1.2f, 0.75f, 8.f);
        pig.rotate(90.f, 0.f, 1.f, 0.f);
        pig.draw(shaders, pig_tex);

        shaders.setMaterialColor(MyGLRenderer.lightgray);
        shaders.setMaterialSpecular(SPECULAR_LIGHT);
        shaders.setMaterialShininess(100);
        pig.translate(0.5f, -0.45f, -0.5f);
        pig.rotate(45.f, 0.f, 1.f, 0.f);
        pig.draw(shaders, pig_tex);

        shaders.setMaterialColor(MyGLRenderer.lightgray);
        shaders.setMaterialSpecular(SPECULAR_LIGHT);
        shaders.setMaterialShininess(100);
        pig.translate(0.5f, 0.35f, -0.5f);
        pig.rotate(45.f, 0.f, 1.f, 0.f);
        pig.draw(shaders, pig_tex);

        shaders.setMaterialColor(MyGLRenderer.lightgray);
        shaders.setMaterialSpecular(SPECULAR_LIGHT);
        shaders.setMaterialShininess(100);
        pig.translate(-0.5f, 0.25f, -0.8f);
        pig.rotate(-45.f, 0.f, 1.f, 0.f);
        pig.scale(3.f, 3.f, 3.f);
        pig.draw(shaders, pig_tex);

        shaders.setMaterialColor(MyGLRenderer.lightgray);
        shaders.setMaterialSpecular(SPECULAR_LIGHT);
        shaders.setMaterialShininess(100);
        turtle.translate(1.2f, 0.f, 7.5f);
        turtle.rotate(-120.f, 0.f, 1.f, 0.f);
        turtle.scale(4.5f, 4.5f, 4.5f);
        turtle.draw(shaders, turtle_tex);

        shaders.setMaterialColor(MyGLRenderer.darkgray);
        shaders.setMaterialSpecular(SPECULAR_LIGHT);
        shaders.setMaterialShininess(1000);
        support.translate(-3.f, 0.f, 6.f);
        support.scale(6.f, 0.2f, 0.2f);
        support.draw(shaders, stone_tex);

        shaders.setMaterialColor(MyGLRenderer.glass);
        shaders.setMaterialSpecular(SPECULAR_LIGHT);
        shaders.setMaterialShininess(10000);
        glass.translate(3.f, 0.f, 5.9f);
        glass.rotate(180, 0.f, 1.f, 0.f);
        glass.scale(6.f, 3.f, 1.f);
        glass.draw(shaders, marble2_tex);


        // Room 2 - Space //
        room.rotate(180, 0.f,1.f,0.f);
        room.translate(0.f,0.f, 12.f);

        room.draw(
                shaders,
                MyGLRenderer.white_alpha,
                MyGLRenderer.white,
                MyGLRenderer.white,
                stone_tex,
                space_tex,
                space_tex
        );

        shaders.setMaterialColor(MyGLRenderer.lightgray);
        shaders.setMaterialSpecular(SPECULAR_LIGHT);
        shaders.setMaterialShininess(100);
        icosphere_div4.translate(1.5f, 1.25f, -7.5f);
        icosphere_div4.rotate(90.f, 1.f, 0.f, 0.f);
        icosphere_div4.rotate(dynamic_angle, 0.f, 0.f, 1.f);
        icosphere_div4.draw(shaders, jupiter_tex);

        shaders.setMaterialColor(MyGLRenderer.darkgray);
        shaders.setMaterialSpecular(SPECULAR_LIGHT);
        shaders.setMaterialShininess(100);
        icosphere_div0.translate(1.5f, 1.25f, -7.5f);
        icosphere_div0.rotate(20.f, 0.f, 0.f, 1.f);
        icosphere_div0.rotate(2*dynamic_angle, 0.f, 1.f, 0.f);
        icosphere_div0.translate(1.2f, 0.f, 0.f);
        icosphere_div0.scale(0.05f, 0.05f, 0.05f);
        icosphere_div0.draw(shaders, stone_tex);

        shaders.setMaterialColor(MyGLRenderer.darkgray);
        shaders.setMaterialSpecular(SPECULAR_LIGHT);
        shaders.setMaterialShininess(100);
        icosphere_div0.translate(0.f, 0.2f, 0.f);
        icosphere_div0.draw(shaders, rust_tex);

        shaders.setMaterialColor(MyGLRenderer.lightgray);
        shaders.setMaterialSpecular(SPECULAR_LIGHT);
        shaders.setMaterialShininess(100);
        icosphere_div4.setModelView(modelviewmatrix);
        icosphere_div4.translate(-1.5f, 1.25f, -7.5f);
        icosphere_div4.rotate(90.f, 1.f, 0.f, 0.f);
        icosphere_div4.rotate(dynamic_angle, 0.f, 0.f, 1.f);
        icosphere_div4.scale(0.5f, 0.5f, 0.5f);
        icosphere_div4.draw(shaders, earth_tex);

        shaders.setMaterialColor(MyGLRenderer.lightgray);
        shaders.setMaterialSpecular(SPECULAR_LIGHT);
        shaders.setMaterialShininess(100);
        icosphere_div4.translate(2.f, 0.f, 0.f);
        icosphere_div4.scale(0.25f, 0.25f, 0.25f);
        icosphere_div4.rotate(-2*dynamic_angle, 0.f, 0.f, 1.f);
        icosphere_div4.draw(shaders, stone_tex);

        shaders.setMaterialColor(MyGLRenderer.lightgray);
        shaders.setMaterialSpecular(SPECULAR_LIGHT);
        shaders.setMaterialShininess(100);
        eden.translate(0.f, 1.5f, -8.f);
        eden.scale(0.1f, 0.1f, 0.1f);
        eden.draw(shaders, eden_tex);

        shaders.setMaterialColor(MyGLRenderer.darkgray);
        shaders.setMaterialSpecular(SPECULAR_LIGHT);
        shaders.setMaterialShininess(1000);
        support.setModelView(modelviewmatrix);
        support.translate(-3.f, 0.f, -5.9f);
        support.scale(6.f, 0.2f, 0.2f);
        support.draw(shaders, stone_tex);

        shaders.setMaterialColor(MyGLRenderer.glass);
        shaders.setMaterialSpecular(SPECULAR_LIGHT);
        shaders.setMaterialShininess(10000);
        glass.setModelView(modelviewmatrix);
        glass.translate(-3.f, 0.f, -6.f);
        glass.scale(6.f, 3.f, 1.f);
        glass.draw(shaders, marble2_tex);


        // Room 3 - Legends //
        room.rotate(90, 0.f,1.f,0.f);
        room.translate(6.f,0.f, 6.f);

        room.draw(
                shaders,
                MyGLRenderer.white_alpha,
                MyGLRenderer.darkgray,
                MyGLRenderer.lightgray,
                marble2_tex,
                sky_above_tex,
                ruin_tex
        );

        shaders.setMaterialColor(MyGLRenderer.lightgray);
        shaders.setMaterialSpecular(SPECULAR_LIGHT);
        shaders.setMaterialShininess(100);
        icosphere_div2.setModelView(modelviewmatrix);
        icosphere_div2.translate(-9.f, 1.5f, 3.f);
        icosphere_div2.scale(1.5f, 1.5f, 1.5f);
        icosphere_div2.draw(shaders, ruin_tex);

        shaders.setMaterialColor(MyGLRenderer.lightgray);
        shaders.setMaterialSpecular(SPECULAR_LIGHT);
        shaders.setMaterialShininess(100);
        icosphere_div2.translate(0.f, 0.f, -4.f);
        icosphere_div2.draw(shaders, ruin_tex);

        shaders.setMaterialColor(MyGLRenderer.darkgray);
        shaders.setMaterialSpecular(SPECULAR_LIGHT);
        shaders.setMaterialShininess(100);
        cube.translate(-7.9f, 0.f, 0.5f);
        cube.scale(0.2f, 0.25f, 1.f);
        cube.draw(shaders, tiles1_tex);

        shaders.setMaterialColor(MyGLRenderer.lightgray);
        shaders.setMaterialSpecular(SPECULAR_LIGHT);
        shaders.setMaterialShininess(100);
        drk_sword.translate(-7.8f, 1.5f, 0.f);
        drk_sword.rotate(180.f, 1.f, 0.f, 1.f);
        drk_sword.draw(shaders, drk_sword_tex);

        shaders.setMaterialColor(MyGLRenderer.lightgray);
        shaders.setMaterialSpecular(SPECULAR_LIGHT);
        shaders.setMaterialShininess(100);
        drk_sword_lux.translate(-8.9f, 1.5f, -1.47f);
        drk_sword_lux.rotate(180.f, 1.f, 0.f, 0.f);
        drk_sword_lux.rotate(-45.f, 0.f, 1.f, 0.f);
        drk_sword_lux.rotate(-10.f, 1.f, 0.f, 0.f);
        drk_sword_lux.draw(shaders, rust_tex);

        shaders.setMaterialColor(MyGLRenderer.darkgray);
        shaders.setMaterialSpecular(SPECULAR_LIGHT);
        shaders.setMaterialShininess(1000);
        support.setModelView(modelviewmatrix);
        support.translate(-5.9f, 0.f, -3.f);
        support.rotate(-90.f, 0.f, 1.f, 0.f);
        support.scale(6.f, 0.2f, 0.2f);
        support.draw(shaders, stone_tex);

        shaders.setMaterialColor(MyGLRenderer.glass);
        shaders.setMaterialSpecular(SPECULAR_LIGHT);
        shaders.setMaterialShininess(10000);
        glass.setModelView(modelviewmatrix);
        glass.translate(-5.8f, 0.f, 3.f);
        glass.rotate(90.f, 0.f, 1.f, 0.f);
        glass.scale(6.f, 3.f, 1.f);
        glass.draw(shaders, marble2_tex);


        // Room 4 - Beach //
        room.rotate(180, 0.f,1.f,0.f);
        room.translate(0.f,0.f, 12.f);

        room.draw(
                shaders,
                MyGLRenderer.white_alpha,
                MyGLRenderer.white,
                MyGLRenderer.lightgray,
                beach_tex,
                sky_above_tex,
                sky_tex
        );

        shaders.setMaterialColor(MyGLRenderer.lightgray);
        shaders.setMaterialSpecular(SPECULAR_LIGHT);
        shaders.setMaterialShininess(1000);
        support.setModelView(modelviewmatrix);
        support.translate(7.4f, 1.54f, 3.1f);
        support.scale(0.2f, 0.2f, 0.5f);
        support.draw(shaders, stone_tex);

        shaders.setMaterialColor(MyGLRenderer.lightgray);
        shaders.setMaterialSpecular(SPECULAR_LIGHT);
        shaders.setMaterialShininess(100);
        torus_ring.translate(7.5f, 1.5f, 2.8f);
        torus_ring.rotate(-90.f, 1.f, 0.f, 0.f);
        torus_ring.scale(0.25f, 0.2f, 0.25f);
        torus_ring.draw(shaders, buoy_tex);

        shaders.setMaterialColor(MyGLRenderer.lightgray);
        shaders.setMaterialSpecular(SPECULAR_LIGHT);
        shaders.setMaterialShininess(100);
        ball1.draw(shaders, beachball_tex);

        shaders.setMaterialColor(MyGLRenderer.lightgray);
        shaders.setMaterialSpecular(SPECULAR_LIGHT);
        shaders.setMaterialShininess(100);
        ball2.draw(shaders, basketball_tex);

        shaders.setMaterialColor(MyGLRenderer.lightgray);
        shaders.setMaterialSpecular(SPECULAR_LIGHT);
        shaders.setMaterialShininess(1000);
        support.setModelView(modelviewmatrix);
        support.translate(7.f, 0.f, -1.f);
        support.scale(1.f, 0.5f, 1.f);
        support.draw(shaders, wall_tex);

        shaders.setMaterialColor(MyGLRenderer.lightgray);
        shaders.setMaterialSpecular(SPECULAR_LIGHT);
        shaders.setMaterialShininess(1000);
        torus_ring.setModelView(modelviewmatrix);
        torus_ring.translate(7.6f, 0.6f, -1.6f);
        torus_ring.scale(0.1f, 0.1f, 0.1f);
        torus_ring.draw(shaders, donut_tex);

        shaders.setMaterialColor(MyGLRenderer.lightgray);
        shaders.setMaterialSpecular(SPECULAR_LIGHT);
        shaders.setMaterialShininess(1000);
        torus_ring.translate(-3.f, 1.f, 3.f);
        torus_ring.rotate(25.f, 1.f, 0.f, 1.f);
        torus_ring.draw(shaders, donut_tex);

        shaders.setMaterialColor(MyGLRenderer.lightgray);
        shaders.setMaterialSpecular(SPECULAR_LIGHT);
        shaders.setMaterialShininess(100);
        toy_chest.translate(6.5f, 0.f, -2.5f);
        toy_chest.rotate(-45.f, 0.f, 1.f, 0.f);
        toy_chest.draw(shaders, toy_chest_tex);

        shaders.setMaterialColor(MyGLRenderer.darkgray);
        shaders.setMaterialSpecular(SPECULAR_LIGHT);
        shaders.setMaterialShininess(1000);
        support.setModelView(modelviewmatrix);
        support.translate(5.9f, 0.f, 3.f);
        support.rotate(90.f, 0.f, 1.f, 0.f);
        support.scale(6.f, 0.2f, 0.2f);
        support.draw(shaders, stone_tex);

        shaders.setMaterialColor(MyGLRenderer.glass);
        shaders.setMaterialSpecular(SPECULAR_LIGHT);
        shaders.setMaterialShininess(10000);
        glass.setModelView(modelviewmatrix);
        glass.translate(5.8f, 0.f, -3.f);
        glass.rotate(-90.f, 0.f, 1.f, 0.f);
        glass.scale(6.f, 3.f, 1.f);
        glass.draw(shaders, marble2_tex);


        // No Textures ?
        shaders.setMaterialColor(MyGLRenderer.lightgray);
        shaders.setMaterialSpecular(SPECULAR_LIGHT);
        shaders.setMaterialShininess(1000);
        cube.setModelView(modelviewmatrix);
        cube.translate(-2.9f, 0.f, 2.9f);
        cube.draw(shaders, default_tex);

        cube.translate(0.f, 1.f, 0.f);
        cube.scale(0.8f, 0.8f, 0.8f);
        cube.draw(shaders, default_tex);

        cube.translate(0.f, 1.f, 0.f);
        cube.scale(0.8f, 0.8f, 0.8f);
        cube.draw(shaders, default_tex);
    }
}
