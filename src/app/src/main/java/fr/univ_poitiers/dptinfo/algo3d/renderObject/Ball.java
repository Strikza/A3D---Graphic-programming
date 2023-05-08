package fr.univ_poitiers.dptinfo.algo3d.renderObject;

import fr.univ_poitiers.dptinfo.algo3d.shaders.LightingShaders;

/**
 * Class to represent a ball
 * @author Samuel Goubeau
 */
public class Ball {

    static final String LOG_TAG = "Ball";

    static public int SLICE = 30;
    static public int QUARTER = 30;

    private final float radius;
    private final float posx;
    private final float posz;
    private Sphere s;

    /**
     * Constructor for a ball
     * @param r : radius of the sphere
     * @param x : position on x
     * @param z : position on z
     */
    public Ball(float r, float x, float z){

        radius = r;
        posx = x;
        posz = z;
        s = new Sphere(SLICE, QUARTER);
    }

    /**
     * Initializes graphics by calling initGraphics from s
     */
    public void initGraphics() {

        s.initGraphics();
    }

    /**
     * Set the model view by calling setModelView from s
     */
    public void setModelView(final float[] modelviewmatrix) {

        s.setModelView(modelviewmatrix);
    }

    /**
     * Draw the sphere by calling draw from s, after all transformations needed
     * @param shaders : Shader to represent the mesh
     * @param texture : id of the texture to draw
     */
    public void draw(final LightingShaders shaders, int texture) {

        s.translate(posx, radius, posz);
        // Whithout this, the sphere isn't well oriented
        s.rotate(90, 1.F, 0.F, 0.F);
        s.scale(radius, radius, radius);
        s.draw(shaders, texture);
    }

    /**
     * Static function to change the number of slices will have
     * @param slice : new number of slices
     */
    public static void setSLICE(int slice){
        SLICE = slice;
    }

    /**
     * Static function to change the number of quarters will have
     * @param quarter : new number of quarters
     */
    public static void setQUARTER(int quarter){
        QUARTER = quarter;
    }
}
