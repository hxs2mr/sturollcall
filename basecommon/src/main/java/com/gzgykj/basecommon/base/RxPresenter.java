package com.gzgykj.basecommon.base;

import android.content.Context;
import android.content.res.Resources;

import com.gzgykj.basecommon.commom.MyRxBus;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import okhttp3.Response;

/**
 * name : HXS
 * e-mail : 1363826037@qq.com
 * descript:控制订阅的生命周期
 * date   : 2019/3/1517:01
 * version: 1.0
 */
public class RxPresenter<T extends BaseView>implements BasePresenter<T> {
    protected  T mView;
    protected Context mContext;
    protected Resources mRes;
    protected CompositeDisposable mCompositeDisposable;

    /**
     * 托管管理  添加托管
     * @param disposable
     */

    protected  void addSubscribe(Disposable disposable)
    {
        if (mCompositeDisposable==null)
        {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }


    /**
     * 添加事件订阅
     * @param <U>
     */
    protected  <U> void addRxBusSubscribe(Class<U> eventType, Consumer<U> act)
    {
        if(mCompositeDisposable == null)
        {
            mCompositeDisposable = new CompositeDisposable();
        }

        mCompositeDisposable.add(MyRxBus.getInstance().toDefaultFlowable(eventType,act));

    }


    /**
     * 清空订阅者
     */
    protected  void  unSubscribe(){
        if(mCompositeDisposable != null)
        {
            mCompositeDisposable.clear();
        }
    }
    /**
     *
     * @param view
     */

    @Override
    public void attachView(T view) {
        this.mView = view;
        this.mContext = view.getContext();
        this.mRes = mContext.getResources();
    }

    @Override
    public void detachView() {
        if(mView!=null)
        {
            mView = null;
        }
        unSubscribe();

    }
}
