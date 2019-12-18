package com.kunlun.douyindemo.com.view;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.EGLContext;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.kunlun.douyindemo.com.common.filters.ImageFilter;
import com.kunlun.douyindemo.com.model.DyRecordRenderer;
import com.kunlun.douyindemo.com.model.OnFrameAvailableListener;
import com.kunlun.douyindemo.com.model.OnSurfaceCreatedCallback;

public class DyRecordSurfaceView extends GLSurfaceView implements SurfaceTexture.OnFrameAvailableListener {

    private OnSurfaceCreatedCallback mCreatedCallback;

    private DyRecordRenderer mRenderer;

    public DyRecordSurfaceView(Context context) {
        super(context);
        setup();
    }

    public DyRecordSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    private void setup() {
        mRenderer = new DyRecordRenderer(this);
        setEGLContextClientVersion(2);
        setRenderer(mRenderer);
        setRenderMode(RENDERMODE_WHEN_DIRTY);
    }

    public void setOnSurfaceCreatedCallback(OnSurfaceCreatedCallback callback) {
        this.mCreatedCallback = callback;
    }

    public void onSurfaceCreated(SurfaceTexture texture, EGLContext context) {
        if (mCreatedCallback != null) {
            mCreatedCallback.onSurfaceCreated(texture, context);
        }
        texture.setOnFrameAvailableListener(this);
    }
    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        requestRender();
    }

    public void setFrameListener(OnFrameAvailableListener listener) {
        mRenderer.setFrameListener(listener);
    }

    public void setPreviewSize(int width,int height){
        mRenderer.setPreviewSize(width,height);
    }

    public void setFilter(ImageFilter filter){
        mRenderer.setFilter(filter);
    }


}
