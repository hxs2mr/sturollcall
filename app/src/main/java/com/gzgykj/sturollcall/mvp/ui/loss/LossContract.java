package com.gzgykj.sturollcall.mvp.ui.loss;

import com.gzgykj.basecommon.base.BasePresenter;
import com.gzgykj.basecommon.base.BaseView;

import java.util.List;

/**
 * Data on :2019/3/21 0021
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :报损契约类
 */
public interface LossContract {
    interface View extends BaseView{
        void loss(List<String> list);
    }
    interface Presenter extends BasePresenter<View>
    {
        void sendDetaiil();
        void loss();
    }
}
