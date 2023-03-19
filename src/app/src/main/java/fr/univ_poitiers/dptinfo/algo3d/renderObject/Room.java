package fr.univ_poitiers.dptinfo.algo3d.renderObject;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import fr.univ_poitiers.dptinfo.algo3d.MyGLRenderer;
import fr.univ_poitiers.dptinfo.algo3d.NoLightShaders;
import fr.univ_poitiers.dptinfo.algo3d.Vec3f;

/**
 * Class to represent a room with a door
 * @author Samuel Goubeau
 */
public class Room {

    private float[] vertexpos;

    private short[] triangles_floor;
    private short[] triangles_ceiling;
    private short[] triangles_wall;
    private short[] edge_wall;

    private int glposbuffer_vertex;
    private int glelementbuffer_floor;
    private int glelementbuffer_ceiling;
    private int glelementbuffer_wall;
    private int glelementbuffer_line;

    private float[] modelviewroom;

    /**
     * Declaration of the 4 buffers needed
     */
    protected ShortBuffer floorbufferS;
    protected ShortBuffer ceilingbufferS;
    protected ShortBuffer wallbufferS;
    protected ShortBuffer linebufferS;


    public Room(){

        modelviewroom=new float[16];

        // Floor vertexes
        Vec3f P0_f = new Vec3f(-3.f, 0.f, 3.f);
        Vec3f P1_f = new Vec3f(3.f, 0.f, 3.f);
        Vec3f P2_f = new Vec3f(3.f, 0.f, -3.f);
        Vec3f P3_f = new Vec3f(-3.f, 0.f, -3.f);

        // Ceiling vertexes
        Vec3f P0_c = new Vec3f(-3.f, 2.5f, 3.f);
        Vec3f P1_c = new Vec3f(3.f, 2.5f, 3.f);
        Vec3f P2_c = new Vec3f(3.f, 2.5f, -3.f);
        Vec3f P3_c = new Vec3f(-3.f, 2.5f, -3.f);

        // Front wall vertexes
        Vec3f P0_fw = new Vec3f(-3.f, 0.f, -3.f);
        Vec3f P1_fw = new Vec3f(3.f, 0.f, -3.f);
        Vec3f P2_fw = new Vec3f(3.f, 2.5f, -3.f);
        Vec3f P3_fw = new Vec3f(-3.f, 2.5f, -3.f);

        Vec3f P30_fw = new Vec3f(-3.f, 2f, -3.f);
        Vec3f P21_fw = new Vec3f(3.f, 2f, -3.f);

        Vec3f Pdoor_0 = new Vec3f(-1.f, 0.f, -3.f);
        Vec3f Pdoor_1 = new Vec3f(1.f, 0.f, -3.f);
        Vec3f Pdoor_2 = new Vec3f(-1.f, 2.f, -3.f);
        Vec3f Pdoor_3 = new Vec3f(1.f, 2.f, -3.f);
        Vec3f Pdoor_4 = new Vec3f(-1.f, 2.5f, -3.f);
        Vec3f Pdoor_5 = new Vec3f(1.f, 2.5f, -3.f);

        // Right wall vertexes
        Vec3f P0_rw = new Vec3f(3.f, 0.f, -3.f);
        Vec3f P1_rw = new Vec3f(3.f, 0.f, 3.f);
        Vec3f P2_rw = new Vec3f(3.f, 2.5f, 3.f);
        Vec3f P3_rw = new Vec3f(3.f, 2.5f, -3.f);

        // Back wall vertexes
        Vec3f P0_bw = new Vec3f(3.f, 0.f, 3.f);
        Vec3f P1_bw = new Vec3f(-3.f, 0.f, 3.f);
        Vec3f P2_bw = new Vec3f(-3.f, 2.5f, 3.f);
        Vec3f P3_bw = new Vec3f(3.f, 2.5f, 3.f);

        // Left wall vertexes
        Vec3f P0_lw = new Vec3f(-3.f, 0.f, 3.f);
        Vec3f P1_lw = new Vec3f(-3.f, 0.f, -3.f);
        Vec3f P2_lw = new Vec3f(-3.f, 2.5f, -3.f);
        Vec3f P3_lw = new Vec3f(-3.f, 2.5f, 3.f);

        vertexpos = new float[]{
                // Floor //
                P0_f.x, P0_f.y, P0_f.z, //0
                P1_f.x, P1_f.y, P1_f.z, //1
                P2_f.x, P2_f.y, P2_f.z, //2
                P3_f.x, P3_f.y, P3_f.z, //3

                // Ceiling //
                P3_c.x, P3_c.y, P3_c.z, //4
                P2_c.x, P2_c.y, P2_c.z, //5
                P1_c.x, P1_c.y, P1_c.z, //6
                P0_c.x, P0_c.y, P0_c.z, //7

                // Front wall //
                // Left quad //
                P0_fw.x, P0_fw.y, P0_fw.z, //8
                Pdoor_0.x, Pdoor_0.y, Pdoor_0.z, //9
                Pdoor_2.x, Pdoor_2.y, Pdoor_2.z, //10
                P30_fw.x, P30_fw.y, P30_fw.z, //11

                // Right quad //
                Pdoor_1.x, Pdoor_1.y, Pdoor_1.z, //12
                P1_fw.x, P1_fw.y, P1_fw.z, //13
                P21_fw.x, P21_fw.y, P21_fw.z, //14
                Pdoor_3.x, Pdoor_3.y, Pdoor_3.z, //15

                // High left quad //
                P30_fw.x, P30_fw.y, P30_fw.z, //16
                Pdoor_2.x, Pdoor_2.y, Pdoor_2.z, //17
                Pdoor_4.x, Pdoor_4.y, Pdoor_4.z, //18
                P3_fw.x, P3_fw.y, P3_fw.z, //19

                // High middle quad //
                Pdoor_2.x, Pdoor_2.y, Pdoor_2.z, //20
                Pdoor_3.x, Pdoor_3.y, Pdoor_3.z, //21
                Pdoor_5.x, Pdoor_5.y, Pdoor_5.z, //22
                Pdoor_4.x, Pdoor_4.y, Pdoor_4.z, //23

                // High right quad //
                Pdoor_3.x, Pdoor_3.y, Pdoor_3.z, //24
                P21_fw.x, P21_fw.y, P21_fw.z, //25
                P2_fw.x, P2_fw.y, P2_fw.z, //26
                Pdoor_5.x, Pdoor_5.y, Pdoor_5.z, //27

                // Right wall
                P0_rw.x, P0_rw.y, P0_rw.z, //28
                P1_rw.x, P1_rw.y, P1_rw.z, //29
                P2_rw.x, P2_rw.y, P2_rw.z, //30
                P3_rw.x, P3_rw.y, P3_rw.z, //31

                // Back wall
                P0_bw.x, P0_bw.y, P0_bw.z, //32
                P1_bw.x, P1_bw.y, P1_bw.z, //33
                P2_bw.x, P2_bw.y, P2_bw.z, //34
                P3_bw.x, P3_bw.y, P3_bw.z, //35

                // Left wall
                P0_lw.x, P0_lw.y, P0_lw.z, //36
                P1_lw.x, P1_lw.y, P1_lw.z, //37
                P2_lw.x, P2_lw.y, P2_lw.z, //38
                P3_lw.x, P3_lw.y, P3_lw.z  //39
        };

        triangles_floor = new short[]{
                0, 1, 2,
                3, 0 ,2
        };

        triangles_ceiling = new short[]{
                4, 5, 6,
                7, 4, 6
        };

        triangles_wall = new short[]{
                // Front wall triangles
                // Left quad
                8, 9, 10,
                11, 8, 10,

                // Right quad
                12, 13, 14,
                15, 12, 14,

                // High left quad
                16, 17, 18,
                19, 16, 18,

                // High middle quad
                20, 21, 22,
                23, 20, 22,

                // High right quad
                24, 25, 26,
                27, 24, 26,

                // Right wall triangles
                28, 29, 30,
                31, 28, 30,

                // Back wall triangle
                32, 33, 34,
                35, 32, 34,

                // Left wall triangle
                36, 37, 38,
                39, 36, 38
        };

        edge_wall = new short[]{
                // Front wall edges
                8, 9,
                9, 10,
                10, 15,
                15, 12,
                12, 13,
                13, 26,
                26, 19,
                19, 8,

                // Right wall edges
                28, 29,
                29, 30,
                30, 31,
                31, 28,

                // Back wall edges
                32, 33,
                33, 34,
                34, 35,
                35, 32,

                // Left wall edges
                36, 37,
                37, 38,
                38, 39,
                39, 36
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

        int[] buffers = new int[5]; // Besoin d’un buffer sur la carte graphique
        GLES20.glGenBuffers(5, buffers, 0); // Allocations des buffers

        glposbuffer_vertex =buffers[0];
        send_vertexes_to_GPU();

        // Floor //

        glelementbuffer_floor =buffers[1];
        send_buffer_to_GPU(floorbufferS, triangles_floor,  glelementbuffer_floor);


        // Ceiling //

        glelementbuffer_ceiling =buffers[2];
        send_buffer_to_GPU(ceilingbufferS, triangles_ceiling, glelementbuffer_ceiling);


        // Wall //

        glelementbuffer_wall =buffers[3];
        send_buffer_to_GPU(wallbufferS, triangles_wall, glelementbuffer_wall);


        // Line //

        glelementbuffer_line =buffers[4];
        send_buffer_to_GPU(linebufferS, edge_wall, glelementbuffer_line);
    }

    public void setModelView(final float[] modelviewmatrix){

        System.arraycopy(modelviewmatrix, 0, modelviewroom, 0, modelviewmatrix.length);
    }

    public void translate(float x, float y, float z){

        Matrix.translateM(modelviewroom,0, x, y, z);
    }

    public void rotate(float angle, float x, float y, float z){

        Matrix.rotateM(modelviewroom, 0, angle, x, y, z);
    }

    public void scale(float x, float y, float z){

        Matrix.scaleM(modelviewroom, 0, x, y, z);
    }


    public void drawFloor(final NoLightShaders shaders)
    {
        shaders.setModelViewMatrix(modelviewroom);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, glposbuffer_vertex);
        shaders.setPositionsPointer(3,GLES20.GL_FLOAT);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, glelementbuffer_floor);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, triangles_floor.length, GLES20.GL_UNSIGNED_SHORT, 0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,0);
    }

    public void drawCeiling(final NoLightShaders shaders)
    {
        shaders.setModelViewMatrix(modelviewroom);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, glposbuffer_vertex);
        shaders.setPositionsPointer(3,GLES20.GL_FLOAT);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, glelementbuffer_ceiling);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, triangles_ceiling.length, GLES20.GL_UNSIGNED_SHORT, 0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,0);
    }

    public void drawWall(final NoLightShaders shaders)
    {
        shaders.setModelViewMatrix(modelviewroom);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, glposbuffer_vertex);
        shaders.setPositionsPointer(3,GLES20.GL_FLOAT);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, glelementbuffer_wall);

        GLES20.glPolygonOffset(2.F,4.F);
        GLES20.glEnable(GLES20.GL_POLYGON_OFFSET_FILL);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, triangles_wall.length, GLES20.GL_UNSIGNED_SHORT, 0);
        GLES20.glDisable(GLES20.GL_POLYGON_OFFSET_FILL);
        shaders.setColor(MyGLRenderer.black);

        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, glelementbuffer_line);
        GLES20.glDrawElements(GLES20.GL_LINES, edge_wall.length,GLES20.GL_UNSIGNED_SHORT, 0);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,0);
    }
}
