package fr.univ_poitiers.dptinfo.algo3d.renderObject;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import fr.univ_poitiers.dptinfo.algo3d.MyGLRenderer;
import fr.univ_poitiers.dptinfo.algo3d.NoLightShaders;

/**
 * Based class to represent a mesh, with all basics methods
 * @author Samuel Goubeau
 */
public class Mesh {

    static final String LOG_TAG = "Mesh";

    protected float[] vertexpos;
    protected int[] triangles;
    protected int[] edges;

    private int glelementbuffer;
    private int glposbuffer_vertex;
    private int glelementbuffer_line;

    private boolean hasEdge;
    private float[] modelview;

    /**
     * Constructor for a mesh
     * @param hasEdge : Specifies whether the mesh has its own edges to be drawn, or whether all triangles should be drawn
     */
    public Mesh(boolean hasEdge){

        this.hasEdge = hasEdge;
        modelview = new float[16];
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
     * @param i_array : array where all indexes are stored
     * @param glelementbuffer : buffer associated to i_array
     */
    private void send_buffer_to_GPU(int[] i_array, int glelementbuffer){

        ByteBuffer bytebuf;
        IntBuffer ib;

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

    /**
     * Initializes all buffers witch be sent to the GPU
     */
    public void initGraphics(){

        int[] buffers;

        if(hasEdge){

            buffers = new int[3];
            GLES20.glGenBuffers(3, buffers, 0);

            glelementbuffer_line = buffers[2];
            send_buffer_to_GPU(edges, glelementbuffer_line);
        }
        else{

            buffers = new int[2];
            GLES20.glGenBuffers(2, buffers, 0);
        }

        glposbuffer_vertex = buffers[0];
        send_vertexes_to_GPU();

        glelementbuffer = buffers[1];
        send_buffer_to_GPU(triangles,  glelementbuffer);

    }

    /**
     * Set the current modelview of the mesh with another modelview
     * @param modelviewmatrix : the modelview that will be copied
     */
    public void setModelView(final float[] modelviewmatrix){

        System.arraycopy(modelviewmatrix, 0, modelview, 0, modelviewmatrix.length);
    }

    /**
     * Transformation of the mesh by a translation of the modelview
     * @param x : translation of the position on x
     * @param y : translation of the position on y
     * @param z : translation of the position on z
     */
    public void translate(float x, float y, float z){

        Matrix.translateM(modelview,0, x, y, z);
    }

    /**
     * Transformation of the mesh by a rotation of the modelview
     * @param x : rotation of the mesh on x
     * @param y : rotation of the mesh on y
     * @param z : rotation of the mesh on z
     */
    public void rotate(float angle, float x, float y, float z){

        Matrix.rotateM(modelview, 0, angle, x, y, z);
    }

    /**
     * Transformation of the mesh by a scale of the modelview
     * @param x : scale of all vertices on x
     * @param y : scale of all vertices on y
     * @param z : scale of all vertices on z
     */
    public void scale(float x, float y, float z){

        Matrix.scaleM(modelview, 0, x, y, z);
    }

    /**
     * Draw the current mesh
     * @param shaders : Shader to represent the mesh
     */
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

        if(hasEdge){

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
