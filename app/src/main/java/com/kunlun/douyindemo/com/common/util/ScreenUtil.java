package com.kunlun.douyindemo.com.common.util;

import android.util.DisplayMetrics;

import com.kunlun.GLApplication;

public class ScreenUtil {

    private static int sScreenWidth;

    private static int sScreenHeight;

    private static void init() {
        DisplayMetrics dm = GLApplication.getContext().getApplicationContext().getResources().getDisplayMetrics();
        sScreenWidth = dm.widthPixels;
        sScreenHeight = dm.heightPixels;
    }

    public static int getScreenWidth() {
        if (sScreenWidth == 0) {
            init();
        }
        return sScreenWidth;
    }

    public static int getScreenHeight() {
        if (sScreenHeight == 0) {
            init();
        }
        return sScreenHeight;
    }
}