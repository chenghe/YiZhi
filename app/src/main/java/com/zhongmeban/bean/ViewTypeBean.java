package com.zhongmeban.bean;

import com.zhongmeban.utils.JsonUtils;

import java.io.Serializable;

/**
 * Description:实体类的基类  定义了错误码。错误信息，成功码
 * user: Chong Chen
 * time：2016/1/14 14:29
 * 邮箱：cchen@ideabinder.com
 */
public class ViewTypeBean implements Serializable {

    public int getViewType() {
        return ViewType;
    }

    public void setViewType(int viewType) {
        ViewType = viewType;
    }

    public int ViewType;

}
