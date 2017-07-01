package com.zhoup.android.aliqrcode.module.view;

/**
 * Created by zhoup on 2017/6/21.
 */
public interface IMainView extends IBaseView{
    void showOpenServiceDialog();

    void showErrorMessage(String e);

    void checkService();
}
