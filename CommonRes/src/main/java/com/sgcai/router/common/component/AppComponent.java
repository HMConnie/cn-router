package com.sgcai.router.common.component;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.sgcai.router.common.retrofit.ServiceGenerator;

import dagger.Component;

/**
 * Created by hinge on 18/6/26.
 */

@ApplicationScope
@Component(modules = {AppModule.class})
public interface AppComponent {

    void inject(Application application);

    SharedPreferences getSharedPreferences();

    Context getApplicationContext();

    ServiceGenerator getServiceGenerator();

}