package fr.univ_poitiers.dptinfo.algo3d;


import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.view.MotionEvent;

/**
 * Class to described the surface view. Mainly based on well-known code.
 */
public class MyGLSurfaceView extends GLSurfaceView
{
    private final MyGLRenderer renderer;
    private final Scene scene;

    public MyGLSurfaceView(Context context, Scene scene)
    {
        super(context);
        this.scene=scene;

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);

        // Set the Renderer for drawing on the GLSurfaceView
        this.renderer = new MyGLRenderer(this,scene);
        setRenderer(this.renderer);

        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        //setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    private final float SCALE_FACTOR = 0.005F;
    private float previousx;
    private float previousy;

    @Override
    public boolean onTouchEvent(MotionEvent e)
    {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        float x = e.getX();
        float y = e.getY();

        float deltax=x-previousx; // motion along x axis
        float deltay=y-previousy; // motion along y axis

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                // to complete
                // You can use deltax and deltay to make mouse motion control the position
                // and/or the orientation of the viewer

                if(e.getPointerCount() == 1){
                    // Rotation sur x & y
                    scene.angley += deltax/4;
                    scene.anglex += deltay/4;

                    // Blocage de la rotation
                    scene.anglex = scene.anglex > 90 ? 90 : scene.anglex;
                    scene.anglex = scene.anglex < -90 ? -90 : scene.anglex;

                    break;
                }
                else if(e.getPointerCount() == 2){
                    // Translation
                    scene.posx += Math.cos(Math.toRadians(scene.angley)) * deltax/200 - Math.sin(Math.toRadians(scene.angley)) * deltay/200;
                    scene.posz += Math.sin(Math.toRadians(scene.angley)) * deltax/200 + Math.cos(Math.toRadians(scene.angley)) * deltay/200;
                }
            case MotionEvent.ACTION_DOWN:
                if(e.getPointerCount() == 3){
                    scene.reset_pos();
                }
        }

        previousx = x;
        previousy = y;
//        this.requestRender();
        return true;
    }

}
