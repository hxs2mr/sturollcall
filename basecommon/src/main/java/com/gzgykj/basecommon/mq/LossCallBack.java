package com.gzgykj.basecommon.mq;

import com.gzgykj.basecommon.model.LossPageBean;

/**
 * Data on :2019/4/9 0009
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :
 */
public interface LossCallBack {
    //分页回调
    void success(LossPageBean bean);
    //添加报损回调
    void addloss(String string);
}
