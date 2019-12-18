package com.kunlun.douyindemo.com.model;

import com.kunlun.douyindemo.com.activity.BaseActivity;

public class BaseActivityPresenter<T extends BaseActivity> extends BasePresenter<T> {

    public BaseActivityPresenter(T target) {
        super(target);
    }
}