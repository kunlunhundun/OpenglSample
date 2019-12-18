package com.kunlun.douyindemo.com.model;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.SurfaceTexture;
import android.opengl.EGLContext;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.kunlun.common.LogUtil;
import com.kunlun.douyindemo.com.activity.AfterEffectActivity;
import com.kunlun.douyindemo.com.activity.DouyinRecordActivity;
import com.kunlun.douyindemo.com.common.audioVideo.ClipInfo;
import com.kunlun.douyindemo.com.common.audioVideo.MediaRecorder;
import com.kunlun.douyindemo.com.common.audioVideo.OnRecordFinishListener;
import com.kunlun.douyindemo.com.common.filters.GlitchFilter;
import com.kunlun.douyindemo.com.common.filters.ScaleFilter;
import com.kunlun.douyindemo.com.common.filters.ShakeEffectFilter;
import com.kunlun.douyindemo.com.common.filters.ShineWhiteFilter;
import com.kunlun.douyindemo.com.common.filters.SoulOutFilter;
import com.kunlun.douyindemo.com.common.filters.VertigoFilter;
import com.kunlun.douyindemo.com.common.util.ConstAV;
import com.kunlun.douyindemo.com.common.util.ConstAV.*;

import com.kunlun.douyindemo.com.common.util.FileUtils;
import com.kunlun.douyindemo.com.common.util.ScreenUtil;
import com.kunlun.douyindemo.com.common.util.StorageUtil;
import com.kunlun.douyindemo.com.ffmpeg.VideoCmdCallback;
import com.kunlun.douyindemo.com.ffmpeg.VideoCommand;
import com.kunlun.douyindemo.com.ffmpeg.VideoQueue;
import com.kunlun.douyindemo.com.view.RecordButton;
import  com.kunlun.douyindemo.com.common.audioVideo.OnRecordProgressListener;
import com.kunlun.permission.PermissionUtils;
import com.kunlun.R;



