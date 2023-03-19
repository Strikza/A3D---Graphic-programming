package fr.univ_poitiers.dptinfo.algo3d.renderObject;

/**
 * Class to represent a square
 * @author Samuel Goubeau
 */
public class Square extends Mesh {

    /**
     * Constructor for a square
     */
    public Square(){
        super(true);

        vertexpos = new float[]{

                0.0F, 0.0F, 0.0F,
                1.0F, 0.0F, 0.0F,
                1.0F, 1.0F, 0.0F,
                0.0F, 1.0F, 0.0F,
                0.0F, 0.0F, -1.0F,
                1.0F, 0.0F, -1.0F,
                1.0F, 1.0F, -1.0F,
                0.0F, 1.0F, -1.0F
        };

        triangles = new int[]{

                0, 1, 2,
                0, 2, 3,
                1, 5, 6,
                1, 6, 2,
                5, 4, 7,
                5, 7, 6,
                4, 0, 3,
                4, 3, 7,
                0, 4, 5,
                0, 5, 1,
                3, 2, 6,
                3, 6, 7
        };

        edges = new int[]{

                0, 1,
                1, 2,
                2, 3,
                3, 0,
                4, 5,
                5, 6,
                6, 7,
                7, 4,
                0, 4,
                1, 5,
                2, 6,
                3, 7
        };
    }
}
