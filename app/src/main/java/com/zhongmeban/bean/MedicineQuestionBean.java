package com.zhongmeban.bean;

import java.io.Serializable;

/**
 * Created by Chengbin He on 2016/5/26.
 */
public class MedicineQuestionBean implements Serializable {

    private Boolean result;
    private String errorCode;

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
