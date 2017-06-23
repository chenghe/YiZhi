package com.zhongmeban.bean.shop.post;

import com.zhongmeban.bean.shop.AddressListBean;

/**
 * Created by User on 2016/10/20.
 */

public class CreatAddressBody {

    public Long id;

    public String consignee;//收貨人

    public String address;

    public String zipCode;

    public String phone;

    public Long areaId;

    public Long memberId;
    public boolean isDefault;


    public CreatAddressBody() {
    }
    public CreatAddressBody(AddressListBean addressListBean) {
        this.id = addressListBean.getId();
        this.consignee = addressListBean.getConsignee();
        this.address = addressListBean.getAddress();
        this.zipCode = addressListBean.getZipCode();
        this.phone = addressListBean.getPhone();
        this.areaId = addressListBean.getAreaId();
        this.memberId = addressListBean.getMemberId();
        this.isDefault = addressListBean.isIsDefault();
    }


    //创建地址的时候不需要传递id
    public CreatAddressBody(String consignee, String address, String zipCode, String phone, Long areaId, Long memberId, boolean isDefault) {
        this.consignee = consignee;
        this.address = address;
        this.zipCode = zipCode;
        this.phone = phone;
        this.areaId = areaId;
        this.memberId = memberId;
        this.isDefault = isDefault;
    }


    //修改地址的时候要传递id
    public CreatAddressBody(Long id, String consignee, String address, String zipCode, String phone, Long areaId, Long memberId, boolean isDefault) {
        this.id = id;
        this.consignee = consignee;
        this.address = address;
        this.zipCode = zipCode;
        this.phone = phone;
        this.areaId = areaId;
        this.memberId = memberId;
        this.isDefault = isDefault;
    }
}
