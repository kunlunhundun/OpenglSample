package com.kunlun.basedemo.util

import android.graphics.Bitmap

/**
 * OpenGL绘制图片对象
 */
class GLImageView : GLObject() {
    var textureId: Int = 0
    var resId: Int = 0
    var bitmap: Bitmap? = null

    /**
     * 4个顶点的坐标
     */
    var position = floatArrayOf(-0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.5f, -0.5f, -0.5f)
}
