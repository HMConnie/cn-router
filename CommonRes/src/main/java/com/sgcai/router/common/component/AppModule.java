package com.sgcai.router.common.component;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.sgcai.router.common.app.NewsLifecycleHandler;
import com.sgcai.router.common.retrofit.ServiceGenerator;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import dagger.Module;
import dagger.Provides;

/**
 * Created by hinge on 18/6/26.
 */

@Module
public class AppModule {

    private final Application app;

    public AppModule(Application app) {
        this.app = app;
    }

    @Provides
    Context getApplicationContext() {
        return app;
    }


    @Provides
    @ApplicationScope
    ServiceGenerator provideServiceGenerator() {
        return new ServiceGenerator();
    }


    @Provides
    @ApplicationScope
    NewsLifecycleHandler provideLifecycleHandler() {
        return new NewsLifecycleHandler();
    }

    @Provides
    @ApplicationScope
    RefWatcher provideRefWatcher() {
        return LeakCanary.install(app);
    }
}
