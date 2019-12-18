package com.kunlun.douyindemo.com.model;

import com.kunlun.douyindemo.com.common.glutil.VideoFrameData;

public interface OnFrameAvailableListener {
    void onFrameAvailable(VideoFrameData frameData);
}