package com.zhongmeban.bean.postbody;

import java.util.List;

/**
 * Created by HeCheng on 2016/11/6.
 */

public class DeleteFavoriteBody {

    private String userId = "";
    private int type;
    private List<Integer> items;

    public DeleteFavoriteBody(String userId, int type, List<Integer> items) {
        this.userId = userId;
        this.type = type;
        this.items = items;
    }
    //不需要userid也能获取数据
    public DeleteFavoriteBody( int type, List<Integer> items) {
        this.type = type;
        this.items = items;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<Integer> getItems() {
        return items;
    }

    public void setItems(List<Integer> items) {
        this.items = items;
    }
}
