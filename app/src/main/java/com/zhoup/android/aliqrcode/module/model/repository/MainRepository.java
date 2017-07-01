package com.zhoup.android.aliqrcode.module.model.repository;

import android.content.Context;

import com.tbruyelle.rxpermissions.RxPermissions;

import rx.Observable;

/**
 * Created by zhoup on 2017/6/22.
 */

public class MainRepository {
    public Observable<Boolean> checkPermissions(Context context, String[] permissions) {
        return RxPermissions.getInstance(context).request(permissions);
    }
}
