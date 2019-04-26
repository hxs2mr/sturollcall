package com.gzgykj.basecommon.response;

import lombok.Data;

/**
 * 作者： XS
 * 邮箱：1363826037@qq.com
 * 描述:MQ
 * 创建时间:  2018\6\27 0027 11:34
 */
@Data
public class MQRespose<T>{
    private String deviceId;
    private int devieceType;
    private String method;
    private String event;
    private String descript;
    private T data;
}
