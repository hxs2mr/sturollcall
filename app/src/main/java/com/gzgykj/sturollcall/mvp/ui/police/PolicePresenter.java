package com.gzgykj.sturollcall.mvp.ui.police;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gzgykj.basecommon.base.RxPresenter;
import com.gzgykj.sturollcall.R;
import com.gzgykj.sturollcall.adapter.NoticeAdapter;
import com.gzgykj.sturollcall.adapter.PoliceAdapter;
import com.gzgykj.sturollcall.app.datamanager.DataManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Data on :2019/3/28 0028
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :
 */
public class PolicePresenter extends RxPresenter<PoliceContract.View>implements PoliceContract.Presenter {
    private DataManager mDataManager;
    public PoliceAdapter adapter;

    @Inject
    public PolicePresenter(DataManager dataManager)
    {
        this.mDataManager = dataManager;
    }

    /**
     * 用于测试的接口方法
     */
    @Override
    public void police() {
        List<String> list =new ArrayList<>();
        list.add("你使用了一键报警,向宿管请求帮助!");
        list.add("你使用了一键报警,向宿管请求帮助!");
        list.add("你使用了一键报警,向宿管请求帮助!");
        list.add("你使用了一键报警,向宿管请求帮助!");
        list.add("你使用了一键报警,向宿管请求帮助!");
        list.add("你使用了一键报警,向宿管请求帮助!");
        if(mView!=null)
        {
            mView.police(list);
        }
    }

    public void initadapter() {
        if (adapter == null)
        {
            adapter = new PoliceAdapter(R.layout.item_police);
            adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN); //切换动画
            adapter.setEnableLoadMore(false);
        }

    }
}
