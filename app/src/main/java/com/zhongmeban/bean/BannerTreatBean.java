package com.zhongmeban.bean;

/**
 * Created by User on 2016/11/7.
 */

public class BannerTreatBean {

    /**
     * createTime : 1477392826835
     * contentId : 1
     * orderby : 1
     * updateTime : 1477392826835
     * id : 1
     * type : 1
     * title : 1
     * isActive : true
     * picture : 123
     */

    private long createTime;
    private int contentId;
    private int orderby;
    private long updateTime;
    private int id;
    private int type;
    private String title;
    private boolean isActive;
    private String picture;
    private String urlAddress;


    public String getUrlAddress() {
        return urlAddress;
    }
    public long getCreateTime() { return createTime;}


    public void setCreateTime(long createTime) { this.createTime = createTime;}


    public int getContentId() { return contentId;}


    public void setContentId(int contentId) { this.contentId = contentId;}


    public int getOrderby() { return orderby;}


    public void setOrderby(int orderby) { this.orderby = orderby;}


    public long getUpdateTime() { return updateTime;}


    public void setUpdateTime(long updateTime) { this.updateTime = updateTime;}


    public int getId() { return id;}


    public void setId(int id) { this.id = id;}


    public int getType() { return type;}


    public void setType(int type) { this.type = type;}


    public String getTitle() { return title;}


    public void setTitle(String title) { this.title = title;}


    public boolean isIsActive() { return isActive;}


    public void setIsActive(boolean isActive) { this.isActive = isActive;}


    public String getPicture() { return picture;}


    public void setPicture(String picture) { this.picture = picture;}
}
