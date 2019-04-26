package com.gzgykj.sturollcall.mvp.ui.sign;

import com.gzgykj.basecommon.base.BasePresenter;
import com.gzgykj.basecommon.base.BaseView;

import java.util.List;

/**
 * Data on :2019/3/21 0021
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :签到统计契约类
 */
public interface SignContract {
    interface View extends BaseView {
        void sign(List<String> list);
    }
    interface Presenter extends BasePresenter<View>
    {
        void sign();
    }
}
