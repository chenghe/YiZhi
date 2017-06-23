package com.zhongmeban.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Description:指标详情
 * user: Chong Chen
 * time：2016/1/21 20:42
 * 邮箱：cchen@ideabinder.com
 */
public class IndiacatorDetail extends BaseBean {
    private IndiacatorData data;

    public IndiacatorData getData() {
        return data;
    }

    public void setData(IndiacatorData data) {
        this.data = data;
    }

    public class IndiacatorData implements Serializable {
        private String result = " ";
        private String notes = "暂无相关信息";
        private int discomfort;
        private String indexName = " ";
        private List<Inspection> inspections;
        private List<Doctor.Disease> diseases;
        private int indexId;

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getDescripiton() {
            return notes;
        }

        public void setDescripiton(String descripiton) {
            notes = descripiton;
        }

        public String getIndexName() {
            return indexName;
        }

        public void setIndexName(String indexName) {
            this.indexName = indexName;
        }

        public int getDiscomfort() {
            return discomfort;
        }

        public void setDiscomfort(int discomfort) {
            this.discomfort = discomfort;
        }

        public List<Doctor.Disease> getDiseases() {
            return diseases;
        }

        public void setDiseases(List<Doctor.Disease> diseases) {
            this.diseases = diseases;
        }

        public List<Inspection> getInspections() {
            return inspections;
        }

        public void setInspections(List<Inspection> inspections) {
            this.inspections = inspections;
        }

        public int getIndexId() {
            return indexId;
        }

        public void setIndexId(int indexId) {
            this.indexId = indexId;
        }


        @Override public String toString() {
            return "IndiacatorData{" +
                "result='" + result + '\'' +
                ", notes='" + notes + '\'' +
                ", discomfort=" + discomfort +
                ", indexName='" + indexName + '\'' +
                ", inspections=" + inspections +
                ", diseases=" + diseases +
                ", indexId=" + indexId +
                '}';
        }
    }

    public class Inspection implements Serializable{
        private boolean isInsurance;
        private String name = "";
        private int id;

        public boolean isInsurance() {
            return isInsurance;
        }

        public void setInsurance(boolean insurance) {
            this.isInsurance = insurance;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }


        @Override public String toString() {
            return "Inspection{" +
                "insurance=" + isInsurance +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
        }
    }


    @Override public String toString() {
        return "IndiacatorDetail{" +
            "data=" + data +
            '}';
    }
}
