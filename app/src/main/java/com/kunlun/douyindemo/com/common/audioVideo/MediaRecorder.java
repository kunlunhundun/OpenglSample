package com.kunlun.douyindemo.com.common.audioVideo;

import android.opengl.EGLContext;
import android.support.annotation.Nullable;

import com.kunlun.douyindemo.com.common.glutil.VideoFrameData;
import com.kunlun.douyindemo.com.common.util.ConstAV;
import com.kunlun.douyindemo.com.common.util.FileUtils;
import com.kunlun.douyindemo.com.model.OnFrameAvailableListener;

public class MediaRecorder implements OnFrameAvailableListener, OnRecordFinishListener {

    private long maxDuration;
    /**
     * 视频还可以拍摄的帧数
     */
    private int remainDuration;

    private OnRecordFinishListener mFinishListener;

    private AudioRecorder audioRecorder;

    private VideoRecorder videoRecorder;

    private boolean supportAudio = true;

    public MediaRecorder(int seconds, @Nullable OnRecordFinishListener listener) {
        mFinishListener = listener;
        audioRecorder = new AudioRecorder();
        videoRecorder = new VideoRecorder();
        remainDuration = seconds * ConstAV.SECOND_IN_US;
        maxDuration = remainDuration;
        audioRecorder.setOnRecordFinishListener(this);
        videoRecorder.setOnRecordFinishListener(this);

        System.out.println("MediaRecorder Runnable thread id " + Thread.currentThread().getId());


    }


    public boolean start(EGLContext context, int width, int height, @ConstAV.SpeedMode int mode) {
        if (remainDuration <= 0) {
            return false;
        }
        FileUtils.createFile(ConstAV.VIDEO_TEMP_FILE_NAME);
        FileUtils.createFile(ConstAV.AUDIO_TEMP_FILE_NAME);
        AudioConfig audio = new AudioConfig(ConstAV.AUDIO_TEMP_FILE_NAME, ConstAV.AudioParams.SAMPLE_RATE, ConstAV.AudioParams.SAMPLE_PER_FRAME);
        audio.setSpeedMode(mode);
        audio.setMaxDuration(remainDuration);
        VideoConfig video = new VideoConfig(ConstAV.VIDEO_TEMP_FILE_NAME, context, width, height, ConstAV.VideoParams.BIT_RATE);
        video.setFactor(MediaConfig.getSpeedFactor(mode));
        video.setMaxDuration(remainDuration);
        audioRecorder.configure(audio);
        videoRecorder.configure(video);
        try {
            audioRecorder.prepareCodec();
            videoRecorder.prepareCodec();
        } catch (Throwable e) {
            audioRecorder.shutdown();
            videoRecorder.shutdown();
            return false;
        }


        if (supportAudio) {
            audioRecorder.start();
        }
        videoRecorder.start();
        return true;
    }

    public void stop() {
        if (supportAudio)
            audioRecorder.stop();
        videoRecorder.stop();
    }

    private void quit() {

    }


    @Override
    public void onFrameAvailable(VideoFrameData frameData) {
        videoRecorder.frameAvailable(frameData);
    }

    public void setOnProgressListener(com.kunlun.douyindemo.com.common.audioVideo.OnRecordProgressListener listener) {
        videoRecorder.setOnProgressListener(listener);
    }

    @Override
    public void onRecordFinish(ClipInfo info) {
        if (info.getType() == ConstAV.VIDEO) {
            remainDuration -= info.getDuration();
        }
        if (mFinishListener != null) {
            mFinishListener.onRecordFinish(info);
        }
    }


    public float getMaxDuration() {
        return maxDuration;
    }
}
