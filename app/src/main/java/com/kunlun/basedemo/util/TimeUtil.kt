package com.kunlun.basedemo.util

import java.util.concurrent.TimeUnit

/**
 * 时间工具类
 *
 */
object TimeUtil {
    fun formatVideoTime(time: Long): String {
        if (time > 0) {
            val minutes = TimeUnit.MILLISECONDS.toMinutes(time)
            val seconds = TimeUnit.MILLISECONDS.toSeconds(time)
            -TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time))
            return String.format("%d:%02d", minutes, seconds)
        }
        return "0:00"
    }
}