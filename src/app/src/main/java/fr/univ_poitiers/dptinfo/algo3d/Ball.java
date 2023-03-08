package fr.univ_poitiers.dptinfo.algo3d;

public class Ball {

    static public int SLICE = 30;
    static public int QUARTER = 30;

    private float radius;
    private float posx;
    private float posz;
    private Sphere s;

    public Ball(float r, float x, float z){

        radius = r;
        posx = x;
        posz = z;
        s = new Sphere(SLICE, QUARTER);
    }

    public void initGraphic(){

        s.initGraphics();
    }

    public void setModelView(final float[] modelviewmatrix) {

        s.setModelView(modelviewmatrix);
    }

    public void draw(final NoLightShaders shaders) {

        s.translate(posx, radius, posz);
        // Whithout this, the sphere isn't well oriented
        s.rotate(90, 1.F, 0.F, 0.F);
        s.scale(radius, radius, radius);
        s.draw(shaders);
    }

    static void setSLICE(int s){
        SLICE = s;
    }

    static void setQUARTER(int q){
        QUARTER = q;
    }
}
