package com.zhongmeban.bean;

/**
 * Created by Chengbin He on 2016/10/20.
 */

public class TreatMentShareBean {

    /**
     * 用户Id
     */
    private String userId = "";
    /**
     * 被收藏的数据Id
     */
    private int favoriteId;
    /**
     * 收藏类型
     */
    private int type;

    public TreatMentShareBean(){

    }

    public TreatMentShareBean(String userId, int favoriteId, int type) {
        this.userId = userId;
        this.favoriteId = favoriteId;
        this.type = type;
    }


    /**
     * 可以不给 userid
     * @param favoriteId
     * @param type
     */
    public TreatMentShareBean( int favoriteId, int type) {
        this.favoriteId = favoriteId;
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(int favoriteId) {
        this.favoriteId = favoriteId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
