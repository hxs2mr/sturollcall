package com.gzgykj.basecommon.model;

import lombok.Data;
import lombok.NonNull;

/**
 * Data on :2019/4/10 0010
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :
 */
@Data
public class MQSendBean {
    private int dormitoryId;
    private int buildingId;
    private int current;

    private int status;

    private int total;



    private int size;
}
