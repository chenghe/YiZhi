package com.zhongmeban.bean.postbody;

/**
 * Created by HeCheng on 2016/11/6.
 */

public class UserLogoutBody {

    private String token;
    private String userId;

    public UserLogoutBody(String token, String userId) {
        this.token = token;
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
