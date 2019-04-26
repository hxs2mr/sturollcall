package com.gzgykj.sturollcall.dagg.scope;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * **********************
 * 功 能:与Fragment同生命周期的
 * *********************
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface FragmentScope {
}
