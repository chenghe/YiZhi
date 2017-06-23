package com.zhongmeban.bean.postbody;

/**
 * Created by Chengbin He on 2016/8/4.
 */
public class UpdateEndOrDeleteMedicineBody {
    private int recordId;
    private Long endTime;


    public UpdateEndOrDeleteMedicineBody(int recordId, Long endTime) {
        this.recordId = recordId;
        this.endTime = endTime;
    }


    public UpdateEndOrDeleteMedicineBody() {
    }


    public Long getEndTime() {
        return endTime;
    }


    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }


    public int getRecordId() {
        return recordId;
    }


    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }
}
