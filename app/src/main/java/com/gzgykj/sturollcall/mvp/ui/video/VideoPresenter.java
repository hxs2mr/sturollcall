package com.gzgykj.sturollcall.mvp.ui.video;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gzgykj.basecommon.base.RxPresenter;
import com.gzgykj.sturollcall.R;
import com.gzgykj.sturollcall.adapter.LossAdapter;
import com.gzgykj.sturollcall.adapter.VideoQinAdapter;
import com.gzgykj.sturollcall.app.datamanager.DataManager;
import com.gzgykj.sturollcall.mvp.model.VideoQinBean;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Data on :2019/3/25 0025
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :语音通话的presenter
 */
public class VideoPresenter  extends RxPresenter<VideoContract.View>implements VideoContract.Presenter {
    private DataManager mDataManager;
    public VideoQinAdapter adapter;
    @Inject
    public  VideoPresenter(DataManager dataManager){
        this.mDataManager = dataManager;
    }

    public void initAdapter()
    {
        if (adapter == null)
        {
            adapter = new VideoQinAdapter(R.layout.item_video_qin);
            adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN); //切换动画
            adapter.setEnableLoadMore(false);
        }

    }

    @Override
    public void qin() {
        /**
         * 测试数据
         */
        List<VideoQinBean> list = new ArrayList<>();
        VideoQinBean bean1 = new VideoQinBean("张三丰 （爸爸）" ,false);
        VideoQinBean bean2 = new VideoQinBean("景甜   （妈妈）" ,false);
        VideoQinBean bean3 = new VideoQinBean("古巨基 （爷爷）" ,false);
        VideoQinBean bean4 = new VideoQinBean("刘亦菲 （奶奶）" ,false);
        list.add(bean1);
        list.add(bean2);
        list.add(bean3);
        list.add(bean4);
        if(mView != null)
        {
            mView.qin(list);
        }
    }
}
