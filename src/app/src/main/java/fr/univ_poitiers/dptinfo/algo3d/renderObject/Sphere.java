package fr.univ_poitiers.dptinfo.algo3d.renderObject;

/**
 * Class to represent a sphere
 * @author Samuel Goubeau
 */
public class Sphere extends Mesh {

    /**
     * Constructor for a sphere
     * @param slice : number of slices
     * @param quarter : number of quarters
     */
    Sphere(int slice, int quarter, boolean hasTexture) {
        super(hasTexture);

        float phi = 360 / (float) quarter;
        float theta = 180 / (float) slice;

        /*
         * Parametric equation :
         *
         * x = r * cos(phi) * cos(theta)
         * y = r * sin(phi) * cos(theta)
         * z = r * sin(theta)
         *
         * here => r == 1.0F
         */
        int nbVertices = ((slice - 1) * quarter) + 2;
        vertexpos = new float[nbVertices * 3];
        normals = new float[nbVertices * 3];
        textures = new float[nbVertices * 2];
        int vertexIndex = 0;
        int textureIndex = 0;

        float x_temp;
        float y_temp;
        float z_temp;

        for (float s = -90.F + theta; s < 90.F; s += theta) {

            double thetaRad = Math.toRadians(s);
            for (float q = 0.F; q < 360.F; q += phi) {

                double phiRad = Math.toRadians(q);
                x_temp = (float) (Math.cos(phiRad) * Math.cos(thetaRad));
                y_temp = (float) (Math.sin(phiRad) * Math.cos(thetaRad));
                z_temp = (float) Math.sin(thetaRad);

                vertexpos[vertexIndex] = x_temp;
                vertexpos[vertexIndex + 1] = y_temp;
                vertexpos[vertexIndex + 2] = z_temp;

                normals[vertexIndex] = x_temp;
                normals[vertexIndex + 1] = y_temp;
                normals[vertexIndex + 2] = z_temp;

                textures[textureIndex] = (float) (phiRad / (2 * Math.PI));
                textures[textureIndex + 1] = (float) (thetaRad / Math.PI + 0.5);

                vertexIndex += 3;
                textureIndex += 2;
            }
        }

        // South pole
        x_temp = (float) (Math.cos(Math.toRadians(0)) * Math.cos(Math.toRadians(-90)));
        y_temp = (float) (Math.sin(Math.toRadians(0)) * Math.cos(Math.toRadians(-90)));
        z_temp = (float) Math.sin(Math.toRadians(-90));

        vertexpos[vertexIndex] = x_temp;
        vertexpos[vertexIndex + 1] = y_temp;
        vertexpos[vertexIndex + 2] = z_temp;

        normals[vertexIndex] = x_temp;
        normals[vertexIndex + 1] = y_temp;
        normals[vertexIndex + 2] = z_temp;

        textures[textureIndex] = 1.f;
        textures[textureIndex + 1] = 0.f;

        vertexIndex += 3;
        textureIndex += 2;

        // North pole
        x_temp = (float) (Math.cos(Math.toRadians(0)) * Math.cos(Math.toRadians(90)));
        y_temp = (float) (Math.sin(Math.toRadians(0)) * Math.cos(Math.toRadians(90)));
        z_temp = (float) Math.sin(Math.toRadians(90));

        vertexpos[vertexIndex] = x_temp;
        vertexpos[vertexIndex + 1] = y_temp;
        vertexpos[vertexIndex + 2] = z_temp;

        normals[vertexIndex] = x_temp;
        normals[vertexIndex + 1] = y_temp;
        normals[vertexIndex + 2] = z_temp;

        textures[textureIndex] = 1.f;
        textures[textureIndex + 1] = 1.f;

        // Triangles indexes
        int nbTriangles = (quarter * 2) + (((slice - 2) * quarter) * 2);
        triangles = new int[nbTriangles * 3];
        int triangleIndex = 0;

        for (int i = 0; i < (nbVertices - quarter - 2); ++i) {

            if ((i + 1) % quarter == 0) {

                triangles[triangleIndex] = i;
                triangles[triangleIndex + 1] = i - quarter + 1;
                triangles[triangleIndex + 2] = i + quarter;
                triangles[triangleIndex + 3] = i + 1;
                triangles[triangleIndex + 4] = i + quarter;
                triangles[triangleIndex + 5] = i - quarter + 1;
            } else {

                triangles[triangleIndex] = i;
                triangles[triangleIndex + 1] = i + 1;
                triangles[triangleIndex + 2] = i + quarter;
                triangles[triangleIndex + 3] = i + quarter + 1;
                triangles[triangleIndex + 4] = i + quarter;
                triangles[triangleIndex + 5] = i + 1;
            }

            triangleIndex += 6;
        }


        // South pole
        for (int i = 0; i < quarter; ++i) {

            triangles[triangleIndex] = nbVertices - 2;
            triangles[triangleIndex + 1] = i;

            if (i == 0) {

                triangles[triangleIndex + 2] = i + quarter - 1;
            } else {

                triangles[triangleIndex + 2] = i - 1;
            }

            triangleIndex += 3;
        }

        // North pole
        for (int i = nbVertices - quarter - 2; i < nbVertices - 2; ++i) {

            triangles[triangleIndex] = nbVertices - 1;
            triangles[triangleIndex + 2] = i;

            if (i == nbVertices - quarter - 2) {

                triangles[triangleIndex + 1] = nbVertices - 3;
            } else {

                triangles[triangleIndex + 1] = i - 1;
            }

            triangleIndex += 3;
        }
    }
}
