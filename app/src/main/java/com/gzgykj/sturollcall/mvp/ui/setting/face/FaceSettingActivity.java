package com.gzgykj.sturollcall.mvp.ui.setting.face;

import android.content.Context;
import android.view.View;

import com.gzgykj.sturollcall.R;
import com.gzgykj.sturollcall.base.BaseActivity;
import com.gzgykj.sturollcall.mvp.ui.setting.SettingContract;
import com.gzgykj.sturollcall.mvp.ui.setting.SettingPresenter;

import butterknife.OnClick;

/**
 * Data on :2019/3/25 0025
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :人脸识别的
 */
public class FaceSettingActivity extends BaseActivity<SettingPresenter>implements SettingContract.View {
    @Override
    protected void initInject() {
            getActivityComponent().inject(this);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public Object setLayout() {
        return R.layout.activity_facesetting;
    }

    @Override
    protected void initEventData() {

    }

    @OnClick({R.id.rel_update_face,R.id.rel_clear_face})
    public void  onclick(View view)
    {
        switch (view.getId())
        {
            case R.id.rel_update_face://更新人脸数据

                break;
            case R.id.rel_clear_face://清除人脸特征

                break;
        }
    }

}
