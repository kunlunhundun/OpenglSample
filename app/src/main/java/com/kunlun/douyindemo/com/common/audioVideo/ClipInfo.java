package com.kunlun.douyindemo.com.common.audioVideo;

import com.kunlun.douyindemo.com.common.util.ConstAV;

public class ClipInfo {

    private String fileName;

    private long duration;

    private int type;

    public ClipInfo(String fileName, long duration,@ConstAV.DataType int type) {
        this.fileName = fileName;
        this.duration = duration;
        this.type = type;
    }

    public String getFileName() {
        return fileName;
    }

    public long getDuration() {
        return duration;
    }

    public @ConstAV.DataType
    int getType() {
        return type;
    }

}