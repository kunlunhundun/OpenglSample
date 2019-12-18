package com.kunlun.douyindemo.com.model;

import android.graphics.SurfaceTexture;
import android.opengl.EGLContext;

public interface OnSurfaceCreatedCallback {
    void onSurfaceCreated(SurfaceTexture texture, EGLContext context);
}
