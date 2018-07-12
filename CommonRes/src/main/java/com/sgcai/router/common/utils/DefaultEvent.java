package com.sgcai.router.common.utils;

/**
 * Created by hinge on 17/9/28.
 */

public class DefaultEvent {
    public int event;
    public Object obj;
    public Object[] objs;

    public DefaultEvent() {
    }

    public DefaultEvent(int event) {
        this.event = event;
    }

    public DefaultEvent(Object obj) {
        this.obj = obj;
    }

    public DefaultEvent(int event, Object... objs) {
        this.event = event;
        this.objs = objs;
    }

    public DefaultEvent(int event, Object obj) {
        this.event = event;
        this.obj = obj;
    }
}
