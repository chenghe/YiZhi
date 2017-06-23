package com.zhongmeban.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Description:检查项目详情
 * user: Chong Chen
 * time：2016/1/18 18:17
 * 邮箱：cchen@ideabinder.com
 */
public class InspectionDetail extends BaseBean implements Serializable {

    private InspectionDetailData data;

    public InspectionDetailData getData() {
        return data;
    }

    public void setData(InspectionDetailData data) {
        this.data = data;
    }

    public class InspectionDetailData implements Serializable {
        private boolean insurance;
        private int inspectionId;
        private int discomfortIndex;//疼痛等级 1 无疼痛 2 轻微疼痛 3 较高疼痛
        private String inspectionMethodName;
        private List<Index> indexs;
        private String notes;
        private String inspectionName;
        private int cycle;
        private int inspectionMethodId;

        public List<Index> getIndexs() {
            return indexs;
        }

        public void setIndexs(List<Index> indexs) {
            this.indexs = indexs;
        }

        public boolean getIsInsurance() {
            return insurance;
        }

        public void setInsurance(boolean insurance) {
            this.insurance = insurance;
        }

        public int getInspectionId() {
            return inspectionId;
        }

        public void setInspectionId(int inspectionId) {
            this.inspectionId = inspectionId;
        }

        public int getDiscomfortIndex() {
            return discomfortIndex;
        }

        public void setDiscomfortIndex(int discomfortIndex) {
            this.discomfortIndex = discomfortIndex;
        }

        public String getInspectionMethodName() {
            return inspectionMethodName;
        }

        public void setInspectionMethodName(String inspectionMethodName) {
            this.inspectionMethodName = inspectionMethodName;
        }

        public String getDescription() {
            return notes;
        }

        public void setDescription(String description) {
            notes = description;
        }

        public String getInspectionName() {
            return inspectionName;
        }

        public void setInspectionName(String inspectionName) {
            this.inspectionName = inspectionName;
        }

        public int getInspectionMethodId() {
            return inspectionMethodId;
        }

        public void setInspectionMethodId(int inspectionMethodId) {
            this.inspectionMethodId = inspectionMethodId;
        }

        public int getCycle() {
            return cycle;
        }

        public void setCycle(int cycle) {
            this.cycle = cycle;
        }
    }

    public class Index implements Serializable{
        private String result;
        private String indexName;
        private String indexId;

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getIndexId() {
            return indexId;
        }

        public void setIndexId(String indexId) {
            this.indexId = indexId;
        }

        public String getIndexName() {
            return indexName;
        }

        public void setIndexName(String indexName) {
            this.indexName = indexName;
        }
    }
}
