package com.zhongmeban.bean.postbody;

/**
 * 更新用户头像Bean
 * Created by HeCheng on 2016/11/6.
 */

public class UpdateUserAvatorBody {
    private String userId;
    private String avatar;

    public UpdateUserAvatorBody(String userId, String avatar) {
        this.userId = userId;
        this.avatar = avatar;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
