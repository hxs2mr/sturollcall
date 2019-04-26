package com.gzgykj.sturollcall.dagg.qualifer;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Data on :2019/3/19 0019
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :区分httpmodule 中的provide   注：当app的链接地址不统一的时候 需要在httmodule重新添加一个provide  并重新创建注解一个@Qualifier文件
 */
@Qualifier
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface APPUrl {
}
