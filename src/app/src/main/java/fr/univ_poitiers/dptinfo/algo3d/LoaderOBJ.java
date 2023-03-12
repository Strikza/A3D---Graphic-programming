package fr.univ_poitiers.dptinfo.algo3d;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to load OBJ file
 * @author Samuel Goubeau
 */
public class LoaderOBJ {

    static final String LOG_TAG = "LoaderOBJ";

    private float[] vertexpos;
    private float[] vertextexture;
    private float[] vertexnormal;
    private short[] triangles;

    private float[] modelviewOBJ;

    protected ShortBuffer objbufferS;
    private int glelementbuffer_sphere;
    private int glposbuffer_vertex;


    public LoaderOBJ(Context context, String fileName){

        modelviewOBJ=new float[16];

        try {
            InputStream ipst = context.getResources().getAssets().open(fileName);
            Log.d(LOG_TAG, "File open");

            int data = ipst.read();
            short first_vertex = -1;
            short second_vertex = -1;
            short last_vertex = -1;
            StringBuilder tmp = new StringBuilder();
            List<Float> tmpVertexpos = new ArrayList<>();
            List<Float> tmpVertextexture = new ArrayList<>();
            List<Float> tmpVertexnormal = new ArrayList<>();
            List<Short> tmpTriangles = new ArrayList<>();

            while(data != -1){
                switch (data){

                    case 'v':
                        Log.d(LOG_TAG, "Vertex line found");
                        // To throw the space after 'v'
                        data = ipst.read();
                        while(data != '\r' && data != -1){

                            data = ipst.read();

                            if(data != ' ' && data != '\r' && data != -1){

                                tmp.append((char) data);
                            }
                            else{

                                tmpVertexpos.add(Float.parseFloat(tmp.toString()));
                                tmp = new StringBuilder();
                            }
                        }

                        data = ipst.read();
                        break;

                    case 'f':
                        Log.d(LOG_TAG, "Face line found");
                        // To throw the space after 'f'
                        data = ipst.read();
                        while(data != '\r' && data != -1){

                            data = ipst.read();

                            if(data != ' ' && data != '\r' && data != -1){

                                if(data != '/'){

                                    tmp.append((char) data);
                                }

                            }
                            else{

                                if(first_vertex == -1){

                                    first_vertex = Short.parseShort(tmp.toString());
                                }
                                else if(second_vertex == -1){

                                    second_vertex = Short.parseShort(tmp.toString());
                                }
                                else {

                                    Log.d(LOG_TAG, "Triangle found");
                                    last_vertex = Short.parseShort(tmp.toString());

                                    tmpTriangles.add(first_vertex);
                                    tmpTriangles.add(second_vertex);
                                    tmpTriangles.add(last_vertex);

                                    second_vertex = last_vertex;

                                    if(data == '\r' || data == -1){

                                        first_vertex = -1;
                                        second_vertex = -1;
                                    }
                                }

                                tmp = new StringBuilder();
                            }
                        }

                        data = ipst.read();
                        break;

                    default:
                        Log.d(LOG_TAG, "Useless line found");
                        while(data != '\n' && data != -1){

                            data = ipst.read();
                        }
                }

                tmp = new StringBuilder();
                data = ipst.read();
            }

            // Initialization of vextexpos
            vertexpos = new float[tmpVertexpos.size()];
            for(int i = 0; i<tmpVertexpos.size(); i++){

                vertexpos[i] = tmpVertexpos.get(i);
            }

            // Initialization of vextextexture
            vertextexture = new float[tmpVertextexture.size()];
            for(int i = 0; i<tmpVertextexture.size(); i++){

                vertextexture[i] = tmpVertextexture.get(i);
            }

            // Initialization of vextexnormal
            vertexnormal = new float[tmpVertexnormal.size()];
            for(int i = 0; i<tmpVertexnormal.size(); i++){

                vertexnormal[i] = tmpVertexnormal.get(i);
            }

            // Initialization of triangles
            triangles = new short[tmpTriangles.size()];
            for(int i = 0; i<tmpTriangles.size(); i++){

                triangles[i] = (short) (tmpTriangles.get(i) - 1);
            }



        } catch (IOException e) {
            Log.d(LOG_TAG, "File not found");
        }
    }

    public void initGraphics(){

        int[] buffers = new int[2]; // Besoin d’un buffer sur la carte graphique
        GLES20.glGenBuffers(2, buffers, 0); // Allocations des buffers

        glposbuffer_vertex =buffers[0];
        send_vertexes_to_GPU();

        glelementbuffer_sphere =buffers[1];
        send_buffer_to_GPU(buffers, objbufferS, triangles,  glelementbuffer_sphere);
    }

    public void setModelView(final float[] modelviewmatrix){

        System.arraycopy(modelviewmatrix, 0, modelviewOBJ, 0, modelviewmatrix.length);
    }

    public void translate(float x, float y, float z){

        Matrix.translateM(modelviewOBJ,0, x, y, z);
    }

    public void rotate(float angle, float x, float y, float z){

        Matrix.rotateM(modelviewOBJ, 0, angle, x, y, z);
    }

    public void scale(float x, float y, float z){

        Matrix.scaleM(modelviewOBJ, 0, x, y, z);
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
     * Global function to send buffers to GPU
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
        GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, s_array.length * Short.BYTES,
                sb, GLES20.GL_STATIC_DRAW);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,0);
    }

    public void draw(final NoLightShaders shaders){

        shaders.setModelViewMatrix(modelviewOBJ);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, glposbuffer_vertex);
        shaders.setPositionsPointer(3,GLES20.GL_FLOAT);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, glelementbuffer_sphere);

        GLES20.glPolygonOffset(2.F,4.F);
        GLES20.glEnable(GLES20.GL_POLYGON_OFFSET_FILL);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, triangles.length, GLES20.GL_UNSIGNED_SHORT, 0);
        GLES20.glDisable(GLES20.GL_POLYGON_OFFSET_FILL);
        shaders.setColor(MyGLRenderer.black);

        for(int i=0; i<triangles.length; i+=3){

            GLES20.glDrawElements(GLES20.GL_LINE_LOOP, 3, GLES20.GL_UNSIGNED_SHORT, i*Short.BYTES);
        }

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,0);
    }
}
