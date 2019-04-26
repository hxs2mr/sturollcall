package com.gzgykj.sturollcall.dagg.mode;

import android.util.Log;

import com.gzgykj.basecommon.utils.AESHelper;
import com.gzgykj.basecommon.utils.Constants;
import com.gzgykj.sturollcall.app.http.Api;
import com.gzgykj.sturollcall.dagg.qualifer.APPUrl;
import com.gzgykj.sturollcall.utils.SystemUtil;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Data on :2019/3/19 0019
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :网络请求
 */
@Module
public class HttpModel {

    @Singleton
    @Provides
    Retrofit.Builder provideRetrofitBuilder(){
        return  new Retrofit.Builder();
    }

    @Singleton
    @Provides
    OkHttpClient.Builder provideOkHttpBuilder()
    {
        return  new OkHttpClient.Builder();
    }

    @Singleton
    @Provides
    OkHttpClient provideClient(OkHttpClient.Builder builder)
    {
        /*服务响应的json*/
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.e("SERVER","Server"+message);
            }
        });
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(httpLoggingInterceptor);

        File cacheFile  = new File(Constants.PATH_SDCARD);
        Cache cache = new Cache(cacheFile,1024*1024*58);
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if(!SystemUtil.isNetworkConnected())//检查网络是否可以
                {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }
                Request response =  request.newBuilder().build();

                if(SystemUtil.isNetworkConnected())
                {
                    //xxx=xx&aaa=aa
                    int max = 0;//有网络时不缓存  保存时间为0
                    response =  request.newBuilder()
                            .addHeader("Cache-Control", "public, max-age=" + max)
                            //.addHeader("sign",initheader().trim())  暂时不加sign
                            .removeHeader("Pragma")
                            .build();
                }else{
                    //无网络时，设置缓存为4周
                    int max = 60*60*24*28;
                    response =  request.newBuilder()
                            .addHeader("Cache-Control", "public, max-age=" + max)
                            .removeHeader("Pragma")
                            .build();
                }
                return chain.proceed(response);
            }
        };
        //设置统一的请求头参数
        //builder.addNetworkInterceptor(interceptor);
        builder.addInterceptor(interceptor);
        //设置缓存
        builder.cache(cache);
        //设置超时
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        //错误重链
        builder.retryOnConnectionFailure(true);
        return builder.build();
    }

    /**
     * AES 加密
     * @return
     */
/*    private String initheader() {
        String aes_str = "model="+Constants.MODEL+"&time="+System.currentTimeMillis();//获取系统时间的13位的时间戳
        Log.d("HXSAES",aes_str);

        //c6YJ0OtelEH7/ThEJ2vC5zBvaEpPZm1EeVJBcitBQTFCUXJYUHFEZHJnMStOMHFUb3VsV0FmT0ZzOXhNKzBKVVduRkM3cVpwTll0YWpET3M=
        String sign = "";//AES加密
        sign = AESHelper.encrypt(aes_str);
        Log.d("HXSAES1",sign);

        String jie = AESHelper.decrypt(sign);
        Log.d("HXSAES2",jie);
        return sign;
    }*/

    private Retrofit createRetrofit(Retrofit.Builder builder,OkHttpClient client,String host){
        System.out.println("请求地址"+host+client.toString());
        return builder
                .baseUrl(host)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    //Api接口
    @Singleton
    @Provides
    @APPUrl //可定义其他的地址
    Retrofit provideAPPRetrofit(Retrofit.Builder builder,OkHttpClient client) {
        return createRetrofit(builder,client, Api.HOST);
    }
    @Singleton
    @Provides
    Api provideAPPService(@APPUrl Retrofit retrofit)
    {
        return retrofit.create(Api.class);
    }

}
