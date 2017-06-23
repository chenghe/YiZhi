package com.zhongmeban.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Description:药品详情的简介
 * user: Chong Chen
 * time：2016/1/21 15:36
 * 邮箱：cchen@ideabinder.com
 */
public class DrugOverview extends BaseBean {
    private DrugOverviewData data;

    public DrugOverviewData getData() {
        return data;
    }

    public void setData(DrugOverviewData data) {
        this.data = data;
    }

    public static  class DrugOverviewData implements Serializable{

        /**
         * insurance : 4
         * sameMedicineNum : 1
         * classify : 1
         * priceMax : 4698
         * showName : 爱必妥 (西妥昔单抗注射液)
         * imports : 0
         * chemicalName : 西妥昔单抗注射液
         * manufacturers : [{"name":"默克雪兰诺有限公司","id":262,"type":0}]
         * taboo : 已知对西妥昔单抗有严重超敏反应(3 级或 4 级)的患者禁用本品。
         伊立替康的有关禁忌，请参阅其使用说明书
         * medicineId : 177
         * diseases : [{"name":"结直肠癌","id":5}]
         * type : 2
         * isActive : true
         * medicineName : 爱必妥
         * special : 0
         * priceMin : 4698
         * pinyinName : aibituo
         * chemicalId : 145
         * prescription : 1
         * useInfo : 建议在经验丰富的实验室按照验证后的方法检测 EGFR；西妥昔单单抗必须在有使用抗癌药物经验的医师指导下使用。在用药过程中及用药结束后一小时内，必须密切监察患者的状况，并必须配备复苏设备。
         首次注滴本品之前，患者必须接受抗组胺药物治疗，建议在随后每次使用本品之前都对患者进行这种治疗。
         本品每周给药一次。初始计量为 400 mg/m2表面积，其后每周 250 mg/m2体表面积。
         初次给药时，建议滴注时间为 120 分钟，随后每周给药的滴注时间为 60 分钟，最大滴注速率不得超过 5 ml/min。
         * isFavorite : false
         * status : 1
         */



        private int insurance;
        private int sameMedicineNum;
        private int classify;
        private String priceMax = " ";
        private String showName = " ";
        private int imports;
        private String chemicalName = " ";
        private String taboo = " ";
        private int medicineId;
        private int type;
        private boolean isActive;
        private String medicineName = " ";
        private int special;
        private String priceMin = " ";
        private String pinyinName = " ";
        private int chemicalId;
        private int prescription;
        private String useInfo;
        private boolean isFavorite;
        private int status;
        /**
         * name : 默克雪兰诺有限公司
         * id : 262
         * type : 0
         */

        private List<ManufacturersBean> manufacturers;
        /**
         * name : 结直肠癌
         * id : 5
         */

        private List<DiseasesBean> diseases;


        public int getInsurance() { return insurance;}


        public void setInsurance(int insurance) { this.insurance = insurance;}


        public int getSameMedicineNum() { return sameMedicineNum;}


        public void setSameMedicineNum(int sameMedicineNum) {
            this.sameMedicineNum = sameMedicineNum;
        }


        public int getClassify() { return classify;}


        public void setClassify(int classify) { this.classify = classify;}


        public String getPriceMax() { return priceMax;}


        public void setPriceMax(String priceMax) { this.priceMax = priceMax;}


        public String getShowName() { return showName;}


        public void setShowName(String showName) { this.showName = showName;}


        public int getImports() { return imports;}


        public void setImports(int imports) { this.imports = imports;}


        public String getChemicalName() { return chemicalName;}


        public void setChemicalName(String chemicalName) { this.chemicalName = chemicalName;}


        public String getTaboo() { return taboo;}


        public void setTaboo(String taboo) { this.taboo = taboo;}


        public int getMedicineId() { return medicineId;}


        public void setMedicineId(int medicineId) { this.medicineId = medicineId;}


        public int getType() { return type;}


        public void setType(int type) { this.type = type;}


        public boolean isIsActive() { return isActive;}


        public void setIsActive(boolean isActive) { this.isActive = isActive;}


        public String getMedicineName() { return medicineName;}


        public void setMedicineName(String medicineName) { this.medicineName = medicineName;}


        public int getSpecial() { return special;}


        public void setSpecial(int special) { this.special = special;}


        public String getPriceMin() { return priceMin;}


        public void setPriceMin(String priceMin) { this.priceMin = priceMin;}


        public String getPinyinName() { return pinyinName;}


        public void setPinyinName(String pinyinName) { this.pinyinName = pinyinName;}


        public int getChemicalId() { return chemicalId;}


        public void setChemicalId(int chemicalId) { this.chemicalId = chemicalId;}


        public int getPrescription() { return prescription;}


        public void setPrescription(int prescription) { this.prescription = prescription;}


        public String getUseInfo() { return useInfo;}


        public void setUseInfo(String useInfo) { this.useInfo = useInfo;}


        public boolean isIsFavorite() { return isFavorite;}


        public void setIsFavorite(boolean isFavorite) { this.isFavorite = isFavorite;}


        public int getStatus() { return status;}


        public void setStatus(int status) { this.status = status;}


        public List<ManufacturersBean> getManufacturers() { return manufacturers;}


        public void setManufacturers(List<ManufacturersBean> manufacturers) {
            this.manufacturers = manufacturers;
        }


        public List<DiseasesBean> getDiseases() { return diseases;}


        public void setDiseases(List<DiseasesBean> diseases) { this.diseases = diseases;}


        @Override public String toString() {
            return "DrugOverviewData{" +
                "insurance=" + insurance +
                ", sameMedicineNum=" + sameMedicineNum +
                ", classify=" + classify +
                ", priceMax='" + priceMax + '\'' +
                ", showName='" + showName + '\'' +
                ", imports=" + imports +
                ", chemicalName='" + chemicalName + '\'' +
                ", taboo='" + taboo + '\'' +
                ", medicineId=" + medicineId +
                ", type=" + type +
                ", isActive=" + isActive +
                ", medicineName='" + medicineName + '\'' +
                ", special=" + special +
                ", priceMin='" + priceMin + '\'' +
                ", pinyinName='" + pinyinName + '\'' +
                ", chemicalId=" + chemicalId +
                ", prescription=" + prescription +
                ", useInfo='" + useInfo + '\'' +
                ", isFavorite=" + isFavorite +
                ", status=" + status +
                ", manufacturers=" + manufacturers +
                ", diseases=" + diseases +
                '}';
        }
    }


    public  static class ManufacturersBean {
        private String name = " ";
        private int id;
        private int type;


        public String getName() { return name;}


        public void setName(String name) { this.name = name;}


        public int getId() { return id;}


        public void setId(int id) { this.id = id;}


        public int getType() { return type;}


        public void setType(int type) { this.type = type;}
    }


    public  static class DiseasesBean {
        private String name = " ";
        private int id;


        public String getName() { return name;}


        public void setName(String name) { this.name = name;}


        public int getId() { return id;}


        public void setId(int id) { this.id = id;}
    }


    @Override public String toString() {
        return "DrugOverview{" +
            "data=" + data +
            '}';
    }
}
