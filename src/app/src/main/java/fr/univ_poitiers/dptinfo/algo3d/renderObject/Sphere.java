package fr.univ_poitiers.dptinfo.algo3d.renderObject;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import fr.univ_poitiers.dptinfo.algo3d.MyGLRenderer;
import fr.univ_poitiers.dptinfo.algo3d.NoLightShaders;

/**
 * Class to represent a sphere
 * @author Samuel Goubeau
 */
public class Sphere {

    private final int nb_slice;
    private final int nb_quarter;

    private final float phi;
    private final float theta;

    private float[] vertexpos;
    private int[] triangles;
    private int vertexIndex;
    private int triangleIndex;
    private int nbVertices;
    private int nbTriangles;

    protected IntBuffer spherebufferI;
    private int glelementbuffer_sphere;
    private int glposbuffer_vertex;

    private float[] modelviewsphere;

    Sphere(int slice, int quarter){
        nb_slice = slice;
        nb_quarter = quarter;

        phi = 360/(float)nb_quarter;
        theta = 180/(float)nb_slice;

        modelviewsphere=new float[16];

        /**
         * Equations :
         *
         * x = r * cos(phi) * cos(theta)
         * y = r * sin(phi) * cos(theta)
         * z = r * sin(theta)
         *
         * here => r == 1.0F
         */
        nbVertices = ((nb_slice-1) * nb_quarter)+2;
        vertexpos = new float[nbVertices*3];
        vertexIndex = 0;

        float x_temp;
        float y_temp;
        float z_temp;

        for (float s = -90.F + theta; s < 90.F; s += theta) {

            for (float q = 0.F; q<360.F; q += phi) {

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
        triangles = new int[nbTriangles * 3];
        triangleIndex = 0;

        for(int i=0; i<(nbVertices-nb_quarter-2); ++i){

            if((i+1)%nb_quarter == 0){

                triangles[triangleIndex] = i;
                triangles[triangleIndex+1] = i-nb_quarter+1;
                triangles[triangleIndex+2] = i+nb_quarter;
                triangles[triangleIndex+3] = i+1;
                triangles[triangleIndex+4] = i+nb_quarter;
                triangles[triangleIndex+5] = i-nb_quarter+1;
            }
            else{

                triangles[triangleIndex] = i;
                triangles[triangleIndex+1] = i+1;
                triangles[triangleIndex+2] = i+nb_quarter;
                triangles[triangleIndex+3] = i+nb_quarter+1;
                triangles[triangleIndex+4] = i+nb_quarter;
                triangles[triangleIndex+5] = i+1;
            }

            triangleIndex+=6;
        }


        // South //
        for(int i=0; i<nb_quarter; ++i){

            triangles[triangleIndex] = nbVertices - 2;
            triangles[triangleIndex + 1] = i;

            if(i == 0){

                triangles[triangleIndex+2] = i+nb_quarter-1;
            }
            else {

                triangles[triangleIndex+2] = i - 1;
            }

            triangleIndex +=3;
        }

        // North //
        for(int i=nbVertices-nb_quarter-2; i<nbVertices-2; ++i){

            triangles[triangleIndex] = nbVertices - 1;
            triangles[triangleIndex + 2] = i;

            if(i == nbVertices-nb_quarter-2){

                triangles[triangleIndex+1] = nbVertices-3;
            }
            else {

                triangles[triangleIndex+1] = i-1;
            }

            triangleIndex +=3;
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

        glelementbuffer_sphere =buffers[1];
        send_buffer_to_GPU(spherebufferI, triangles,  glelementbuffer_sphere);
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
