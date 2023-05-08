package fr.univ_poitiers.dptinfo.algo3d.renderObject;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import fr.univ_poitiers.dptinfo.algo3d.MyGLRenderer;
import fr.univ_poitiers.dptinfo.algo3d.shaders.LightingShaders;

/**
 * Class to represent a crossroad
 * @author Samuel Goubeau
 */
public class Crossroad {

    static final String LOG_TAG = "Crossroad";

    private float[] vertexpos;
    private float[] normals;
    private float[] textures;

    private short[] triangles_floor;
    private short[] triangles_ceiling;

    private Door door;

    private int glposbuffer_vertex;
    private int glposbuffer_normal;
    private int gltexturebuffer;
    private int glelementbuffer_floor;
    private int glelementbuffer_ceiling;

    private float[] modelviewroom;

    /**
     * Constructor for a crossroad
     */
    public Crossroad(){

        modelviewroom=new float[16];

        door = new Door();

        vertexpos = new float[]{
                // Floor //
                -3.f, 0.f, 3.f,
                3.f, 0.f, 3.f,
                3.f, 0.f, -3.f,
                -3.f, 0.f, -3.f,

                // Ceiling //
                -3.f, 2.5f, 3.f,
                3.f, 2.5f, 3.f,
                3.f, 2.5f, -3.f,
                -3.f, 2.5f, -3.f
        };

        normals = new float[]{
                // Floor
                0.f, 1.f, 0.f,
                0.f, 1.f, 0.f,
                0.f, 1.f, 0.f,
                0.f, 1.f, 0.f,

                // Ceilling
                0.f, -1.f, 0.f,
                0.f, -1.f, 0.f,
                0.f, -1.f, 0.f,
                0.f, -1.f, 0.f
        };

        textures = new float[]{
                // Floor
                1.f, 0.f,
                0.f, 0.f,
                0.f, 1.f,
                1.f, 1.f,

                // Ceilling
                1.f, 0.f,
                0.f, 0.f,
                0.f, 1.f,
                1.f, 1.f
        };

        triangles_floor = new short[]{

                0, 1, 2,
                3, 0 ,2
        };

        triangles_ceiling = new short[]{

                4, 7, 6,
                4, 6, 5
        };
    }

