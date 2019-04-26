package com.gzgykj.basecommon.response;

import lombok.Data;

/**
 * 作者： XS
 * 邮箱：1363826037@qq.com
 * 描述:
 * 创建时间:  2018\6\27 0027 11:34
 */
@Data
public class AppRespose<T>   {
    private int code;
    private String msg;
    private T data;
}
