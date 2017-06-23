package com.zhongmeban.bean.postbody;

/**
 * Created by Chengbin He on 2016/8/15.
 */
public class DeleteRecordBody {

    private int recordId;

    public DeleteRecordBody(int recordId){
        this.recordId = recordId;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }
}
