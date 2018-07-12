package com.sgcai.router.common.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sgcai.router.common.app.App;
import com.sgcai.router.common.utils.Events;
import com.sgcai.router.common.utils.RxBus;
import com.trello.rxlifecycle.components.support.RxFragment;

import rx.functions.Action1;

public abstract class BaseFragment extends RxFragment {


    protected BaseActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutView(), container, false);
        initView(view);
        initEvents();
        return view;
    }

    private void initEvents() {
        RxBus.with(this).setEvent(Events.DEFAULT).onNext(new Action1<Events<?>>() {
            @Override
            public void call(Events<?> events) {
                BaseFragment.this.onEventMainThread(events);
            }
        }).create();
    }


    protected abstract int getLayoutView();

    protected abstract void initView(View view);


    public void toActivity(Class<?> clazz) {
        activity.toActivity(clazz);
    }

    public void toActivity(Class<?> clazz, Bundle bundle) {
        activity.toActivity(clazz, bundle);
    }

    protected void onEventMainThread(Events<?> events) {
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (BaseActivity) context;
    }


    @Override
    public void onDestroy() {
        App.getInstance().getRefWatcher().watch(this);
        super.onDestroy();
    }
}
