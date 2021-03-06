package de.greenrobot.dao.attention;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "PATIENT".
 */
public class Patient implements java.io.Serializable {

    /** Not-null value. */
    private String patientId;
    private Long comfirmTime;
    private Long birthday;
    private Long diseaseId;
    private Integer relation;
    private Integer gender;
    private Integer status;
    private Integer medicineNum;
    private String diseaseName;
    private String userId;
    private String avatar;
    private Boolean isActive;

    public Patient() {
    }

    public Patient(String patientId) {
        this.patientId = patientId;
    }

    public Patient(String patientId, Long comfirmTime, Long birthday, Long diseaseId, Integer relation, Integer gender, Integer status, Integer medicineNum, String diseaseName, String userId, String avatar, Boolean isActive) {
        this.patientId = patientId;
        this.comfirmTime = comfirmTime;
        this.birthday = birthday;
        this.diseaseId = diseaseId;
        this.relation = relation;
        this.gender = gender;
        this.status = status;
        this.medicineNum = medicineNum;
        this.diseaseName = diseaseName;
        this.userId = userId;
        this.avatar = avatar;
        this.isActive = isActive;
    }

    /** Not-null value. */
    public String getPatientId() {
        return patientId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public Long getComfirmTime() {
        return comfirmTime;
    }

    public void setComfirmTime(Long comfirmTime) {
        this.comfirmTime = comfirmTime;
    }

    public Long getBirthday() {
        return birthday;
    }

    public void setBirthday(Long birthday) {
        this.birthday = birthday;
    }

    public Long getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(Long diseaseId) {
        this.diseaseId = diseaseId;
    }

    public Integer getRelation() {
        return relation;
    }

    public void setRelation(Integer relation) {
        this.relation = relation;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getMedicineNum() {
        return medicineNum;
    }

    public void setMedicineNum(Integer medicineNum) {
        this.medicineNum = medicineNum;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

}
