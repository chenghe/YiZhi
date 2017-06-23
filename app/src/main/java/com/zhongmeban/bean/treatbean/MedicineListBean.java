package com.zhongmeban.bean.treatbean;

import java.io.Serializable;

/**
 * 功能描述：
 * 作者：ysf on 2016/12/5 11:09
 */
public class MedicineListBean implements Serializable{

    /**
     * chemicalName : 测试内容m2f7
     * id : 75832
     * insurance : 46453
     * price : 测试内容cdno
     * showName : 测试内容5c5y
     * special : 57845
     */

    private String chemicalName;
    private int id;
    private int insurance;
    private String price;
    private String showName;
    private int special;
    private String medicineName;//敖康欣


    public String getMedicineName() {
        return medicineName;
    }


    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }


    public String getChemicalName() { return chemicalName;}


    public void setChemicalName(String chemicalName) { this.chemicalName = chemicalName;}


    public int getId() { return id;}


    public void setId(int id) { this.id = id;}


    public int getInsurance() { return insurance;}


    public void setInsurance(int insurance) { this.insurance = insurance;}


    public String getPrice() { return price;}


    public void setPrice(String price) { this.price = price;}


    public String getShowName() { return showName;}


    public void setShowName(String showName) { this.showName = showName;}


    public int getSpecial() { return special;}


    public void setSpecial(int special) { this.special = special;}


    public MedicineListBean(String chemicalName, int id, int insurance, String price, String showName, int special) {
        this.chemicalName = chemicalName;
        this.id = id;
        this.insurance = insurance;
        this.price = price;
        this.showName = showName;
        this.special = special;
    }
}
