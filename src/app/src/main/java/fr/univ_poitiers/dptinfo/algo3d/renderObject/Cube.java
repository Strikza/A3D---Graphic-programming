package fr.univ_poitiers.dptinfo.algo3d.renderObject;

/**
 * Class to represent a square
 * @author Samuel Goubeau
 */
public class Cube extends Mesh {

    static final String LOG_TAG = "Cube";

    /**
     * Constructor for a square
     */
    public Cube(boolean hasTexture){
        super(hasTexture);

        vertexpos = new float[]{

                0.0F, 0.0F, 0.0F,
                1.0F, 0.0F, 0.0F,
                1.0F, 1.0F, 0.0F,
                0.0F, 1.0F, 0.0F,

                1.0F, 0.0F, 0.0F,
                1.0F, 0.0F, -1.0F,
                1.0F, 1.0F, -1.0F,
                1.0F, 1.0F, 0.0F,

                1.0F, 0.0F, -1.0F,
                0.0F, 0.0F, -1.0F,
                0.0F, 1.0F, -1.0F,
                1.0F, 1.0F, -1.0F,

                0.0F, 0.0F, -1.0F,
                0.0F, 0.0F, 0.0F,
                0.0F, 1.0F, 0.0F,
                0.0F, 1.0F, -1.0F,

                0.0F, 0.0F, 0.0F,
                0.0F, 0.0F, -1.0F,
                1.0F, 0.0F, -1.0F,
                1.0F, 0.0F, 0.0F,

                0.0F, 1.0F, 0.0F,
                1.0F, 1.0F, 0.0F,
                1.0F, 1.0F, -1.0F,
                0.0F, 1.0F, -1.0F
        };

        normals = new float[]{

                0.f, 0.f, 1.f,
                0.f, 0.f, 1.f,
                0.f, 0.f, 1.f,
                0.f, 0.f, 1.f,

                1.f, 0.f, 0.f,
                1.f, 0.f, 0.f,
                1.f, 0.f, 0.f,
                1.f, 0.f, 0.f,

                0.f, 0.f, -1.f,
                0.f, 0.f, -1.f,
                0.f, 0.f, -1.f,
                0.f, 0.f, -1.f,

                -1.f, 0.f, 0.f,
                -1.f, 0.f, 0.f,
                -1.f, 0.f, 0.f,
                -1.f, 0.f, 0.f,

                0.f, -1.f, 0.f,
                0.f, -1.f, 0.f,
                0.f, -1.f, 0.f,
                0.f, -1.f, 0.f,

                0.f, 1.f, 0.f,
                0.f, 1.f, 0.f,
                0.f, 1.f, 0.f,
                0.f, 1.f, 0.f
        };

        textures = new float[] {

                0.f, 1.f,
                1.f, 1.f,
                1.f, 0.f,
                0.f, 0.f,

                0.f, 1.f,
                1.f, 1.f,
                1.f, 0.f,
                0.f, 0.f,

                0.f, 1.f,
                1.f, 1.f,
                1.f, 0.f,
                0.f, 0.f,

                0.f, 1.f,
                1.f, 1.f,
                1.f, 0.f,
                0.f, 0.f,

                0.f, 0.f,
                0.f, 1.f,
                1.f, 1.f,
                1.f, 0.f,

                0.f, 0.f,
                0.f, 1.f,
                1.f, 1.f,
                1.f, 0.f,
        };

        triangles = new int[]{

                0, 1, 2,
                0, 2, 3,

                4, 5, 6,
                4, 6, 7,

                8, 9, 10,
                8, 10, 11,

                12, 13, 14,
                12, 14, 15,

                16, 17, 18,
                16, 18, 19,

                20, 21, 22,
                20, 22, 23
        };
    }
}
