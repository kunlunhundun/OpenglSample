package com.kunlun.basedemo.util

/**
 * GL透明度动画
 *
 */
class GLAlphaAnimation : GLAnimation() {
    var fromAlpha: Float = 0.toFloat()
    var toAlpha: Float = 0.toFloat()

    override fun applyTransformation(percent: Float, `object`: GLObject) {
        super.applyTransformation(percent, `object`)
        `object`.alpha = fromAlpha + (toAlpha - fromAlpha) * percent
    }
}