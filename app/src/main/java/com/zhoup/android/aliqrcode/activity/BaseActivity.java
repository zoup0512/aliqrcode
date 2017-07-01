package com.zhoup.android.aliqrcode.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zhoup.android.aliqrcode.application.MyApplication;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        ButterKnife.bind(this);
        initViews(savedInstanceState);
    }
    protected abstract void initViews(Bundle savedInstanceState);

    public abstract int getContentViewId();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getRefWatcher(this).watch(this);
    }
}
