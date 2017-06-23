package com.zhongmeban.bean.postbody;

/**
 * Created by Chengbin He on 2016/5/26.
 */
public class MedicineQuestionBody {
    private String medicineId;//药品Id
    private String questionContent;//提问内容

    public String getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(String medicineId) {
        this.medicineId = medicineId;
    }

    public String getQuestionContent() {
        return questionContent;
    }

    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
    }
}
