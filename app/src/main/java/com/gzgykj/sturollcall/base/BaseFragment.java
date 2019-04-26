package com.gzgykj.sturollcall.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.gzgykj.basecommon.base.BasePresenter;
import com.gzgykj.basecommon.base.BaseView;
import com.gzgykj.basecommon.exception.ApiException;
import com.gzgykj.basecommon.utils.ActivityUtil;
import com.gzgykj.sturollcall.app.App;
import com.gzgykj.sturollcall.dagg.component.DaggerFragmentComponent;
import com.gzgykj.sturollcall.dagg.component.FragmentComponent;
import com.gzgykj.sturollcall.dagg.mode.FragmentModule;
import com.gzgykj.sturollcall.utils.ToastUtil;
import com.kaopiz.kprogresshud.KProgressHUD;

import javax.inject.Inject;

/**
 * Data on :2019/3/20 0020
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :
 */
public abstract class BaseFragment<T extends BasePresenter> extends BaseMainFragment implements BaseView {
    @Inject
    protected  T mPresenter;
    /** 公共的loadview**/
    protected KProgressHUD LoadingView;

    protected FragmentComponent getFragmentComponent()
    {
        return DaggerFragmentComponent.builder()
                .appComponent(App.getAppComponent())
                .fragmentModule(getFragmentModule())
                .build();
    }
    protected FragmentModule getFragmentModule(){
        return new FragmentModule(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initInject();
        if(mPresenter!=null)
        {
            mPresenter.attachView(this);
        }
        super.onViewCreated(view, savedInstanceState);
        LoadingView  =KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);

    }
    @Override
    public void onDestroy() {
        if (mPresenter!=null){
            mPresenter.detachView();
        }
        super.onDestroy();
    }

    @Override
    public void showErrorMsg(String msg) {
        ToastUtil.Error(msg);
    }

    @Override
    public void stateError(Throwable e) {
        if(LoadingView.isShowing())
        {
            LoadingView.dismiss();
        }
        if(e instanceof ApiException)
        {
            ApiException apiException = (ApiException) e;
            if (apiException.getCode() == -1) {

            }else if (apiException.getCode() == -2){

            } else {
                ToastUtil.Error(apiException.getMessage());
            }
        }else {
            if(e.getMessage()!=null)
            {
                if(e.getMessage().contains("End of input at"))
                {
                    ToastUtil.Error("接口跑丢了!");
                }else if(e.getMessage().contains("HTTP 504")){
                    ToastUtil.Error("网络错误!");
                }else  if(e.getMessage().contains("failed to connect to")){
                    ToastUtil.Error("连接错误!");
                }else  if(e.getMessage().contains("timeout")){
                    ToastUtil.Error("连接超时!");
                }else  if(e.getMessage().contains("HTTP 502")){
                    ToastUtil.Error("服务异常!");
                }else  if(e.getMessage().contains("HTTP 404")){
                    ToastUtil.Error("接口跑丢了!");
                }else {
                    ToastUtil.Error(e.getMessage());
                }
            }
        }
    }
    @Override
    public void stateError(Exception e) {
        if(e instanceof  Throwable)
        {
            stateError((Throwable) e);
        }else {
            ToastUtil.Error(e.getMessage());
        }
    }

    @Override
    public void stateError() {
        if (LoadingView.isShowing()) {
            LoadingView.dismiss();
        }
    }

    protected abstract void initInject();
}
