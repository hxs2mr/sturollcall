package com.gzgykj.sturollcall.mvp.ui.sign;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gzgykj.basecommon.base.RxPresenter;
import com.gzgykj.sturollcall.R;
import com.gzgykj.sturollcall.adapter.LossAdapter;
import com.gzgykj.sturollcall.adapter.SignAdapter;
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
public class SignPresenter extends RxPresenter<SignContract.View> implements SignContract.Presenter{
    private DataManager mDataManager;
    public SignAdapter adapter;
    @Inject
    public  SignPresenter(DataManager dataManager)
    {
        this.mDataManager = dataManager;
    }

    @Override
    public void sign() {
        List<String> tests = new ArrayList<>();
        tests.add("2019-03-21 17:00 ~ 2019-03-21 18:00");
        tests.add("2019-03-22 17:00 ~ 2019-03-21 18:00");
        tests.add("2019-03-23 17:00 ~ 2019-03-21 18:00");
        tests.add("2019-03-24 17:00 ~ 2019-03-21 18:00");
        if(mView!=null)
        {
            mView.sign(tests);
        }
    }
    public   void  initAdapter()
    {
        if (adapter == null)
        {
            adapter = new SignAdapter(R.layout.item_sign);
            adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN); //切换动画
            adapter.setEnableLoadMore(false);
        }

    }
}
