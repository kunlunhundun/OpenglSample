package com.kunlun.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.cry.zero_camera.activity.camera_filter.CameraCaptureFilterActivity
import com.cry.zero_camera.activity.capture.CameraCaptureActivity
import com.cry.zero_camera.activity.double_input.DoubleInput2Activity
import com.cry.zero_camera.activity.movie.GenerateMovieActivity
import com.cry.zero_camera.activity.ppt.PhotoAnimateSimpleActivity
import com.cry.zero_camera.activity.ppt.ccc.RecordFBOActivity
import com.cry.zero_camera.activity.video.VideoCaptureFilterActivity
import com.cry.zero_camera.preview.CameraActivity
import com.kunlun.R
import kotlinx.android.synthetic.main.activity_camera_opengl.*

class CameraOpenglActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_opengl)

        toZeroCamera.setOnClickListener {
            startActivity(Intent(this, CameraActivity::class.java))
        }

        toZeroCameraCapture.setOnClickListener {
            startActivity(Intent(this, CameraCaptureActivity::class.java))
        }

        toZeroCameraCaptureFilter.setOnClickListener {
            startActivity(Intent(this, CameraCaptureFilterActivity::class.java))
        }
        toZeroVideoCaptureFilter.setOnClickListener {
            startActivity(Intent(this, VideoCaptureFilterActivity::class.java))
        }
        toZeroDoubleInput2.setOnClickListener {
            startActivity(Intent(this, DoubleInput2Activity::class.java))
        }

        toRecordFBO.setOnClickListener {
            startActivity(Intent(this,RecordFBOActivity::class.java))
        }
        toPhotoAnimate.setOnClickListener {
            startActivity(Intent(this,PhotoAnimateSimpleActivity::class.java))
        }

        generateMovieActivity.setOnClickListener {
            startActivity(Intent(this, GenerateMovieActivity::class.java))
        }


    }


}
