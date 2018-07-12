package com.sgcai.router.common.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;


import com.sgcai.router.common.BuildConfig;
import com.sgcai.router.common.utils.LogUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.Stack;

/**
 * Created by hinge on 17/5/23.
 */

public class NewsLifecycleHandler implements Application.ActivityLifecycleCallbacks {

    // I use four separate variables here. You can, of course, just use two and
    // increment/decrement them instead of using four and incrementing them all.
    private int resumed;
    private int paused;
    private int started;
    private int stopped;
    private final Stack<Activity> activityStack;

    public NewsLifecycleHandler() {
        this.activityStack = new Stack<>();
        this.resumed = 0;
        this.paused = 0;
        this.started = 0;
        this.stopped = 0;
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        LogUtil.e(BuildConfig.APPLICATION_ID, "activity add = " + activity.getClass().getSimpleName());//检测Activity是否销毁
        activityStack.add(activity);
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        LogUtil.e(BuildConfig.APPLICATION_ID, "activity remove = " + activity.getClass().getSimpleName());//检测Activity是否销毁
        activityStack.remove(activity);
    }

    @Override
    public void onActivityResumed(Activity activity) {
        ++resumed;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        ++paused;
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
        ++started;
    }

    @Override
    public void onActivityStopped(Activity activity) {
        ++stopped;
    }

    // If you want a static function you can use to check if your application is
    // foreground/background, you can use the following:


    public boolean isApplicationVisible() {
        return started > stopped;
    }

    public boolean isApplicationInForeground() {
        return resumed > paused;
    }

    public boolean isApplicationInBackground() {
        return started == stopped;
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    public void finishActivity(Class<Activity> clazz) {
        if (!hasMainActivity(clazz)) return;

        for (int i = 0; i < activityStack.size(); i++) {
            Activity activity = activityStack.get(i);
            if (null != activity && activity.getClass() != clazz) {
                activity.finish();
                activityStack.remove(activity);
                --i;
            }
        }
    }

    public void appExit() {
        try {
            finishAllActivity();
            MobclickAgent.onKillProcess(App.getInstance());//保存统计信息
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ActivityManager activityMgr = (ActivityManager) App.getInstance().getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.killBackgroundProcesses(App.getInstance().getPackageName());
        }
    }

    public boolean hasMainActivity(Class<Activity> clazz) {
        boolean hasMainActivity = false;
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i) && activityStack.get(i).getClass() == clazz) {
                hasMainActivity = true;
                break;
            }
        }
        return hasMainActivity;
    }

    public Activity fromActivity() {
        int index = activityStack.size() - 2;
        if (index >= 0 && activityStack.size() > index) {
            return activityStack.get(index);
        }
        return null;
    }

    public Activity getCurrentActivity() {
        int index = activityStack.size() - 1;
        if (index >= 0 && activityStack.size() > index) {
            return activityStack.get(index);
        }
        return null;
    }

    public Stack<Activity> getActivityStack() {
        return activityStack;
    }
}