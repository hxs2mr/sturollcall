package com.gzgykj.sturollcall.app.http;

import com.gzgykj.basecommon.response.AppRespose;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * Data on :2019/3/19 0019
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on : 网络接口
 */
public interface Api {
    String HOST="http://192.168.0.143:9999/";

    @GET("xxx")
    @FormUrlEncoded
    Observable<AppRespose<String>> xxx(@QueryMap Map<String, String> action);//get请求共同数据格式

    @FormUrlEncoded
    @POST("sendsms")
    Observable<AppRespose<String>> sendsms(@FieldMap Map<String, String> action);//发送验证码 多用于post请求中表单字段,Filed和FieldMap需要FormUrlEncoded结合使用


    @FormUrlEncoded
    @POST("cashier/userfeatures/add")
    Observable<AppRespose<String>> addface(@FieldMap Map<String, Object> action);

    //测试
    //登录接口
    @GET("auth/oauth/token")
    Observable<AppRespose<String>> login(@QueryMap Map<String,String> map);

}
