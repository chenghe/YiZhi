package com.zhongmeban.bean.postbody;

/**
 * 更新用户头像Bean
 * Created by HeCheng on 2016/11/6.
 */

public class UpdateUserNameBody {
    private String userId;
    private String username;

    public UpdateUserNameBody(String userId, String userName) {
        this.userId = userId;
        username = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAvatar() {
        return username;
    }

    public void setAvatar(String userName) {
        username = userName;
    }
}
