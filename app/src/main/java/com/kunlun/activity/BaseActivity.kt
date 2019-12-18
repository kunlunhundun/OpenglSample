package com.kunlun.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import com.kunlun.GLApplication
import com.kunlun.basedemo.util.GLAlphaAnimation

abstract class BaseActivity : AppCompatActivity() {
    private val isAddActivityList = "isAddActivityList"
    private var mActivity: Activity? = null
    private var onstop = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 21) {
//            val decorView = window.decorView
//            val option = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
//            decorView.systemUiVisibility = option
//            window.statusBarColor = Color.TRANSPARENT
        }
//        AndroidWorkaround.assistActivity(findViewById(android.R.id.content))

        mActivity = this
        if (intent.getBooleanExtra(isAddActivityList, false)) {
            GLApplication.activityListManager.addActivity(mActivity)

        }
    }

    override fun onStart() {
        super.onStart()
        if (!onstop) {
           // initData()
           // initListener()
        } else {
            onstop = false
        }

    }


    override fun onResume() {
        super.onResume()
        GLApplication.activityListManager.currentActivity = mActivity
    }

    override fun onPause() {
        super.onPause()
    }


    override fun onStop() {
        super.onStop()
        onstop = true
        if (GLApplication.activityListManager.currentActivity == mActivity) {
            GLApplication.activityListManager.currentActivity = null

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        GLApplication.activityListManager.removeActivity(mActivity)
        mActivity = null
    }



    fun goToPage(intent: Intent, isAdd: Boolean) {

        intent.putExtra(isAddActivityList, isAdd)
        startActivity(intent)
    }

    fun goToPageForResult(intent: Intent, isAdd: Boolean, requstCode: Int) {
        intent.putExtra(isAddActivityList, isAdd)
        startActivityForResult(intent, requstCode)
    }


    fun goToPage(intent: Intent) {

        goToPage(intent, true)

    }

   // abstract fun initData()
  //  abstract fun initListener()

}