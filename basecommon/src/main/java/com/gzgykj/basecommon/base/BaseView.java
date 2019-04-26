package com.gzgykj.basecommon.base;

import android.content.Context;

/**
 * name : HXS
 * e-mail : 1363826037@qq.com
 * descript:基View基本接口
 * date   : 2019/3/1516:48
 * version: 1.0
 */
public  interface  BaseView {

    /**
     * 显示错误信息
     * @param msg
     */
    void  showErrorMsg(String msg);


    Context getContext();
    /**
     * 是否显示亮色主题
     * @param isLight
     */
    //void  useLightMode(boolean isLight);

    //加载数据

    /**
     * 状态----错误
     */
    void stateError();

    void stateError(Throwable e);

    void stateError(Exception e);

    /**
     * 状态----成功
     */
 //   void  stateSuccess();

}
