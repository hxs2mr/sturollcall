package com.gzgykj.basecommon.model.facerealm;

import java.io.Serializable;

import lombok.Data;

/**
 * desc   :
 * author : josh.lu
 * e-mail : 1113799552@qq.com
 * date   : 2018/9/413:16
 * version: 1.0
 */
@Data
public class FeatureBean<T> implements Serializable {

    private static final long serialVersionUID = -7438988832177271883L;
    /** 
     * deviceId : 5
     * devieceType : 0
     * method : UPDATE
     * event : FACE_FEATURES
     * data : {"id":null,"schoolId":62,"userId":1222,"userType":1,"featuresType":3,"features":"6xcgcCItQk42Ig==","createTime":1536036940835}
     * descript : null
     */
    private String deviceId;
    private int devieceType;
    private String method;
    private String event;
    private T data;
    private Object descript;



}
