package com.gzgykj.sturollcall.mvp.ui.police;

import com.gzgykj.basecommon.base.BasePresenter;
import com.gzgykj.basecommon.base.BaseView;

import java.util.List;

/**
 * Data on :2019/3/28 0028
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :报警记录契约类
 */
public interface PoliceContract {
    interface View extends BaseView{
        void police(List<String> list);
    }
    interface Presenter extends BasePresenter<View>
    {

        void police();
    }
}
