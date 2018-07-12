package com.sgcai.router.common.utils;

import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;

import com.sgcai.router.common.base.BaseActivity;
import com.sgcai.router.common.base.PermissionShadowActivity;

import rx.functions.Action1;

/**
 * Created by hinge on 17/4/12.
 */

public final class PermissionUtil {

    /**
     * 权限回调接口
     */
    public interface OnOpenPermissionListener {
        void onOpen(boolean isOpen);
    }

    private PermissionUtil() {

    }


    /**
     * 6.0 － 5.0 之间的权限处理
     *
     * @param context
     * @param permission 权限名称
     * @param listener   回调
     */
    public static void openPermission(BaseActivity context, String permission, final OnOpenPermissionListener listener) {
        if (context == null || listener == null) return;
        RxBus.with(context).setEvent(Events.DEFAULT)
                .onNext(new Action1<Events<?>>() {
                    @Override
                    public void call(Events<?> events) {
                        DefaultEvent event = (DefaultEvent) events.content;
                        if (event.event == GlobalConstants.EventCode.PERMISSION_INFO) {
                            listener.onOpen((Boolean) event.obj);
                        }
                    }
                }).onError(new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                listener.onOpen(false);
            }
        }).create();

        Intent intent = new Intent(context, PermissionShadowActivity.class);
        intent.putExtra("permission", permission);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);


    }

    /**
     * 跳转到权限设置界面
     */
    public static Intent getAppDetailSettingIntent(Context context) {

        // vivo 点击设置图标>加速白名单>我的app
        // 点击软件管理>软件管理权限>软件>我的app>信任该软件
        Intent appIntent = context.getPackageManager().getLaunchIntentForPackage("com.iqoo.secure");
        if (appIntent != null) {
            return appIntent;
        }

        // oppo 点击设置图标>应用权限管理>按应用程序管理>我的app>我信任该应用
        // 点击权限隐私>自启动管理>我的app
        appIntent = context.getPackageManager().getLaunchIntentForPackage("com.oppo.safe");
        if (appIntent != null) {
            return appIntent;
        }

        // miui
        appIntent = context.getPackageManager().getLaunchIntentForPackage("com.miui.securitycenter");
        if (appIntent != null) {
            return appIntent;
        }

        // meizu
        appIntent = context.getPackageManager().getLaunchIntentForPackage("com.meizu.safe");
        if (appIntent != null) {
            return appIntent;
        }
        // huawei
        appIntent = context.getPackageManager().getLaunchIntentForPackage("com.huawei.systemmanager");
        if (appIntent != null) {
            return appIntent;
        }

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        return intent;
    }

    /**
     * 判断权限gps权限是否开启
     *
     * @return
     */
    public static boolean isOPenGPS(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return gps || network;

    }


    /**
     * 测试当前摄像头能否被使用
     *
     * @return
     */
    public static boolean isCameraCanUse() {
        boolean canUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open(); //通过Camera.open() 拿到的Camera
            Camera.Parameters mParameters = mCamera.getParameters();
            mCamera.setParameters(mParameters);// setParameters 是针对魅族MX5 做的。MX5
        } catch (Exception e) {
            canUse = false;
        }
        if (canUse && mCamera != null) {
            mCamera.release();
        }
        return canUse;
    }


}
