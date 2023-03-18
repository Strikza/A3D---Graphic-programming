package fr.univ_poitiers.dptinfo.algo3d.renderObject;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import fr.univ_poitiers.dptinfo.algo3d.MyGLRenderer;
import fr.univ_poitiers.dptinfo.algo3d.NoLightShaders;

/**
 * Class to represent a square
 * @author Samuel Goubeau
 */
public class Square {

    private float[] vertexpos;
    private float[] vertextexture;
    private float[] vertexnormal;

    private short[] triangles;
    private short[] textures;
    private short[] normals;

    protected ShortBuffer squarebufferS;
    private int glelementbuffer_square;
    private int glposbuffer_vertex;

    private float[] modelviewsquare;

    /**
     * Edge display
     */
    private short[] edges;
    private int nbindexlines;
    private int glelementbuffer_line;
    protected ShortBuffer linebufferS;

    public Square(){

        modelviewsquare=new float[16];

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

        triangles = new short[]{

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

        edges = new short[]{

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
     * @param sb
     * @param s_array
     * @param glelementbuffer
     */
    private void send_buffer_to_GPU(ShortBuffer sb, short[] s_array, int glelementbuffer){

        ByteBuffer bytebuf;

        bytebuf = ByteBuffer.allocateDirect(s_array.length * Short.BYTES);
        bytebuf.order(ByteOrder.nativeOrder());
        sb = bytebuf.asShortBuffer();
        sb.put(s_array);
        sb.position(0);

        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, glelementbuffer);
        GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, s_array.length * Short.BYTES,
                sb, GLES20.GL_STATIC_DRAW);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,0);
    }

    public void initGraphics(){

        int[] buffers = new int[3]; // Besoin d’un buffer sur la carte graphique
        GLES20.glGenBuffers(3, buffers, 0); // Allocations des buffers

        glposbuffer_vertex =buffers[0];
        send_vertexes_to_GPU();

        glelementbuffer_square =buffers[1];
        send_buffer_to_GPU(squarebufferS, triangles,  glelementbuffer_square);

        glelementbuffer_line =buffers[2];
        send_buffer_to_GPU(linebufferS, edges, glelementbuffer_line);
    }

    public void setModelView(final float[] modelviewmatrix){

        System.arraycopy(modelviewmatrix, 0, modelviewsquare, 0, modelviewmatrix.length);
    }

    public void translate(float x, float y, float z){

        Matrix.translateM(modelviewsquare,0, x, y, z);
    }

    public void rotate(float angle, float x, float y, float z){

        Matrix.rotateM(modelviewsquare, 0, angle, x, y, z);
    }

    public void scale(float x, float y, float z){

        Matrix.scaleM(modelviewsquare, 0, x, y, z);
    }

    public void draw(final NoLightShaders shaders){

        shaders.setModelViewMatrix(modelviewsquare);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, glposbuffer_vertex);
        shaders.setPositionsPointer(3,GLES20.GL_FLOAT);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, glelementbuffer_square);

        GLES20.glPolygonOffset(2.F,4.F);
        GLES20.glEnable(GLES20.GL_POLYGON_OFFSET_FILL);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, triangles.length, GLES20.GL_UNSIGNED_SHORT, 0);
        GLES20.glDisable(GLES20.GL_POLYGON_OFFSET_FILL);
        shaders.setColor(MyGLRenderer.black);

        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, glelementbuffer_line);
        GLES20.glDrawElements(GLES20.GL_LINES, edges.length,GLES20.GL_UNSIGNED_SHORT, 0);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,0);
    }
}
