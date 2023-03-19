package fr.univ_poitiers.dptinfo.algo3d.renderObject;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Pair;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import fr.univ_poitiers.dptinfo.algo3d.MyGLRenderer;
import fr.univ_poitiers.dptinfo.algo3d.NoLightShaders;

/**
 * Class to represent an icosphere
 * @author Samuel Goubeau
 */
public class Icosphere extends Mesh{

    private float[] icoVertexpos;
    private int vertexIndex;

    private int[] icoTriangles;
    private int triangleIndex;

    private Map<Pair<Integer, Integer>, Integer> middleKnown;

    public Icosphere(int nbDiv){
        super(false);

        middleKnown = new HashMap<>();

        vertexpos = new float[]{

                0.F, -1.F, 0.F,
                1.F, 0.F, 0.F,
                0.F, 0.F, 1.F,
                -1.F, 0.F, 0.F,
                0.F, 0.F, -1.F,
                0.F, 1.F, 0.F
        };
        vertexIndex = vertexpos.length;

        triangles = new int[]{

                0, 1, 2,
                0, 2, 3,
                0, 3, 4,
                0, 4, 1,
                5, 4, 3,
                5, 3, 2,
                5, 2, 1,
                5, 1, 4
        };
        triangleIndex = 0;

        if(nbDiv > 0){
            icoTriangles = new int[(int) (3 * 8 * Math.pow(4, nbDiv))];
            icoVertexpos = new float[icoTriangles.length*3/2 + 6 - icoTriangles.length];

            System.arraycopy(vertexpos, 0, icoVertexpos, 0, vertexpos.length);

            for(int i=0; i<triangles.length; i+=3){

                rec_triangleDivision(
                        triangles[i],
                        triangles[i+1],
                        triangles[i+2],
                        nbDiv
                );
            }

            vertexpos = icoVertexpos;
            triangles = icoTriangles;
        }
    }

    private void rec_triangleDivision(int v1, int v2, int v3, int nbDiv){

        if(nbDiv == 0){

            icoTriangles[triangleIndex] = v1;
            icoTriangles[triangleIndex + 1] = v2;
            icoTriangles[triangleIndex + 2] = v3;
            triangleIndex += 3;
        }
        else{

            int vm1 = getMiddleVertices(v1, v2);
            int vm2 = getMiddleVertices(v2, v3);
            int vm3 = getMiddleVertices(v3, v1);

            rec_triangleDivision(v1, vm1, vm3, nbDiv-1);
            rec_triangleDivision(vm1, v2, vm2, nbDiv-1);
            rec_triangleDivision(vm3, vm2, v3, nbDiv-1);
            rec_triangleDivision(vm1, vm2, vm3, nbDiv-1);
        }
    }

    private int getMiddleVertices(int v1, int v2){

        float middle_x = (icoVertexpos[v1*3] + icoVertexpos[v2*3])/2;
        float middle_y = (icoVertexpos[v1*3+1] + icoVertexpos[v2*3+1])/2;
        float middle_z = (icoVertexpos[v1*3+2] + icoVertexpos[v2*3+2])/2;

        double n = Math.sqrt(middle_x*middle_x + middle_y*middle_y + middle_z*middle_z);

        Pair<Integer, Integer> k = (v1 < v2) ? new Pair<>(v1, v2) : new Pair<>(v2, v1);

        if(middleKnown.containsKey(k)){

            return middleKnown.get(k);
        }
        else{

            int indexMiddle = vertexIndex/3;
            icoVertexpos[vertexIndex] = (float) (middle_x/n);
            icoVertexpos[vertexIndex + 1] = (float) (middle_y/n);
            icoVertexpos[vertexIndex + 2] = (float) (middle_z/n);
            vertexIndex += 3;

            middleKnown.put(k, indexMiddle);

            return indexMiddle;
        }
    }
}
