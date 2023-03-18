package fr.univ_poitiers.dptinfo.algo3d.renderObject;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

import fr.univ_poitiers.dptinfo.algo3d.MyGLRenderer;
import fr.univ_poitiers.dptinfo.algo3d.NoLightShaders;

/**
 * Class to load OBJ file
 * @author Samuel Goubeau
 */
public class LoaderOBJ {

    static final String LOG_TAG = "LoaderOBJ";

    private float[] vertexpos;
    private float[] vertextexture;
    private float[] vertexnormal;
    private int[] triangles;
    private int[] textures;
    private int[] normals;

    private float[] modelviewOBJ;

    protected IntBuffer objbufferI;
    private int glelementbuffer_obj;
    private int glposbuffer_vertex;


    public LoaderOBJ(Context context, String fileName){

        modelviewOBJ=new float[16];

        try {
            InputStream ipst = context.getResources().getAssets().open(fileName);
            Log.d(LOG_TAG, "File open");

            // Initialization list
            /**
             * 0 : index position
             * 1 : index texture
             * 2 : index normal
             */
            final short INDEX_TEXTURE = 1;
            final short INDEX_NORMAL = 2;
            short index;
            int data = ipst.read();
            int first_vertex = -1;
            int second_vertex = -1;
            int last_vertex;
            int first_texture = -1;
            int second_texture = -1;
            int last_texture;
            int first_normal = -1;
            int second_normal = -1;
            int last_normal;
            StringBuilder tmp = new StringBuilder();
            List<Float> tmpVertexpos = new ArrayList<>();
            List<Float> tmpVertextexture = new ArrayList<>();
            List<Float> tmpVertexnormal = new ArrayList<>();
            List<Integer> tmpTriangles = new ArrayList<>();
            List<Integer> tmpTextures = new ArrayList<>();
            List<Integer> tmpNormals = new ArrayList<>();

            while(data != -1){
                switch (data){

                    case 'v':
                        Log.d(LOG_TAG, "Vertex line found");
                        List<Float> current_vertexes;
                        data = ipst.read();

                        switch (data){

                            case 't':
                                current_vertexes = tmpVertextexture;
                                data = ipst.read();
                                break;
                            case 'n':
                                current_vertexes = tmpVertexnormal;
                                data = ipst.read();
                                break;
                            default:
                                current_vertexes = tmpVertexpos;
                        }

                        while(data != '\r' && data != -1){

                            data = ipst.read();

                            if(data != ' ' && data != '\r' && data != -1){

                                tmp.append((char) data);
                            }
                            else{

                                current_vertexes.add(Float.parseFloat(tmp.toString()));
                                tmp = new StringBuilder();
                            }
                        }

                        data = ipst.read();
                        break;

                    case 'f':
                        Log.d(LOG_TAG, "Face line found");
                        // To throw the space after 'f'
                        data = ipst.read();
                        index = 0;

                        while(data != '\r' && data != -1){

                            data = ipst.read();

                            if(data != ' ' && data != '/' && data != '\r' && data != -1){

                                tmp.append((char) data);
                            }
                            else{

                                if(tmp.length() != 0){

                                    switch (index){

                                        case INDEX_TEXTURE:
                                            if(first_texture == -1){

                                                first_texture = Integer.parseInt(tmp.toString());
                                            }
                                            else if(second_texture == -1){

                                                second_texture =  Integer.parseInt(tmp.toString());
                                            }
                                            else{

                                                Log.d(LOG_TAG, "Triangle found");
                                                last_texture =  Integer.parseInt(tmp.toString());

                                                tmpTextures.add(first_texture);
                                                tmpTextures.add(second_texture);
                                                tmpTextures.add(last_texture);

                                                second_texture = last_texture;
                                            }
                                            break;
                                        case INDEX_NORMAL:
                                            if(first_normal == -1){

                                                first_normal =  Integer.parseInt(tmp.toString());
                                            }
                                            else if(second_normal == -1){

                                                second_normal =  Integer.parseInt(tmp.toString());
                                            }
                                            else{

                                                last_normal = Integer.parseInt(tmp.toString());

                                                tmpNormals.add(first_normal);
                                                tmpNormals.add(second_normal);
                                                tmpNormals.add(last_normal);

                                                second_normal = last_normal;
                                            }
                                            break;
                                        default:
                                            if(first_vertex == -1){

                                                first_vertex =  Integer.parseInt(tmp.toString());
                                            }
                                            else if(second_vertex == -1){

                                                second_vertex =  Integer.parseInt(tmp.toString());
                                            }
                                            else{

                                                last_vertex =  Integer.parseInt(tmp.toString());

                                                tmpTriangles.add(first_vertex);
                                                tmpTriangles.add(second_vertex);
                                                tmpTriangles.add(last_vertex);

                                                second_vertex = last_vertex;
                                            }
                                    }

                                    if(data == '\r' || data == -1){

                                        first_vertex = -1;
                                        second_vertex = -1;
                                        first_texture = -1;
                                        second_texture = -1;
                                        first_normal = -1;
                                        second_normal = -1;
                                    }

                                    tmp = new StringBuilder();
                                }

                                if(data == '/'){

                                    index++;
                                }
                                else if(data == ' '){

                                    index = 0;
                                }
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
            triangles = new int[tmpTriangles.size()];
            for(int i = 0; i<tmpTriangles.size(); i++){

                triangles[i] =  tmpTriangles.get(i) - 1;
            }

            // Initialization of textures
            textures = new int[tmpTextures.size()];
            for(int i = 0; i<tmpTextures.size(); i++){

                textures[i] = tmpTextures.get(i) - 1;
            }

            // Initialization of normals
            normals = new int[tmpNormals.size()];
            for(int i = 0; i<tmpNormals.size(); i++){

                normals[i] = tmpNormals.get(i) - 1;
            }

        } catch (IOException e) {
            Log.d(LOG_TAG, "File not found");
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

        glelementbuffer_obj =buffers[1];
        send_buffer_to_GPU(objbufferI, triangles,  glelementbuffer_obj);
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

    public void draw(final NoLightShaders shaders){

        shaders.setModelViewMatrix(modelviewOBJ);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, glposbuffer_vertex);
        shaders.setPositionsPointer(3,GLES20.GL_FLOAT);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, glelementbuffer_obj);

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
