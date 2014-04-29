package net.objecthunter.flick3d;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;

public class Flick3d extends Activity {
    private Flick3dSurfaceView glView;
    private RotationTimerTask rotTask;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        glView = new Flick3dSurfaceView(this);
        this.setContentView(glView);
        rotTask = new RotationTimerTask(glView);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        new Thread(rotTask).start();
        Log.v("Flick3D", "posted rotation task");
        return true;
    }
}
