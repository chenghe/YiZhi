package com.zhongmeban.bean.postbody;

/**
 * 医生案例Post请求Body
 * Created by Chengbin He on 2016/5/11.
 */
public class DoctorCaseBody extends PageBody {
    private String doctorId;

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }
}
