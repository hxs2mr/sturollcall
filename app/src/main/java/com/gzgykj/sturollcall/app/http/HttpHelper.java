package com.gzgykj.sturollcall.app.http;

import com.gzgykj.basecommon.response.AppRespose;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.QueryMap;

/**
 * Data on :2019/3/19 0019
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :定义所有获取数据的接口  统一管理
 */
public interface HttpHelper {
    //测试api
    Observable<AppRespose<String>> xxx(@QueryMap Map<String, String> action);//共同数据格式

    Observable<AppRespose<String>> sendsms(@FieldMap Map<String, String> action);//发送验证码

    Observable<AppRespose<String>> addface(@FieldMap Map<String, Object> action);

    Observable<AppRespose<String>> login(@FieldMap Map<String, String> action);
}
