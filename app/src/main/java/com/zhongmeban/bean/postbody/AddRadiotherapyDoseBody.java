package com.zhongmeban.bean.postbody;

/**
 * Created by HeCheng on 2016/10/5.
 */

public class AddRadiotherapyDoseBody {

    private int id;
    private String currentDosage;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCurrentDosage() {
        return currentDosage;
    }

    public void setCurrentDosage(String currentDosage) {
        this.currentDosage = currentDosage;
    }
}
