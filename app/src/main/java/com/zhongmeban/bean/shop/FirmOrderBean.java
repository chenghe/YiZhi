package com.zhongmeban.bean.shop;

import java.io.Serializable;
import java.util.List;

/**
 * Created by User on 2016/10/27.
 */

public class FirmOrderBean implements Serializable {

    /**
     * id : 1
     * sn :
     * type : 1
     * status :
     * price : 1.02
     * fee :
     * freight : 0
     * tax :
     * promotionDiscount :
     * couponDiscount :
     * offsetAmount :
     * amount : 41900
     * amountPaid : 0
     * refundAmount :
     * rewardPoint :
     * exchangePoint :
     * weight : 16880
     * quantity : 11
     * shippedQuantity : 111
     * returnedQuantity : 11
     * consignee : 2
     * areaName : 北京市东城区
     * address : huu
     * zipCode : null
     * phone : 18618227583
     * memo : null
     * expire : 0
     * isUseCouponCode : 11
     * isExchangePoint : false
     * isAllocatedStock : false
     * paymentMethodName : 1
     * paymentMethodType :
     * shippingMethodName :
     * lockKey :
     * lockExpire :
     * completeDate :
     * orderItems : [{"id":1,"sn":"20150101102_7","name":"苹果 iPhone 6","type":"general","price":5200,"weight":400,"isDelivery":true,"thumbnail":"http://image.demo.shopxx.net/4.0/201501/d7f59d79-1958-4059-852c-0d6531788b48-thumbnail.jpg","quantity":1,"shippedQuantity":0,"returnedQuantity":0,"specifications":["金色","16GB"]},{"id":null,"sn":"20150101115","name":"OPPO R8007 R1S","type":"general","price":2600,"weight":380,"isDelivery":true,"thumbnail":"http://image.demo.shopxx.net/4.0/201501/d61e4a9e-ee3f-4508-ba8f-cd07539c4072-thumbnail.jpg","quantity":2,"shippedQuantity":0,"returnedQuantity":0,"specifications":[]},{"id":null,"sn":"20150101118","name":"酷派 8670","type":"general","price":600,"weight":460,"isDelivery":true,"thumbnail":"http://image.demo.shopxx.net/4.0/201501/d959ab2a-caf9-4a5e-94ff-193d6e561a4d-thumbnail.jpg","quantity":2,"shippedQuantity":0,"returnedQuantity":0,"specifications":[]},{"id":null,"sn":"20150101128","name":"华硕 TP500LN","type":"general","price":6800,"weight":3400,"isDelivery":true,"thumbnail":"http://image.demo.shopxx.net/4.0/201501/9da8b878-da95-4d48-9f0b-c6562dd8793a-thumbnail.jpg","quantity":3,"shippedQuantity":0,"returnedQuantity":0,"specifications":[]},{"id":null,"sn":"20150101130","name":"三星 NP455R4J","type":"general","price":2800,"weight":2200,"isDelivery":true,"thumbnail":"http://image.demo.shopxx.net/4.0/201501/03c8560a-bac4-473d-b46f-387a927b3218-thumbnail.jpg","quantity":1,"shippedQuantity":0,"returnedQuantity":0,"specifications":[]}]
     * orderTime : 0
     */

    private Long id;
    private String sn;
    private int type;
    private String status;
    private String price;
    private String fee;
    private int freight;
    private String tax;
    private String promotionDiscount;
    private String couponDiscount;
    private String offsetAmount;
    private int amount;
    private int amountPaid;
    private String refundAmount;
    private String rewardPoint;
    private String exchangePoint;
    private int weight;
    private int quantity;
    private int shippedQuantity;
    private int returnedQuantity;
    private String consignee;
    private String areaName;
    private String address;
    private Object zipCode;
    private String phone;
    private Object memo;
    private int expire;
    private int isUseCouponCode;
    private boolean isExchangePoint;
    private boolean isAllocatedStock;
    private String paymentMethodName;
    private String paymentMethodType;
    private String shippingMethodName;
    private String lockKey;
    private String lockExpire;
    private String completeDate;
    private int orderTime;
    /**
     * id : 1
     * sn : 20150101102_7
     * name : 苹果 iPhone 6
     * type : general
     * price : 5200
     * weight : 400
     * isDelivery : true
     * thumbnail : http://image.demo.shopxx.net/4.0/201501/d7f59d79-1958-4059-852c-0d6531788b48-thumbnail.jpg
     * quantity : 1
     * shippedQuantity : 0
     * returnedQuantity : 0
     * specifications : ["金色","16GB"]
     */

    private List<OrderItemsBean> orderItems;


    public Long getId() { return id;}


    public void setId(Long id) { this.id = id;}


    public String getSn() { return sn;}


    public void setSn(String sn) { this.sn = sn;}


    public int getType() { return type;}


    public void setType(int type) { this.type = type;}


    public String getStatus() { return status;}


    public void setStatus(String status) { this.status = status;}


    public String getPrice() { return price;}


    public void setPrice(String price) { this.price = price;}


    public String getFee() { return fee;}


    public void setFee(String fee) { this.fee = fee;}


    public int getFreight() { return freight;}


    public void setFreight(int freight) { this.freight = freight;}


    public String getTax() { return tax;}


    public void setTax(String tax) { this.tax = tax;}


    public String getPromotionDiscount() { return promotionDiscount;}


    public void setPromotionDiscount(String promotionDiscount) {
        this.promotionDiscount = promotionDiscount;
    }


    public String getCouponDiscount() { return couponDiscount;}


    public void setCouponDiscount(String couponDiscount) { this.couponDiscount = couponDiscount;}


