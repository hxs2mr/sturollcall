package com.gzgykj.basecommon.response;
import com.google.gson.Gson;

/**
 * 作者： XS
 * 邮箱：1363826037@qq.com
 * 描述:
 * 创建时间:  2019\1\5 0005 17:00
 */
public class BaseResult {

    /**
     * status : 0
     * message : 授权码失败
     * data : []
     */

    private int status;
    private String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
