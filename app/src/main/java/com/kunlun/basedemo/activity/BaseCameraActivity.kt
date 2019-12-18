package com.kunlun.basedemo.activity

import android.Manifest
import android.app.Activity
import android.graphics.drawable.Animatable
import android.hardware.Camera
import android.opengl.GLSurfaceView
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.kunlun.R
import com.kunlun.activity.BaseActivity
import com.kunlun.basedemo.L11_1_CameraRenderer
import com.kunlun.basedemo.camera.CameraApi14
import com.kunlun.basedemo.util.AspectFrameLayout
import java.util.ArrayList
import java.util.concurrent.Executors

class BaseCameraActivity : BaseActivity(), View.OnClickListener {
    /**
     * 用于解决不同长宽比视频的适配问题，让GL绘制尽量处于填充满容器的状态
     */
    private lateinit var contentWrapper: AspectFrameLayout
    private lateinit var glSurfaceView: GLSurfaceView
    private lateinit var switchCamera: View
    private lateinit var renderer: L11_1_CameraRenderer
    private var isFront = true
    private val camera = CameraApi14()
    private val executor = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_camera)
        checkPermission()

        contentWrapper = findViewById(R.id.activity_camera_content_wrapper)
        glSurfaceView = findViewById(R.id.activity_camera_surfaceView)
        glSurfaceView.setEGLContextClientVersion(2)
        renderer = L11_1_CameraRenderer(this, camera)
        glSurfaceView.setRenderer(renderer)
        glSurfaceView.renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
        glSurfaceView.setOnClickListener(this)
        switchCamera = findViewById(R.id.activity_camera_switch)
        switchCamera.setOnClickListener(this)
    }



    override fun onStart() {
        super.onStart()
        openCamera()
    }

    override fun onStop() {
        super.onStop()
        camera.close()
    }

    private fun openCamera() {
        camera.open(if (isFront) Camera.CameraInfo.CAMERA_FACING_FRONT else Camera.CameraInfo.CAMERA_FACING_BACK)
        camera.preview()
        glSurfaceView.requestRender()
    }

    private fun switchCamera() {
        isFront = !isFront
        openCamera()
        renderer.switchCamera()
    }

    override fun onClick(v: View?) {
        when (v) {
            switchCamera -> {
                executor.execute {
                    switchCamera()
                }

                val imageView = v as ImageView
                val drawable = imageView.drawable
                if (drawable is Animatable) drawable.start()
            }
        }
    }

    private fun checkPermission() {
        TedPermission.with(this)
                .setPermissionListener(object : PermissionListener {
                    override fun onPermissionGranted() {
                    }

                    override fun onPermissionDenied(deniedPermissions: ArrayList<String>) {
                        Toast.makeText(this@BaseCameraActivity, "Permission Denied\n" + deniedPermissions.toString(), Toast
                                .LENGTH_SHORT).show()
                    }
                })
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at " + "[Setting] > [Permission]")
                .setPermissions(Manifest.permission.CAMERA)
                .check()
    }
}