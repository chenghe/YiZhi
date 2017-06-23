package com.zhongmeban.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 创建指标检查记录
 * Created by Chengbin He on 2016/7/26.
 */
public class CreateIndexRecord implements Serializable {

    private IndexItem indexItem;
    private List<Indexs> indexs;

    public IndexItem getIndexItem() {
        return indexItem;
    }

    public void setIndexItem(IndexItem indexItem) {
        this.indexItem = indexItem;
    }

    public List<Indexs> getIndexs() {
        return indexs;
    }

    public void setIndexs(List<Indexs> indexs) {
        this.indexs = indexs;
    }

    public class IndexItem{

        private String patientId;
        private String hospitalId;
        private String type;
        private String time;

        public String getPatientId() {
            return patientId;
        }

        public void setPatientId(String patientId) {
            this.patientId = patientId;
        }

        public String getHospitalId() {
            return hospitalId;
        }

        public void setHospitalId(String hospitalId) {
            this.hospitalId = hospitalId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }

    public class Indexs{

        private String indexId;
        private String value;
        private String unitId;
        private String time;

        public String getIndexId() {
            return indexId;
        }

        public void setIndexId(String indexId) {
            this.indexId = indexId;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getUnitId() {
            return unitId;
        }

        public void setUnitId(String unitId) {
            this.unitId = unitId;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
