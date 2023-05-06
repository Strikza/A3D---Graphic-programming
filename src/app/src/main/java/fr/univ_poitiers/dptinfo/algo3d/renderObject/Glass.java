package fr.univ_poitiers.dptinfo.algo3d.renderObject;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import fr.univ_poitiers.dptinfo.algo3d.shaders.LightingShaders;

/**
 * Class to represent a glass
 * @author Samuel Goubeau
 */
public class Glass {

    static final String LOG_TAG = "Glass";

    protected float[] vertexpos;
    protected float[] normals;
    protected float[] textures;
    protected int[] triangles;

    private int glelementbuffer;
    private int glposbuffer_vertex;
    private int glposbuffer_normal;
    private int gltexturebuffer;

    private int texture;

    private float[] modelview;

    /**
     * Constructor for a glass
     */
    public Glass() {

        modelview = new float[16];

        vertexpos = new float[]{

                0.0F, 0.0F, 0.0F,
                1.0F, 0.0F, 0.0F,
                1.0F, 1.0F, 0.0F,
                0.0F, 1.0F, 0.0F,
        };

        normals = new float[] {

                0.f, 0.f, 1.f,
                0.f, 0.f, 1.f,
                0.f, 0.f, 1.f,
                0.f, 0.f, 1.f
        };

        textures = new float[] {

                0.f, 1.f,
                1.f, 1.f,
                1.f, 0.f,
                0.f, 0.f,
        };

        triangles = new int[] {
                0, 1, 2,
                0, 2, 3
        };
    }

    /**
     * Global function to send float buffers to GPU
     *
     * @param f_array         : array where all indexes are stored
     * @param glelementbuffer : buffer associated to f_array
     */
    private void send_floatBuffer_to_GPU(float[] f_array, int glelementbuffer) {

        ByteBuffer bytebuf;
        FloatBuffer fb;

        bytebuf = ByteBuffer.allocateDirect(f_array.length * Float.BYTES);
        bytebuf.order(ByteOrder.nativeOrder());
        fb = bytebuf.asFloatBuffer();
        fb.put(f_array);
        fb.position(0);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, glelementbuffer);
        GLES20.glBufferData(
                GLES20.GL_ARRAY_BUFFER,
                f_array.length * Float.BYTES,
                fb,
                GLES20.GL_STATIC_DRAW
        );
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
    }

    /**
     * Global function to send buffers to GPU
     *
     * @param i_array         : array where all indexes are stored
     * @param glelementbuffer : buffer associated to i_array
     */
    private void send_shortBuffer_to_GPU(int[] i_array, int glelementbuffer) {

        ByteBuffer bytebuf;
        IntBuffer ib;

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
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    /**
     * Initializes all buffers witch be sent to the GPU
     */
    public void initGraphics(int texture) {

        this.texture = texture;

        int[] buffers;

        buffers = new int[4];
        GLES20.glGenBuffers(4, buffers, 0);

        // Vertex //
        glposbuffer_vertex = buffers[0];
        send_floatBuffer_to_GPU(vertexpos, glposbuffer_vertex);

        // Normal //
        glposbuffer_normal = buffers[1];
        send_floatBuffer_to_GPU(normals, glposbuffer_normal);

        // Texture //
        gltexturebuffer = buffers[3];
        send_floatBuffer_to_GPU(textures, gltexturebuffer);

        // Triangle //
        glelementbuffer = buffers[2];
        send_shortBuffer_to_GPU(triangles, glelementbuffer);
    }

    /**
     * Set the current modelview of the mesh with another modelview
     *
     * @param modelviewmatrix : the modelview that will be copied
     */
    public void setModelView(final float[] modelviewmatrix) {

        System.arraycopy(modelviewmatrix, 0, modelview, 0, modelviewmatrix.length);
    }

    /**
     * Transformation of the mesh by a translation of the modelview
     *
     * @param x : translation of the position on x
     * @param y : translation of the position on y
     * @param z : translation of the position on z
     */
    public void translate(float x, float y, float z) {

        Matrix.translateM(modelview, 0, x, y, z);
    }

    /**
     * Transformation of the mesh by a rotation of the modelview
     *
     * @param x : rotation of the mesh on x
     * @param y : rotation of the mesh on y
     * @param z : rotation of the mesh on z
     */
    public void rotate(float angle, float x, float y, float z) {

        Matrix.rotateM(modelview, 0, angle, x, y, z);
    }

    /**
     * Transformation of the mesh by a scale of the modelview
     *
     * @param x : scale of all vertices on x
     * @param y : scale of all vertices on y
     * @param z : scale of all vertices on z
     */
    public void scale(float x, float y, float z) {

        Matrix.scaleM(modelview, 0, x, y, z);
    }

    /**
     * Draw the current mesh
     *
     * @param shaders : Shader to represent the mesh
     */
    public void draw(final LightingShaders shaders) {

        shaders.setModelViewMatrix(modelview);
        shaders.setNormalizing(true);
        shaders.setLighting(true);
        shaders.setTexturing(true);

        // Enable lending
        GLES20.glEnable(GLES20.GL_BLEND);

        // Vertex //
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, glposbuffer_vertex);
        shaders.setPositionsPointer(3, GLES20.GL_FLOAT);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, glelementbuffer);

        // Normal //
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, glposbuffer_normal);
        shaders.setNormalsPointer(3, GLES20.GL_FLOAT);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, glelementbuffer);

        // Texture //
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA,GLES20.GL_ONE_MINUS_SRC_ALPHA);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, gltexturebuffer);
        shaders.setTexturesPointer(2, GLES20.GL_FLOAT);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture);
        shaders.setTextureUnit(0);

        GLES20.glPolygonOffset(2.F, 4.F);
        GLES20.glEnable(GLES20.GL_POLYGON_OFFSET_FILL);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, triangles.length, GLES20.GL_UNSIGNED_INT, 0);
        GLES20.glDisable(GLES20.GL_POLYGON_OFFSET_FILL);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);

        // Disable blending
        GLES20.glDisable(GLES20.GL_BLEND);
    }
}
