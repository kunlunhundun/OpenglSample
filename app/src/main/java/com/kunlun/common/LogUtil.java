package com.kunlun.common;

import android.util.Log;

public class LogUtil {

    private static final String TAG = "GLVideoEffect";

    public static void e(String tag, String msg, Throwable e) {
        Log.e(tag, msg, e);
    }

    public static void e(String tag, Throwable e) {
        Log.e(tag, "", e);
    }

    public static void e(String msg) {
        e(TAG, msg);
    }

    public static void e(Throwable e) {
        e(TAG, e);
    }

    public static void d(String tag, String msg) {
        Log.d(tag, msg);
    }

    public static void d(String msg) {
        d(TAG, msg);
    }

    public static void e(String tag, String msg) {
        Log.e(tag, msg);
    }

    public static void w(String msg) {
        e(TAG, msg);
    }

}
