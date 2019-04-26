package com.gzgykj.sturollcall.mvp.model;

import java.util.List;

import lombok.Data;

/**
 * Data on :2019/4/18 0018
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :
 */
@Data
public class StuListBean {
    List<Long> listID;

    public StuListBean(List<Long> listID) {
        this.listID = listID;
    }
}
