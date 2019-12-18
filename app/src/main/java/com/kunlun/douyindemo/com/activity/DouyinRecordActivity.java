package com.kunlun.douyindemo.com.activity;

import android.graphics.SurfaceTexture;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.RadioGroup;

import com.kunlun.R;
import com.kunlun.camera.CameraCompat;
import com.kunlun.douyindemo.com.common.filters.ImageFilter;
import com.kunlun.douyindemo.com.common.filters.SoulOutFilter;
import com.kunlun.douyindemo.com.model.OnFrameAvailableListener;
import com.kunlun.douyindemo.com.model.RecordPresenter;
import com.kunlun.douyindemo.com.view.DyRecordSurfaceView;
import com.kunlun.douyindemo.com.view.ProgressView;
import com.kunlun.douyindemo.com.view.RecordButton;
import com.kunlun.douyindemo.com.view.RoundImageView;

public class DouyinRecordActivity extends BaseActivity<RecordPresenter>  {

    private DyRecordSurfaceView  mSurfaceView;

    private CameraCompat mCamera;

    private CheckBox mCbFlashLight;

    private RecordButton mButton;

    private RadioGroup mSpeedButtons;

    private ProgressView mProgressView;

    private RoundImageView img_glitch;


    private CameraCompat.CameraSize mPreviewSize;

    @Override
    protected void initPresenter() {
        mPresenter = new RecordPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_douyin_record);

        mPresenter.init();
        mCamera = CameraCompat.newInstance(this);

        mSurfaceView = findViewById(R.id.sv_record);
        mSurfaceView.setOnSurfaceCreatedCallback(mPresenter);
        CheckBox cb = findViewById(R.id.cb_flashlight);
        cb.setOnCheckedChangeListener(mPresenter);
        mCbFlashLight = cb;
        cb = findViewById(R.id.cb_camera);
        cb.setOnCheckedChangeListener(mPresenter);
        mButton = findViewById(R.id.btn_record);
        mButton.setOnRecordListener(mPresenter);
        findViewById(R.id.btn_next).setOnClickListener(mPresenter);
        mSpeedButtons = findViewById(R.id.rg_speed);
        mSpeedButtons.setOnCheckedChangeListener(mPresenter);
        findViewById(R.id.btn_delete).setOnClickListener(mPresenter);

        img_glitch = findViewById(R.id.img_glitch);
        img_glitch.setOnClickListener(mPresenter);

        findViewById(R.id.img_scale).setOnClickListener(mPresenter);
        findViewById(R.id.img_shake).setOnClickListener(mPresenter);
        findViewById(R.id.img_shine).setOnClickListener(mPresenter);
        findViewById(R.id.img_soul).setOnClickListener(mPresenter);
        findViewById(R.id.img_vertigo).setOnClickListener(mPresenter);


        mProgressView = findViewById(R.id.record_progress);

        mSurfaceView.setFilter(new SoulOutFilter());

    }


    @Override
    protected void onResume() {
        super.onResume();
        mSurfaceView.onResume();
        mCamera.startPreView();
        mPreviewSize = mCamera.getOutputSize();
        mSurfaceView.setPreviewSize(mPreviewSize.height,mPreviewSize.width);
        hideSystemNavigationBar();
    }

    @Override
    protected void onPause() {
        mPresenter.stopRecord();
        mCamera.stopPreView(true);
        mSurfaceView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public void setFilter(ImageFilter imgFilter) {
        mSurfaceView.setFilter(imgFilter);
    }

    public CameraCompat.CameraSize getCameraSize(){
        return mCamera.getOutputSize();
    }


    public void setFrameListener(OnFrameAvailableListener listener) {
        mSurfaceView.setFrameListener(listener);
    }

    public void startPreview(final SurfaceTexture texture) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mCamera.setSurfaceTexture(texture);
                mCamera.startPreView();
            }
        });
    }

    public void switchCamera(boolean checked) {
        mCbFlashLight.setEnabled(!checked);
        mCamera.switchCamera();
        mCbFlashLight.setChecked(false);
    }

    public void switchFlashLight(boolean checked) {
        if (checked) {
            mCamera.turnLight(true);
        } else {
            mCamera.turnLight(false);
        }
    }



    public void hideViews() {
        findViewById(R.id.record_group).setVisibility(View.GONE);
    }

    public void showViews() {
        findViewById(R.id.record_group).setVisibility(View.VISIBLE);
    }

    public void setProgress(float progress){
        mProgressView.setLoadingProgress(progress);
    }

    public void addProgress(float progress){
        mProgressView.addProgress(progress);
    }

    public void deleteProgress(){
        mProgressView.deleteProgress();
    }


}
