package com.zhoup.android.aliqrcode.utils;

import android.accessibilityservice.AccessibilityService;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityNodeInfo;

import com.zhoup.android.aliqrcode.application.MyApplication;
import com.zhoup.android.aliqrcode.module.model.bean.ExpandAccessibilityNodeInfo;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by zhoup on 2017/6/23.
 */

public class AccessibilityServiceHelper {
    /**
     * 通过id查找
     */
    public static ExpandAccessibilityNodeInfo findNodeInfosById(AccessibilityNodeInfo nodeInfo, String resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2 && nodeInfo != null) {
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(resId);
            if (list != null && !list.isEmpty()) {
                return new ExpandAccessibilityNodeInfo(list.get(0), resId);
            }
        }
        return null;
    }

    /**
     * 通过id查找
     */
    public static ExpandAccessibilityNodeInfo findNodeInfosById(AccessibilityNodeInfo nodeInfo, String resId, int index) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2 && nodeInfo != null) {
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(resId);
            if (list != null && !list.isEmpty()) {
                return new ExpandAccessibilityNodeInfo(list.get(index), resId);
            }
        }
        return null;
    }

    /**
     * 通过id查找
     */
    public static AccessibilityNodeInfo findAccessibilityNodeInfosById(AccessibilityNodeInfo nodeInfo, String resId, int index) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(resId);
            if (list != null && !list.isEmpty()) {
                return list.size() > index ? list.get(index) : null;
            }
        }
        return null;
    }

    /**
     * 通过id查找
     */
    public static AccessibilityNodeInfo findAccessibilityNodeInfosById(AccessibilityNodeInfo nodeInfo, String resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(resId);
            if (list != null && !list.isEmpty()) {
                return list.get(0);
            }
        }
        return null;
    }

    /**
     * 通过文本查找
     */
    public static ExpandAccessibilityNodeInfo findNodeInfosByText(AccessibilityNodeInfo nodeInfo, String resId, String text) {
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(text);
        if (list == null || list.isEmpty()) {
            return null;
        }
        return new ExpandAccessibilityNodeInfo(list.get(0), resId);
    }

    /**
     * 通过关键字查找
     */
    public static ExpandAccessibilityNodeInfo findNodeInfosByTexts(AccessibilityNodeInfo nodeInfo, String resId, String... texts) {
        for (String key : texts) {
            ExpandAccessibilityNodeInfo info = findNodeInfosByText(nodeInfo, resId, key);
            if (info != null) {
                return info;
            }
        }
        return null;
    }

    /**
     * 通过组件名字查找
     */
    public static AccessibilityNodeInfo findNodeInfosByClassName(AccessibilityNodeInfo nodeInfo, String className) {
        if (TextUtils.isEmpty(className)) {
            return null;
        }
        for (int i = 0; i < nodeInfo.getChildCount(); i++) {
            AccessibilityNodeInfo node = nodeInfo.getChild(i);
            if (className.equals(node.getClassName())) {
                return node;
            }
        }
        return null;
    }

    /**
     * 找父组件
     */
    public static AccessibilityNodeInfo findParentNodeInfosByClassName(AccessibilityNodeInfo nodeInfo, String className) {
        if (nodeInfo == null) {
            return null;
        }
        if (TextUtils.isEmpty(className)) {
            return null;
        }
        if (className.equals(nodeInfo.getClassName())) {
            return nodeInfo;
        }
        return findParentNodeInfosByClassName(nodeInfo.getParent(), className);
    }

    private static final Field sSourceNodeField;

    static {
        Field field = null;
        try {
            field = AccessibilityNodeInfo.class.getDeclaredField("mSourceNodeId");
            field.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        sSourceNodeField = field;
    }

    public static long getSourceNodeId(AccessibilityNodeInfo nodeInfo) {
        if (sSourceNodeField == null) {
            return -1;
        }
        try {
            return sSourceNodeField.getLong(nodeInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static String getViewIdResourceName(AccessibilityNodeInfo nodeInfo) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return nodeInfo.getViewIdResourceName();
        }
        return null;
    }

    /**
     * 返回主界面事件
     */
    public static void performHome(AccessibilityService service) {
        if (service == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
        }
    }

    /**
     * 返回事件
     */
    public static void performBack(AccessibilityService service) {
        if (service == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            LogUtil.i("模拟点击了物理返回键 GLOBAL_ACTION_BACK 按钮");
            service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
        }
    }

    /**
     * 点击事件
     */
    public static void performClick(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) {
            return;
        }
        if (nodeInfo.isClickable()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        } else {
            performClick(nodeInfo.getParent());
        }
    }

    public static void performInput(AccessibilityNodeInfo nodeInfo, String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Bundle arguments = new Bundle();
            arguments.putCharSequence(
                    AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, text);
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
        }
    }

    /**
     * 输入事件
     */
    public static void performInput(Context context, AccessibilityNodeInfo nodeInfo, String label, String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            ClipboardManager clipboard = (ClipboardManager) MyApplication.getApplicationContext(context).getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(label, text);
            clipboard.setPrimaryClip(clip);
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
            LogUtil.i("写入了 label = " + label + " , text = " + text);
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_PASTE);
        }
    }
}
