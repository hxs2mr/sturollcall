package com.gzgykj.sturollcall.mvp.model;

import java.util.List;

import lombok.Data;

/**
 * Data on :2019/4/11 0011
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :
 */
@Data
public class PoliceBean {
    String success;
    /**
     * current : 1
     * pages : 4
     * records : [{"buildingId":1,"dormitoryId":1,"gmtCreate":1554964435000,"id":1,"status":0},{"buildingId":1,"dormitoryId":1,"gmtCreate":1554964435000,"id":2,"status":0},{"buildingId":1,"dormitoryId":1,"gmtCreate":1554964435000,"id":3,"status":0},{"buildingId":1,"dormitoryId":1,"gmtCreate":1554964435000,"id":4,"status":0},{"buildingId":1,"dormitoryId":1,"gmtCreate":1554964435000,"id":5,"status":0},{"buildingId":1,"dormitoryId":1,"gmtCreate":1554964435000,"id":6,"status":0},{"buildingId":1,"dormitoryId":1,"gmtCreate":1554964435000,"id":7,"status":0},{"buildingId":1,"dormitoryId":1,"gmtCreate":1554964435000,"id":8,"status":0},{"buildingId":1,"dormitoryId":1,"gmtCreate":1554964435000,"id":9,"status":0},{"buildingId":1,"dormitoryId":1,"gmtCreate":1554964435000,"id":10,"status":0}]
     * searchCount : true
     * size : 10
     * total : 35
     */
    private int current;
    private int pages;
    private boolean searchCount;
    private int size;
    private int total;
    private List<RecordsBean> records;

    @Data
    public static class RecordsBean {
        /**
         * buildingId : 1
         * dormitoryId : 1
         * gmtCreate : 1554964435000
         * id : 1
         * status : 0
         */
        private int buildingId;
        private int dormitoryId;
        private long gmtCreate;
        private int id;
        private int status;
    }
}
