package com.sgcai.router.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;

import com.sgcai.router.common.app.App;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DealUnKnowException implements UncaughtExceptionHandler {

    // 系统默认的UncaughtException处理类
    private UncaughtExceptionHandler mDefaultHandler;
    // CrashHandler实例
    private static DealUnKnowException INSTANCE = new DealUnKnowException();
    // 程序的Context对象
    private Context mContext;
    // 用来存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<String, String>();

    // 用于格式化日期,作为日志文件名的一部分
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    // 保存的路径
    private String resPath = null;

    public ExceptionCallBack exceptionCallBack;

    /**
     * 保证只有一个CrashHandler实例
     */
    private DealUnKnowException() {
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static DealUnKnowException getInstance() {
        return INSTANCE;
    }


    /**
     * 功能:初始化处理器
     *
     * @param context
     * @author: BaoZhiYuan
     * @date:2014-8-23下午1:24:20
     */
    public void init(Context context, String savePath, ExceptionCallBack callBack) {
        mContext = context;
        this.exceptionCallBack = callBack;
        this.resPath = savePath;
        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }


    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            if (exceptionCallBack != null) {
                exceptionCallBack.happenedException();
            }
        }
    }

    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }

        collectDeviceInfo(mContext);

        saveCrashInfo2File(ex);

        return true;
    }

    /**
     * 功能:收集设备的基本信息
     *
     * @param ctx
     * @author: BaoZhiYuan
     * @date:2014-8-23下午1:37:29
     */
    public void collectDeviceInfo(Context ctx) {
        try {
            /***加入是哪个(activity)界面出现异常***/
            Activity currentActivity = App.getInstance().getNewsLifecycleHandler().getCurrentActivity();
            if (currentActivity != null) {
                infos.put("activity", currentActivity.getClass().getName());
            }
            /***加入当前的版本信息***/
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("version", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    private String saveCrashInfo2File(Throwable ex) {
        // 1.写设备信息
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }
        // 2.写异常信息
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        try {
            long timestamp = System.currentTimeMillis();
            String time = formatter.format(new Date());
            String fileName = mContext.getPackageName() + "-" + time + "-" + timestamp + ".log";
            String path = resPath + File.separator + fileName;
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File file = new File(path);
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(sb.toString().getBytes());
                fos.close();
            }

            if (exceptionCallBack != null) {
                exceptionCallBack.success(path, sb.toString());
            }
            return fileName;
        } catch (Exception e) {
            if (exceptionCallBack != null) {
                exceptionCallBack.failed(sb.toString());
            }
        }
        return null;
    }


    public interface ExceptionCallBack {

        // 保存日志成功的回调函数
        void success(String path, String errorInfo);

        // 保存日志失败的回调函数
        void failed(String error);

        // 发生错误信息的处理
        void happenedException();
    }
}
