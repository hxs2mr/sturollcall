package com.gzgykj.sturollcall.mvp.ui.notice;

import com.gzgykj.basecommon.base.BasePresenter;
import com.gzgykj.basecommon.base.BaseView;

import java.util.List;

/**
 * Data on :2019/3/21 0021
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :通知通告  契约类
 */
public interface NoticeContract {
    interface  View extends BaseView{
        /**测试的数据**/
        void notice(List<String> test);
    }
    interface Presenter extends BasePresenter<View>
    {
        /**测试的接口名称**/
        void notice();
    }
}
