package com.kunlun.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.knight.glview.CameraGLSurfaceView;
import com.kunlun.R;

public class MultipleVideoActivity extends BaseActivity {

    private CameraGLSurfaceView mCameraGLSurfaceView;

    private Button mSwitchCamera;

    private Button mChangeMp4;

    private Button mPlayMp4;

    private Button mPlayNewMp4;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_video);

        mCameraGLSurfaceView = findViewById(R.id.mCameraGLSurfaceView);

        mSwitchCamera = findViewById(R.id.mSwitchCamera);

        mChangeMp4 = findViewById(R.id.mChangeMp4);

        mPlayMp4 = findViewById(R.id.mPlayMp4);

        mPlayNewMp4 = findViewById(R.id.mPlayNewMp4);

        mSwitchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // mCameraGLSurfaceView
            }
        });


        mChangeMp4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        mPlayMp4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        mPlayNewMp4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
    }

}
