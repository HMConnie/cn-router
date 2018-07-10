package com.sgcai.taotao.user.compontent;

import android.widget.Toast;

import com.sgcai.router.common.component.ActivityScope;
import com.sgcai.router.common.component.AppComponent;
import com.sgcai.taotao.user.ui.LoginActivity;

import dagger.Component;

/**
 * Created by hinge on 18/6/26.
 */

@ActivityScope
@Component(modules = {LoginActivityModule.class}, dependencies = AppComponent.class)
public interface LoginActivityComponent {
    void inject(LoginActivity activity);
    Toast getToast();
}
