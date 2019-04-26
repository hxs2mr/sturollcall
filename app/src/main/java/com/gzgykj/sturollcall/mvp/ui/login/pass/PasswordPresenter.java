package com.gzgykj.sturollcall.mvp.ui.login.pass;

import com.gzgykj.basecommon.base.RxPresenter;
import com.gzgykj.sturollcall.app.datamanager.DataManager;

import javax.inject.Inject;

/**
 * Data on :2019/3/26 0026
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :
 */
public class PasswordPresenter extends RxPresenter<PasswordContract.View>implements PasswordContract.Presenter {
    private DataManager mDataManager;
    @Inject
    public PasswordPresenter(DataManager dataManager)
    {
        mDataManager = dataManager;
    }
}
