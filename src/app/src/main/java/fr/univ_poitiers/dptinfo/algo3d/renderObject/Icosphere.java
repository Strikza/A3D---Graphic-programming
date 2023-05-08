package fr.univ_poitiers.dptinfo.algo3d.renderObject;

import android.util.Pair;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to represent an icosphere
 * @author Samuel Goubeau
 */
public class Icosphere extends Mesh{

    static final String LOG_TAG = "Icosphere";

    private float[] icoVertexpos;
    private int vertexIndex;

    private int[] icoTriangles;
    private int triangleIndex;

    // To store middle vetices already known
    private Map<Pair<Integer, Integer>, Integer> middleKnown;

    /**
     * Constructor for an icosphere
     * @param nbDiv : number of division to create the sphere
     */
    public Icosphere(int nbDiv){
        super();

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

        normals = vertexpos;

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
            icoVertexpos = new float[icoTriangles.length * 3/2 + 6 - icoTriangles.length];

            System.arraycopy(vertexpos, 0, icoVertexpos, 0, vertexpos.length);
            System.arraycopy(vertexpos, 0, normals, 0, vertexpos.length);

            for(int i=0; i<triangles.length; i+=3){

                rec_triangleDivision(
                        triangles[i],
                        triangles[i+1],
                        triangles[i+2],
                        nbDiv
                );
            }

            // All things are pointers in Java, isn't that convenient ?
            vertexpos = icoVertexpos;
            normals = icoVertexpos;
            triangles = icoTriangles;
        }

        textures = new float[vertexpos.length * 2/3];

        int textureIndex = 0;
        for(int i = 0; i < vertexpos.length; i += 3){

            double phi = Math.atan2(vertexpos[i + 1], vertexpos[i]);
            double theta = Math.asin(vertexpos[i + 2]);

            textures[textureIndex++] = (float) (phi / (2 * Math.PI));
            textures[textureIndex++] = (float) (theta / Math.PI + 0.5);
        }
    }

    /**
     * Recursive method to divide triangles and normalize vertices
     * @param v1 : first vertex
     * @param v2 : seconde vertex
     * @param v3 : third vertex
     * @param nbDiv : number of remaining divisions
     */
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

    /**
     * Get the vertex normalized at the middle of v1 and v2
     * @param v1 : first vetex
     * @param v2 : second vertex
     * @return : index of new vertex
     */
    private int getMiddleVertices(int v1, int v2){

        float middle_x = (icoVertexpos[v1*3] + icoVertexpos[v2*3])/2;
        float middle_y = (icoVertexpos[v1*3+1] + icoVertexpos[v2*3+1])/2;
        float middle_z = (icoVertexpos[v1*3+2] + icoVertexpos[v2*3+2])/2;

        double n = Math.sqrt(middle_x*middle_x + middle_y*middle_y + middle_z*middle_z);

        Pair<Integer, Integer> vertices_pair = (v1 < v2) ? new Pair<>(v1, v2) : new Pair<>(v2, v1);

        // Check if the pair already in the map
        if(middleKnown.containsKey(vertices_pair)){

            return middleKnown.get(vertices_pair);
        }
        else{

            int indexMiddle = vertexIndex/3;
            icoVertexpos[vertexIndex] = (float) (middle_x/n);
            icoVertexpos[vertexIndex + 1] = (float) (middle_y/n);
            icoVertexpos[vertexIndex + 2] = (float) (middle_z/n);

            vertexIndex += 3;

            middleKnown.put(vertices_pair, indexMiddle);

            return indexMiddle;
        }
    }
}
