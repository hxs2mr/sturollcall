package com.gzgykj.basecommon.model;

import java.util.List;

import lombok.Data;

/**
 * Data on :2019/4/10 0010
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :
 */
@Data
public class LossPageBean {

    /**
     * records : [{"id":1,"userName":"123","buildingId":1,"dormitoryId":1,"articleName":"123","spec":"1","reportDesc":"2","status":1,"handleTime":null,"gmtCreate":1554798601000,"gmtModified":1554831455000},{"id":2,"userName":"456","buildingId":1,"dormitoryId":1,"articleName":"213","spec":"1","reportDesc":"23","status":1,"handleTime":1554798620000,"gmtCreate":1554798617000,"gmtModified":1554769822000}]
     * total : 2
     * size : 10
     * current : 1
     * searchCount : true
     * pages : 1
     */
    private int total;
    private int size;
    private int current;
    private boolean searchCount;
    private int pages;
    private List<RecordsBean> records;

    @Data
    public static class RecordsBean {
        /**
         * id : 1
         * userName : 123
         * buildingId : 1
         * dormitoryId : 1
         * articleName : 123
         * spec : 1
         * reportDesc : 2
         * status : 1
         * handleTime : null
         * gmtCreate : 1554798601000
         * gmtModified : 1554831455000
         */
        private int id;
        private String userName;
        private int buildingId;
        private int dormitoryId;
        private String articleName;
        private String spec;
        private String reportDesc;
        private int status;
        private Object handleTime;
        private long gmtCreate;
        private long gmtModified;

    }
}
