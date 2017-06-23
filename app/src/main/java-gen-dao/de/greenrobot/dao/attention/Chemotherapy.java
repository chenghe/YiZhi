package de.greenrobot.dao.attention;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "CHEMOTHERAPY".
 */
public class Chemotherapy implements java.io.Serializable {

    private long id;
    private Long startTime;
    private Integer status;
    private Integer chemotherapyAim;
    private Integer weeksCount;
    private Integer notesType;
    private Integer times;
    private String notes;
    private String doctorName;
    private String patientId;
    private Boolean isActive;

    public Chemotherapy() {
    }

    public Chemotherapy(long id) {
        this.id = id;
    }

    public Chemotherapy(long id, Long startTime, Integer status, Integer chemotherapyAim, Integer weeksCount, Integer notesType, Integer times, String notes, String doctorName, String patientId, Boolean isActive) {
        this.id = id;
        this.startTime = startTime;
        this.status = status;
        this.chemotherapyAim = chemotherapyAim;
        this.weeksCount = weeksCount;
        this.notesType = notesType;
        this.times = times;
        this.notes = notes;
        this.doctorName = doctorName;
        this.patientId = patientId;
        this.isActive = isActive;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getChemotherapyAim() {
        return chemotherapyAim;
    }

    public void setChemotherapyAim(Integer chemotherapyAim) {
        this.chemotherapyAim = chemotherapyAim;
    }

    public Integer getWeeksCount() {
        return weeksCount;
    }

    public void setWeeksCount(Integer weeksCount) {
        this.weeksCount = weeksCount;
    }

    public Integer getNotesType() {
        return notesType;
    }

    public void setNotesType(Integer notesType) {
        this.notesType = notesType;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

}
