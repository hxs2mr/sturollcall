package com.gzgykj.sturollcall.mvp.ui.setting;

import com.gzgykj.basecommon.base.RxPresenter;
import com.gzgykj.sturollcall.app.datamanager.DataManager;

import javax.inject.Inject;

/**
 * Data on :2019/3/21 0021
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :
 */
public class SettingPresenter extends RxPresenter<SettingContract.View>implements SettingContract.Presenter {
    private DataManager mDataManager;
    @Inject
    public SettingPresenter(DataManager dataManager)
    {
        mDataManager = dataManager;
    }
}
