package com.zhongmeban.bean;

/**
 * Created by Chengbin He on 2016/8/12.
 */
public class AttentionOtherTherapeutic {
    private SurgeryMethodsByDiseaseId.Data data;
    private boolean check;

    public SurgeryMethodsByDiseaseId.Data getData() {
        return data;
    }

    public void setData(SurgeryMethodsByDiseaseId.Data data) {
        this.data = data;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
