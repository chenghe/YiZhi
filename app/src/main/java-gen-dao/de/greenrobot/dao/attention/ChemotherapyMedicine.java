package de.greenrobot.dao.attention;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "CHEMOTHERAPY_MEDICINE".
 */
public class ChemotherapyMedicine implements java.io.Serializable {

    private Long id;
    private Long medicineId;
    private Long chemotherapyId;
    private String medicineName;
    private String chemicalName;
    private String showName;

    public ChemotherapyMedicine() {
    }

    public ChemotherapyMedicine(Long id) {
        this.id = id;
    }

    public ChemotherapyMedicine(Long id, Long medicineId, Long chemotherapyId, String medicineName, String chemicalName, String showName) {
        this.id = id;
        this.medicineId = medicineId;
        this.chemotherapyId = chemotherapyId;
        this.medicineName = medicineName;
        this.chemicalName = chemicalName;
        this.showName = showName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(Long medicineId) {
        this.medicineId = medicineId;
    }

    public Long getChemotherapyId() {
        return chemotherapyId;
    }

    public void setChemotherapyId(Long chemotherapyId) {
        this.chemotherapyId = chemotherapyId;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getChemicalName() {
        return chemicalName;
    }

    public void setChemicalName(String chemicalName) {
        this.chemicalName = chemicalName;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

}
