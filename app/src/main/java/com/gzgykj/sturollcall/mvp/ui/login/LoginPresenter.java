package com.gzgykj.sturollcall.mvp.ui.login;

import com.gzgykj.basecommon.base.RxPresenter;
import com.gzgykj.sturollcall.app.datamanager.DataManager;

import javax.inject.Inject;

/**
 * Data on :2019/3/20 0020
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :
 */
public class LoginPresenter extends RxPresenter<LoginContract.View>implements LoginContract.Presenter {
    private DataManager mDataManager;
    @Inject
    public LoginPresenter(DataManager dataManager)
    {
        this.mDataManager = dataManager;
    }
}
