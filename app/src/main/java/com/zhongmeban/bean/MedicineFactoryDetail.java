package com.zhongmeban.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 药厂详情Bean
 * Created by Chengbin He on 2016/5/3.
 */
public class MedicineFactoryDetail extends BaseBean {
    private MedicineFactoryDetailData data;

    public MedicineFactoryDetailData getData() {
        return data;
    }

    public void setData(MedicineFactoryDetailData data) {
        this.data = data;
    }

    public class MedicineFactoryDetailData implements Serializable {
        private String notes;
        private String address;
        private String manufacturerName;
        private String scope;
        private String manufacturerId;
        private String logo;
        private String slogen;
        private String type;
        private List<Medicine> medicines;

        public List<Medicine> getMedicines() {
            return medicines;
        }

        public void setMedicines(List<Medicine> medicines) {
            this.medicines = medicines;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }

        public String getDescripiton() {
            return notes;
        }

        public void setDescripiton(String descripiton) {
            notes = descripiton;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getManufacturerName() {
            return manufacturerName;
        }

        public void setManufacturerName(String manufacturerName) {
            this.manufacturerName = manufacturerName;
        }

        public String getManufacturerId() {
            return manufacturerId;
        }

        public void setManufacturerId(String manufacturerId) {
            this.manufacturerId = manufacturerId;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getSlogen() {
            return slogen;
        }

        public void setSlogen(String slogen) {
            this.slogen = slogen;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
    public class Medicine implements Serializable {
        private String chemicalName;
        private String medicineId;
        private String medicineName;
        private String showName;


        public String getShowName() {
            return showName;
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

        public String getMedicineId() {
            return medicineId;
        }

        public void setMedicineId(String medicineId) {
            this.medicineId = medicineId;
        }
    }
}
