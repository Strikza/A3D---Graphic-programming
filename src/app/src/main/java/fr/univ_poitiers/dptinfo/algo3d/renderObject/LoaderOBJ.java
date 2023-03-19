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
public class LoaderOBJ extends Mesh{

    static final String LOG_TAG = "LoaderOBJ";

    private float[] vertextexture;
    private float[] vertexnormal;
    private int[] textures;
    private int[] normals;

    public LoaderOBJ(Context context, String fileName){
        super(false);

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
}
