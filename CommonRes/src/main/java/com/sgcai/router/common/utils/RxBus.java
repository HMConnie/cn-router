package com.sgcai.router.common.utils;

import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.android.FragmentEvent;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

public class RxBus {

    private static RxBus rxBus;
    private final Subject<Events<?>, Events<?>> _bus;

    private RxBus() {
        _bus = new SerializedSubject<>(PublishSubject.<Events<?>>create());
    }

    public static RxBus getInstance() {
        if (rxBus == null) {
            synchronized (RxBus.class) {
                if (rxBus == null) {
                    rxBus = new RxBus();
                }
            }
        }
        return rxBus;
    }

    private void send(Events<?> o) {
        _bus.onNext(o);
    }

    public void send(@Events.EventCode int code, Object content) {
        Events<Object> event = new Events<>();
        event.code = code;
        event.content = content;
        send(event);
    }

    public Observable<Events<?>> toObservable() {
        return _bus;
    }

    public static SubscriberBuilder with(LifecycleProvider provider) {
        return new SubscriberBuilder(provider);
    }


    public static class SubscriberBuilder {

        private LifecycleProvider mFragLifecycleProvider;
        private FragmentEvent mFragmentEndEvent;
        private ActivityEvent mActivityEndEvent;
        private int event;
        private Action1<? super Events<?>> onNext;
        private Action1<Throwable> onError;

        public SubscriberBuilder(LifecycleProvider provider) {
            this.mFragLifecycleProvider = provider;
        }


        public SubscriberBuilder setEvent(@Events.EventCode int event) {
            this.event = event;
            return this;
        }

        public SubscriberBuilder setEndEvent(FragmentEvent event) {
            this.mFragmentEndEvent = event;
            return this;
        }

        public SubscriberBuilder setEndEvent(ActivityEvent event) {
            this.mActivityEndEvent = event;
            return this;
        }

        public SubscriberBuilder onNext(Action1<? super Events<?>> action) {
            this.onNext = action;
            return this;
        }

        public SubscriberBuilder onError(Action1<Throwable> action) {
            this.onError = action;
            return this;
        }


        public void create() {
            _create();
        }


        private Subscription _create() {
            if (mFragLifecycleProvider != null) {
                Observable.Transformer<Object, Object> objectFrgtObjectTransformer = mFragmentEndEvent == null ? mFragLifecycleProvider.bindToLifecycle() : mFragLifecycleProvider.bindUntilEvent(mFragmentEndEvent);
                return RxBus.getInstance().toObservable()
                        .compose(objectFrgtObjectTransformer) // 绑定生命周期
                        .filter(new Func1<Object, Boolean>() {

                            @Override
                            public Boolean call(Object o) {
                                Events<?> events = (Events<?>) o;
                                return events.code == event;
                            }
                        })   //过滤 根据code判断返回事件
                        .subscribe(new Action1<Object>() {

                            @Override
                            public void call(Object o) {
                                onNext.call((Events<?>) o);
                            }
                        }, onError == null ? new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        } : onError);
            }
            if (mFragLifecycleProvider != null) {
                Observable.Transformer<Object, Object> objectActObjectTransformer = mActivityEndEvent == null ? mFragLifecycleProvider.bindToLifecycle() : mFragLifecycleProvider.bindUntilEvent(mActivityEndEvent);
                return RxBus.getInstance().toObservable()
                        .compose(objectActObjectTransformer)
                        .filter(new Func1<Object, Boolean>() {
                            @Override
                            public Boolean call(Object o) {
                                Events<?> events = (Events<?>) o;
                                return events.code == event;
                            }
                        })
                        .subscribe(new Action1<Object>() {

                            @Override
                            public void call(Object o) {
                                onNext.call((Events<?>) o);
                            }
                        }, onError == null ? (Action1<Throwable>) new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        } : onError);
            }
            return null;
        }
    }
}