package com.zhongmeban.bean.treatbean;

import java.io.Serializable;

/**
 * 友盟分享Bean
 * Created by Chengbin He on 2016/12/13.
 */

public class UShareBean implements Serializable{

    private String targetUrl;
    private String title;
    private String text;

    public UShareBean(String targetUrl, String title, String text) {
        this.targetUrl = targetUrl;
        this.title = title;
        this.text = text;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }
}
