package com.gzgykj.sturollcall.mvp.ui.setting.face;

import android.annotation.SuppressLint;

import com.alibaba.fastjson.JSON;
import com.gzgykj.basecommon.base.RxPresenter;
import com.gzgykj.basecommon.commom.RxUtil;
import com.gzgykj.basecommon.utils.GsonUtil;
import com.gzgykj.sturollcall.app.datamanager.DataManager;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.observers.ResourceObserver;

/**
 * Data on :2019/4/24 0024
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :
 */
public class FaceAddPresenter extends RxPresenter<FaceAddContract.View>implements FaceAddContract.Presenter {
    private DataManager mDataManager;
    @Inject
    public FaceAddPresenter(DataManager dataManager)
    {
        this.mDataManager  =dataManager;
    }

    //上传人脸信息
    @SuppressLint("CheckResult")
    @Override
    public void addface(String userid ,int userSex,String schoolId,Map<Integer,byte[]> featuresMap) {
        Map<String, Object> map = new HashMap<>();
        map.put("userid","123456");
        map.put("userSex","0");
        map.put("schoolId","1");
        map.put("featuresMap",featuresMap);
        map.put("termitarType","0");
        Logger.d("HXS请求的参数:"+JSON.toJSONString(map));

        mDataManager.addface(map)
                .compose(RxUtil.rxSchedulerObservableHelper())
                .compose(RxUtil.handlerLYLResult())
                .subscribeWith(new ResourceObserver<String>() {
                    @Override
                    public void onNext(String s) {
                    if(mView!=null)
                        {
                            mView.addface(s);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.i("错误信息:" + e.getMessage());
                     if(mView!=null)
                        {
                         mView.stateError(e);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
