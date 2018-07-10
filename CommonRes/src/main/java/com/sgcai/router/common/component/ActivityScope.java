package com.sgcai.router.common.component;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by hinge on 18/6/27.
 */
@Scope
@Documented
@Retention(RUNTIME)
public @interface ActivityScope {}