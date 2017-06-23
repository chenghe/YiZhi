package com.zhongmeban.bean;

import de.greenrobot.dao.attention.ChemotherapyCourse;

/**
 * Created by Chengbin He on 2016/10/13.
 */

public class ChemotherapyDetailInfoBean {
    private int status;
    private String name;//标题
    private String content;//内容

    public ChemotherapyDetailInfoBean(){

    }

    public ChemotherapyDetailInfoBean(String name,String content ) {
        this.content = content;
        this.name = name;
    }

    public ChemotherapyDetailInfoBean(int status, String name) {
        this.status = status;
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
