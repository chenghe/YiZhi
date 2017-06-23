package com.zhongmeban.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * user: Chong Chen
 * time：2016/1/4 14:59
 * 邮箱：cchen@ideabinder.com
 */
public class TestBean implements Serializable {

    public Integer id;
    public String content;
    public int type;

    private boolean collect;

    public TestBean(Integer id, String content, boolean collect) {
        this.id = id;
        this.content = content;
        this.collect = collect;
    }
    public TestBean(String content) {
        this.content = content;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public void setType(int type) {
        this.type = type;
    }
    public int getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCollect(boolean collect) {
        this.collect = collect;
    }

    public boolean isCollect(){
        return collect;
    }

    @Override
    public String toString() {
        return "TestBean{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", collect=" + collect +
                '}';
    }
}
