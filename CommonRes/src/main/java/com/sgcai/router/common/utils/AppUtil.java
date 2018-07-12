package com.sgcai.router.common.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.sgcai.router.common.BuildConfig;
import com.sgcai.router.common.app.App;
import com.sgcai.router.common.base.BaseActivity;

import java.io.File;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by hinge on 17/3/27.
 */

public final class AppUtil {

    private static final String DEFAULT_CHANNELID = "official";// 默认返回官方渠道号

    private static String mChannelId = null;

    private AppUtil() {

    }

    private static SharedPreferences getSharedPreferences() {
        return App.getInstance().getSharedPreferences(GlobalConstants.SpName.BENBEN_SP_FILE, Context.MODE_PRIVATE);
    }

    /**
     * 获取渠道号
     *
     * @return
     */
    public static String getChannelId() {
        if (TextUtils.isEmpty(mChannelId)) {
            App context = App.getInstance();
            String channelId = getSharedPreferences().getString(GlobalConstants.SpName.CHANNEL_ID, "");
            if (TextUtils.isEmpty(channelId)) {// 缓存为空
                channelId = getMetaData(context, GlobalConstants.SpName.CHANNEL_ID); // 从清单文件中拿
                if (TextUtils.isEmpty(channelId)) { // 清单文件为空
                    channelId = DEFAULT_CHANNELID; // 返回默认的官方渠道号
                } else {
                    SharedPreferences.Editor edit = getSharedPreferences().edit();
                    edit.putString(GlobalConstants.SpName.CHANNEL_ID, channelId);
                    edit.commit();
                }
            }
            mChannelId = channelId;
        }
        return mChannelId;
    }

    /**
     * 获取版本名
     *
     * @return 当前应用的版本号
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static int getVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取meta－data数据
     */
    public static String getMetaData(Context context, String key) {
        Bundle metaData = null;
        Object value = null;
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                value = metaData.get(key);
                BigDecimal bigDecimal;
                if (value instanceof Long) {
                    long lVal = (long) value;
                    bigDecimal = new BigDecimal(lVal);
                    value = bigDecimal.longValue();
                } else if (value instanceof Integer) {
                    int iVal = (int) value;
                    bigDecimal = new BigDecimal(iVal);
                    value = bigDecimal.intValue();
                } else if (value instanceof Float) {
                    float fVal = (float) value;
                    bigDecimal = new BigDecimal(fVal);
                    value = bigDecimal.longValue();
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return String.valueOf(value);
    }

    public static void installApk(File file) {
        try {
            App context = App.getInstance();
            Activity currentActivity = context.getNewsLifecycleHandler().getCurrentActivity();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                boolean requestPackageInstalls = context.getPackageManager().canRequestPackageInstalls();
                if (requestPackageInstalls) {
                    install(file, context);
                } else {
                    if (currentActivity != null && currentActivity instanceof BaseActivity) {
                        ((BaseActivity) currentActivity).showUnknowInstallAppDialog();
                    }
                }
            } else {
                install(file, context);
            }
        } catch (Throwable e) {
            EMUtil.reportUMError(e);
        }


    }

