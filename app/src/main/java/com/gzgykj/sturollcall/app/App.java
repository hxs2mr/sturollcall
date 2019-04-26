package com.gzgykj.sturollcall.app;

import android.app.Application;

import com.arcsoft.face.FaceEngine;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.gykj.acface.common.Constants;
import com.gzgykj.basecommon.mq.LossCallBack;
import com.gzgykj.sturollcall.R;
import com.gzgykj.sturollcall.dagg.component.AppComponent;
import com.gzgykj.sturollcall.dagg.component.DaggerAppComponent;
import com.gzgykj.sturollcall.dagg.mode.AppModel;
import com.gzgykj.sturollcall.dagg.mode.HttpModel;
import com.gzgykj.sturollcall.mq.RabbiMqEngine;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import java.io.File;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Data on :2019/3/19 0019
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :
 */
public class App extends Application {
    private static App instance;
    public static AppComponent appComponent;
    public RequestOptions RECYCLER_OPTIONS;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Realm.init(this);
        //realm初始化
        File file = new File(com.gzgykj.basecommon.utils.Constants.FACE_DB_PATH);
        if(!file.exists()){
            file.mkdir();
        }
        RealmConfiguration config = new RealmConfiguration.Builder()
                .directory(file)
                .build();
        Realm.setDefaultConfiguration(config);

        init();
        //初始化RabbitMq  连接设置
        RabbiMqEngine.getRabbiMqEngine().setUpConnectionFactory();
    }

    private void init() {
        //设置图片的加载测曰
        RECYCLER_OPTIONS =
                new RequestOptions()
                        .centerCrop()
                        .error(R.mipmap.icon_img_load_detail)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .dontAnimate();
        new Thread(new Runnable() {
            @Override
            public void run() {
                FaceEngine faceEngine = new FaceEngine();
                final int activeCode = faceEngine.active(instance, Constants.APP_ID, Constants.SDK_KEY);
            }
        }).start();

        StringBuffer param = new StringBuffer();
        param.append("appid=5c949419");
        param.append(",");
        // 设置使用v5+
        param.append(SpeechConstant.ENGINE_MODE+"="+SpeechConstant.MODE_MSC);
        SpeechUtility.createUtility(this, param.toString());
    }

    public static synchronized App getInstance() {
        return instance;
    }

    public static AppComponent getAppComponent() {
        if (appComponent == null) {
            appComponent = DaggerAppComponent.builder()
                    .appModel(new AppModel(instance))
                    .httpModel(new HttpModel())
                    .build();
        }
        return appComponent;
    }
    /**
     * 连接RabbitMq
     */
    public void connectRabbitMq(String  deviceId){
        RabbiMqEngine.getRabbiMqEngine().connect(deviceId);
    }

    //获取数据库保持的路径
    public String getFACE_DB_PATH() {
        return com.gzgykj.basecommon.utils.Constants.FACE_DB_PATH;
    }
}
