package fr.univ_poitiers.dptinfo.algo3d.renderObject;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import fr.univ_poitiers.dptinfo.algo3d.MyGLRenderer;
import fr.univ_poitiers.dptinfo.algo3d.NoLightShaders;

public class Torus {

    private final int nb_slice;
    private final int nb_quarter;
    private final float R;
    private final float r;

    private final float phi;
    private final float theta;

    private float[] vertexpos;
    private int[] triangles;
    private int vertexIndex;
    private int triangleIndex;
    private int nbVertices;
    private int nbTriangles;


    protected IntBuffer torebufferI;
    private int glelementbuffer_tore;
    private int glposbuffer_vertex;

    private float[] modelviewtore;

    public Torus(int slice, int quarter, float major_radius, float minor_radius){

        // TODO : renvoyer une erreur si slice ou quarter sont inférieurs à 3
        nb_slice = Math.max(slice, 3);
        nb_quarter = Math.max(quarter, 3);

        R = major_radius;
        r = minor_radius;
        phi = 360/(float)nb_quarter;
        theta = 360/(float)nb_slice;

        modelviewtore=new float[16];

        /**
         * Equations :
         *
         * x = (R + r*cos(phi)) * cos(theta)
         * y = r*sin(phi)
         * z = (R + r*cos(phi)) * sin(theta)
         *
         */
        nbVertices = (nb_quarter+1) * (nb_slice+1);
        vertexpos = new float[nbVertices * 3];
        vertexIndex = 0;

        float x_temp;
        float y_temp;
        float z_temp;

        for (float s = 0.F; s <= 360.F; s += theta) {

            double thetaRad = Math.toRadians(s);
            for (float q = 0.F; q <= 360.F; q += phi) {

                double phiRad = Math.toRadians(q);
                x_temp = (float) ((R + r * Math.cos(phiRad)) * Math.cos(thetaRad));
                y_temp = (float) (r * Math.sin(phiRad));
                z_temp = (float) ((R + r * Math.cos(phiRad)) * Math.sin(thetaRad));

                vertexpos[vertexIndex] = x_temp;
                vertexpos[vertexIndex + 1] = y_temp;
                vertexpos[vertexIndex + 2] = z_temp;
                vertexIndex += 3;
            }
        }

        nbTriangles = nb_quarter * nb_slice * 2;
        triangles = new int[nbTriangles * 3];
        triangleIndex = 0;


        for (int s=0; s<nb_slice; ++s){

            for (int q=0; q<nb_quarter; ++q){

                triangles[triangleIndex] = (s * (nb_quarter + 1)) + q;
                triangles[triangleIndex + 1] = (s * (nb_quarter + 1)) + q + 2 + nb_quarter;
                triangles[triangleIndex + 2] = (s * (nb_quarter + 1)) + q + 1 + nb_quarter;
                triangles[triangleIndex + 3] = (s * (nb_quarter + 1)) + q;
                triangles[triangleIndex + 4] = (s * (nb_quarter + 1)) + q + 1;
                triangles[triangleIndex + 5] = (s * (nb_quarter + 1)) + q + 2 + nb_quarter;

                triangleIndex += 6;
            }
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
        GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, i_array.length * Integer.BYTES,
                ib, GLES20.GL_STATIC_DRAW);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,0);
    }

    public void initGraphics(){

        int[] buffers = new int[2]; // Besoin d’un buffer sur la carte graphique
        GLES20.glGenBuffers(2, buffers, 0); // Allocations des buffers

        glposbuffer_vertex =buffers[0];
        send_vertexes_to_GPU();

        glelementbuffer_tore =buffers[1];
        send_buffer_to_GPU(torebufferI, triangles,  glelementbuffer_tore);
    }

    public void setModelView(final float[] modelviewmatrix){

        System.arraycopy(modelviewmatrix, 0, modelviewtore, 0, modelviewmatrix.length);
    }

    public void translate(float x, float y, float z){

        Matrix.translateM(modelviewtore,0, x, y, z);
    }

    public void rotate(float angle, float x, float y, float z){

        Matrix.rotateM(modelviewtore, 0, angle, x, y, z);
    }

    public void scale(float x, float y, float z){

        Matrix.scaleM(modelviewtore, 0, x, y, z);
    }

    public void draw(final NoLightShaders shaders){

        shaders.setModelViewMatrix(modelviewtore);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, glposbuffer_vertex);
        shaders.setPositionsPointer(3,GLES20.GL_FLOAT);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, glelementbuffer_tore);

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