    /**
     * Global function to send float buffers to GPU
     * @param f_array : array where all indexes are stored
     * @param glelementbuffer : buffer associated to f_array
     */
    private void send_floatBuffer_to_GPU(float[] f_array, int glelementbuffer){

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
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0);
    }

    /**
     * Global function to send short buffers to GPU
     * @param s_array : array where all indexes are stored
     * @param glelementbuffer : buffer associated to f_array
     */
    private void send_shortBuffer_to_GPU(short[] s_array, int glelementbuffer){

        ByteBuffer bytebuf;
        ShortBuffer sb;

        bytebuf = ByteBuffer.allocateDirect(s_array.length * Short.BYTES);
        bytebuf.order(ByteOrder.nativeOrder());
        sb = bytebuf.asShortBuffer();
        sb.put(s_array);
        sb.position(0);

        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, glelementbuffer);
        GLES20.glBufferData(
                GLES20.GL_ELEMENT_ARRAY_BUFFER,
                s_array.length * Short.BYTES,
                sb,
                GLES20.GL_STATIC_DRAW
        );
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,0);
    }

    /**
     * Initializes all buffers witch be sent to the GPU
     */
    public void initGraphics(){

        int[] buffers = new int[6];
        GLES20.glGenBuffers(6, buffers, 0);

        // Vertex //
        glposbuffer_vertex = buffers[0];
        send_floatBuffer_to_GPU(vertexpos, glposbuffer_vertex);

        // Normal //
        glposbuffer_normal = buffers[1];
        send_floatBuffer_to_GPU(normals, glposbuffer_normal);

        // Texture //
        gltexturebuffer = buffers[2];
        send_floatBuffer_to_GPU(textures, gltexturebuffer);

        // Floor //
        glelementbuffer_floor = buffers[3];
        send_shortBuffer_to_GPU(triangles_floor,  glelementbuffer_floor);

        // Ceiling //
        glelementbuffer_ceiling = buffers[4];
        send_shortBuffer_to_GPU(triangles_ceiling, glelementbuffer_ceiling);

        // Door //
        door.initGraphics();
    }

    /**
     * Set the current modelview of the mesh with another modelview
     * @param modelviewmatrix : the modelview that will be copied
     */
    public void setModelView(final float[] modelviewmatrix){

        System.arraycopy(modelviewmatrix, 0, modelviewroom, 0, modelviewmatrix.length);
    }

    /**
     * Transformation of the mesh by a translation of the modelview
     * @param x : translation of the position on x
     * @param y : translation of the position on y
     * @param z : translation of the position on z
     */
    public void translate(float x, float y, float z){

        Matrix.translateM(modelviewroom,0, x, y, z);
    }

    /**
     * Transformation of the mesh by a rotation of the modelview
     * @param x : rotation of the mesh on x
     * @param y : rotation of the mesh on y
     * @param z : rotation of the mesh on z
     */
    public void rotate(float angle, float x, float y, float z){

        Matrix.rotateM(modelviewroom, 0, angle, x, y, z);
    }

    /**
     * Transformation of the mesh by a scale of the modelview
     * @param x : scale of all vertices on x
     * @param y : scale of all vertices on y
     * @param z : scale of all vertices on z
     */
    public void scale(float x, float y, float z){

        Matrix.scaleM(modelviewroom, 0, x, y, z);
    }

    /**
     * Draw the floor
     * @param shaders : Shader to represent the mesh
     * @param floor_tex : id of the floor texture to draw
     */
    private void drawFloor(final LightingShaders shaders, int floor_tex)
    {
        shaders.setModelViewMatrix(modelviewroom);

        // Enable lending
        GLES20.glEnable(GLES20.GL_BLEND);

        // Vertex //
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, glposbuffer_vertex);
        shaders.setPositionsPointer(3,GLES20.GL_FLOAT);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, glelementbuffer_floor);

        // Normal //
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, glposbuffer_normal);
        shaders.setNormalsPointer(3, GLES20.GL_FLOAT);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, glelementbuffer_floor);

        // Texture //
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA,GLES20.GL_ONE_MINUS_SRC_ALPHA);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, gltexturebuffer);
        shaders.setTexturesPointer(2, GLES20.GL_FLOAT);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, floor_tex);
        shaders.setTextureUnit(0);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, triangles_floor.length, GLES20.GL_UNSIGNED_SHORT, 0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,0);

        // Disable blending
        GLES20.glDisable(GLES20.GL_BLEND);
    }

    /**
     * Draw the ceilling
     * @param shaders : Shader to represent the mesh
     * @param  ceiling_tex : id of the ceiling texture to draw
     */
    private void drawCeiling(final LightingShaders shaders, int ceiling_tex)
    {
        shaders.setModelViewMatrix(modelviewroom);

        // Vertex //
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, glposbuffer_vertex);
        shaders.setPositionsPointer(3,GLES20.GL_FLOAT);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, glelementbuffer_ceiling);

        // Normal //
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, glposbuffer_normal);
        shaders.setNormalsPointer(3, GLES20.GL_FLOAT);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, glelementbuffer_ceiling);

        // Texture //
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, gltexturebuffer);
        shaders.setTexturesPointer(2, GLES20.GL_FLOAT);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, ceiling_tex);
        shaders.setTextureUnit(1);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, triangles_ceiling.length, GLES20.GL_UNSIGNED_SHORT, 0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,0);
    }

    /**
     * Draw the doors
     * @param shaders : Shader to represent the mesh
     * @param door_tex : id of the door texture to draw
     */
    private void drawDoors(final LightingShaders shaders, int door_tex)
    {
        door.translate(0.f,0.f, -3.f);
        door.draw(shaders, door_tex);

        door.rotate(-90.f, 0.f, 1.f, 0.f);
        door.translate(3.f,0.f, -3.f);
        door.draw(shaders, door_tex);

        door.rotate(180.f, 0.f, 1.f, 0.f);
        door.translate(0.f,0.f, -6.f);
        door.draw(shaders, door_tex);

        door.rotate(90.f, 0.f, 1.f, 0.f);
        door.translate(-3.f,0.f, -3.f);
        door.draw(shaders, door_tex);
    }

    /**
     * Draw the room
     * @param shaders : Shader to represent the mesh
     * @param color_floor : the color of the floor
     * @param color_ceiling : the color of the ceilling
     * @param color_door : color of the wall
     * @param floor_tex :id of the floor texture to draw
     * @param ceiling_tex : id of the ceiling texture to draw
     * @param door_tex : id of the door texture to draw
     */
    public void draw(
            final LightingShaders shaders,
            float[] color_floor,
            float[] color_ceiling,
            float[] color_door,
            int floor_tex,
            int ceiling_tex,
            int door_tex
    ){
        shaders.setNormalizing(true);
        shaders.setLighting(true);
        shaders.setTexturing(true);

        door.setModelView(modelviewroom);

        shaders.setMaterialColor(color_floor);
        shaders.setMaterialSpecular(MyGLRenderer.white);
        shaders.setMaterialShininess(100);
        drawFloor(shaders, floor_tex);

        shaders.setMaterialColor(color_ceiling);
        shaders.setMaterialSpecular(MyGLRenderer.white);
        shaders.setMaterialShininess(100);
        drawCeiling(shaders, ceiling_tex);

        shaders.setMaterialColor(color_door);
        shaders.setMaterialSpecular(MyGLRenderer.white);
        shaders.setMaterialShininess(100);
        drawDoors(shaders, door_tex);
    }
}
