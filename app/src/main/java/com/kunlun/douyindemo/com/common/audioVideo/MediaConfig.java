package com.kunlun.douyindemo.com.common.audioVideo;

import com.kunlun.douyindemo.com.common.util.ConstAV;

public class MediaConfig {



    private String filePath;

    private VideoConfig videoConfig;

    private AudioConfig audioConfig;

    private long maxDuration;

    private boolean supportAudio;

    private @ConstAV.SpeedMode
    int mSpeedMode;

    public MediaConfig(String filePath) {
        this.filePath = filePath;
    }

    public void setSpeedMode(@ConstAV.SpeedMode int speedMode) {
        this.mSpeedMode = speedMode;
    }

    public void setSupportAudio(boolean supportAudio) {
        this.supportAudio = supportAudio;
    }

    public void configure(AudioConfig audio, VideoConfig video) {
        this.audioConfig = audio;
        this.videoConfig = video;
    }

    public void setMaxDuration(long maxDuration) {
        this.maxDuration = maxDuration;
    }

    public long getMaxDuration() {
        return maxDuration;
    }

    public String getFilePath() {
        return filePath;
    }

    public int getSpeedMode() {
        return mSpeedMode;
    }

    public VideoConfig getVideoConfig() {
        return videoConfig;
    }

    public AudioConfig getAudioConfig() {
        return audioConfig;
    }

    public boolean supportAudio() {
        return supportAudio;
    }

    public float getSpeedFactor() {
        return getSpeedFactor(mSpeedMode);
    }

    public static float getSpeedFactor(@ConstAV.SpeedMode int speedMode) {
        switch (speedMode) {
            case ConstAV.MODE_EXTRA_SLOW:
                return 1f / 3;
            case ConstAV.MODE_SLOW:
                return 0.5f;
            case ConstAV.MODE_NORMAL:
                return 1f;
            case ConstAV.MODE_FAST:
                return 2f;
            case ConstAV.MODE_EXTRA_FAST:
                return 3f;
        }
        return 1;
    }
}