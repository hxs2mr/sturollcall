package com.gzgykj.sturollcall.mvp.ui.video;

import com.gzgykj.basecommon.base.BasePresenter;
import com.gzgykj.basecommon.base.BaseView;
import com.gzgykj.sturollcall.mvp.model.VideoQinBean;

import java.util.List;

/**
 * Data on :2019/3/25 0025
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :语音通话契约类
 */
public interface VideoContract {
    interface   View extends BaseView{
        void qin(List<VideoQinBean> beans);
    }
    interface Presenter extends BasePresenter<View>
    {
        void qin();
    }
}
