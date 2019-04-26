package com.gzgykj.sturollcall.mvp.ui.setting.face;

import com.gzgykj.basecommon.base.BasePresenter;
import com.gzgykj.basecommon.base.BaseView;

import java.util.Map;

/**
 * Data on :2019/4/24 0024
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :
 */
public interface FaceAddContract {
    interface View extends BaseView{
            void addface(String date);
    }
    interface Presenter extends BasePresenter<View>
    {
            void addface(String userid ,int userSex,String schoolId,Map<Integer,byte[]> featuresMap);
    }
}
