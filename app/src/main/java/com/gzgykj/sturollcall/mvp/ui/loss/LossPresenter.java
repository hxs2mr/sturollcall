package com.gzgykj.sturollcall.mvp.ui.loss;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gzgykj.basecommon.base.RxPresenter;
import com.gzgykj.basecommon.model.AddLossBean;
import com.gzgykj.basecommon.response.MQRespose;
import com.gzgykj.basecommon.utils.Constants;
import com.gzgykj.basecommon.utils.GsonUtil;
import com.gzgykj.sturollcall.R;
import com.gzgykj.sturollcall.adapter.LossAdapter;
import com.gzgykj.sturollcall.adapter.NoticeAdapter;
import com.gzgykj.sturollcall.app.datamanager.DataManager;
import com.gzgykj.sturollcall.mq.RabbiMqEngine;
import com.gzgykj.sturollcall.utils.SharedPreferencedUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Data on :2019/3/21 0021
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :
 */
public class LossPresenter extends RxPresenter<LossContract.View>implements LossContract.Presenter {
    private DataManager mDataManager;
    public LossAdapter adapter;
    @Inject
    public LossPresenter(DataManager dataManager)
    {
        mDataManager = dataManager;
    }

    @Override
    public void sendDetaiil() {
        MQRespose<Integer> entity = new MQRespose<Integer>();
        int  dormitoryId = SharedPreferencedUtils.getInt(Constants.DORMITORYID,1);
        entity.setDevieceType(2);
        entity.setDeviceId("654321");
        entity.setMethod("SELECT");
        entity.setEvent("DORMITORY_BEDS");
        entity.setData(dormitoryId);
        RabbiMqEngine.getRabbiMqEngine().sendEndMessage(true, JSON.toJSONString(entity));
    }

    @Override
    public void loss() {
        List<String> tests = new ArrayList<>();
        tests.add("小行星");
        tests.add("刘德华");
        tests.add("周星驰");
        tests.add("王祖贤");
        tests.add("化学系");

        if(mView!=null)
        {
            mView.loss(tests);
        }

    }
    public   void  initAdapter()
    {
        if (adapter == null)
        {
            adapter = new LossAdapter(R.layout.item_loss);
            adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN); //切换动画
            adapter.setEnableLoadMore(false);
        }

    }

}
