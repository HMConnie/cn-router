package com.sgcai.router.common.component;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.sgcai.router.common.retrofit.ServiceGenerator;

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
    SharedPreferences providerSP() {
        return app.getSharedPreferences("app", Context.MODE_PRIVATE);
    }


    @Provides
    @ApplicationScope
    ServiceGenerator provideServiceGenerator() {
        return new ServiceGenerator();
    }
}
