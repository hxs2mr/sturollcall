package com.gzgykj.sturollcall.mvp.ui.login.pass;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

import com.gzgykj.basecommon.utils.ActivityUtil;
import com.gzgykj.sturollcall.R;
import com.gzgykj.sturollcall.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Data on :2019/3/26 0026
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :用户密码登录
 */
public class PasswordFragment extends BaseFragment<PasswordPresenter>implements PasswordContract.View {
    @BindView(R.id.btn_login)
    AppCompatButton btn_login;
    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }
    @Override
    public Object setLayout() {
        return R.layout.fragment_passlogin;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
    }

    @OnClick(R.id.btn_login)
    public  void  onclick(View view)
    {
        LoadingView.setLabel("登录中...").show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                LoadingView.dismiss();
                ActivityUtil.getInstance().closeActivity(getActivity());
            }
        },1000);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser)
        {
            showSoftInput(btn_login);
        }
    }
}
