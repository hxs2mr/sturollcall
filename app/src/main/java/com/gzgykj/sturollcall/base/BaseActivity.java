package com.gzgykj.sturollcall.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import com.gzgykj.basecommon.base.BasePresenter;
import com.gzgykj.basecommon.base.BaseView;
import com.gzgykj.basecommon.exception.ApiException;
import com.gzgykj.sturollcall.app.App;
import com.gzgykj.sturollcall.dagg.component.ActivityComponent;
import com.gzgykj.sturollcall.dagg.component.DaggerActivityComponent;
import com.gzgykj.sturollcall.dagg.mode.ActivityModel;
import com.gzgykj.sturollcall.utils.ToastUtil;
import com.kaopiz.kprogresshud.KProgressHUD;

import javax.inject.Inject;
/**
 * Data on :2019/3/19 0019
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :MVP 基累  加入了dagger2 依赖注解管理
 */
public  abstract  class BaseActivity<T extends BasePresenter> extends BaseMainActivity implements BaseView {
    @Inject
    protected  T mPresenter;

    /*设置基础的LoadView*/
    protected KProgressHUD LoadingView;
    /**
     * 注入依赖
     */
    protected abstract void initInject();

    protected ActivityComponent getActivityComponent()
    {
        return DaggerActivityComponent.builder()
                .appComponent(App.getAppComponent())
                .activityModel(getActivityModule())
                .build();
    }
    private ActivityModel getActivityModule() {
        return new ActivityModel(this);
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    protected void onViewCreatre() {
        super.onViewCreatre();
        initInject();
        if(mPresenter!=null)
        {
            mPresenter.attachView(this);
        }

        LoadingView = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
    }
    @Override
    protected void onDestroy() {
        if(mPresenter!=null)
        {
            mPresenter.detachView();
        }
        super.onDestroy();
    }

    /**
     * 显示错误信息
     * @param msg
     */
    @Override
    public void showErrorMsg(String msg) {
        ToastUtil.Error(msg);
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
    public void stateError(Throwable e) {
        if(LoadingView.isShowing())
        {
            LoadingView.dismiss();
        }

        if(e instanceof ApiException)
        {
            ApiException apiException = (ApiException) e;
            if (apiException.getCode() == -1) {
                ToastUtil.Error("请先登录!");
            }else if (apiException.getCode() == -2){
                ToastUtil.Error("请先登录！");
            } else {
                ToastUtil.Error(apiException.getMessage());
            }
        }
    }

    @Override
    public void stateError() {
        if(LoadingView.isShowing())
        {
            LoadingView.dismiss();
        }
    }
}
