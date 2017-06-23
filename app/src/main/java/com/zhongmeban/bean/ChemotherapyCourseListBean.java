package com.zhongmeban.bean;

import java.util.List;

import de.greenrobot.dao.attention.ChemotherapyCourse;
import de.greenrobot.dao.attention.ChemotherapyCourseMedicine;

/**
 * 化疗疗程列表Bean
 * Created by Chengbin He on 2016/10/17.
 */

public class ChemotherapyCourseListBean {
    private ChemotherapyCourse chemotherapyCourse;
    private List<ChemotherapyCourseMedicine> dbChemotherapyCourseMedicineList;

    public ChemotherapyCourseListBean(ChemotherapyCourse chemotherapyCourse, List<ChemotherapyCourseMedicine> dbChemotherapyCourseMedicineList) {
        this.chemotherapyCourse = chemotherapyCourse;
        this.dbChemotherapyCourseMedicineList = dbChemotherapyCourseMedicineList;
    }

    public ChemotherapyCourse getChemotherapyCourse() {
        return chemotherapyCourse;
    }

    public void setChemotherapyCourse(ChemotherapyCourse chemotherapyCourse) {
        this.chemotherapyCourse = chemotherapyCourse;
    }

    public List<ChemotherapyCourseMedicine> getDbChemotherapyCourseMedicineList() {
        return dbChemotherapyCourseMedicineList;
    }

    public void setDbChemotherapyCourseMedicineList(List<ChemotherapyCourseMedicine> dbChemotherapyCourseMedicineList) {
        this.dbChemotherapyCourseMedicineList = dbChemotherapyCourseMedicineList;
    }
}
