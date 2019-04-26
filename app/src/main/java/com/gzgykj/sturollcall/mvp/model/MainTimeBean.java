package com.gzgykj.sturollcall.mvp.model;

/**
 * Data on :2019/3/20 0020
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :  当前时间  星期  年月日  老列
 */
public class MainTimeBean {
    String hhmm;
    String week;
    String yymmdd;
    String oldtime;

    public MainTimeBean()
    {

    }
    public MainTimeBean(String hhmm, String week, String yymmdd, String oldtime) {
        this.hhmm = hhmm;
        this.week = week;
        this.yymmdd = yymmdd;
        this.oldtime = oldtime;
    }

    public String getHhmm() {
        return hhmm;
    }

    public void setHhmm(String hhmm) {
        this.hhmm = hhmm;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getYymmdd() {
        return yymmdd;
    }

    public void setYymmdd(String yymmdd) {
        this.yymmdd = yymmdd;
    }

    public String getOldtime() {
        return oldtime;
    }

    public void setOldtime(String oldtime) {
        this.oldtime = oldtime;
    }
}
