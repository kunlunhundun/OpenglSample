package com.kunlun.douyindemo.com.common.glutil;

import static android.opengl.GLES20.glUseProgram;

public class TextureProgram {

    private int mProgramId;

    public TextureProgram(String vertexCode, String fragmentCode) {
        mProgramId = GLUtils.buildProgram(vertexCode, fragmentCode);
    }

    public void useProgram() {
        glUseProgram(mProgramId);
    }

    public int getProgramId() {
        return mProgramId;
    }

    public void enableAttrs() {

    }

    public void disableAttrs() {

    }
}
