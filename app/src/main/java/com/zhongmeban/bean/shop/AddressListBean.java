package com.zhongmeban.bean.shop;

import java.io.Serializable;

/**
 * Created by User on 2016/10/20.
 */

public class AddressListBean implements Serializable {

    /**
     * id : 2
     * consignee : ide奥比岛ner
     * areaName : 北京市
     * address : 123
     * zipCode : 100213
     * phone : 18600494848
     * isDefault : false
     * areaId : 1
     * memberId : 2
     */

    private Long id;
    private String consignee;
    private String areaName;
    private String address;
    private String zipCode;
    private String phone;
    private boolean isDefault;
    private Long areaId;
    private Long memberId;


    public Long getId() { return id;}


    public void setId(Long id) { this.id = id;}


    public String getConsignee() { return consignee;}


    public void setConsignee(String consignee) { this.consignee = consignee;}


    public String getAreaName() { return areaName;}


    public void setAreaName(String areaName) { this.areaName = areaName;}


    public String getAddress() { return address;}


    public void setAddress(String address) { this.address = address;}


    public String getZipCode() { return zipCode;}


    public void setZipCode(String zipCode) { this.zipCode = zipCode;}


    public String getPhone() { return phone;}


    public void setPhone(String phone) { this.phone = phone;}


    public boolean isIsDefault() { return isDefault;}


    public void setIsDefault(boolean isDefault) { this.isDefault = isDefault;}


    public Long getAreaId() { return areaId;}


    public void setAreaId(Long areaId) { this.areaId = areaId;}


    public Long getMemberId() { return memberId;}


    public void setMemberId(Long memberId) { this.memberId = memberId;}


    @Override public String toString() {
        return "AddressListBean{" +
            "id=" + id +
            ", consignee='" + consignee + '\'' +
            ", areaName='" + areaName + '\'' +
            ", address='" + address + '\'' +
            ", zipCode='" + zipCode + '\'' +
            ", phone='" + phone + '\'' +
            ", isDefault=" + isDefault +
            ", areaId=" + areaId +
            ", memberId=" + memberId +
            '}';
    }
}
