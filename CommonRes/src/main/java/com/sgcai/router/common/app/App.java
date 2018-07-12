package com.sgcai.router.common.app;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.sgcai.router.common.BuildConfig;
import com.sgcai.router.common.component.AppComponent;
import com.sgcai.router.common.component.AppModule;
import com.sgcai.router.common.component.DaggerAppComponent;
import com.sgcai.router.common.utils.AppUtil;
import com.sgcai.router.common.utils.DealUnKnowException;
import com.sgcai.router.common.utils.GlobalConstants;
import com.sgcai.router.common.utils.ResPathCenter;
import com.squareup.leakcanary.RefWatcher;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.zhy.autolayout.config.AutoLayoutConifg;


/**
 * Created by hinge on 18/6/21.
 */

public class App extends Application implements DealUnKnowException.ExceptionCallBack {
    private AppComponent appComponent;
    private static App mInstance;
    private static volatile boolean isInit = false;

    public static App getInstance() {
        return mInstance;
    }

    private NewsLifecycleHandler mNewsLifecycleHandler;
    private RefWatcher mRefWatcher;

    public NewsLifecycleHandler getNewsLifecycleHandler() {
        return mNewsLifecycleHandler;
    }
    public RefWatcher getRefWatcher() {
        return mRefWatcher;
    }
    public AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        String processName = AppUtil.getAppName(this);
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        if (!TextUtils.equals(processName, getPackageName()) || isInit) {
            return;
        }
        init();
    }

    /**
     * 分割Dex文件，解决方法限制，解决方法数是有限制的差不多64000
     *
     * @param base
     */
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void init() {
        isInit = true;
        mInstance = this;
        if (BuildConfig.DEBUG) {           // These two lines must be written before init, otherwise these configurations will be invalid in the init process
            ARouter.openLog();     // Print log
            ARouter.openDebug();   // Turn on debugging mode (If you are running in InstantRun mode, you must turn on debug mode! Online version needs to be closed, otherwise there is a security risk)
        }
        ARouter.init(this); // As early as possible, it is recommended to initialize in the Application

        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
        appComponent.inject(this);

        mNewsLifecycleHandler = appComponent.provideLifecycleHandler();
        mRefWatcher = appComponent.provideRefWatcher();

        registerActivityLifecycleCallbacks(mNewsLifecycleHandler);

        DealUnKnowException.getInstance().init(this, ResPathCenter.getInstance().getLogPath(), this);
        /*** AutoLayout 适配布局***/
        AutoLayoutConifg.getInstance().useDeviceSize();

        String umAppKey = AppUtil.getMetaData(this, GlobalConstants.UMENG_APPKEY);
        String umChannel = AppUtil.getMetaData(this, GlobalConstants.UMENG_CHANNEL);

        /** 友盟统计 **/
        UMConfigure.setLogEnabled(BuildConfig.DEBUG);
        UMConfigure.init(this, umAppKey, umChannel, UMConfigure.DEVICE_TYPE_PHONE, null);
        MobclickAgent.setSessionContinueMillis(1000); // 设置session的间隔时间
        MobclickAgent.setCatchUncaughtExceptions(BuildConfig.DEBUG); //release时,关闭错误统计、自己捕获错误信息
    }

    @Override
    public void success(String path, String errorInfo) {
        MobclickAgent.reportError(this, errorInfo);
    }

    @Override
    public void failed(String error) {
        MobclickAgent.reportError(this, error);
    }

    @Override
    public void happenedException() {
        /**** 解决Activity重叠问题 ***/
        mNewsLifecycleHandler.finishAllActivity();
        Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent restart = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC, System.currentTimeMillis() + 2000, restart);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
