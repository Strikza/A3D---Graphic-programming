package fr.univ_poitiers.dptinfo.algo3d;

public class Ball {

    private float radius;

    private float posx;
    private float posz;

    private Sphere s;

    public Ball(float r, float x, float z){

        radius = r;
        posx = x;
        posz = z;
        s = new Sphere((short)20, (short)20);
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
        s.draw(shaders);
    }
}
