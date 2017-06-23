package com.zhongmeban.bean;

import java.util.List;

import de.greenrobot.dao.attention.Chemotherapy;
import de.greenrobot.dao.attention.ChemotherapyCourse;

/**
 * Created by Chengbin He on 2016/10/17.
 */

public class ChemotherapyListBean {

    private Chemotherapy chemotherapy;
    private List<ChemotherapyCourse> dbChemotherapyCourseist;

    public ChemotherapyListBean(Chemotherapy chemotherapy, List<ChemotherapyCourse> dbChemotherapyCourseist) {
        this.chemotherapy = chemotherapy;
        this.dbChemotherapyCourseist = dbChemotherapyCourseist;
    }

    public Chemotherapy getChemotherapy() {
        return chemotherapy;
    }

    public void setChemotherapy(Chemotherapy chemotherapy) {
        this.chemotherapy = chemotherapy;
    }

    public List<ChemotherapyCourse> getDbChemotherapyCourseist() {
        return dbChemotherapyCourseist;
    }

    public void setDbChemotherapyCourseist(List<ChemotherapyCourse> dbChemotherapyCourseist) {
        this.dbChemotherapyCourseist = dbChemotherapyCourseist;
    }
}
