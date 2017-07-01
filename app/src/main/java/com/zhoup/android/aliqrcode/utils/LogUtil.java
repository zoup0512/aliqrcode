package com.zhoup.android.aliqrcode.utils;

import android.os.Environment;
import android.util.Log;


import com.zhoup.android.aliqrcode.BuildConfig;
import com.zhoup.android.aliqrcode.consts.AppConst;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;

/**
 * Created by zhoup on 2017/6/22.
 */

public class LogUtil {

    private static final boolean DEBUG = BuildConfig.DEBUG;
    private static final String TAG = "AccessibilityService";
    private static final Calendar calendar = Calendar.getInstance();

    public static void i(String tag, String info) {
        if (DEBUG) {
            Log.i(tag, info);
        }
        printLog(info);
    }

    public static void i(String info) {
        if (DEBUG) {
            Log.i(TAG, info);
        }
        printLog(info);
    }

    public static void e(String tag, String info) {
        if (DEBUG) {
            Log.i(tag, info);
        }
        printLog(info);
    }

    public static void e(String info) {
        if (DEBUG) {
            Log.i(TAG, info);
        }
        printLog(info);
    }

    private static void printLog(final String log) {
        String parentPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                + AppConst.LOG_DIR;
        String fileName = "log_" + "_" + calendar.get(Calendar.YEAR) + "_"
                + (calendar.get(Calendar.MONTH) + 1) + "_" + calendar.get(Calendar.DAY_OF_MONTH)
                + ".txt";
        File file = new File(parentPath, fileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            OutputStream output = new FileOutputStream(file.getAbsolutePath(), true);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(output, "UTF-8");
            outputStreamWriter.write(log + "\n");
            outputStreamWriter.flush();
            outputStreamWriter.close();
            output.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
