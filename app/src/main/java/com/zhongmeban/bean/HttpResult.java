package com.zhongmeban.bean;

import java.io.Serializable;

/**
 * Created by User on 2016/10/20.
 */

public class HttpResult<T> implements Serializable {
    public int errorCode;
    public String errorMessage;
    private boolean result;
    public T data;


    public HttpResult(int errorCode, String errorMessage, boolean result, T data) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.result = result;
        this.data = data;
    }


    public HttpResult(int errorCode, String errorMessage, T data) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.data = data;
    }


    public boolean isResult() {
        return result;
    }


    public void setResult(boolean result) {
        this.result = result;
    }


    @Override public String toString() {
        return "HttpResult{" +
            "errorCode=" + errorCode +
            ", errorMessage='" + errorMessage + '\''
            + "T=" + data.toString();
    }


    public int getErrorCode() {
        return errorCode;
    }


    public String getErrorMessage() {
        return errorMessage;
    }


    public T getData() {
        return data;
    }
}