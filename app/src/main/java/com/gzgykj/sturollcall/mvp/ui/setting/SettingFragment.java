package com.gzgykj.sturollcall.mvp.ui.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.gzgykj.sturollcall.R;
import com.gzgykj.sturollcall.base.BaseFragment;
import com.gzgykj.sturollcall.mvp.ui.setting.face.FaceFragment;
import com.gzgykj.sturollcall.widget.PasswordDialog;

import butterknife.OnClick;

/**
 * Data on :2019/3/21 0021
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :设置
 */
public class SettingFragment extends BaseFragment<SettingPresenter>implements SettingContract.View {

    private PasswordDialog passwordDialog;
    public static   SettingFragment newInstance()
    {
        return new SettingFragment();
    }
    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_setting;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        passwordDialog = new PasswordDialog(getContext());
    }

    @OnClick({R.id.face_rellayout,R.id.password_rellayout})
    public void onclick(View view){
        switch (view.getId())
        {
            case R.id.face_rellayout:

                start(new FaceFragment());  //添加面部特征值

              //ActivityUtil.getInstance().openActivity(getActivity(), FaceSettingActivity.class);  //如果已经添加了之后  是更新人脸数据
                 break;
            case R.id.password_rellayout://修改密码
                passwordDialog.show();
                break;
        }

    }
}
