package com.kunlun.basedemo

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.util.Log
import com.kunlun.basedemo.util.ShaderHelper
import java.io.BufferedOutputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.ByteBuffer
import javax.microedition.khronos.opengles.GL10

abstract class BaseRenderer(val context: Context) : GLSurfaceView.Renderer {
    protected var program = 0
    public var rendererCallback: RendererCallback? = null
    public var isReadCurrentFrame = false
    protected var outputWidth: Int = 0
    protected var outputHeight: Int = 0

    /**
     * 渲染完毕的回调
     */
    interface RendererCallback {
        /**
         * 渲染完毕
         *
         * @param data   缓存数据
         * @param width  数据宽度
         * @param height 数据高度
         */
        fun onRendererDone(data: ByteBuffer, width: Int, height: Int)
    }

    /**
     * 创建OpenGL程序对象
     *
     * @param vertexShader   顶点着色器代码
     * @param fragmentShader 片段着色器代码
     */
    protected fun makeProgram(vertexShader: String, fragmentShader: String) {
        // 步骤1：编译顶点着色器
        val vertexShaderId = ShaderHelper.compileVertexShader(vertexShader)
        // 步骤2：编译片段着色器
        val fragmentShaderId = ShaderHelper.compileFragmentShader(fragmentShader)
        // 步骤3：将顶点着色器、片段着色器进行链接，组装成一个OpenGL程序
        program = ShaderHelper.linkProgram(vertexShaderId, fragmentShaderId)


        ShaderHelper.validateProgram(program)


        // 步骤4：通知OpenGL开始使用该程序
        GLES20.glUseProgram(program)
    }

    protected fun getUniform(name: String): Int {
        return GLES20.glGetUniformLocation(program, name)
    }

    protected fun getAttrib(name: String): Int {
        return GLES20.glGetAttribLocation(program, name)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        outputWidth = width
        outputHeight = height
    }

    /**
     * 获取当前画面帧,并回调接口
     */
    protected fun onReadPixel(x: Int = 0, y: Int = 0, width: Int = outputWidth, height: Int = outputHeight) {
        if (!isReadCurrentFrame) {
            return
        }
        isReadCurrentFrame = false
        /*  val buffer = ByteBuffer.allocate(width * height * BufferUtil.BYTES_PER_FLOAT)
          GLES20.glReadPixels(x,
                  y,
                  width,
                  height,
                  GLES20.GL_RGBA,
                  GLES20.GL_UNSIGNED_BYTE, buffer)
          rendererCallback!!.onRendererDone(buffer, width, height) */


        val rgbaBuf = ByteBuffer.allocateDirect(width * height * 4)
        rgbaBuf.position(0)
        val start = System.nanoTime()
        GLES20.glReadPixels(0, 0, width, height, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE,
                rgbaBuf)
        rendererCallback!!.onRendererDone(rgbaBuf, width, height)
        val end = System.nanoTime()
        Log.e("TryOpenGL", "glReadPixels: " + (end - start))


    }

    fun saveRgb2Bitmap(buf: ByteBuffer, filename: String, width: Int, height: Int) {
        print("TryOpenGL"+ "Creating $filename")
        Log.w("", "TryOpenGL"+ "Creating $filename")
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


    protected fun readPixel(w: Int = outputWidth, h: Int = outputHeight): Bitmap {
        val buffer = ByteBuffer.allocate(w * h * 4)
        GLES20.glReadPixels(0,
                0,
                w,
                h,
                GLES20.GL_RGBA,
                GLES20.GL_UNSIGNED_BYTE, buffer)

        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        bitmap.copyPixelsFromBuffer(buffer)
        return bitmap
    }

    public open fun onClick() {}

}