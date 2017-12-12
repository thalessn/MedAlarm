package com.gmail.thales_silva_nascimento.alarmmed;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;
import static com.gmail.thales_silva_nascimento.alarmmed.Utils.convertDpToPixel;

/**
 * Created by thales on 14/10/17.
 */

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder mHolder;
    private Camera mCamera;
    private View view;


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        CircleImageView frame = (CircleImageView) view.findViewById(R.id.imgMedCam);
        setMeasuredDimension(frame.getLayoutParams().width, frame.getLayoutParams().width);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        //Responsável por alterar o formato da pré-visualização da camera.
        //https://stackoverflow.com/questions/22261226/display-camera-preview-on-a-circular-surface-porthole-effect
        Path clipPath = new Path();
        //TODO: define the circle you actually want
        clipPath.addCircle((Float) convertDpToPixel(85), (Float) convertDpToPixel(85), (Float) convertDpToPixel(85), Path.Direction.CW);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        canvas.clipPath(clipPath);
        canvas.drawPath(clipPath, paint);
        this.setZOrderOnTop(true);
        super.dispatchDraw(canvas);
    }

    public CameraPreview(Context context, Camera camera, View v) {
        super(context);
        mCamera = camera;
        //View necessário para encontrar a CircleImageView que conterá a camera.
        this.view = v;
        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            mCamera.setDisplayOrientation(90);
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();

        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
//        if(mCamera != null){
//            mCamera.stopPreview();
//            mCamera.setPreviewCallback(null);
//            mCamera.release();
//        }

    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null){
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e){
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }
}
