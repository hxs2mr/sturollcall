package com.gzgykj.basecommon.model.facerealm;

/**
 * Data on :2019/4/12 0012
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on : 人脸数据库添加回调
 */

public interface IAddFaceListener {

    void OnSuccess();

    void onError(Throwable error);
}
