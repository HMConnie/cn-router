package com.sgcai.router.app.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.sgcai.router.app.R;
import com.sgcai.router.common.utils.GlobalConstants;
import com.sgcai.router.common.utils.RouterHub;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by hinge on 18/6/28.
 */

@Route(path = RouterHub.APP_SPLASH_ACTIVITY)
public class SplashActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(GlobalConstants.LAUNCHER_ADVERTISEMENT_DELAY + 1)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Long, Integer>() {
                    @Override
                    public Integer call(Long increaseTime) {
                        return GlobalConstants.LAUNCHER_ADVERTISEMENT_DELAY - increaseTime.intValue();
                    }
                })
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        jumpMainActivity();
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(Integer integer) {
                    }
                });
    }

    private void jumpMainActivity() {
        ARouter.getInstance().build(RouterHub.APP_MAIN_ACTIVITY).navigation(this);
        finish();
    }
}
