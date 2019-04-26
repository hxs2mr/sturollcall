package com.gzgykj.sturollcall.utils;

import android.app.Activity;
import android.util.Log;

import com.gzgykj.sturollcall.mvp.ui.rollcall.timer.TimerMessage;
import com.orhanobut.logger.Logger;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Data on :2019/4/16 0016
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :
 */
public class TimeDataUtil {

    private Timer timer = new Timer();
    //单利
    private static TimeDataUtil instance= null;

    //单锁
    public static synchronized TimeDataUtil getInstance()
    {
       if(instance == null) {
           instance = new TimeDataUtil();
       }
        return  instance;
    }

    public void StartTimer( Activity activity,TimerMessage timerMessage ){
        if (timer == null) {
            timer = new Timer();
        }
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Logger.e("HXS timer定时器");
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        timerMessage.messgae("减一");
                    }
                });
            }
        };
        timer.schedule(timerTask,0,1000);
    }

    public void Cancle() {
        if (timer != null)
        {
            timer.cancel();
        }
        timer = null;
    }
    public String getStringTime(long time) {
           time=time/1000;//总秒数
        int s= (int) (time%60);//秒
        int m= (int) (time/60);//分
        int h=(int) (time/3600);//秒
        return String.format(Locale.CHINA,"%02d时%02d分%02d秒",h,m,s);
    }
}
