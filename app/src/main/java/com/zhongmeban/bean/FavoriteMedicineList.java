package com.zhongmeban.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Chengbin He on 2016/11/9.
 */

public class FavoriteMedicineList extends BaseBean{
    private DrugListData data;

    public DrugListData getData() {
        return data;
    }

    public void setData(DrugListData data) {
        this.data = data;
    }

    public class DrugListData implements Serializable {
        private List<DrugScourceItem> sourceItems;
        private String serverTime;

        public List<DrugScourceItem> getSourceItems() {
            return sourceItems;
        }

        public void setSourceItems(List<DrugScourceItem> sourceItems) {
            this.sourceItems = sourceItems;
        }

        public String getServerTime() {
            return serverTime;
        }

        public void setServerTime(String serverTime) {
            this.serverTime = serverTime;
        }

    }

    public class DrugScourceItem implements Serializable {

        private long medicineId;
        private Long updateTime;
        private Long chemicalId;
        private int insurance;
        private int classify;
        private int imports;
        private int type;
        private int special;
        private int prescription;
        private int status;
        private String showName;
        private String chemicalName;
        private String medicineName;
        private String pinyinName;
        private String priceMax;
        private String priceMin;
        private boolean isActive;


        public long getMedicineId() {
            return medicineId;
        }


        public void setMedicineId(long medicineId) {
            this.medicineId = medicineId;
        }


        public Long getUpdateTime() {
            return updateTime;
        }


        public void setUpdateTime(Long updateTime) {
            this.updateTime = updateTime;
        }


        public Long getChemicalId() {
            return chemicalId;
        }


        public void setChemicalId(Long chemicalId) {
            this.chemicalId = chemicalId;
        }


        public int getInsurance() {
            return insurance;
        }


        public void setInsurance(int insurance) {
            this.insurance = insurance;
        }


        public int getClassify() {
            return classify;
        }


        public void setClassify(int classify) {
            this.classify = classify;
        }


        public int getImports() {
            return imports;
        }


        public void setImports(int imports) {
            this.imports = imports;
        }


        public int getType() {
            return type;
        }


        public void setType(int type) {
            this.type = type;
        }


        public int getSpecial() {
            return special;
        }


        public void setSpecial(int special) {
            this.special = special;
        }


        public int getPrescription() {
            return prescription;
        }


        public void setPrescription(int prescription) {
            this.prescription = prescription;
        }


        public int getStatus() {
            return status;
        }


        public void setStatus(int status) {
            this.status = status;
        }


        public String getShowName() {
            return showName;
        }


        public void setShowName(String showName) {
            this.showName = showName;
        }


        public String getChemicalName() {
            return chemicalName;
        }


        public void setChemicalName(String chemicalName) {
            this.chemicalName = chemicalName;
        }


        public String getMedicineName() {
            return medicineName;
        }


        public void setMedicineName(String medicineName) {
            this.medicineName = medicineName;
        }


        public String getPinyinName() {
            return pinyinName;
        }


        public void setPinyinName(String pinyinName) {
            this.pinyinName = pinyinName;
        }


        public String getPriceMax() {
            return priceMax;
        }


        public void setPriceMax(String priceMax) {
            this.priceMax = priceMax;
        }


        public String getPriceMin() {
            return priceMin;
        }


        public void setPriceMin(String priceMin) {
            this.priceMin = priceMin;
        }


        public boolean isActive() {
            return isActive;
        }


        public void setActive(boolean active) {
            isActive = active;
        }
    }
}
