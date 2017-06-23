package de.greenrobot.dao.attention;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "SURGERY_METHODS".
 */
public class SurgeryMethods implements java.io.Serializable {

    private Long id;
    private String surgeryMethodName;
    private Long surgeryMethodId;
    private Long surgeryRecordId;

    public SurgeryMethods() {
    }

    public SurgeryMethods(Long id) {
        this.id = id;
    }

    public SurgeryMethods(Long id, String surgeryMethodName, Long surgeryMethodId, Long surgeryRecordId) {
        this.id = id;
        this.surgeryMethodName = surgeryMethodName;
        this.surgeryMethodId = surgeryMethodId;
        this.surgeryRecordId = surgeryRecordId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSurgeryMethodName() {
        return surgeryMethodName;
    }

    public void setSurgeryMethodName(String surgeryMethodName) {
        this.surgeryMethodName = surgeryMethodName;
    }

    public Long getSurgeryMethodId() {
        return surgeryMethodId;
    }

    public void setSurgeryMethodId(Long surgeryMethodId) {
        this.surgeryMethodId = surgeryMethodId;
    }

    public Long getSurgeryRecordId() {
        return surgeryRecordId;
    }

    public void setSurgeryRecordId(Long surgeryRecordId) {
        this.surgeryRecordId = surgeryRecordId;
    }

}