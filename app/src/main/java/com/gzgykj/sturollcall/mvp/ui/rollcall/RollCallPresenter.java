package com.gzgykj.sturollcall.mvp.ui.rollcall;

import android.os.CountDownTimer;

import com.gzgykj.basecommon.base.RxPresenter;
import com.gzgykj.sturollcall.app.datamanager.DataManager;

import java.util.Locale;

import javax.inject.Inject;

/**
 * Data on :2019/3/20 0020
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :
 */
public class RollCallPresenter extends RxPresenter<RollCallContract.View>implements RollCallContract.Presenter {
    private DataManager mDataManager;
    public CountDownTimer mCountDownTimer;
    @Inject
    public RollCallPresenter(DataManager dataManager)
    {
        this.mDataManager = dataManager;
    }
    /**
     * 时间计数
     */
    @Override
    public void timer(long starttime) {
         mCountDownTimer = new CountDownTimer(starttime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                   String str = "剩余" + (millisUntilFinished / 1000) + "秒";

                long   time=millisUntilFinished/1000;//总秒数
                int s= (int) (time%60);//秒
                int m= (int) (time/60);//分
                int h=(int) (time/3600);//时

                if(mView!=null)
                {
                    mView.timer(  String.format(Locale.CHINA,"%02d时%02d分%02d秒",h,m,s));
                }
            }

            @Override
            public void onFinish() {
                if(mView!=null)
                {
                    mView.timer("end");
                }
            }
        };
        mCountDownTimer.start();
    }
}
