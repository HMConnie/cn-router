package com.sgcai.router.common.utils;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by hinge on 17/3/23.
 */

public class Events<T> {

    //所有事件的CODE
    public static final int DEFAULT = 0x1020; //点击事件

    //枚举
    @IntDef({DEFAULT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface EventCode {
    }


    public
    @EventCode
    int code;

    public T content;

    public static <T> Events<T> setContent(T t) {
        Events<T> events = new Events<T>();
        events.content = t;
        return events;
    }

    public <T> T getContent() {
        return (T) content;
    }

}
