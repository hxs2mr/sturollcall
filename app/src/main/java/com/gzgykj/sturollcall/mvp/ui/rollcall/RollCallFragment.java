package com.gzgykj.sturollcall.mvp.ui.rollcall;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;

import com.gzgykj.basecommon.utils.ActivityUtil;
import com.gzgykj.basecommon.utils.Constants;
import com.gzgykj.sturollcall.R;
import com.gzgykj.sturollcall.base.BaseFragment;
import com.gzgykj.sturollcall.mq.RabbiMqEngine;
import com.gzgykj.sturollcall.mvp.model.CallStopBean;
import com.gzgykj.sturollcall.mvp.model.EvAddWarrBean;
import com.gzgykj.sturollcall.mvp.model.EventBean;
import com.gzgykj.sturollcall.mvp.model.InCallBean;
import com.gzgykj.sturollcall.mvp.ui.face.FaceActivity;
import com.gzgykj.sturollcall.mvp.ui.jxcall.JXActivity;
import com.gzgykj.sturollcall.mvp.ui.rollcall.timer.TimerMessage;
import com.gzgykj.sturollcall.utils.DateUtil;
import com.gzgykj.sturollcall.utils.SoundTipUtil;
import com.gzgykj.sturollcall.utils.TimeDataUtil;
import com.gzgykj.sturollcall.utils.ToastUtil;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import me.yokeyword.fragmentation.ExtraTransaction;

/**
 * Data on :2019/3/20 0020
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :  需要初始化指静脉设备
 */
public class RollCallFragment extends BaseFragment<RollCallPresenter> implements RollCallContract.View {


    @BindView(R.id.tv_timer)
    AppCompatTextView tv_timer;

    @BindView(R.id.tv_datatime)
    AppCompatTextView tv_datatime;

    private long starttime=22000;

    private int rollCallId= -1;
    public static RollCallFragment newInstance() {
        return new RollCallFragment();
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }
    @Override
    public Object setLayout() {
        return R.layout.fragment_rollcall;
    }

    /**
     * 数据懒加载
     * @param savedInstanceState
     */
    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        EventBus.getDefault().register(this);

        //语音播放
        // SoundTipUtil.soundTip(getContext(),"点名开始请准备");
    }

    @Override
    public void onNewBundle(Bundle args) {
        super.onNewBundle(args);
    }
    @OnClick({R.id.jx_call_btn,R.id.face_call_btn,R.id.iv_police})
    public void onclick(View view)
    {
        switch (view.getId())
        {
            case R.id.jx_call_btn://指纹识别
              //  ToastUtil.Normal("点击");
                ActivityUtil.getInstance().openActivity(getActivity(), JXActivity.class);
                break;
            case R.id.face_call_btn://人脸识别
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.CALLID,rollCallId);

                ActivityUtil.getInstance().openActivity(getActivity(), FaceActivity.class,bundle);
                break;
            case R.id.iv_police://一键报警
                //MQ
                RabbiMqEngine.getRabbiMqEngine().sendMessage(true,Constants.ADDWarring, Constants.PAGE_SIEZ,0,0,0); //发送消息初始化
                break;
        }
    }
    @Subscribe
    public void MessageAddWaring(EvAddWarrBean bean)
    {
        System.out.println("报警成功 请等");
        ToastUtil.Info("报警成功 请等待！");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
       // TimeDataUtil.getInstance().Cancle();
    }

    // 第二种时间计时
    @Override
    public void timer(String time) {
         tv_timer.setText(time);
        if(time.equals("end"))
        {
            //语音播放
            SoundTipUtil.soundTip(getContext(),"点名结束");
            ToastUtil.Success("点名结束");
            tv_timer.setText( "点名结束");
            mPresenter.mCountDownTimer.cancel();
        }
    }

    //开始点名
    @Subscribe
    public void incalltime(InCallBean inCallBean)
    {
        SoundTipUtil.soundTip(getContext(),"点名开始请准备");
        long getendtime = DateUtil.getTimeStamp("yyyy年MM月dd HH时mm分",inCallBean.getRollCallRule().getSingleEndDate()+" "+inCallBean.getRollCallRule().getEndTime());
        long starttime = DateUtil.getTimeStamp("yyyy年MM月dd HH时mm分",inCallBean.getRollCallRule().getSingleStartDate()+" "+inCallBean.getRollCallRule().getStartTime());
        long newtime =System.currentTimeMillis();
        long length = getendtime  - newtime;

        Logger.e("HXS此次点名的区间:"+length);
        //开始计时测试
        mPresenter.timer(getendtime - newtime);
        tv_datatime.setText(inCallBean.getRollCallRule().getSingleStartDate()+" "+inCallBean.getRollCallRule().getStartTime()+"~"+inCallBean.getRollCallRule().getSingleEndDate()+" "+inCallBean.getRollCallRule().getEndTime());
        Constants.ISCALL =  true;
        rollCallId = inCallBean.getRollCallId();
    }

    //结束点名
    @Subscribe
    public void stopcall(CallStopBean bean)
    {
        //语音播放
        SoundTipUtil.soundTip(getContext(),"点名结束");
        ToastUtil.Success("点名结束");
        tv_timer.setText( "点名结束");
        if (mPresenter.mCountDownTimer!=null)
        {
            mPresenter.mCountDownTimer.cancel();
        }
        if(bean.getOver()==rollCallId)
        {
            Constants.ISCALL =  false;//在点名界面的时候·  判断是否结束了此次点名
        }
        rollCallId  = -1;
    }


    /**
     * 第一种方法计时  不用
     */
/*
    private void testTImer() {
        TimeDataUtil.getInstance().StartTimer( getActivity(),new TimerMessage() {
            @Override
            public void messgae(String str) {
                 starttime =  starttime-1000;
                 Logger.e("HXS 减一"+starttime);
                 if(starttime>=0){
                     if(tv_timer!=null)
                     {
                         tv_timer.setText( TimeDataUtil.getInstance().getStringTime(starttime));
                     }
                 }else {
                     ToastUtil.Success("点名结束");
                     TimeDataUtil.getInstance().Cancle();
                 }
            }
            @Override
            public void end(String str) {

            }
        });
    }
*/

}
