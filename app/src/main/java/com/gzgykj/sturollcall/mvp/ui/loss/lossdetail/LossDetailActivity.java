package com.gzgykj.sturollcall.mvp.ui.loss.lossdetail;

import android.content.Context;

import com.gzgykj.basecommon.model.AddLossBean;
import com.gzgykj.basecommon.model.LossPageBean;
import com.gzgykj.basecommon.mq.LossCallBack;
import com.gzgykj.basecommon.response.MQRespose;
import com.gzgykj.sturollcall.R;
import com.gzgykj.sturollcall.base.BaseActivity;
import com.gzgykj.sturollcall.mq.RabbiMqEngine;
import com.gzgykj.sturollcall.mvp.ui.loss.LossContract;
import com.gzgykj.sturollcall.mvp.ui.loss.LossPresenter;
import com.gzgykj.sturollcall.utils.ToastUtil;

import java.util.List;

/**
 * Data on :2019/3/25 0025
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :通知详情
 */
public class LossDetailActivity extends BaseActivity<LossPresenter>implements LossContract.View , LossCallBack {
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
        return R.layout.activity_lossdetail;
    }
    @Override
    protected void initEventData() {
      //  setshowToolbarRight(false);
        setToolbarleftBack();
        setToolbarLeftTitle("查看详情");
        /**
         * 发送消息获取详情
         */
        mPresenter.sendDetaiil();
    }

    @Override
    public void loss(List<String> list) {

    }

    @Override
    public void success(LossPageBean bean) {

    }

    @Override
    public void addloss(String string) {

    }

    /**
     * 获取到的消息
     * @param action
     */
}
