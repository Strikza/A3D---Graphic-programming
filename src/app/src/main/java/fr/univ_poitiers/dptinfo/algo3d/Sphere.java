package fr.univ_poitiers.dptinfo.algo3d;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Sphere {

    private short nb_slice;
    private short nb_quarter;

    private float phi;
    private float theta;

    private float[] vertexpos;
    private short[] triangles;
    private int vertexIndex;
    private int nbTriangles;

    protected ShortBuffer spherebufferS;
    private int glelementbuffer_sphere;
    private int glposbuffer_vertex;

    private float[] modelviewsphere;

    Sphere(short slice, short quarter){
        nb_slice = slice;
        nb_quarter = quarter;

        phi = 360/(float)nb_quarter;
        theta = 180/(float)(nb_slice);

        modelviewsphere=new float[16];

        /**
         * Equations :
         *
         * x = r * cos(phi) * cos(theta) + xc
         * y = r * sin(phi) * cos(theta) + yc
         * z = r * sin(theta) + zc
         *
         * here => r == 1.0F
         */
        int nbVertices = ((nb_slice-1) * nb_quarter)+2;
        vertexpos = new float[nbVertices*3];
        vertexIndex = 0;

        float x_temp;
        float y_temp;
        float z_temp;

        for (float s = -90.F + theta; s <= 90.F-theta; s += theta) {

            for (float q = 0; q<360; q += phi) {

                x_temp = (float) (Math.cos(Math.toRadians(q)) * Math.cos(Math.toRadians(s)));
                y_temp = (float) (Math.sin(Math.toRadians(q)) * Math.cos(Math.toRadians(s)));
                z_temp = (float) Math.sin(Math.toRadians(s));

                vertexpos[vertexIndex] = x_temp;
                vertexpos[vertexIndex +1] = y_temp;
                vertexpos[vertexIndex +2] = z_temp;

                vertexIndex += 3;
            }
        }

        // Pôle sud //

        x_temp = (float) (Math.cos(Math.toRadians(0)) * Math.cos(Math.toRadians(-90)));
        y_temp = (float) (Math.sin(Math.toRadians(0)) * Math.cos(Math.toRadians(-90)));
        z_temp = (float) Math.sin(Math.toRadians(-90));

        vertexpos[vertexIndex] = x_temp;
        vertexpos[vertexIndex +1] = y_temp;
        vertexpos[vertexIndex +2] = z_temp;

        vertexIndex += 3;

        // Pôle nord //

        x_temp = (float) (Math.cos(Math.toRadians(0)) * Math.cos(Math.toRadians(90)));
        y_temp = (float) (Math.sin(Math.toRadians(0)) * Math.cos(Math.toRadians(90)));
        z_temp = (float) Math.sin(Math.toRadians(90));

        vertexpos[vertexIndex] = x_temp;
        vertexpos[vertexIndex +1] = y_temp;
        vertexpos[vertexIndex +2] = z_temp;

        nbTriangles = (nb_quarter * 2) + (((nb_slice - 2) * nb_quarter) * 2);
        triangles = new short[nbTriangles *3];


        int i_triangle = 0;
        for(int i=0; i<(nbVertices-nb_quarter-2); ++i){

            if((i+1)%nb_quarter == 0){

                triangles[i_triangle] = (short)(i);
                triangles[i_triangle+1] = (short)(i-nb_quarter+1);
                triangles[i_triangle+2] = (short)(i+nb_quarter);
                triangles[i_triangle+3] = (short)(i+1);
                triangles[i_triangle+4] = (short)(i+nb_quarter);
                triangles[i_triangle+5] = (short)(i-nb_quarter+1);
            }
            else{

                triangles[i_triangle] = (short)i;
                triangles[i_triangle+1] = (short)(i+1);
                triangles[i_triangle+2] = (short)(i+nb_quarter);
                triangles[i_triangle+3] = (short)(i+nb_quarter+1);
                triangles[i_triangle+4] = (short)(i+nb_quarter);
                triangles[i_triangle+5] = (short)(i+1);
            }

            i_triangle+=6;
        }


        // South //
        for(int i=0; i<nb_quarter; ++i){

            triangles[i_triangle] = (short) (nbVertices - 2);
            triangles[i_triangle + 1] = (short) i;

            if(i == 0){

                triangles[i_triangle+2] = (short) (i+nb_quarter-1);
            }
            else {

                triangles[i_triangle+2] = (short) (i - 1);
            }

            i_triangle +=3;
        }

        // North //
        for(int i=nbVertices-nb_quarter-2; i<nbVertices-2; ++i){

            triangles[i_triangle] = (short) (nbVertices - 1);
            triangles[i_triangle + 2] = (short) i;

            if(i == nbVertices-nb_quarter-2){

                triangles[i_triangle+1] = (short) (nbVertices-3);
            }
            else {

                triangles[i_triangle+1] = (short) (i-1);
            }

            i_triangle +=3;
        }
    }

    /**
     * Send vertexes to GPU's buffer
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
     * Global function to send buffers to the GPU
     * @param buffers
     * @param sb
     * @param s_array
     * @param glelementbuffer
     */
    private void send_buffer_to_GPU(int[] buffers, ShortBuffer sb, short[] s_array, int glelementbuffer){

        ByteBuffer bytebuf;

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

    public void initGraphics(){

        int[] buffers = new int[2]; // Besoin d’un buffer sur la carte graphique
        GLES20.glGenBuffers(2, buffers, 0); // Allocations des buffers

        glposbuffer_vertex =buffers[0];
        send_vertexes_to_GPU();

        glelementbuffer_sphere =buffers[1];
        send_buffer_to_GPU(buffers, spherebufferS, triangles,  glelementbuffer_sphere);
    }

    public void setModelView(final float[] modelviewmatrix){

        System.arraycopy(modelviewmatrix, 0, modelviewsphere, 0, modelviewmatrix.length);
    }

    public void translate(float x, float y, float z){

        Matrix.translateM(modelviewsphere,0, x, y, z);
    }

    public void rotate(float angle, float x, float y, float z){

        Matrix.rotateM(modelviewsphere, 0, angle, x, y, z);
    }

    public void scale(float x, float y, float z){

        Matrix.scaleM(modelviewsphere, 0, x, y, z);
    }

    public void draw(final NoLightShaders shaders){

        shaders.setModelViewMatrix(modelviewsphere);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, glposbuffer_vertex);
        shaders.setPositionsPointer(3,GLES20.GL_FLOAT);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, glelementbuffer_sphere);

        GLES20.glPolygonOffset(2.F,4.F);
        GLES20.glEnable(GLES20.GL_POLYGON_OFFSET_FILL);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, nbTriangles *3, GLES20.GL_UNSIGNED_SHORT, 0);
        GLES20.glDisable(GLES20.GL_POLYGON_OFFSET_FILL);
        shaders.setColor(MyGLRenderer.black);

        for(int i=0; i<triangles.length; i+=3){

            GLES20.glDrawElements(GLES20.GL_LINE_LOOP, 3, GLES20.GL_UNSIGNED_SHORT, i*Short.BYTES);
        }

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,0);
    }

}
