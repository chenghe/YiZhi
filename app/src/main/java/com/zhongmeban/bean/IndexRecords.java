package com.zhongmeban.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 获取指标检查记录
 * Created by Chengbin He on 2016/7/26.
 */
public class IndexRecords implements Serializable{
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data{
        private String serverTime;
        private Boolean finish;
        private List<Source> source;
        private String errorCode;

        public List<Source> getSource() {
            return source;
        }

        public void setSource(List<Source> source) {
            this.source = source;
        }

        public String getServerTime() {
            return serverTime;
        }

        public void setServerTime(String serverTime) {
            this.serverTime = serverTime;
        }

        public Boolean getFinish() {
            return finish;
        }

        public void setFinish(Boolean finish) {
            this.finish = finish;
        }

        public String getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(String errorCode) {
            this.errorCode = errorCode;
        }
    }

    public class Source{
        private List<Indexs> indexs;
        private IndexItem indexItem;

        public List<Indexs> getIndexs() {
            return indexs;
        }

        public void setIndexs(List<Indexs> indexs) {
            this.indexs = indexs;
        }

        public IndexItem getIndexItem() {
            return indexItem;
        }

        public void setIndexItem(IndexItem indexItem) {
            this.indexItem = indexItem;
        }
    }

    public class Indexs{
//        private String unitName;//已废弃
        private String patientId;
        private long createTime;
        private int indexItemRecordId;// 标志物记录item id，每次保存生成一个，对应多个标志物
        private String indexName;
//        private String unitId;
        private int indexId;// 标志物本身 id
        private long updateTime;
        private float normalMin;//最小标准值
        private float normalMax;//最大标准值
        private long time;
        private int id;// 主键 id
        private int status;//1正常 2异常
        private String value;
        private String unitName;//单位名称
        private Boolean isActive;

        public float getNormalMin() {
            return normalMin;
        }

        public void setNormalMin(float normalMin) {
            this.normalMin = normalMin;
        }

        public float getNormalMax() {
            return normalMax;
        }

        public void setNormalMax(float normalMax) {
            this.normalMax = normalMax;
        }

        public String getUnitName() {
            return unitName;
        }

        public void setUnitName(String unitName) {
            this.unitName = unitName;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getPatientId() {
            return patientId;
        }

        public void setPatientId(String patientId) {
            this.patientId = patientId;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public int getIndexItemRecordId() {
            return indexItemRecordId;
        }

        public void setIndexItemRecordId(int indexItemRecordId) {
            this.indexItemRecordId = indexItemRecordId;
        }

        public String getIndexName() {
            return indexName;
        }

        public void setIndexName(String indexName) {
            this.indexName = indexName;
        }


        public int getIndexId() {
            return indexId;
        }

        public void setIndexId(int indexId) {
            this.indexId = indexId;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public Boolean getActive() {
            return isActive;
        }

        public void setActive(Boolean active) {
            isActive = active;
        }
    }

    public class IndexItem{

        private String patientId;
        private int hospitalId;
        private long createTime;
        private long updateTime;
        private long time;
        private String hospitalName;
        private int id;
        private int type;
        private Boolean isActive;

        public String getPatientId() {
            return patientId;
        }

        public void setPatientId(String patientId) {
            this.patientId = patientId;
        }

        public int getHospitalId() {
            return hospitalId;
        }

        public void setHospitalId(int hospitalId) {
            this.hospitalId = hospitalId;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public String getHospitalName() {
            return hospitalName;
        }

        public void setHospitalName(String hospitalName) {
            this.hospitalName = hospitalName;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public Boolean getActive() {
            return isActive;
        }

        public void setActive(Boolean active) {
            isActive = active;
        }
    }

}
