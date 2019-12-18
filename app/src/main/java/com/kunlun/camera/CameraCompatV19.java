package com.kunlun.camera;

import android.Manifest;
import android.content.Context;
import android.hardware.Camera;

import com.kunlun.common.LogUtil;
import com.kunlun.permission.PermissionManager;
import com.kunlun.permission.SimplePermissionCallback;

import java.util.List;

import static android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO;

public class CameraCompatV19  extends CameraCompat {

    private static final String TAG = "CameraCompatV19";

    private Camera mCamera;

    private boolean mIsLightOn;

    public CameraCompatV19(Context context) {
        super(context);
    }

    @Override
    protected void initCameraInfo() {
        int numberOfCameras = Camera.getNumberOfCameras();
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                setFrontCameraId(i);
            } else {
                setBackCameraId(i);
            }
        }
    }

    @Override
    protected void onStartPreview() {
        try {
            mCamera.setPreviewTexture(mSurfaceTexture);
            mCamera.startPreview();
        } catch (Throwable e) {
            LogUtil.e(TAG, e);
        }
    }

    @Override
    public void onStopPreview() {
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
        mCameraReady = false;
    }

    @Override
    protected void onOpenCamera(final int cameraType) {

        try {
            mCamera = Camera.open(cameraType == FRONT_CAMERA ? getFrontCameraIdV19() :
                    getBackCameraIdV19());
            initialize();
        } catch (Throwable e) {
            LogUtil.e(TAG, e);
        }

//        PermissionManager.instance().checkPermission(new String[]{Manifest.permission.CAMERA},
//                new SimplePermissionCallback() {
//                    @Override
//                    public void onPermissionGranted(String permission) {
//                        try {
//                            mCamera = Camera.open(cameraType == FRONT_CAMERA ? getFrontCameraIdV19() :
//                                    getBackCameraIdV19());
//                            initialize();
//                        } catch (Throwable e) {
//                            LogUtil.e(TAG, e);
//                        }
//                    }
//                });
    }


    @Override
    protected void onTurnLight(boolean on) {
        if (mIsLightOn == on) {
            return;
        }
        mIsLightOn = on;
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setFlashMode(on ? Camera.Parameters.FLASH_MODE_TORCH :
                Camera.Parameters.FLASH_MODE_OFF);
        mCamera.setParameters(parameters);
    }


    private void initialize() {
        if (mCamera == null) {
            return;
        }
        try {
            mCamera.setDisplayOrientation(90);
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setFocusMode(FOCUS_MODE_CONTINUOUS_VIDEO);
            List<Camera.Size> previewSizeList = parameters.getSupportedPreviewSizes();
            setOutputSize(CameraUtil.findBestSize(DESIRED_HEIGHT, previewSizeList));
            parameters.setPreviewSize(getOutputSize().width, getOutputSize().height);
            mCamera.setParameters(parameters);
            if (mSurfaceTexture != null) {
                onStartPreview();
            }
            mCameraReady = true;
        } catch (Throwable e) {
            LogUtil.e(TAG, "", e);
            mCameraReady = false;
        }
    }

}
