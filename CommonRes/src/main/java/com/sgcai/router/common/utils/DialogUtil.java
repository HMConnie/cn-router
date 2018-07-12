package com.sgcai.router.common.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;


/**
 * 对话框工具类
 */

public class DialogUtil {

    private DialogUtil() {

    }

    /**
     * 创建上传或下载进度框
     *
     * @param context
     * @param msg
     * @return
     */
    public static MaterialDialog createProgressDialog(Context context, String msg) {
        return new MaterialDialog.Builder(context)
                .content(msg)
                .progress(false, 100, true)
                .show();
    }

    /**
     * 加载loading对话框
     *
     * @param context
     * @return
     */
    public static MaterialDialog createLoadingDialog(Context context) {
        return new MaterialDialog.Builder(context)
                .progress(true, 0)
                .cancelable(true)
                .build();
    }

    /**
     * 显示普通不带title的对话框
     */
    public static void showNormal(Activity activity, String message, String cancle, String ok, MaterialDialog.SingleButtonCallback positiveCallback,
                                  MaterialDialog.SingleButtonCallback negativeCallback) {
        final MaterialDialog d = new MaterialDialog.Builder(activity)
                .content(message)
                .positiveText(ok)
                .negativeText(cancle)
                .onNegative(negativeCallback)
                .onPositive(positiveCallback)
                .build();

        d.getActionButton(DialogAction.POSITIVE).setTextSize(16);
        d.getActionButton(DialogAction.NEGATIVE).setTextSize(16);
        d.getContentView().setTextSize(18);
        d.show();
    }


    /**
     * 显示普通带title和content的对话框
     */
    public static void showNormal(Activity activity, String title, String message, String cancle, String ok, MaterialDialog.SingleButtonCallback positiveCallback,
                                  MaterialDialog.SingleButtonCallback negativeCallback) {
        final MaterialDialog d = new MaterialDialog.Builder(activity)
                .cancelable(false)
                .title(title)
                .content(message)
                .positiveText(ok)
                .negativeText(cancle)
                .onNegative(negativeCallback)
                .onPositive(positiveCallback)
                .build();

        d.getActionButton(DialogAction.POSITIVE).setTextSize(16);
        d.getActionButton(DialogAction.NEGATIVE).setTextSize(16);
        d.getContentView().setTextSize(18);
        d.show();
    }

    /**
     * 显示普通带title和content的对话框
     */
    public static MaterialDialog createNormal(Activity activity, int title, int message, int cancle, int ok, MaterialDialog.SingleButtonCallback positiveCallback,
                                              MaterialDialog.SingleButtonCallback negativeCallback) {
        MaterialDialog d = new MaterialDialog.Builder(activity)
                .cancelable(false)
                .title(title)
                .content(message)
                .positiveText(ok)
                .negativeText(cancle)
                .onNegative(negativeCallback)
                .onPositive(positiveCallback)
                .build();

        d.getActionButton(DialogAction.POSITIVE).setTextSize(16);
        d.getActionButton(DialogAction.NEGATIVE).setTextSize(16);
        d.getContentView().setTextSize(18);
        return d;
    }

    /**
     * 自定义内容的对话框
     */
    public static void showNormalCustomView(Activity activity, View customView, String cancle, String positive, MaterialDialog.SingleButtonCallback positiveCallback,
                                            MaterialDialog.SingleButtonCallback negativeCallback) {
        final MaterialDialog d = new MaterialDialog.Builder(activity)
                .customView(customView, false)
                .positiveText(positive)
                .negativeText(cancle)
                .onNegative(negativeCallback)
                .onPositive(positiveCallback)
                .build();

        d.getActionButton(DialogAction.POSITIVE).setTextSize(16);
        d.getActionButton(DialogAction.NEGATIVE).setTextSize(16);
        d.show();
    }


}
