package com.gzgykj.sturollcall.mvp.ui.main;

import com.gzgykj.basecommon.base.BasePresenter;
import com.gzgykj.basecommon.base.BaseView;
import com.gzgykj.sturollcall.mvp.model.MainTimeBean;

import java.util.List;
import java.util.Map;

/**
 * Data on :2019/3/20 0020
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :
 */
public interface MainContract {
    interface  View extends BaseView{
        void time(MainTimeBean bean);
    }

    interface Prensenter extends BasePresenter<View>
    {
         void time();
        void SendMQRealm(List<Long> stylist);
        void login(Map<String,String> data);
    }
}