    private static void install(File file, Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//判读版本是否在7.0以上
            Uri apkUri = FileProvider.getUriForFile(context, "com.sgcai.benben.provider", file);//在AndroidManifest中的android:authorities值
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件
            install.setDataAndType(apkUri, "application/vnd.android.package-archive");
            context.startActivity(install);
        } else {
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(install);
        }
    }

    public static boolean isForeground(Activity activity) {
        return isForeground(activity, activity.getClass().getName());
    }

    /**
     * 判断某个界面是否在前台
     *
     * @param context   Context
     * @param className 界面的类名
     * @return 是否在前台显示
     */
    public static boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className))
            return false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName()))
                return true;
        }
        return false;
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;

        Object obj = null;

        Field field = null;

        int x = 0, sbar = 0;

        try {

            c = Class.forName("com.android.internal.R$dimen");

            obj = c.newInstance();

            field = c.getField("status_bar_height");

            x = Integer.parseInt(field.get(obj).toString());

            sbar = context.getResources().getDimensionPixelSize(x);

        } catch (Exception e1) {

            e1.printStackTrace();

        }

        return sbar;
    }

    /**
     * 打开系统浏览器
     *
     * @param activity
     * @param url
     */
    public static void openBrowser(Activity activity, String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        activity.startActivity(i);
    }

    /**
     * 版本号比较
     *
     * @param serviceVersion 服务端版本号
     * @return
     */
    public static int compareVersion(Context context, String serviceVersion) {
        if (context == null || TextUtils.isEmpty(serviceVersion)) return 0;

        String currentVersion = getVersionName(context);
        if (currentVersion.equals(serviceVersion)) {
            return 0;
        }
        String[] version1Array = currentVersion.split("\\.");
        String[] version2Array = serviceVersion.split("\\.");
        int index = 0;
        // 获取最小长度值
        int minLen = Math.min(version1Array.length, version2Array.length);
        int diff = 0;
        // 循环判断每位的大小
        while (index < minLen && (diff = Integer.parseInt(version1Array[index]) - Integer.parseInt(version2Array[index])) == 0) {
            index++;
        }
        if (diff == 0) {
            // 如果位数不一致，比较多余位数
            for (int i = index; i < version1Array.length; i++) {
                if (Integer.parseInt(version1Array[i]) > 0) {
                    return 1;
                }
            }

            for (int i = index; i < version2Array.length; i++) {
                if (Integer.parseInt(version2Array[i]) > 0) {
                    return -1;
                }
            }
            return 0;
        } else {
            return diff > 0 ? 1 : -1;
        }
    }

    /**
     * 判断是否安装过支付宝
     *
     * @param context
     * @return
     */
    public static boolean checkAliPayInstalled(Context context) {
        try {
            Uri uri = Uri.parse("alipays://platformapi/startApp");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            ComponentName componentName = intent.resolveActivity(context.getPackageManager());
            return componentName != null;
        } catch (Exception e) {
            return false;
        }

    }

    /**
     * 判断是否安装过微信
     *
     * @param context
     * @return
     */
    public static boolean checkWeChatInstalled(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }

        return false;

    }

    /**
     * InputMethodManager 解决内存泄漏问题
     */
    public static void fixInputMethodManagerLeak(Context destContext) {
        if (destContext == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) destContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }

        String[] arr = new String[]{"mCurRootView", "mServedView", "mNextServedView"};
        Field f;
        Object obj_get;
        for (int i = 0; i < arr.length; i++) {
            String param = arr[i];
            try {
                f = imm.getClass().getDeclaredField(param);
                if (!f.isAccessible()) {
                    f.setAccessible(true);
                }
                obj_get = f.get(imm);
                if (obj_get != null && obj_get instanceof View) {
                    View v_get = (View) obj_get;
                    if (v_get.getContext() == destContext) { // 被InputMethodManager持有引用的context是想要目标销毁的
                        f.set(imm, null); // 置空，破坏掉path to gc节点
                    } else {
                        // 不是想要目标销毁的，即为又进了另一层界面了，不要处理，避免影响原逻辑,也就不用继续for循环了
                        LogUtil.e(BuildConfig.APPLICATION_ID, "fixInputMethodManagerLeak break, context is not suitable, get_context="
                                + v_get.getContext() + " dest_context=" + destContext);
                        break;
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    /**
     * 根据Pid获取当前进程的名字，一般就是当前app的包名
     *
     * @return 返回进程的名字
     */
    public static String getAppName(Context context) {
        int pid = android.os.Process.myPid();
        String processName;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List list = activityManager.getRunningAppProcesses();
        Iterator i = list.iterator();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pid) {
                    // 根据进程的信息获取当前进程的名字
                    processName = info.processName;
                    // 返回当前进程名
                    return processName;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 没有匹配的项，返回为null
        return null;
    }

    public static void toAppSettings(Context context) {
        Intent intent;
        // 先判断当前系统版本
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {  // 3.0以上
            intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
        } else {
            intent = new Intent();
            intent.setClassName("com.android.settings", "com.android.settings.WirelessSettings");
        }
        context.startActivity(intent);
    }

    public static void testToApp(Context context, String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }

    public static void toSettings(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(localIntent);
    }

    /**
     * 延迟执行任务,单位:秒
     *
     * @param activity
     * @param seconds
     * @param runnable
     */
    public static void runOnUiThreadDelay(BaseActivity activity, final int seconds, final Runnable runnable) {
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(seconds + 1)
                .compose(activity.<Long>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Long, Integer>() {
                    @Override
                    public Integer call(Long increaseTime) {
                        return seconds - increaseTime.intValue();
                    }
                })
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i(BuildConfig.APPLICATION_ID, "runOnUIThreadDelay:onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(BuildConfig.APPLICATION_ID, "runOnUIThreadDelay:onError");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        runnable.run();
                    }
                });
    }
}
