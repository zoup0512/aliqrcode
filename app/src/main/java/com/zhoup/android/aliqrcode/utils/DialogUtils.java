package com.zhoup.android.aliqrcode.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 *Created by zhoup on 2017/6/22.
 */

public class DialogUtils {

    private DialogUtils() {
        //no instance
    }

    public static void showMessage(Context context, int titleId, int messageId, boolean cancelable,
                                   int positionId, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(titleId)
                .setMessage(messageId)
                .setCancelable(cancelable)
                .setPositiveButton(positionId, onClickListener).show();
    }

    public static void showMessage(Context context, int titleId, int messageId, boolean cancelable,
                                   int negativeId, DialogInterface.OnClickListener negativeOnClickListener,
                                   int positionId, DialogInterface.OnClickListener positionOnclickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(titleId)
                .setMessage(messageId)
                .setCancelable(cancelable)
                .setNegativeButton(negativeId, negativeOnClickListener)
                .setPositiveButton(positionId, positionOnclickListener).show();
    }

    public static void showMessage(Context context, int titleId, String message, boolean cancelable,
                                   int negativeId, DialogInterface.OnClickListener negativeOnClickListener,
                                   int positionId, DialogInterface.OnClickListener positionOnclickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(titleId)
                .setMessage(message)
                .setCancelable(cancelable)
                .setNegativeButton(negativeId, negativeOnClickListener)
                .setPositiveButton(positionId, positionOnclickListener).show();
    }


}
