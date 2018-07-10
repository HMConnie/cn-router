package com.sgcai.router.app.compontent;

import android.widget.Toast;

import com.sgcai.router.app.ui.MainActivity;
import com.sgcai.router.common.component.ActivityScope;
import com.sgcai.router.common.component.AppComponent;

import dagger.Component;

/**
 * Created by hinge on 18/6/26.
 */
@ActivityScope
@Component(modules = {MainActivityModule.class}, dependencies = AppComponent.class)
public interface MainActivityComponent {
    void inject(MainActivity activity);
    Toast getToast();
}
