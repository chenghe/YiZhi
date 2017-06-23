package com.zhongmeban.bean.postbody;

/**
 * 药品问答列表Body
 * Created by Chengbin He on 2016/5/26.
 */
public class MedicineQuestionListBody extends PageBody{

    private int skip;//从第几条开始计算
    private String orderBy;//时间 排序
    private String medicineId;//药品Id

    public MedicineQuestionListBody(String medicineId){
        this.medicineId = medicineId;
    }

    public int getSkip() {
        return skip;
    }

    public void setSkip(int skip) {
        this.skip = skip;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(String medicineId) {
        this.medicineId = medicineId;
    }
}
