package de.greenrobot.dao.attention;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "ASSIST_MEDICINE_RECORD".
 */
public class AssistMedicineRecord implements java.io.Serializable {

    private long id;
    /** Not-null value. */
    private String patientId;
    private Long startTime;
    private Long endTime;
    private boolean isActive;

    public AssistMedicineRecord() {
    }

    public AssistMedicineRecord(long id) {
        this.id = id;
    }

    public AssistMedicineRecord(long id, String patientId, Long startTime, Long endTime, boolean isActive) {
        this.id = id;
        this.patientId = patientId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isActive = isActive;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getPatientId() {
        return patientId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

}
