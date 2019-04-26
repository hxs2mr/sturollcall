package com.gzgykj.sturollcall.mvp.ui.rollcall;

import com.gzgykj.basecommon.base.BasePresenter;
import com.gzgykj.basecommon.base.BaseView;

/**
 * Data on :2019/3/20 0020
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :
 */
public interface RollCallContract {
    interface  View extends BaseView{
        void  timer(String time);
    }
    interface  Presenter extends BasePresenter<View>
    {
        void timer(long starttime);
    }
}
