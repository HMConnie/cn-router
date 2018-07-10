package com.sgcai.router.common.app;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.sgcai.router.common.BuildConfig;
import com.sgcai.router.common.component.AppComponent;
import com.sgcai.router.common.component.AppModule;
import com.sgcai.router.common.component.DaggerAppComponent;


/**
 * Created by hinge on 18/6/21.
 */

public class App extends Application {
    private AppComponent appComponent;


    private static App mInstance;

    public static App getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        if (BuildConfig.DEBUG) {           // These two lines must be written before init, otherwise these configurations will be invalid in the init process
            ARouter.openLog();     // Print log
            ARouter.openDebug();   // Turn on debugging mode (If you are running in InstantRun mode, you must turn on debug mode! Online version needs to be closed, otherwise there is a security risk)
        }
        ARouter.init(this); // As early as possible, it is recommended to initialize in the Application

        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
        appComponent.inject(this);
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
