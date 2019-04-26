package com.gzgykj.basecommon.model;

import java.io.Serializable;
import java.util.List;

/**
 * Data on :2019/4/9 0009
 * By User :HXS
 * Email on :1363826037@qq.com
 * Description on :
 */
public class LossBean implements Serializable {
    private String deviceId;
    private int devieceType;
    private int buildingId;
    private int dormitoryId;
    private String method;
    private String event;
    private DataBean data;
    private Object descript;

    public int getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(int buildingId) {
        this.buildingId = buildingId;
    }

    public int getDormitoryId() {
        return dormitoryId;
    }

    public void setDormitoryId(int dormitoryId) {
        this.dormitoryId = dormitoryId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getDevieceType() {
        return devieceType;
    }

    public void setDevieceType(int devieceType) {
        this.devieceType = devieceType;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public Object getDescript() {
        return descript;
    }

    public void setDescript(Object descript) {
        this.descript = descript;
    }

    public static class DataBean implements Serializable {

        private int code;
        private String msg;
        private int dormitoryId;
        private int current;
        private int size;
        private int status;
        private int total;
        private int buildingId;
        private String userName;
        private String articleName;
        private String spec;
        private String reportDesc;


        public int getBuildingId() {
            return buildingId;
        }

        public void setBuildingId(int buildingId) {
            this.buildingId = buildingId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getArticleName() {
            return articleName;
        }

        public void setArticleName(String articleName) {
            this.articleName = articleName;
        }

        public String getSpec() {
            return spec;
        }

        public void setSpec(String spec) {
            this.spec = spec;
        }

        public String getReportDesc() {
            return reportDesc;
        }

        public void setReportDesc(String reportDesc) {
            this.reportDesc = reportDesc;
        }

        public List<RcordsBean>  records;

        public List<RcordsBean> getRecords() {
            return records;
        }

        public void setRecords(List<RcordsBean> records) {
            this.records = records;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getDormitoryId() {
            return dormitoryId;
        }

        public void setDormitoryId(int dormitoryId) {
            this.dormitoryId = dormitoryId;
        }

        public int getCurrent() {
            return current;
        }

        public void setCurrent(int current) {
            this.current = current;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
