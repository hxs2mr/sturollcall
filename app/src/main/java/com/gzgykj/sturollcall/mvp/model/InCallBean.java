package com.gzgykj.sturollcall.mvp.model;

import lombok.Data;

/**
 * Data on :2019/4/23 0023
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :
 */
@Data
public class InCallBean {

    /**
     * rollCallId : 68
     * rollCallRule : {"buildingId":1,"dormitoryList":"1","endTime":"16时55分","gmtCreate":1556009190000,"gmtModified":1555980685000,"id":22,"rollType":0,"singleEndDate":"2019年04月23","singleStartDate":"2019年04月23","startTime":"16时50分","status":1}
     */
    private int rollCallId;
    private RollCallRuleBean rollCallRule;

    @Data
    public static class RollCallRuleBean {
        /**
         * buildingId : 1
         * dormitoryList : 1
         * endTime : 16时55分
         * gmtCreate : 1556009190000
         * gmtModified : 1555980685000
         * id : 22
         * rollType : 0
         * singleEndDate : 2019年04月23
         * singleStartDate : 2019年04月23
         * startTime : 16时50分
         * status : 1
         */

        private int buildingId;
        private String dormitoryList;
        private String endTime;
        private long gmtCreate;
        private long gmtModified;
        private int id;
        private int rollType;
        private String singleEndDate;
        private String singleStartDate;
        private String startTime;
        private int status;
    }
}
