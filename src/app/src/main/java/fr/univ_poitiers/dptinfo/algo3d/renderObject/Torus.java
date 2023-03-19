package fr.univ_poitiers.dptinfo.algo3d.renderObject;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import fr.univ_poitiers.dptinfo.algo3d.MyGLRenderer;
import fr.univ_poitiers.dptinfo.algo3d.NoLightShaders;

public class Torus extends Mesh {

    public Torus(int slice, int quarter, float R, float r){
        super(false);

        // TODO : renvoyer une erreur si slice ou quarter sont inférieurs à 3
        slice = Math.max(slice, 3);
        quarter = Math.max(quarter, 3);


        float phi = 360/(float)quarter;
        float theta = 360/(float)slice;

        /**
         * Equations :
         *
         * x = (R + r * cos(phi)) * cos(theta)
         * y = r * sin(phi)
         * z = (R + r * cos(phi)) * sin(theta)
         *
         */
        int nbVertices = (quarter+1) * (slice+1);
        vertexpos = new float[nbVertices * 3];
        int vertexIndex = 0;

        float x_temp;
        float y_temp;
        float z_temp;

        for (float s = 0.F; s <= 360.F; s += theta) {

            double thetaRad = Math.toRadians(s);
            for (float q = 0.F; q <= 360.F; q += phi) {

                double phiRad = Math.toRadians(q);
                x_temp = (float) ((R + r * Math.cos(phiRad)) * Math.cos(thetaRad));
                y_temp = (float) (r * Math.sin(phiRad));
                z_temp = (float) ((R + r * Math.cos(phiRad)) * Math.sin(thetaRad));

                vertexpos[vertexIndex] = x_temp;
                vertexpos[vertexIndex + 1] = y_temp;
                vertexpos[vertexIndex + 2] = z_temp;
                vertexIndex += 3;
            }
        }

        int nbTriangles = quarter * slice * 2;
        triangles = new int[nbTriangles * 3];
        int triangleIndex = 0;

        for (int s=0; s<slice; ++s){

            for (int q=0; q<quarter; ++q){

                triangles[triangleIndex] = (s * (quarter + 1)) + q;
                triangles[triangleIndex + 1] = (s * (quarter + 1)) + q + 2 + quarter;
                triangles[triangleIndex + 2] = (s * (quarter + 1)) + q + 1 + quarter;
                triangles[triangleIndex + 3] = (s * (quarter + 1)) + q;
                triangles[triangleIndex + 4] = (s * (quarter + 1)) + q + 1;
                triangles[triangleIndex + 5] = (s * (quarter + 1)) + q + 2 + quarter;

                triangleIndex += 6;
            }
        }

    }
}
