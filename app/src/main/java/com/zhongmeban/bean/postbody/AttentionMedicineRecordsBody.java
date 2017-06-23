package com.zhongmeban.bean.postbody;

import java.util.List;

/**
 * 创建用药记录，批量提交 Body
 * Created by Chengbin He on 2016/8/6.
 */
public class AttentionMedicineRecordsBody {
    private List<CreateMedicineRecordListBody> records;

    public AttentionMedicineRecordsBody(List<CreateMedicineRecordListBody> records){
        this.records = records;
    }

    public List<CreateMedicineRecordListBody> getRecords() {
        return records;
    }

    public void setRecords(List<CreateMedicineRecordListBody> records) {
        this.records = records;
    }
}
