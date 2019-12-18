package com.kunlun.douyindemo.com.common.audioVideo;

import com.kunlun.douyindemo.com.common.util.ConstAV;

public class AudioConfig {

    private int sampleRate;

    private int samplePerFrame;

    private @ConstAV.SpeedMode
    int speedMode;

    private String fileName;

    private long maxDuration;

    public AudioConfig(String fileName, int sampleRate, int samplePerFrame) {
        this.sampleRate = sampleRate;
        this.samplePerFrame = samplePerFrame;
        this.fileName = fileName;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public int getSamplePerFrame() {
        return samplePerFrame;
    }

    public int getSpeedMode() {
        return speedMode;
    }

    public void setSpeedMode(int speedMode) {
        this.speedMode = speedMode;
    }

    public String getFileName(){
        return fileName;
    }

    public long getMaxDuration() {
        return maxDuration;
    }

    public void setMaxDuration(long maxDuration) {
        this.maxDuration = maxDuration;
    }
}
