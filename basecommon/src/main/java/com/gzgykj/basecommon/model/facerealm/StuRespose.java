package com.gzgykj.basecommon.model.facerealm;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * Data on :2019/4/15 0015
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :
 */
@Data
public class StuRespose implements Serializable {

    /**
     * code : 0
     * data : [{"bedsName":"1号床铺","classId":1,"dormitoryId":1,"gmtCreate":1555056362000,"gmtModified":1555263735000,"id":1,"status":1,"userId":6645,"userName":"学生一"},{"bedsName":"2号床铺","classId":1,"dormitoryId":1,"gmtCreate":1555056384000,"gmtModified":1555263744000,"id":2,"status":1,"userId":5418,"userName":"学生二"}]
     * msg : success
     */
    private int code;
    private String msg;
    private List<StuDataBean> data;
}
