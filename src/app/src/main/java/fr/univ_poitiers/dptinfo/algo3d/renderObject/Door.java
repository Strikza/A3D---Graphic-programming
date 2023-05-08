package fr.univ_poitiers.dptinfo.algo3d.renderObject;

public class Door extends Mesh{
    /**
     * Constructor for a door
     */
    public Door() {
        super();

        vertexpos = new float[]{

                // Left quad //
                -3.f, 0.f, 0.f,
                -1.f, 0.f, 0.f,
                -1.f, 2.f, 0.f,
                -3.f, 2f, 0.f,

                // Right quad //
                1.f, 0.f, 0.f,
                3.f, 0.f, 0.f,
                3.f, 2f, 0.f,
                1.f, 2.f, 0.f,

                // High left quad //
                -3.f, 2f, 0.f,
                -1.f, 2.f, 0.f,
                -1.f, 2.5f, 0.f,
                -3.f, 2.5f, 0.f,

                // High middle quad //
                -1.f, 2.f, 0.f,
                1.f, 2.f, 0.f,
                1.f, 2.5f, 0.f,
                -1.f, 2.5f, 0.f,

                // High right quad //
                1.f, 2.f, 0.f,
                3.f, 2f, 0.f,
                3.f, 2.5f, 0.f,
                1.f, 2.5f, 0.f
        };

        normals = new float[] {

                // Left quad
                0.f, 0.f, 1.f,
                0.f, 0.f, 1.f,
                0.f, 0.f, 1.f,
                0.f, 0.f, 1.f,

                // Right quad
                0.f, 0.f, 1.f,
                0.f, 0.f, 1.f,
                0.f, 0.f, 1.f,
                0.f, 0.f, 1.f,

                // High left quad
                0.f, 0.f, 1.f,
                0.f, 0.f, 1.f,
                0.f, 0.f, 1.f,
                0.f, 0.f, 1.f,

                // High middle quad
                0.f, 0.f, 1.f,
                0.f, 0.f, 1.f,
                0.f, 0.f, 1.f,
                0.f, 0.f, 1.f,

                // High right quad
                0.f, 0.f, 1.f,
                0.f, 0.f, 1.f,
                0.f, 0.f, 1.f,
                0.f, 0.f, 1.f
        };

        textures = new float[] {

                // Left quad
                0.f, 1.f,
                1.f/3.f, 1.f,
                1.f/3.f, 0.5f/2.5f,
                0.f, 0.5f/2.5f,

                // Right quad
                2.f/3.f, 1.f,
                1.f, 1.f,
                1.f, 0.5f/2.5f,
                2.f/3.f, 0.5f/2.5f,

                // High left quad
                0.f, 0.5f/2.5f,
                1.f/3.f, 0.5f/2.5f,
                1.f/3.f, 0.f,
                0.f, 0.f,

                // High middle quad
                1.f/3.f, 0.5f/2.5f,
                2.f/3.f, 0.5f/2.5f,
                2.f/3.f, 0.f,
                1.f/3.f, 0.f,

                // High right quad
                2.f/3.f, 0.5f/2.5f,
                1.f, 0.5f/2.5f,
                1.f, 0.f,
                2.f/3.f, 0.f
        };


        triangles = new int[]{

                // Left quad
                0, 1, 2,
                3, 0, 2,

                // Right quad
                4, 5, 6,
                7, 4, 6,

                // High left quad
                8, 9, 10,
                11, 8, 10,

                // High middle quad
                12, 13, 14,
                15, 12, 14,

                // High right quad
                16, 17, 18,
                19, 16, 18
        };
    }
}
