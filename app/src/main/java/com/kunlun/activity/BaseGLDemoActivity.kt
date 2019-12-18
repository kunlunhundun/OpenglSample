package com.kunlun.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.*
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.kunlun.R
import com.kunlun.basedemo.*
import com.kunlun.basedemo.activity.BaseCameraActivity
import com.kunlun.basedemo.activity.BaseVideoActivity
import com.kunlun.basedemo.util.*
import java.io.*
import java.nio.ByteBuffer
import java.util.ArrayList

class BaseGLDemoActivity: BaseActivity(), AdapterView.OnItemClickListener {
    private lateinit var root: ViewGroup
    private lateinit var listView: ListView
    private var glSurfaceView: GLSurfaceView? = null
    private var imageView: ImageView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_gldemo)

        root = findViewById<View>(R.id.main_root) as ViewGroup
        listView = findViewById<View>(R.id.main_list) as ListView
        val adapter = ArrayAdapter<MainListItems.Item>(this,
                android.R.layout.simple_list_item_1,android.R.id.text1, MainListItems.ITEMS)
        listView.adapter = adapter
        listView.onItemClickListener = this

        TedPermission.with(this)
                .setPermissionListener(object : PermissionListener {
                    override fun onPermissionGranted() {
                        //                        Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    }

                    override fun onPermissionDenied(deniedPermissions: ArrayList<String>) {
                        Toast.makeText(this@BaseGLDemoActivity, "Permission Denied\n" + deniedPermissions.toString(), Toast
                                .LENGTH_SHORT).show()
                    }
                })
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at " + "[Setting] > [Permission]")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check()

    }



    override fun onResume() {
        super.onResume()
        if (glSurfaceView != null) {
            glSurfaceView!!.onResume()
        }
    }

    override fun onPause() {
        super.onPause()
        if (glSurfaceView != null) {
            glSurfaceView!!.onPause()
        }
    }

    override fun onBackPressed() {
        if (glSurfaceView != null) {
            // 展示了GLSurfaceView，则删除ListView之外的其余控件
            var childCount = root.childCount
            var i = 0
            while (i < childCount) {
                if (root.getChildAt(i) !== listView) {
                    root.removeViewAt(i)
                    childCount--
                    i--
                }
                i++
            }
            glSurfaceView = null
        } else {
            super.onBackPressed()
        }
    }

    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        val clickClass = MainListItems.getClass(position)
        if (clickClass == L10_1_VideoRenderer::class.java) {
            startActivity(Intent(this, BaseVideoActivity::class.java))
            return
        }
        if (clickClass == L11_1_CameraRenderer::class.java) {
            startActivity(Intent(this, BaseCameraActivity::class.java))
            return
        }
        glSurfaceView = GLSurfaceView(this)
        root.addView(glSurfaceView)
        glSurfaceView!!.setEGLContextClientVersion(2)
        glSurfaceView!!.setEGLConfigChooser(false)

        val renderer = MainListItems.getRenderer(clickClass, this)
        if (renderer == null) {
            Toast.makeText(this, "反射构建渲染器失败", Toast.LENGTH_SHORT).show()
            return
        }

        glSurfaceView!!.setRenderer(renderer)
        glSurfaceView!!.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY

        if (clickClass == L100_Architecture::class.java) {
            chooseArchitecture(renderer as L100_Architecture)
        } else if (clickClass == L7_1_FBORenderer::class.java || clickClass == L7_2_FBORenderer::class.java) {
            readCurrentFrame(renderer as BaseRenderer)
        } else if (clickClass == L8_1_FilterRenderer::class.java) {
            glSurfaceView!!.renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
        }else if (clickClass == L6_2_TextureRenderer::class.java) {
            readCurrentFrame1( renderer as BaseRenderer)
            renderer.isReadCurrentFrame = true
        }

        glSurfaceView!!.setOnClickListener {
            glSurfaceView!!.requestRender()
            if (renderer is BaseRenderer) {
                renderer.onClick()
            }
        }
    }

    private fun chooseArchitecture(renderer: L100_Architecture) {
        val imageView = GLImageView()
        imageView.resId = R.drawable.tuzki
        imageView.x = 400f
        imageView.y = 400f
        imageView.alpha = 1f
        renderer.addGLImageView(imageView)

        val imageView2 = GLImageView()
        imageView2.resId = R.mipmap.ic_launcher
        imageView2.x = 200f
        imageView2.y = 1920f
        imageView2.alpha = 1f
        //        renderer.addGLImageView(imageView2);

        // 透明度
        val alphaAnimation = GLAlphaAnimation()
        alphaAnimation.fromAlpha = 0.5f
        alphaAnimation.toAlpha = 1f
        alphaAnimation.interpolator = DecelerateInterpolator()
        val now = System.currentTimeMillis()

        // 位移
        val translateAnimation = GLTranslateAnimation()
        translateAnimation.fromX = 500f
        translateAnimation.fromY = 0f
        translateAnimation.toX = 500f
        translateAnimation.toY = 1400f
        translateAnimation.interpolator = DecelerateInterpolator(2f)

        // 缩放
        val scaleAnimation = GLScaleAnimation()
        scaleAnimation.fromX = 0.5f
        scaleAnimation.fromY = 0.5f
        scaleAnimation.toX = 2.0f
        scaleAnimation.toY = 2.0f
        scaleAnimation.interpolator = DecelerateInterpolator(2f)

        // 旋转
        val rotateAnimation = GLRotateAnimation()
        rotateAnimation.fromDegrees = 0f
        rotateAnimation.toDegrees = 360f
        rotateAnimation.interpolator = OvershootInterpolator()

        val set = GLAnimationSet()
        set.addAnimation(translateAnimation)
        set.addAnimation(rotateAnimation)
        set.addAnimation(alphaAnimation)
        set.addAnimation(scaleAnimation)
        imageView.glAnimation = set

        set.startTime = now
        set.duration = 3000
        set.setListener(object : GLAnimation.GLAnimationListener {
            override fun onStart() {}

            override fun onEnd() {}

            override fun onProgress(percent: Float) {
                glSurfaceView!!.requestRender()
            }
        })
        imageView2.glAnimation = alphaAnimation
    }


    private fun readCurrentFrame1(renderer: BaseRenderer) {

        renderer.rendererCallback = object : BaseRenderer.RendererCallback {
            override fun onRendererDone(data: ByteBuffer, width: Int, height: Int) {
                val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                bitmap.copyPixelsFromBuffer(data)

                var  file : File?  = null;
                var fileName = "tedccc" + ".png"
                var root =  File(Environment.getExternalStorageDirectory(), getPackageName());
                var dir =  File(root, "images");
                if (dir.mkdirs() || dir.isDirectory()) {

                    var tempFile = File(dir,fileName)
                    file = tempFile
                }
                try {
                    var fos =  FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    Log.d("", "TryOpenGL"+ "Creating $file")
                    Log.e("", "TryOpenGL"+ "Creating $file")

                    fos.flush();
                    fos.close();
                } catch ( e : FileNotFoundException) {
                    e.printStackTrace();
                } catch ( e : IOException) {
                    e.printStackTrace();
                }
            }

        }
    }


    @SuppressLint("SdCardPath")
    private fun readCurrentFrame(renderer: BaseRenderer) {
        imageView = ImageView(this)
        val params = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        root.addView(imageView, params)
        renderer.rendererCallback = object : BaseRenderer.RendererCallback {
            override fun onRendererDone(data: ByteBuffer, width: Int, height: Int) {
                val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                bitmap.copyPixelsFromBuffer(data)

                runOnUiThread {
                    imageView!!.setImageBitmap(bitmap)
                    print("readcurrentframe bitmap---->" )
                }
                var  file : File?  = null;
                var fileName = "tedddbb" + ".jpg"
                var root =  File(Environment.getExternalStorageDirectory(), getPackageName());
                var dir =  File(root, "images");
                if (dir.mkdirs() || dir.isDirectory()) {

                    var tempFile = File(dir,fileName)
                    file = tempFile
                }
                try {
                    var fos =  FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();
                } catch ( e : FileNotFoundException) {
                    e.printStackTrace();
                } catch ( e : IOException) {
                    e.printStackTrace();
                }

//                val destFile = File("/sdcard/A/test"
//                        //+ String.valueOf(System.currentTimeMillis())
//                        + ".jpg")
//                try {
//                    File("/sdcard/A").mkdirs()
//                    destFile.createNewFile()
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }

                // imageView.post { imageView.setImageBitmap(BitmapFactory.decodeFile(file!!.path))

                Thread(Runnable {
                    // var saveResult =   BitmapLess.`$save`(bitmap, Bitmap.CompressFormat.JPEG, 100, destFile)

                    //   print("readcurrentframe---->" + saveResult)

                    //  imageView.post { imageView.setImageBitmap(BitmapFactory.decodeFile(destFile.path)) }
                }).start()
                // data.clear()
            }

        }

        imageView!!.setOnClickListener {
            renderer.isReadCurrentFrame = true
            glSurfaceView!!.requestRender()
        }
    }

    fun saveRgb2Bitmap(buf: ByteBuffer, filename: String, width: Int, height: Int) {
        Log.e("TryOpenGL", "Creating $filename")
        Log.e("", "TryOpenGL"+ "Creating $filename")
        var bos: BufferedOutputStream? = null
        try {
            bos = BufferedOutputStream(FileOutputStream(filename))
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            bmp.copyPixelsFromBuffer(buf)
            bmp.compress(Bitmap.CompressFormat.PNG, 90, bos)
            bmp.recycle()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (bos != null) {
                try {
                    bos!!.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
    }


}
