package com.sgcai.taotao.user.compontent;

import android.content.Context;
import android.widget.Toast;

import com.sgcai.router.common.component.ActivityScope;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by hinge on 18/6/26.
 */

@Module
public class LoginActivityModule {

    @Provides
    @ActivityScope
    Toast provideToast(Context context) {
        return Toast.makeText(context, "哈哈", Toast.LENGTH_LONG);
    }
}