    public String getOffsetAmount() { return offsetAmount;}


    public void setOffsetAmount(String offsetAmount) { this.offsetAmount = offsetAmount;}


    public int getAmount() { return amount;}


    public void setAmount(int amount) { this.amount = amount;}


    public int getAmountPaid() { return amountPaid;}


    public void setAmountPaid(int amountPaid) { this.amountPaid = amountPaid;}


    public String getRefundAmount() { return refundAmount;}


    public void setRefundAmount(String refundAmount) { this.refundAmount = refundAmount;}


    public String getRewardPoint() { return rewardPoint;}


    public void setRewardPoint(String rewardPoint) { this.rewardPoint = rewardPoint;}


    public String getExchangePoint() { return exchangePoint;}


    public void setExchangePoint(String exchangePoint) { this.exchangePoint = exchangePoint;}


    public int getWeight() { return weight;}


    public void setWeight(int weight) { this.weight = weight;}


    public int getQuantity() { return quantity;}


    public void setQuantity(int quantity) { this.quantity = quantity;}


    public int getShippedQuantity() { return shippedQuantity;}


    public void setShippedQuantity(int shippedQuantity) { this.shippedQuantity = shippedQuantity;}


    public int getReturnedQuantity() { return returnedQuantity;}


    public void setReturnedQuantity(int returnedQuantity) {
        this.returnedQuantity = returnedQuantity;
    }


    public String getConsignee() { return consignee;}


    public void setConsignee(String consignee) { this.consignee = consignee;}


    public String getAreaName() { return areaName;}


    public void setAreaName(String areaName) { this.areaName = areaName;}


    public String getAddress() { return address;}


    public void setAddress(String address) { this.address = address;}


    public Object getZipCode() { return zipCode;}


    public void setZipCode(Object zipCode) { this.zipCode = zipCode;}


    public String getPhone() { return phone;}


    public void setPhone(String phone) { this.phone = phone;}


    public Object getMemo() { return memo;}


    public void setMemo(Object memo) { this.memo = memo;}


    public int getExpire() { return expire;}


    public void setExpire(int expire) { this.expire = expire;}


    public int getIsUseCouponCode() { return isUseCouponCode;}


    public void setIsUseCouponCode(int isUseCouponCode) { this.isUseCouponCode = isUseCouponCode;}


    public boolean isIsExchangePoint() { return isExchangePoint;}


    public void setIsExchangePoint(boolean isExchangePoint) {
        this.isExchangePoint = isExchangePoint;
    }


    public boolean isIsAllocatedStock() { return isAllocatedStock;}


    public void setIsAllocatedStock(boolean isAllocatedStock) {
        this.isAllocatedStock = isAllocatedStock;
    }


    public String getPaymentMethodName() { return paymentMethodName;}


    public void setPaymentMethodName(String paymentMethodName) {
        this.paymentMethodName = paymentMethodName;
    }


    public String getPaymentMethodType() { return paymentMethodType;}


    public void setPaymentMethodType(String paymentMethodType) {
        this.paymentMethodType = paymentMethodType;
    }


    public String getShippingMethodName() { return shippingMethodName;}


    public void setShippingMethodName(String shippingMethodName) {
        this.shippingMethodName = shippingMethodName;
    }


    public String getLockKey() { return lockKey;}


    public void setLockKey(String lockKey) { this.lockKey = lockKey;}


    public String getLockExpire() { return lockExpire;}


    public void setLockExpire(String lockExpire) { this.lockExpire = lockExpire;}


    public String getCompleteDate() { return completeDate;}


    public void setCompleteDate(String completeDate) { this.completeDate = completeDate;}


    public int getOrderTime() { return orderTime;}


    public void setOrderTime(int orderTime) { this.orderTime = orderTime;}


    public List<OrderItemsBean> getOrderItems() { return orderItems;}


    public void setOrderItems(List<OrderItemsBean> orderItems) { this.orderItems = orderItems;}


    public static class OrderItemsBean {
        private Long id;
        private String sn;
        private String name;
        private String type;
        private int price;
        private int weight;
        private boolean isDelivery;
        private String thumbnail;
        private int quantity;
        private int shippedQuantity;
        private int returnedQuantity;
        private List<String> specifications;


        public Long getId() { return id;}


        public void setId(Long id) { this.id = id;}


        public String getSn() { return sn;}


        public void setSn(String sn) { this.sn = sn;}


        public String getName() { return name;}


        public void setName(String name) { this.name = name;}


        public String getType() { return type;}


        public void setType(String type) { this.type = type;}


        public int getPrice() { return price;}


        public void setPrice(int price) { this.price = price;}


        public int getWeight() { return weight;}


        public void setWeight(int weight) { this.weight = weight;}


        public boolean isIsDelivery() { return isDelivery;}


        public void setIsDelivery(boolean isDelivery) { this.isDelivery = isDelivery;}


        public String getThumbnail() { return thumbnail;}


        public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail;}


        public int getQuantity() { return quantity;}


        public void setQuantity(int quantity) { this.quantity = quantity;}


        public int getShippedQuantity() { return shippedQuantity;}


        public void setShippedQuantity(int shippedQuantity) {
            this.shippedQuantity = shippedQuantity;
        }


        public int getReturnedQuantity() { return returnedQuantity;}


        public void setReturnedQuantity(int returnedQuantity) {
            this.returnedQuantity = returnedQuantity;
        }


        public List<String> getSpecifications() { return specifications;}


        public void setSpecifications(List<String> specifications) {
            this.specifications = specifications;
        }
    }
}
