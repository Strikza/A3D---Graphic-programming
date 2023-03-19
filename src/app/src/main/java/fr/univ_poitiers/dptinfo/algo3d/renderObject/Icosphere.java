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
public class Icosphere {

    private float[] vertexpos;
    private float[] icoVertexpos;
    private int vertexIndex;

    private int[] triangles;
    private int[] icoTriangles;
    private int triangleIndex;

    protected IntBuffer icospherebufferI;
    private int glelementbuffer_icosphere;
    private int glposbuffer_vertex;

    private float[] modelviewicosphere;

    private Map<Pair<Integer, Integer>, Integer> middleKnown;

    public Icosphere(int nbDiv){

        middleKnown = new HashMap<>();
        modelviewicosphere=new float[16];

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

    /**
     * Send vertices to GPU's buffer
     */
    private void send_vertexes_to_GPU(){
        ByteBuffer bytebuf = ByteBuffer.allocateDirect(vertexpos.length * Float.BYTES);
        bytebuf.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bytebuf.asFloatBuffer();
        fb.put(vertexpos);
        fb.position(0);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, glposbuffer_vertex);
        GLES20.glBufferData(
                GLES20.GL_ARRAY_BUFFER,
                vertexpos.length * Float.BYTES,
                fb,
                GLES20.GL_STATIC_DRAW
        );
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0);
    }

    /**
     * Global function to send buffers to GPU
     * @param ib
     * @param i_array
     * @param glelementbuffer
     */
    private void send_buffer_to_GPU(IntBuffer ib, int[] i_array, int glelementbuffer){

        ByteBuffer bytebuf;

        bytebuf = ByteBuffer.allocateDirect(i_array.length * Integer.BYTES);
        bytebuf.order(ByteOrder.nativeOrder());
        ib = bytebuf.asIntBuffer();
        ib.put(i_array);
        ib.position(0);

        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, glelementbuffer);
        GLES20.glBufferData(
                GLES20.GL_ELEMENT_ARRAY_BUFFER,
                i_array.length * Integer.BYTES,
                ib,
                GLES20.GL_STATIC_DRAW
        );
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,0);
    }

    public void initGraphics(){

        int[] buffers = new int[2]; // Besoin d’un buffer sur la carte graphique
        GLES20.glGenBuffers(2, buffers, 0); // Allocations des buffers

        glposbuffer_vertex =buffers[0];
        send_vertexes_to_GPU();

        glelementbuffer_icosphere =buffers[1];
        send_buffer_to_GPU(icospherebufferI, triangles,  glelementbuffer_icosphere);
    }

    public void setModelView(final float[] modelviewmatrix){

        System.arraycopy(modelviewmatrix, 0, modelviewicosphere, 0, modelviewmatrix.length);
    }

    public void translate(float x, float y, float z){

        Matrix.translateM(modelviewicosphere,0, x, y, z);
    }

    public void rotate(float angle, float x, float y, float z){

        Matrix.rotateM(modelviewicosphere, 0, angle, x, y, z);
    }

    public void scale(float x, float y, float z){

        Matrix.scaleM(modelviewicosphere, 0, x, y, z);
    }

    public void draw(final NoLightShaders shaders){

        shaders.setModelViewMatrix(modelviewicosphere);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, glposbuffer_vertex);
        shaders.setPositionsPointer(3,GLES20.GL_FLOAT);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, glelementbuffer_icosphere);

        GLES20.glPolygonOffset(2.F,4.F);
        GLES20.glEnable(GLES20.GL_POLYGON_OFFSET_FILL);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, triangles.length, GLES20.GL_UNSIGNED_INT, 0);
        GLES20.glDisable(GLES20.GL_POLYGON_OFFSET_FILL);
        shaders.setColor(MyGLRenderer.black);

        for(int i=0; i<triangles.length; i+=3){

            GLES20.glDrawElements(GLES20.GL_LINE_LOOP, 3, GLES20.GL_UNSIGNED_INT, i*Integer.BYTES);
        }

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,0);
    }
}
