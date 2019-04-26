package com.gzgykj.basecommon.model.facerealm;

import io.realm.RealmObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Data on :2019/4/12 0012
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :人脸识别数据库表
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class FaceRealm extends RealmObject {
    private long userId;
    private int featuresType;
    private byte[] features;
    private int userSex;
    //private int userType;

}
