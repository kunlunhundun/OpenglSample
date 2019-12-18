package com.kunlun.douyindemo.com.model;

public class BasePresenter<T> {
    private T mTarget;

    public BasePresenter(T target) {
        this.mTarget = target;
    }

    public T getTarget() {
        return mTarget;
    }

    public void onCreate() {

    }

    public void onStart() {

    }

    public void onResume() {

    }

    public void onPause() {

    }

    public void onStop() {

    }

    public void onDestroy() {

    }
}

