package com.zhoup.android.aliqrcode.module.presenter;

import com.zhoup.android.aliqrcode.module.view.IBaseView;

/**
 * Created by zhoup on 2017/6/21.
 */

public interface BasePresenter<T extends IBaseView> {
    void attachView(T view);
    void detachView();
}
