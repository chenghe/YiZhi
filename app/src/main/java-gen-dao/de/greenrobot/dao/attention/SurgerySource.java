package de.greenrobot.dao.attention;

import java.io.Serializable;
import java.util.List;

/**
 * 关注模块 手术记录
 * Created by Chengbin He on 2016/8/13.
 */
public class SurgerySource implements Serializable{

    private List<SurgeryMethods> surgeryMethods;
    private SurgeryRecord surgeryRecord;

    public SurgerySource(List<SurgeryMethods> surgeryMethods,SurgeryRecord surgeryRecord){
        this.surgeryMethods = surgeryMethods;
        this.surgeryRecord = surgeryRecord;
    }

    public List<SurgeryMethods> getSurgeryMethods() {
        return surgeryMethods;
    }

    public void setSurgeryMethods(List<SurgeryMethods> surgeryMethods) {
        this.surgeryMethods = surgeryMethods;
    }

    public SurgeryRecord getSurgeryRecord() {
        return surgeryRecord;
    }

    public void setSurgeryRecord(SurgeryRecord surgeryRecord) {
        this.surgeryRecord = surgeryRecord;
    }
}
