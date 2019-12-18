package com.kunlun.basedemo.util

/**
 * OpenGL绘制对象工具类
 */

object GLObjectUtils {

    fun setToCenter(`object`:GLObject) {
        setToCenterX(`object`)
        setToCenterY(`object`)
    }

    fun setToCenterX(`object`:GLObject) {
        `object`.x = (`object`.parentWidth - `object`.width) / 2
    }

    fun setToCenterY(`object`: GLObject) {
        `object`.y = (`object`.parentHeight - `object`.height) / 2
    }
}
