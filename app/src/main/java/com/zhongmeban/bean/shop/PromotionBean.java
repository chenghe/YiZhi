package com.zhongmeban.bean.shop;

import java.io.Serializable;

/**
 * Created by User on 2016/10/24.
 * 轮播图的促销
 */

public class PromotionBean implements
    Serializable {

    /**
     * id : 2
     * name : 折扣促销
     * title : 联想笔记本促销专场
     * image : http://image.demo.shopxx.net/4.0/201501/98229d3b-08b7-4888-a99e-cf21a2a03b65.jpg
     * beginDate : 1477276401000
     * endDate : 1477276401000
     * minimumPrice : 10000
     * maximumPrice : 10000
     * priceExpression : 401000
     * pointExpression : 10000
     * isFreeShipping : false
     * isCouponAllowed : true
     * introduction : fds
     */

    private int id;
    private String name;
    private String title;
    private String image;
    private long beginDate;
    private long endDate;
    private int minimumPrice;
    private int maximumPrice;
    private int priceExpression;
    private int pointExpression;
    private boolean isFreeShipping;
    private boolean isCouponAllowed;
    private String introduction;


    public int getId() { return id;}


    public void setId(int id) { this.id = id;}


    public String getName() { return name;}


    public void setName(String name) { this.name = name;}


    public String getTitle() { return title;}


    public void setTitle(String title) { this.title = title;}


    public String getImage() { return image;}


    public void setImage(String image) { this.image = image;}


    public long getBeginDate() { return beginDate;}


    public void setBeginDate(long beginDate) { this.beginDate = beginDate;}


    public long getEndDate() { return endDate;}


    public void setEndDate(long endDate) { this.endDate = endDate;}


    public int getMinimumPrice() { return minimumPrice;}


    public void setMinimumPrice(int minimumPrice) { this.minimumPrice = minimumPrice;}


    public int getMaximumPrice() { return maximumPrice;}


    public void setMaximumPrice(int maximumPrice) { this.maximumPrice = maximumPrice;}


    public int getPriceExpression() { return priceExpression;}


    public void setPriceExpression(int priceExpression) { this.priceExpression = priceExpression;}


    public int getPointExpression() { return pointExpression;}


    public void setPointExpression(int pointExpression) { this.pointExpression = pointExpression;}


    public boolean isIsFreeShipping() { return isFreeShipping;}


    public void setIsFreeShipping(boolean isFreeShipping) { this.isFreeShipping = isFreeShipping;}


    public boolean isIsCouponAllowed() { return isCouponAllowed;}


    public void setIsCouponAllowed(boolean isCouponAllowed) {
        this.isCouponAllowed = isCouponAllowed;
    }


    public String getIntroduction() { return introduction;}


    public void setIntroduction(String introduction) { this.introduction = introduction;}
}