import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RecordPresenter  extends BaseActivityPresenter<DouyinRecordActivity>  implements RecordButton.OnRecordListener,
        OnRecordFinishListener,
        OnRecordProgressListener,
        View.OnClickListener,
        OnSurfaceCreatedCallback,
        RadioGroup.OnCheckedChangeListener,
        CompoundButton.OnCheckedChangeListener{

    private static final String mFinalPath = StorageUtil.getExternalStoragePath() + File.separator + "temp.mp4";

    private List<String> videoList = new ArrayList<>();

    /**
     * 初始速度1.0f
     */
    private @ConstAV.SpeedMode
    int mMode = ConstAV.MODE_NORMAL;

    private MediaRecorder mRecorder;

    private EGLContext mGlContext;

    private VideoQueue mQueue;

    private boolean mStarted = false;


    private ClipInfo audioInfo;
    private ClipInfo videoInfo;
    private float currentProgress;

    public RecordPresenter(DouyinRecordActivity target) {
        super(target);
    }

    public void init() {
        mQueue = new VideoQueue();
        mRecorder = new MediaRecorder(15,this);
        mRecorder.setOnProgressListener(this);
    }




    @Override
    public void onRecordStart() {
        int width = ScreenUtil.getScreenWidth();
        int height = ScreenUtil.getScreenHeight();
        //540 960
        if (!mRecorder.start(mGlContext, getTarget().getCameraSize().width, getTarget().getCameraSize().height, mMode)) {
            Toast.makeText(getTarget(), "视频已达到最大长度", Toast.LENGTH_SHORT).show();
            return;
        }
        mStarted = true;
        getTarget().hideViews();
    }

    @Override
    public void onRecordStop() {
        if (!mStarted) {
            return;
        }
        mRecorder.stop();
    }

    @Override
    public void onRecordFinish(ClipInfo info) {
        if (info.getType() == ConstAV.VIDEO) {
            currentProgress = info.getDuration() * 1.0f / mRecorder.getMaxDuration();
        }
        setClipInfo(info);
    }

    public void setClipInfo(final ClipInfo info) {
        getTarget().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (info.getType()) {
                    case ConstAV.VIDEO:
                        videoInfo = info;
                        break;
                    case ConstAV.AUDIO:
                        audioInfo = info;
                        break;
                }
                mergeVideoAudio();
            }
        });
    }

    private void mergeVideoAudio() {
        if (audioInfo == null || videoInfo == null) {
            return;
        }
        final String currentFile = generateFileName();
        FileUtils.createFile(currentFile);
        getTarget().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getTarget().addProgress(currentProgress);
                getTarget().showViews();
                currentProgress = 0f;
            }
        });
        mQueue.execCommand(VideoCommand.mergeVideoAudio(videoInfo.getFileName(),
                audioInfo.getFileName(), currentFile).toArray(), new VideoCmdCallback() {
            @Override
            public void onCommandFinish(boolean success) {
                videoList.add(currentFile);
                FileUtils.deleteFile(audioInfo.getFileName());
                FileUtils.deleteFile(videoInfo.getFileName());
                audioInfo = null;
                videoInfo = null;
                getTarget().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getTarget().addProgress(currentProgress);
                        getTarget().showViews();
                        currentProgress = 0f;
                    }
                });
            }
        });
    }

    @Override
    public void onRecordProgress(long duration) {
        final float progress = duration * 1.0f / mRecorder.getMaxDuration();
        getTarget().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getTarget().setProgress(progress);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                if (videoList.size() == 0) {
                    return;
                }
                onNextClick();
                break;
            case R.id.btn_delete:
                if (videoList.isEmpty()) {
                    return;
                }
                String file = videoList.remove(videoList.size() - 1);
                getTarget().deleteProgress();
                FileUtils.deleteFile(file);
                break;
            case R.id.img_soul:
                getTarget().setFilter(new SoulOutFilter());
                break;
            case R.id.img_scale:
                getTarget().setFilter(new ScaleFilter());
                break;
            case R.id.img_shine:
                getTarget().setFilter(new ShineWhiteFilter());
                break;
            case R.id.img_vertigo:
                getTarget().setFilter(new VertigoFilter());
                break;
            case R.id.img_glitch:
                getTarget().setFilter(new GlitchFilter());
                break;
            case R.id.img_shake:
                getTarget().setFilter(new ShakeEffectFilter());
                break;

        }

    }


    private void onNextClick() {
        final Dialog dialog = ProgressDialog.show(getTarget(), "正在合成", "正在合成");
        FileUtils.createFile(mFinalPath);
        mQueue.execCommand(VideoCommand.mergeVideo(videoList, mFinalPath).toArray(), new VideoCmdCallback() {
            @Override
            public void onCommandFinish(boolean success) {
                dialog.dismiss();
                Toast.makeText(getTarget(), "合成完毕", Toast.LENGTH_SHORT).show();
                AfterEffectActivity.start(getTarget(),mFinalPath);
            }
        });
    }


    @Override
    public void onSurfaceCreated(SurfaceTexture texture, EGLContext context) {
        try {
            mGlContext = context;
            getTarget().setFrameListener(mRecorder);
        } catch (Throwable e) {
            LogUtil.e(e);
        }
        getTarget().startPreview(texture);
    }




    public void stopRecord() {
        mRecorder.stop();

    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.btn_extra_slow:
                mMode = ConstAV.MODE_EXTRA_SLOW;
                break;
            case R.id.btn_slow:
                mMode = ConstAV.MODE_SLOW;
                break;
            case R.id.btn_normal:
                mMode = ConstAV.MODE_NORMAL;
                break;
            case R.id.btn_fast:
                mMode = ConstAV.MODE_FAST;
                break;
            case R.id.btn_extra_fast:
                mMode = ConstAV.MODE_EXTRA_FAST;
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_camera:
                getTarget().switchCamera(isChecked);
                break;
            case R.id.cb_flashlight:
                getTarget().switchFlashLight(isChecked);
                break;
        }
    }


    private String generateFileName() {
        return StorageUtil.getExternalStoragePath() + File.separator + "temp" + videoList.size() + ".mp4";
    }


}
