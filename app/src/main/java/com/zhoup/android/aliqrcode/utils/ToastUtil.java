package com.zhoup.android.aliqrcode.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by zhoup on 2017/6/22.
 */

public class ToastUtil {

    private static Toast mToast;

    private ToastUtil() {
        //no instance
    }

    public static void showToast(Context context, CharSequence text, int duration) {
        if (mToast != null) {
            mToast.setText(text);
        } else {
            mToast = Toast.makeText(context, text, duration);
        }
        mToast.show();
    }

    public static void showToast(Context context, int resId, int duration) {
        if (mToast != null) {
            mToast.setText(resId);
        } else {
            mToast = Toast.makeText(context, resId, duration);
        }
        mToast.show();
    }

}
