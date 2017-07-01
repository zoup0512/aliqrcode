package com.zhoup.android.aliqrcode.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.zhoup.android.aliqrcode.R;
import com.zhoup.android.aliqrcode.activity.MainActivity;
import com.zhoup.android.aliqrcode.application.MyApplication;
import com.zhoup.android.aliqrcode.consts.AppConst;
import com.zhoup.android.aliqrcode.module.model.bean.AlipayAmountEvent;
import com.zhoup.android.aliqrcode.module.model.bean.ExpandAccessibilityNodeInfo;
import com.zhoup.android.aliqrcode.module.model.bean.QRCodeBean;
import com.zhoup.android.aliqrcode.task.AlipayQRTask;
import com.zhoup.android.aliqrcode.utils.AccessibilityServiceHelper;
import com.zhoup.android.aliqrcode.utils.LogUtil;
import com.zhoup.android.aliqrcode.utils.NotificationUtils;
import com.zhoup.android.aliqrcode.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by zhoup on 2017/6/22.
 */

public class AliQRService extends AccessibilityService {

    private static AliQRService mAliQRService;
    // 执行一系列的任务
    private AlipayQRTask mTask;
    // 是否应该结束
    private boolean quit;
    private QRCodeBean mCodeBean;
    // 是否生成二维码, 防止在生成二维码的同时多次修改数据
    private boolean generate;
    private boolean skip;
    private AtomicInteger notificationId = new AtomicInteger(1);
    private PowerManager.WakeLock mWakeLock;
    private int count = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        PowerManager pm = (PowerManager) MyApplication.getApplicationContext(this).getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Tag");
        mTask = new AlipayQRTask();
        // foreground service
        Notification notification = NotificationUtils.buildNotification(this, R.mipmap.ic_launcher,
                getString(R.string.app_name), getString(R.string.alipay_autoservice_running), false,
                PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0));
        startForeground(notificationId.incrementAndGet(), notification);

    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (!quit) {
            AccessibilityNodeInfo nodeInfo = event.getSource();
            ExpandAccessibilityNodeInfo target = searchNodeInfo(nodeInfo);
            //支付宝首页更多（“+”）菜单
            if (target == null) {
                if (getRootInActiveWindow() != null) {
                    mTask.clickMoreMenu(getRootInActiveWindow());
                }
                return;
            }
            switch (target.getId()) {
//                case AppConst.OPEN_MENU_ID:
//                    mTask.clickTargetView(target);
//                    break;
                case AppConst.COLLECT_MONEY:
                    mTask.clickTargetView(target);
                    break;
                case AppConst.SET_MONEY_ID: //设置金额
                    mTask.clickTargetView(target);
                    break;
                case AppConst.ADD_GATHER_REASON_ID:
                    mTask.clickTargetView(target);
                    break;
                case AppConst.REASON_RELATIVELAYOUT_ID:
                    // 模拟输入收款理由
                    mTask.inputQRCodeInfo(AliQRService.this, mCodeBean, target, getRootInActiveWindow());
                    break;
                case AppConst.SAVE_PICTURE: // 保存图片
                    if (skip) {
                        mTask.clickTargetView(target);
                        count = count + 1;
                        mTask.goGlobalBack(this);
                        skip=false;
                        generate=false;
                        if (count >= AppConst.QRCODE_TOTAL_COUNT) {
                            quit = true;
                            mTask.goGlobalBack(this);
                            this.stopSelf();
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 查找节点
     *
     * @param nodeInfo
     * @return
     */
    private ExpandAccessibilityNodeInfo searchNodeInfo(AccessibilityNodeInfo nodeInfo) {
        ExpandAccessibilityNodeInfo target;

        if (getRootInActiveWindow() != null) {
            target = AccessibilityServiceHelper.findNodeInfosById(getRootInActiveWindow(), AppConst.OPEN_MENU_ID);
            if (target != null) {
                return target;
            }
        }
        if (!skip) {
            target = AccessibilityServiceHelper.findNodeInfosById(getRootInActiveWindow(), AppConst.SET_MONEY_ID);
            if (target != null) {
                skip = true;
                return target;
            }
        }
        target = AccessibilityServiceHelper.findNodeInfosById(nodeInfo, AppConst.ADD_GATHER_REASON_ID);
        if (target != null) {
            return target;
        }
        if (!generate) {
            target = AccessibilityServiceHelper.findNodeInfosById(nodeInfo, AppConst.REASON_RELATIVELAYOUT_ID);
            if (target != null) {
                generate = true;
                return target;
            }
        }
        target = AccessibilityServiceHelper.findNodeInfosById(nodeInfo, AppConst.SAVE_PICTURE);
        if (target != null) {
            generate=false;
            return target;
        }
        target = AccessibilityServiceHelper.findNodeInfosByText(nodeInfo, AppConst.COLLECT_MONEY, "收钱");
        if (target != null) {
            return target;
        }
        return null;
    }

    @Override
    public void onInterrupt() {
        LogUtil.i("中断...");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if (mWakeLock.isHeld()) {
            mWakeLock.release();
        }
        return super.onUnbind(intent);
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        LogUtil.i("无障碍服务连接成功");
        mWakeLock.acquire();
        quit = false;
        mAliQRService = this;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.i("AlipayService onDestroy");
        stopForeground(true);
        if (mWakeLock.isHeld()) {
            mWakeLock.release();
        }
        mAliQRService = null;
        mTask = null;
        MyApplication.getRefWatcher(this).watch(this);
        EventBus.getDefault().unregister(this);
    }

    private void gotoAlipay(QRCodeBean mQRCodeBean) {
        this.mCodeBean = mQRCodeBean;
        String packageName = AppConst.ALIPAY_PACKAGE_NAME;
        Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent != null) {
            startActivity(intent);
        } else {
            ToastUtil.showToast(this.getApplicationContext(), getString(R.string.uninstall_alipay),
                    Toast.LENGTH_SHORT);
        }
    }

    /**
     * 判断服务是否已经启动
     *
     * @return
     */
    public static boolean isRunning() {
        if (mAliQRService == null) {
            return false;
        }
        AccessibilityManager accessibilityManager = (AccessibilityManager) mAliQRService.getSystemService(Context.ACCESSIBILITY_SERVICE);
        AccessibilityServiceInfo info = mAliQRService.getServiceInfo();
        if (info == null) {
            return false;
        }
        List<AccessibilityServiceInfo> list = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        Iterator<AccessibilityServiceInfo> iterator = list.iterator();

        boolean isConnect = false;
        while (iterator.hasNext()) {
            AccessibilityServiceInfo i = iterator.next();
            if (i.getId().equals(info.getId())) {
                isConnect = true;
                break;
            }
        }
        if (!isConnect) {
            return false;
        }
        return true;
    }


    // 接收支付宝账号
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onAlipayAmountEvent(AlipayAmountEvent event) {
        if (event != null) {
            BigDecimal alipayAmount = event.getAmount();
            QRCodeBean qrCodeBean = new QRCodeBean();
            qrCodeBean.setAmount(alipayAmount.toString());
            qrCodeBean.setAssociatedCode("AAA000");
            gotoAlipay(qrCodeBean);
        }
    }

}
