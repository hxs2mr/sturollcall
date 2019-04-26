package com.gzgykj.sturollcall.mvp.ui.notice;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gzgykj.basecommon.base.RxPresenter;
import com.gzgykj.sturollcall.R;
import com.gzgykj.sturollcall.adapter.NoticeAdapter;
import com.gzgykj.sturollcall.app.datamanager.DataManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Data on :2019/3/21 0021
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :
 */
public class NoticePresenter extends RxPresenter<NoticeContract.View> implements NoticeContract.Presenter {
    private DataManager mDataManager;
    public NoticeAdapter adapter;
    @Inject
    public NoticePresenter(DataManager dataManager)
    {
        this.mDataManager = dataManager;
    }

    @Override
    public void notice() {
        initadapter();
        /**
         * 测试的数据
         */
        List<String> texts = new ArrayList<>();
        for(int i = 0; i< 20 ; i ++)
        {
            texts.add("关于本周宿舍卫生大扫除问题"+i);
        }
        if(mView!=null)
        {
            mView.notice(texts);
        }
    }
    private void initadapter() {
        if (adapter == null)
        {
            adapter = new NoticeAdapter(R.layout.item_notice);
            adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN); //切换动画
            adapter.setEnableLoadMore(false);
        }

    }
}
