package com.sgcai.router.common.base;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.sgcai.router.common.BuildConfig;
import com.sgcai.router.common.R;
import com.sgcai.router.common.app.App;
import com.sgcai.router.common.component.AppComponent;
import com.sgcai.router.common.dialog.ProgresDialog;
import com.sgcai.router.common.utils.AppUtil;
import com.sgcai.router.common.utils.DialogUtil;
import com.sgcai.router.common.utils.Events;
import com.sgcai.router.common.utils.GlobalConstants;
import com.sgcai.router.common.utils.LogUtil;
import com.sgcai.router.common.utils.NotificationUtil;
import com.sgcai.router.common.utils.PermissionUtil;
import com.squareup.leakcanary.RefWatcher;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.umeng.analytics.MobclickAgent;
import com.zhy.autolayout.AutoFrameLayout;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 继承RxFragmentActivity，防止Rxjava内存泄露
 */
public abstract class BaseActivity extends RxAppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private static final String[] PERMS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA};
    private static final String LAYOUT_LINEARLAYOUT = "LinearLayout";
    private static final String LAYOUT_FRAMELAYOUT = "FrameLayout";
    private static final String LAYOUT_RELATIVELAYOUT = "RelativeLayout";


    public static final int DIALOG_PEMISSION_REQ = 1001; // 权限提示对话框
    public static final int DIALOG_LOADING_REQ = 1002; // 加载对话框
    public static final int DIALOG_ENABLE_NOFITY_REQ = 1004; // 通知栏开启提示对话框
    public static final int DIALOG_ENABLE_UNKNOWN_APP = 1005; // 跳转到未知来源界面打开
    private boolean mIsPermissionOtherApp = false; // 是否从跳转了其他app
    private boolean mIsPermisssionDialogShow = false; // 是否弹出了权限对话框

    public abstract void inject(AppComponent appComponent);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inject(App.getInstance().getAppComponent());
    }

    /**
     * 显示网络加载对话框
     *
     * @param msg
     */
    public void showNetWorkDialog(String msg) {
        showNetWorkDialog(msg, true);
    }

    /**
     * 显示网络加载对话框,是否取消
     *
     * @param msg
     */
    public void showNetWorkDialog(String msg, boolean isCancelable) {
        Bundle bundle = new Bundle();
        bundle.putString(GlobalConstants.BUNDLE_STR_KEY, msg);
        bundle.putBoolean(GlobalConstants.BUNDLE_STR_NEW_KEY, isCancelable);
        showDialog(DIALOG_LOADING_REQ, bundle);
    }

    /**
     * dismiss网络加载加载对话框
     */
    public void dismissNetWorkDialog() {
        removeDialog(DIALOG_LOADING_REQ);
    }

    /**
     * android8.0未知来源的对话框
     */
    public void showUnknowInstallAppDialog() {
        showDialog(DIALOG_ENABLE_UNKNOWN_APP);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        if (mIsPermissionOtherApp && (!mIsPermisssionDialogShow)) {
            requiredInitPermissions();
        }
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPause(this);
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        AppUtil.fixInputMethodManagerLeak(this);
        App.getInstance().getRefWatcher().watch(this);
        super.onDestroy();
    }

    /**
     * 检测通知栏是否开启
     */
    public void checkNotifyEnable() {
        if (!NotificationUtil.isNotificationEnabled(this)) {
            showDialog(DIALOG_ENABLE_NOFITY_REQ);
        }
    }

    /**
     * 延迟执行
     *
     * @param seconds
     * @param runnable
     */
    protected void runOnUiThreadDelay(final int seconds, final Runnable runnable) {
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(seconds + 1)
                .compose(this.<Long>bindToLifecycle())
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
                        runnable.run();
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(BuildConfig.APPLICATION_ID, "runOnUIThreadDelay:onError");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        LogUtil.i(BuildConfig.APPLICATION_ID, "runOnUIThreadDelay:onNext");

                    }
                });
    }

    /**
     * 网络状态变化的监听意图过滤器
     */
    protected IntentFilter getNetWorkChangeIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANG");
        return intentFilter;
    }


    protected void onEventMainThread(Events<?> events) {
    }

    /**
     * 权限获取成功
     */
    protected void onPermissionCallbackSuccess() {
    }

    /**
     * 权限获取失败
     */
    protected void onPermissionCallbackFailed() {
    }


    /**
     * 是否有权限
     *
     * @return
     */
    public boolean hasPermissions() {
        return EasyPermissions.hasPermissions(this, PERMS);
    }


    @AfterPermissionGranted(GlobalConstants.RC_INIT_PERMISSION)
    public void requiredInitPermissions() {
        if (EasyPermissions.hasPermissions(this, PERMS)) {
            onPermissionCallbackSuccess();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.public_granted_init), GlobalConstants.RC_INIT_PERMISSION, PERMS);
        }
    }

    /**
     * 授予的权限回调
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (requestCode == GlobalConstants.RC_INIT_PERMISSION) {
            if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
                if (perms.size() == PERMS.length) {
                    onPermissionCallbackSuccess();
                } else {
                    showDialog(DIALOG_PEMISSION_REQ);
                }
            } else {
                onPermissionCallbackFailed();
            }
        }
    }

    /**
     * 拒绝的权限回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == GlobalConstants.RC_INIT_PERMISSION) {
            if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
                if (perms.size() == 0) {
                    onPermissionCallbackSuccess();
                } else {
                    showDialog(DIALOG_PEMISSION_REQ);
                }
            } else {
                onPermissionCallbackFailed();
            }
        }

    }


    /**
     * 屏幕适配 集成AutoLayoutActivity  代码
     */
    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = null;
        if (name.equals(LAYOUT_FRAMELAYOUT)) {
            view = new AutoFrameLayout(context, attrs);
        }

        if (name.equals(LAYOUT_LINEARLAYOUT)) {
            view = new AutoLinearLayout(context, attrs);
        }

        if (name.equals(LAYOUT_RELATIVELAYOUT)) {
            view = new AutoRelativeLayout(context, attrs);
        }

        if (view != null) return view;

        return super.onCreateView(name, context, attrs);
    }


    public void toActivity(Class<?> toClsActivity) {
        toActivity(toClsActivity, null);
    }


    public void toActivity(Class<?> toClsActivity, Bundle bundle) {
        Intent intent = new Intent(this, toClsActivity);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    public void toActivityForResult(Class<?> toClsActivity, int requestCode) {
        toActivityForResult(toClsActivity, null, requestCode);
    }

    public void toActivityForResult(Class<?> toClsActivity, Bundle bundle, int requestCode) {
        Intent intent = new Intent(this, toClsActivity);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_PEMISSION_REQ) {
            MaterialDialog d = new MaterialDialog.Builder(this)
                    .title(getString(R.string.public_granted_title))
                    .content(getString(R.string.public_granted_init))
                    .negativeColor(Color.GRAY)
                    .cancelable(false)
                    .positiveText(R.string.public_dialog_notify_positive)
                    .negativeText(R.string.public_dialog_negative)
                    .build();

            d.getActionButton(DialogAction.POSITIVE).setTextSize(16);
            d.getActionButton(DialogAction.NEGATIVE).setTextSize(16);
            d.getContentView().setTextSize(18);
            return d;
        } else if (id == DIALOG_LOADING_REQ) {
            return new ProgresDialog(this);
        } else if (id == DIALOG_ENABLE_NOFITY_REQ) {
            return DialogUtil.createNormal(this, R.string.public_notifty_title, R.string.public_notifty_message
                    , R.string.public_dialog_notify_positive
                    , R.string.public_dialog_negative, new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                        }
                    }, new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                            AppUtil.toSettings(BaseActivity.this);
                        }
                    });
        } else if (id == DIALOG_ENABLE_UNKNOWN_APP) {
            return DialogUtil.createNormal(this, R.string.public_unknown_app_title, R.string.public_unknown_app_message
                    , R.string.public_dialog_notify_positive
                    , R.string.public_dialog_negative, new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                        }
                    }, new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                            Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    });

        }
        return super.onCreateDialog(id);
    }


    @Override
    protected void onPrepareDialog(int id, final Dialog dialog, final Bundle args) {
        if (id == DIALOG_PEMISSION_REQ && dialog instanceof MaterialDialog) {
            MaterialDialog materialDialog = (MaterialDialog) dialog;
            materialDialog.getBuilder().onNegative(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    dialog.dismiss();
                    BaseActivity.this.mIsPermisssionDialogShow = false;
                    onPermissionCallbackFailed();
                }
            }).onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    dialog.dismiss();
                    Intent intent = PermissionUtil.getAppDetailSettingIntent(BaseActivity.this);
                    startActivity(intent);

                    mIsPermisssionDialogShow = false;
                    mIsPermissionOtherApp = true;
                }
            });
            mIsPermisssionDialogShow = true;
        } else if (id == DIALOG_LOADING_REQ && dialog instanceof MaterialDialog) {
            MaterialDialog materialDialog = (MaterialDialog) dialog;
            String content = args.getString(GlobalConstants.BUNDLE_STR_KEY);
            boolean isCancelable = args.getBoolean(GlobalConstants.BUNDLE_STR_NEW_KEY, true);
            materialDialog.setContent(content);
            materialDialog.setCancelable(isCancelable);
        }
        super.onPrepareDialog(id, dialog, args);
    }


}
