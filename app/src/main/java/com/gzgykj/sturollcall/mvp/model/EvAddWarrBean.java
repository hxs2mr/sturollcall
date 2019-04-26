package com.gzgykj.sturollcall.mvp.model;

import lombok.Data;
import lombok.ToString;

/**
 * Data on :2019/4/11 0011
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :Eventbus 添加报警
 */
@Data
public class EvAddWarrBean {
     private  String Add;
    public EvAddWarrBean(String add) {
        Add = add;
    }
}
