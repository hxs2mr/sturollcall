package com.gzgykj.basecommon.base;

/**
 * name : HXS
 * e-mail : 1363826037@qq.com
 * descript:基Presenter 接口
 * date   : 2019/3/1516:56
 * version: 1.0
 */
public interface BasePresenter<T extends BaseView> {

    /**
     * 设置view
     * @param view
     */
    void  attachView(T view);

    /**
     * 移除View
     */
    void  detachView();
}
