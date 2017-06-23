package com.zhongmeban.bean.treatbean;

import java.io.Serializable;

/**
 * 功能描述：
 * 作者：ysf on 2016/12/19 11:17
 */
public class TreatTipsBean implements Serializable {

    /**
     * time : 1481969504336
     * writer : 丁香医生
     * id : 3
     * urlAddress : http://test.yizhi360.com:6064/tips/detail/3
     * title : 哪些人容易胆囊炎发作？
     */

    private long time;
    private String writer;
    private int id;
    private String urlAddress;
    private String title;


    public long getTime() { return time;}


    public void setTime(long time) { this.time = time;}


    public String getWriter() { return writer;}


    public void setWriter(String writer) { this.writer = writer;}


    public int getId() { return id;}


    public void setId(int id) { this.id = id;}


    public String getUrlAddress() { return urlAddress;}


    public void setUrlAddress(String urlAddress) { this.urlAddress = urlAddress;}


    public String getTitle() { return title;}


    public void setTitle(String title) { this.title = title;}
}
