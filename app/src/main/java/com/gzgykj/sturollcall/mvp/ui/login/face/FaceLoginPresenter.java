package com.gzgykj.sturollcall.mvp.ui.login.face;

import com.gzgykj.basecommon.base.RxPresenter;
import com.gzgykj.sturollcall.app.datamanager.DataManager;

import javax.inject.Inject;

/**
 * Data on :2019/3/26 0026
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :人脸登录presenter
 */
public class FaceLoginPresenter extends RxPresenter<FaceLoginContract.View>implements FaceLoginContract.Presenter {
    private DataManager mDatamanager;
    @Inject
    public FaceLoginPresenter(DataManager dataManager)
    {
        this.mDatamanager = dataManager;
    }

}
