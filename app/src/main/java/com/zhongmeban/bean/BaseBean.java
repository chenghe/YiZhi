package com.zhongmeban.bean;

import com.zhongmeban.utils.JsonUtils;

import java.io.Serializable;

/**
 * Description:实体类的基类  定义了错误码。错误信息，成功码
 * user: Chong Chen
 * time：2016/1/14 14:29
 * 邮箱：cchen@ideabinder.com
 */
public class BaseBean implements Serializable {

    private int errorCode;

    private String errorMessage;

    public static int SUCCESS_CODE = 0;

    public static int getSuccessCode() {
        return SUCCESS_CODE;
    }

    public static void setSuccessCode(int successCode) {
        SUCCESS_CODE = successCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean success() {
        return SUCCESS_CODE == errorCode;
    }

    public static BaseBean jsonSuccess(String jsonStr) {
        return JsonUtils.fromJson(jsonStr, BaseBean.class);
    }

}
