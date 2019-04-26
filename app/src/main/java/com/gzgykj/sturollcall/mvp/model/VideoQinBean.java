package com.gzgykj.sturollcall.mvp.model;

/**
 * Data on :2019/3/25 0025
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :
 */
public class VideoQinBean {
    String name;
    boolean isclick;

    public VideoQinBean(String name, boolean isclick) {
        this.name = name;
        this.isclick = isclick;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isIsclick() {
        return isclick;
    }

    public void setIsclick(boolean isclick) {
        this.isclick = isclick;
    }
}
