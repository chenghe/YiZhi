package de.greenrobot.dao.attention;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "MEDICINE_RECORD".
 */
public class MedicineRecord implements java.io.Serializable {

    private long id;
    private Long status;
    private Long medicineId;
    private Long medicineItemId;
    private String patientId;
    private Long startTime;
    private Long endTime;
    private String chemicalName;
    private String medicineName;
    private boolean isActive;

    public MedicineRecord() {
    }

    public MedicineRecord(long id) {
        this.id = id;
    }

    public MedicineRecord(long id, Long status, Long medicineId, Long medicineItemId, String patientId, Long startTime, Long endTime, String chemicalName, String medicineName, boolean isActive) {
        this.id = id;
        this.status = status;
        this.medicineId = medicineId;
        this.medicineItemId = medicineItemId;
        this.patientId = patientId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.chemicalName = chemicalName;
        this.medicineName = medicineName;
        this.isActive = isActive;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(Long medicineId) {
        this.medicineId = medicineId;
    }

    public Long getMedicineItemId() {
        return medicineItemId;
    }

    public void setMedicineItemId(Long medicineItemId) {
        this.medicineItemId = medicineItemId;
    }

    public String getPatientId() {
        return patientId;
    }

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

    public String getChemicalName() {
        return chemicalName;
    }

    public void setChemicalName(String chemicalName) {
        this.chemicalName = chemicalName;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

}