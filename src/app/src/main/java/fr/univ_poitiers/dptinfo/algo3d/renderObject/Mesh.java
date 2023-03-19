package fr.univ_poitiers.dptinfo.algo3d.renderObject;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import fr.univ_poitiers.dptinfo.algo3d.MyGLRenderer;
import fr.univ_poitiers.dptinfo.algo3d.NoLightShaders;

public class Mesh {

    protected float[] vertexpos;
    protected int[] triangles;
    protected int[] edges;

    protected IntBuffer bufferI;
    protected IntBuffer linebufferI;
    private int glelementbuffer;
    private int glposbuffer_vertex;
    private int glelementbuffer_line;

    private boolean asEdge;
    private float[] modelview;

    public Mesh(boolean e){

        asEdge = e;
        modelview=new float[16];
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
        GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, i_array.length * Integer.BYTES,
                ib, GLES20.GL_STATIC_DRAW);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,0);
    }

    public void initGraphics(){

        int[] buffers;

        if(asEdge){

            buffers = new int[3]; // Besoin d’un buffer sur la carte graphique
            GLES20.glGenBuffers(3, buffers, 0); // Allocations des buffers

            glelementbuffer_line =buffers[2];
            send_buffer_to_GPU(linebufferI, edges, glelementbuffer_line);
        }
        else{

            buffers = new int[2]; // Besoin d’un buffer sur la carte graphique
            GLES20.glGenBuffers(2, buffers, 0); // Allocations des buffers
        }

        glposbuffer_vertex =buffers[0];
        send_vertexes_to_GPU();

        glelementbuffer =buffers[1];
        send_buffer_to_GPU(bufferI, triangles,  glelementbuffer);

    }

    public void setModelView(final float[] modelviewmatrix){

        System.arraycopy(modelviewmatrix, 0, modelview, 0, modelviewmatrix.length);
    }

    public void translate(float x, float y, float z){

        Matrix.translateM(modelview,0, x, y, z);
    }

    public void rotate(float angle, float x, float y, float z){

        Matrix.rotateM(modelview, 0, angle, x, y, z);
    }

    public void scale(float x, float y, float z){

        Matrix.scaleM(modelview, 0, x, y, z);
    }

    public void draw(final NoLightShaders shaders){

        shaders.setModelViewMatrix(modelview);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, glposbuffer_vertex);
        shaders.setPositionsPointer(3,GLES20.GL_FLOAT);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, glelementbuffer);

        GLES20.glPolygonOffset(2.F,4.F);
        GLES20.glEnable(GLES20.GL_POLYGON_OFFSET_FILL);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, triangles.length, GLES20.GL_UNSIGNED_INT, 0);
        GLES20.glDisable(GLES20.GL_POLYGON_OFFSET_FILL);
        shaders.setColor(MyGLRenderer.black);

        if(asEdge){

            GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, glelementbuffer_line);
            GLES20.glDrawElements(GLES20.GL_LINES, edges.length,GLES20.GL_UNSIGNED_INT, 0);
        }
        else{

            for(int i=0; i<triangles.length; i+=3){

                GLES20.glDrawElements(GLES20.GL_LINE_LOOP, 3, GLES20.GL_UNSIGNED_INT, i*Integer.BYTES);
            }
        }


        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,0);
    }
}
