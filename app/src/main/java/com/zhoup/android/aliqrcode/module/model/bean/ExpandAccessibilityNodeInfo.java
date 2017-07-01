package com.zhoup.android.aliqrcode.module.model.bean;

import android.view.accessibility.AccessibilityNodeInfo;

/**
 * Created by zhoup on 2017/6/23.
 */

public class ExpandAccessibilityNodeInfo {
    private AccessibilityNodeInfo accessibilityNodeInfo;
    private String id;

    public ExpandAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo, String id){
        this.accessibilityNodeInfo = accessibilityNodeInfo;
        this.id = id;
    }

    public AccessibilityNodeInfo getAccessibilityNodeInfo() {
        return accessibilityNodeInfo;
    }

    public void setAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        this.accessibilityNodeInfo = accessibilityNodeInfo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
