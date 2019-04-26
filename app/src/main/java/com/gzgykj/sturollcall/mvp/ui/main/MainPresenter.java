package com.gzgykj.sturollcall.mvp.ui.main;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.util.Log;

import com.gykj.jxfvlibrary.listener.OnJxFvListener;
import com.gykj.jxfvlibrary.manager.JXFvManager;
import com.gykj.jxfvlibrary.manager.ThreadManager;
import com.gzgykj.basecommon.base.RxPresenter;
import com.gzgykj.basecommon.commom.RxUtil;
import com.gzgykj.basecommon.model.jxrealm.DeviceRealmManager;
import com.gzgykj.basecommon.model.jxrealm.DeviceidRealm;
import com.gzgykj.basecommon.model.jxrealm.IRealmListener;
import com.gzgykj.basecommon.model.facerealm.FaceRealm;
import com.gzgykj.sturollcall.app.App;
import com.gzgykj.sturollcall.app.datamanager.DataManager;
import com.gzgykj.sturollcall.mq.RabbiMqEngine;
import com.gzgykj.sturollcall.mvp.model.MainTimeBean;
import com.gzgykj.sturollcall.utils.ToastUtil;
import com.orhanobut.logger.Logger;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import io.reactivex.observers.ResourceObserver;
import io.realm.Realm;
import io.realm.RealmResults;

import static com.gzgykj.sturollcall.utils.DateUtil.getCurrentDateHHmm;
import static com.gzgykj.sturollcall.utils.DateUtil.getCurrentDateString;
import static com.gzgykj.sturollcall.utils.DateUtil.getWeek;
import static com.gzgykj.sturollcall.utils.DateUtil.getoldyear;

/**
 * Data on :2019/3/20 0020
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :
 */
public class MainPresenter extends RxPresenter<MainContract.View>implements MainContract.Prensenter {

    private DataManager mDataManager;
    private Timer mTimer;
    private TimerTask mDateTask;
    private TimerTask mWeatherTask;
    private Handler mHandler;

    private Realm mRealm;
    private    MainTimeBean bean = new MainTimeBean();
    private  boolean JX_FACE_DELETE=false;
    @Inject
    public MainPresenter(DataManager dataManager)
    {
        this.mDataManager = dataManager;
    }
    /**
     * 获取时间 日期 农历
     */
    @Override
    public void time() {
        mTimer = new Timer();
        mHandler = new Handler();
        mDateTask =new TimerTask() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        initdate();
                    }
                });
            }
        };
        mTimer.schedule(mDateTask,0,30*1000);

    }

    /**
     * 发送MQ消息 用于接收人脸数据  更新数据库
     */
    @Override
    public void SendMQRealm(List<Long> stulist) {
      if(null == mRealm )
       {
         mRealm = Realm.getDefaultInstance();
       }
        RealmResults<FaceRealm> face = mRealm.where(FaceRealm.class).findAll();
        int jxcount = JXFvManager.getInstance().jxCountGroupFeatures();//指筋脉数据库
        Logger.e("HXS 人脸数据库大小:  "+face.size() +"    指静块脉大小:"+jxcount);

        if(null == face || face.size() <= 0 || jxcount == 0){  //如果数据库中没有数据    则发送消息到 RabbitMQ中获取人脸以及指静脉的数据

            RabbiMqEngine.getRabbiMqEngine().sendFaceJXMessage(face.size() <= 0,jxcount == 0,stulist);
        }
    }

    @SuppressLint("CheckResult")
    @Override
    public void login(Map<String, String> data) {
        mDataManager.login(data)
                .compose(RxUtil.rxSchedulerObservableHelper())
                .compose(RxUtil.handlerLYLResult())
                .subscribeWith(new ResourceObserver<String>() {
                    @Override
                    public void onNext(String s) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void initdate() {
        Date date = new Date();
        bean.setHhmm(getCurrentDateHHmm(date));
        bean.setYymmdd(getCurrentDateString());
        bean.setWeek(getWeek());
        bean.setOldtime(getoldyear());
            if(mView!=null)
            {
                mView.time(bean);
            }
    }

    public void canceltime()
    {
        if(mTimer!=null)
        {
            mTimer.cancel();
        }
    }

    /**
     * 注意一个deviceid  对应每间寝室
     * 删除学生 人脸数据库和指静脉数据库
     */
    public boolean deleteFaceAndFvRealm(final long deviceid) {
        JX_FACE_DELETE = false;
        final RealmResults<FaceRealm> faceAll = Realm.getDefaultInstance().where(FaceRealm.class).findAll();
        if(null != faceAll && faceAll.size()>0){
            Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    faceAll.deleteAllFromRealm();
                    JX_FACE_DELETE = true;
                }
            });
        }else {
            JX_FACE_DELETE = true;
        }
        ThreadManager.getInstance().submit(new Runnable() {
            @Override
            public void run() {
                //初始化数据库分组信息
                byte[] group1_byte = new byte[20];
                System.arraycopy(String.valueOf(deviceid).getBytes(),0,group1_byte,0,String.valueOf(deviceid).getBytes().length);
                JXFvManager.getInstance().jxRemoveGroupVeinFeature(new OnJxFvListener() {
                    @Override
                    public void success() {
                        Log.d("yxxs","清除指静脉数据成功");
                    }

                    @Override
                    public void failed(String error) {
                        Log.d("yxxs",error);
                    }
                });
            }
        });
        return JX_FACE_DELETE;
    }


    public void initRealmData(final long deviceid) {
        //初始化静芯指静脉
        JXFvManager.getInstance().jxInitVeinDatabase(App.getInstance().getFACE_DB_PATH() + "fv", new OnJxFvListener() {
            @Override
            public void success() {
                //初始化数据库分组信息
                byte[] group1_byte = new byte[20];
                System.arraycopy(String.valueOf(deviceid).getBytes(),0,group1_byte,0,String.valueOf(deviceid).getBytes().length);
                JXFvManager.getInstance().setGroup1Byte(group1_byte);
            }
            @Override
            public void failed(String error) {
                Log.d("yxxs","创建指静脉数据库失败"+error);
            }
        });
        RealmResults<DeviceidRealm> all = Realm.getDefaultInstance().where(DeviceidRealm.class).findAll();
        if(null == all || all.size() == 0){
            //初始化学校数据库
            //删除学校相关的人脸指纹数据库
            if(deleteFaceAndFvRealm(deviceid)){
                DeviceRealmManager.getManager().addSchoolRealm(deviceid, new IRealmListener() {
                    @Override
                    public void OnSuccess() {
                    }
                    @Override
                    public void onError(Throwable error) {
                        ToastUtil.Error("添加寝室失败!");
                    }
                });
            }
        }else {
            if(all.get(0).getDevice_id() != deviceid){
                //       CashManager.getCashApi().clearDeviceId();
                //删除学校相关的人脸 指纹数据库
                if(deleteFaceAndFvRealm(deviceid)){
                    DeviceRealmManager.getManager().updateSchoolToRealm(deviceid, new IRealmListener() {
                        @Override
                        public void OnSuccess() {
                        }

                        @Override
                        public void onError(Throwable error) {
                            ToastUtil.Error("更新寝室失败!");
                        }
                    });
                }
            }
        }

    }
}
