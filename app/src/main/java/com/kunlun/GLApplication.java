package com.kunlun;

import android.app.Application;

import com.kunlun.common.ActivityListManager;
import com.kunlun.permission.KnightPermission;
import com.kunlun.permission.PermissionManager;
import com.kunlun.permission.PermissionUtils;

public class GLApplication extends Application {

    private static Application mContext;
    public static ActivityListManager activityListManager;


    @Override
    public void onCreate() {
        super.onCreate();
        setContext(this);
        PermissionManager.instance();
        KnightPermission.INSTANCE.init(this);
       activityListManager = new ActivityListManager();

    }

    public static void setContext(Application context) {
        mContext = context;
    }

    public static Application getContext(){
        return  mContext;
    }

    public static void registerActivityLifeCycle(Application.ActivityLifecycleCallbacks cb) {
        mContext.registerActivityLifecycleCallbacks(cb);
    }


}
