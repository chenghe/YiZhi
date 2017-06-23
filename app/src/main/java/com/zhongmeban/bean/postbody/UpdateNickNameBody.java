package com.zhongmeban.bean.postbody;

/**
 * 更新用户头像Bean
 * Created by HeCheng on 2016/11/6.
 */

public class UpdateNickNameBody {
    private String userId;
    private String nickname;

    public UpdateNickNameBody(String userId, String nickname) {
        this.userId = userId;
        this.nickname = nickname;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAvatar() {
        return nickname;
    }

    public void setAvatar(String nickname) {
        nickname = nickname;
    }
}
