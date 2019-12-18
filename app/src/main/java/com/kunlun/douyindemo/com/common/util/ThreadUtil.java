package com.kunlun.douyindemo.com.common.util;

import android.os.Handler;
import android.os.HandlerThread;

public class ThreadUtil {

    public static Handler newHandlerThread(String tag) {
        HandlerThread thread = new HandlerThread(tag);
        thread.start();
        return new Handler(thread.getLooper());
    }
}
