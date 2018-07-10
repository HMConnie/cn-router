package com.sgcai.router.app.compontent;

import android.content.Context;
import android.widget.Toast;

import com.sgcai.router.common.component.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by hinge on 18/6/26.
 */

@Module
public class MainActivityModule {

    @ActivityScope
    @Provides
    Toast provideToast(Context context) {
        return Toast.makeText(context, "哈哈", Toast.LENGTH_LONG);
    }
}
