package com.kunlun.douyindemo.com.common.glutil;

import com.kunlun.douyindemo.com.common.filters.ImageFilter;

public class VideoFrameData {
    private ImageFilter mFilter;
    private float[] mMatrix;
    private long mTimeStamp;
    private int mTextureId;

    public VideoFrameData(ImageFilter filter, float[] matrix, long timestamp, int textureId) {
        this.mFilter = filter;
        this.mMatrix = matrix;
        this.mTimeStamp = timestamp;
        this.mTextureId = textureId;
    }

    public ImageFilter getFilter() {
        return mFilter;
    }

    public float[] getMatrix() {
        return mMatrix;
    }

    public long getTimestamp() {
        return mTimeStamp;
    }

    public int getTextureId() {
        return mTextureId;
    }
}
