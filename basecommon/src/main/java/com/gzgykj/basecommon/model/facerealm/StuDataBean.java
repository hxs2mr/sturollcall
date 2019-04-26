package com.gzgykj.basecommon.model.facerealm;

import java.io.Serializable;

import lombok.Data;

/**
 * Data on :2019/4/15 0015
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :
 */
@Data
public class StuDataBean implements Serializable {
    /**
     * bedsName : 1号床铺
     * classId : 1
     * dormitoryId : 1
     * gmtCreate : 1555056362000
     * id : 1
     * status : 1
     * userId : 1
     * userName : 学生一
     */

    private String bedsName;
    private int classId;
    private int dormitoryId;
    private long gmtCreate;
    private int id;
    private int status;
    private Long userId;
    private String userName;

}
