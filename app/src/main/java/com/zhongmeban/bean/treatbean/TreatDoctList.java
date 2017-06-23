package com.zhongmeban.bean.treatbean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Chengbin He on 2016/12/8.
 */

public class TreatDoctList implements Serializable{

    private List<TreatDoctor> data;
    private String rrorMessage;
    private int errorCode;


    public List<TreatDoctor> getData() {
        return data;
    }

    public void setData(List<TreatDoctor> data) {
        this.data = data;
    }

    public String getRrorMessage() {
        return rrorMessage;
    }

    public void setRrorMessage(String rrorMessage) {
        this.rrorMessage = rrorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public class TreatDoctListData {

    }
}
