package com.zhongmeban.bean.treatbean;

import java.io.Serializable;

/**
 * Created by Chengbin He on 2016/12/15.
 */

public class WebViewBean implements Serializable{

    private String url ;
    private String title;
    private String id;
    private int frome;

    public WebViewBean() {
    }

    public WebViewBean(String url, String title, String id, int frome) {
        this.url = url;
        this.title = title;
        this.id = id;
        this.frome = frome;
    }

    public int getFrome() {
        return frome;
    }

    public void setFrome(int frome) {
        this.frome = frome;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
