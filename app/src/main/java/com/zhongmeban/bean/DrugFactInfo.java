package com.zhongmeban.bean;

import java.io.Serializable;

/**
 * Description:药厂信息
 * user: Chong Chen
 * time：2016/1/28 18:46
 * 邮箱：cchen@ideabinder.com
 */
public class DrugFactInfo extends BaseBean {
    private DrugFactData data;

    public DrugFactData getData() {
        return data;
    }

    public void setData(DrugFactData data) {
        this.data = data;
    }

    public class DrugFactData implements Serializable {
        private String descripiton;
        private String address;
        private String manufacturerName;
        private String scope;
        private String manufacturerId;
        private String slogen;
        private int type;

        public String getDescripiton() {
            return descripiton;
        }

        public void setDescripiton(String descripiton) {
            this.descripiton = descripiton;
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

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }

        public String getManufacturerId() {
            return manufacturerId;
        }

        public void setManufacturerId(String manufacturerId) {
            this.manufacturerId = manufacturerId;
        }

        public String getSlogen() {
            return slogen;
        }

        public void setSlogen(String slogen) {
            this.slogen = slogen;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
