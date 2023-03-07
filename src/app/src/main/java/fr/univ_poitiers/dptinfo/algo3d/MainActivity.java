package fr.univ_poitiers.dptinfo.algo3d;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.service.controls.actions.ModeAction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;

import fr.univ_poitiers.dptinfo.algo3d.R;

/**
 * Class to described the only activity of the application
 * @author Philippe Meseure
 * @version 1.0
 */
public class MainActivity extends Activity
{
    /**
     * TAG for logging errors
     */
    private static final String LOG_TAG = "3D Algorithmic";


    /**
     * Singleton : only instance of MainActivity
     */
    private static MainActivity instance;

    /**
     * View where OpenGL can draw
     */
    private MyGLSurfaceView glview;
    /**
     * Reference to the Scene environment
     */
    private Scene scene;

    /**
     * Creation of the surface view and the scene
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        log("Starting "+ getString(R.string.app_name) +"...");

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity
        this.scene =new Scene();
        this.glview = new MyGLSurfaceView(this,this.scene);
        setContentView(this.glview);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        /**
         * Creation of reset button
         */
        DisplayMetrics screen = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(screen);

        Button reset = new Button(this);
        reset.setX(screen.widthPixels - 245);
        reset.setY(20);
        reset.setText(R.string.reset_text);
        reset.setOnClickListener(view -> scene.reset_pos());
        this.addContentView(reset, new AbsListView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    /**
     * Pause of the application. To do...
     */
    @Override
    protected void onPause() {
        super.onPause();
        log("Pausing "+ getString(R.string.app_name) +".");
        this.glview.onPause();
    }

    /**
     * Resume of the application. To do...
     */
    @Override
    protected void onResume() {
        super.onResume();
        log("Resuming "+ getString(R.string.app_name) +".");
        glview.onResume();
    }

    /**
     * Method used to send message to the log console
     * @param message message to display in log
     */
    static public void log(String message)
    {
        Log.e(LOG_TAG, message);
    }
}

