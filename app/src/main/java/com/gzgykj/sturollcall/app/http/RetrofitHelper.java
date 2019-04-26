package com.gzgykj.sturollcall.app.http;

import com.gzgykj.basecommon.response.AppRespose;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Data on :2019/3/19 0019
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :请求实现类
 */
public class RetrofitHelper implements HttpHelper {

    private Api mApi;
    @Inject
    public RetrofitHelper(Api api)
    {
        this.mApi = api;
    }
    @Override
    public Observable<AppRespose<String>> xxx(Map<String, String> action) {
        return mApi.xxx(action);
    }

    @Override
    public Observable<AppRespose<String>> sendsms(Map<String, String> action) {
        return mApi.sendsms(action);
    }

    @Override
    public Observable<AppRespose<String>> addface(Map<String, Object> action) {
        return mApi.addface(action);
    }

    @Override
    public Observable<AppRespose<String>> login(Map<String, String> action) {
        return mApi.login(action);
    }
}
