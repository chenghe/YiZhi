package com.zhongmeban.bean;

import java.io.Serializable;

/**
 * 关注创建通用返回
 * Created by Chengbin He on 2016/7/25.
 */
public class CreateOrDeleteBean implements Serializable{

    private Boolean result;
    private String errorMessage;
    private int errorCode;
    private String data;

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
