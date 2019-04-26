package com.gzgykj.sturollcall.mvp.ui.jxcall;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;

import com.gykj.jxfvlibrary.listener.OnJxFvListener;
import com.gykj.jxfvlibrary.listener.OnRecognizeListener;
import com.gykj.jxfvlibrary.manager.JXFvManager;
import com.gzgykj.sturollcall.R;
import com.gzgykj.sturollcall.base.BaseActivity;
import com.gzgykj.sturollcall.mvp.ui.face.FaceActivity;
import com.gzgykj.sturollcall.utils.SoundTipUtil;
import com.gzgykj.sturollcall.utils.ToastUtil;
import com.orhanobut.logger.Logger;

import butterknife.BindView;

/**
 * Data on :2019/3/21 0021
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :指静脉识别界面
 */
public class JXActivity extends BaseActivity<JXPresenter>implements JXContract.View, OnJxFvListener, OnRecognizeListener {
    @BindView(R.id.jx_iv)
    AppCompatImageView jx_iv;//手指动画
    private AnimationDrawable animaition;
    private Handler handler;
    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public Context getContext() {
        return this;
    }
    @Override
    public Object setLayout() {
        return R.layout.activity_jx;
    }
    @Override
    protected void initEventData() {
        setshowToolbarRight(false);
        setToolbarleftBack();
        setToolbarLeftTitle("指纹签到");
        jx_iv.setBackgroundResource(R.drawable.jx_anim);
         animaition = (AnimationDrawable)jx_iv.getBackground();
        animaition.setOneShot(false);   //设置是否只播放一次，和上面xml配置效果一致
        animaition.start();
        handler = new Myhander();
        initJXFV();

        //语音播放
        SoundTipUtil.soundTip(getContext(),"抬起手指放进去");
    }

    private void initJXFV() {
        JXFvManager.getInstance().iswhile = true;
        JXFvManager.getInstance().setOnJxFvListener(this);
        JXFvManager.getInstance().jxInitUSBDriver();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        animaition.stop();
        handler.removeCallbacksAndMessages(null);
        JXFvManager.getInstance().jxDeInitUSBDriver();
    }

    @Override
    public void success() {
        switch (JXFvManager.getInstance().getJxType())
        {
            case TOUCHED: //按下手指
                JXFvManager.getInstance().jxInitCapEnv();
                break;
            case COLLECTED://设备检测
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JXFvManager.getInstance().jxLoadVeinSample();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
            case LOAD://获取指纹信息
                try {
                    JXFvManager.getInstance().jxGrabVeinFeature();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case GRAB://开始比对数据库
                JXFvManager.getInstance().jxRecognizeVeinFeatureInGroup(this);
                break;
        }
    }

    @Override
    public void failed(String error) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                JXFvManager.getInstance().setIsFingerTouched(true);
                ToastUtil.Error("识别:"+error);
            }
        },1000);
    }
    /**
     * 比对成功成功
     * @param viens
     */
    @Override
    public void recognizeSuccess(String viens) {
        if(null == viens || TextUtils.isEmpty(viens)){
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.Warning("未找到用户信息,请更换手指");
                }
            });
            //2秒以后在重新检测
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    JXFvManager.getInstance().setIsFingerTouched(true);
                }
            },2000);
        }else {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.Error("识别结果:"+viens);
                }
            });
            //识别成功
    /*        Message msg = Message.obtain();
            msg.what = RECOGNIZE_SUCCESS;
            msg.obj = viens;
            mHandler.sendMessage(msg);*/
        }
    }

    /**
     * 比对失败
     * @param error
     */
    @Override
    public void recognizeFailed(String error) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                JXFvManager.getInstance().setIsFingerTouched(true);
            }
        }, 500);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (error) {
//                    case "样本格式不正确":
//                        ToastUtils.showToast(ToastType.WARNING,error);
//                        break;
                    case "数据库忙碌":
                        ToastUtil.Error(error);
                        break;
                    case "USB权限错误":
                        ToastUtil.Error( error);
                        break;
                    case "设备未授权":
                        ToastUtil.Error(error);
                        break;
                    case "未检测到指静脉设备":
                        ToastUtil.Error(error);
                        break;
                }
            }
        });
    }

    class  Myhander extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

}
