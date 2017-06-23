package com.zhongmeban.bean;

import java.io.Serializable;

/**
 * Created by Chengbin He on 2016/6/14.
 */
public class LoginIdentifyingCode implements Serializable{

    private boolean result;
    private String errorCode;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
