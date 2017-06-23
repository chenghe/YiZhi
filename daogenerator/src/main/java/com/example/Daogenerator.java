package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class Daogenerator {

    private static final int AttentionSchemaVersion = 2;
    private static final int DBSchemaVersion = 1;

    public static void main(String[] args) throws Exception {
//        Schema schema = new Schema(DBSchemaVersion, "de.greenrobot.dao");
//        addIndicator(schema);
//        addInspection(schema);
//        addInterpretation(schema);
//        addTumor(schema);
//        addMedicine(schema);
//        addCity(schema);

        Schema attentionSchema = new Schema(AttentionSchemaVersion, "de.greenrobot.dao.attention");
        addRecordIndex(attentionSchema);
        addRecordIndexItem(attentionSchema);
        addMedicineRecord(attentionSchema);
        addAssistMedicineRecord(attentionSchema);
        addSurgeryRecord(attentionSchema);
        addSurgeryMethods(attentionSchema);
        addHospitalized(attentionSchema);
        addRadiotherapy(attentionSchema);
//        addRadiotherapySuspendedRecords(attentionSchema);
        addChemotherapy(attentionSchema);
        addChemotherapyMedicine(attentionSchema);
        addPatient(attentionSchema);
        addAttentionNotices(attentionSchema);

//        new DaoGenerator().generateAll(schema, "E:\\zhongmeban1.0\\app\\src\\main\\java-gen-dao");
        new DaoGenerator().generateAll(attentionSchema, "E:\\zhongmeban1.0\\app\\src\\main\\java-gen-dao");
    }


    /**
     * 创建 指标解读 表
     *
     * @param schema
     */
    private static void addIndicator(Schema schema) {
        Entity indicator = schema.addEntity("Indicator");
        indicator.implementsSerializable();
        indicator.addLongProperty("id").notNull().primaryKey();
        indicator.addLongProperty("updateTime");
        indicator.addIntProperty("status");
        indicator.addStringProperty("name");
        indicator.addStringProperty("pinyinName");
        indicator.addStringProperty("pinyinFullName");
        indicator.addStringProperty("unit");//单位名称
        indicator.addStringProperty("fullName");
//        indicator.addStringProperty("markerDose");
        indicator.addBooleanProperty("isActive");
        indicator.addBooleanProperty("isLandmark");//是否为标志物
//        indicator.addBooleanProperty("attentionMarker");

    }

    /**
     * 创建 检查项目 表
     *
     * @param schema
     */
    private static void addInspection(Schema schema) {
        Entity inspection = schema.addEntity("Inspection");
        inspection.implementsSerializable();
        inspection.addLongProperty("id").notNull().primaryKey();
        inspection.addLongProperty("updateTime");
        inspection.addStringProperty("name");
        inspection.addStringProperty("pinyinName");
        inspection.addStringProperty("pinyinFullName");
        inspection.addStringProperty("fullName");
        inspection.addIntProperty("status");
        inspection.addBooleanProperty("isActive");
        inspection.addBooleanProperty("insurance");
    }

    /**
     * 创建 名词解释 表
     *
     * @param schema
     */
    private static void addInterpretation(Schema schema) {
        Entity interpretation = schema.addEntity("Interpretation");
        interpretation.implementsSerializable();
        interpretation.addLongProperty("interpretationId").primaryKey().notNull();
        interpretation.addStringProperty("name");
        interpretation.addStringProperty("pinyinName");
        interpretation.addBooleanProperty("isActive");
    }

    /**
     * 创建 认识肿瘤 表
     *
     * @param schema
     */
    private static void addTumor(Schema schema) {
        Entity tumor = schema.addEntity("Tumor");
        tumor.implementsSerializable();
        tumor.addLongProperty("id").notNull().primaryKey();
        tumor.addStringProperty("name");
        tumor.addStringProperty("pinyinName");
        tumor.addIntProperty("status");
        tumor.addBooleanProperty("isActive");
    }

    /**
     * 创建 药箱子 表
     *
     * @param schema
     */
    private static void addMedicine(Schema schema) {
        Entity medicine = schema.addEntity("Medicine");
        medicine.implementsSerializable();
        medicine.addLongProperty("medicineId").notNull().primaryKey();//药品Id
        medicine.addLongProperty("updateTime");//更新时间
        medicine.addLongProperty("chemicalId");//化学名Id

        medicine.addIntProperty("insurance");//医保药1甲类 2乙类 3工伤 4否
        medicine.addIntProperty("classify");//中西药1中药 2西药 3生物血液制品
        medicine.addIntProperty("imports");//进口药1是 2否
        medicine.addIntProperty("type");//1.对症用药2.靶向用药4.镇痛8.其他 16.化疗用药
        medicine.addIntProperty("special");//特种药1是 2否
        medicine.addIntProperty("prescription");//处方药1是 2否
        medicine.addIntProperty("status");//1可用 2不可用

        medicine.addStringProperty("showName");//展示名称
        medicine.addStringProperty("chemicalName");
        medicine.addStringProperty("medicineName");
        medicine.addStringProperty("pinyinName");
        medicine.addStringProperty("priceMax");//价格
        medicine.addStringProperty("priceMin");//价格

        medicine.addBooleanProperty("isActive").notNull();

    }

    /**
     * 创建 城市 表
     *
     * @param schema
     */
    private static void addCity(Schema schema) {
        Entity city = schema.addEntity("City");
        city.implementsSerializable();
        city.addLongProperty("id").notNull().primaryKey();
        city.addStringProperty("name").notNull();
        city.addStringProperty("pinyinName").notNull();
    }

    /**
     * 创建 标志物名称 表
     *
     * @param schema
     */
    private static void addRecordIndex(Schema schema) {
        Entity recordIndex = schema.addEntity("RecordIndex");
        recordIndex.implementsSerializable();
        recordIndex.addLongProperty("id").notNull().primaryKey();
        recordIndex.addLongProperty("indexItemRecordId").notNull();
        recordIndex.addStringProperty("patientId").notNull();
//        recordIndex.addLongProperty("createTime").notNull();
        recordIndex.addStringProperty("indexName").notNull();
        recordIndex.addLongProperty("indexId").notNull();
        recordIndex.addFloatProperty("normalMin");//最小单位
        recordIndex.addFloatProperty("normalMax");//最大单位
//        recordIndex.addLongProperty("updateTime").notNull();
        recordIndex.addLongProperty("time").notNull();
        recordIndex.addIntProperty("status").notNull();//状态 1 正常 2异常
        recordIndex.addStringProperty("value").notNull();
        recordIndex.addStringProperty("unitName");//标志物单位
        recordIndex.addBooleanProperty("isActive").notNull();
    }

    /**
     * 创建 标志物详情 表
     *
     * @param schema
     */
    private static void addRecordIndexItem(Schema schema) {
        Entity recordIndexItem = schema.addEntity("RecordIndexItem");
        recordIndexItem.implementsSerializable();
        recordIndexItem.addLongProperty("id").notNull().primaryKey();
        recordIndexItem.addStringProperty("patientId").notNull();
        recordIndexItem.addLongProperty("hospitalId");
//        recordIndexItem.addLongProperty("createTime").notNull();
//        recordIndexItem.addLongProperty("updateTime").notNull();
        recordIndexItem.addLongProperty("time").notNull();
        recordIndexItem.addStringProperty("hospitalName");
        recordIndexItem.addLongProperty("type").notNull();
        recordIndexItem.addBooleanProperty("isActive").notNull();

    }

    /**
     * 创建 辅助用药 表
     *
     * @param schema
     */
    private static void addAssistMedicineRecord(Schema schema) {


        Entity medicineRecord = schema.addEntity("AssistMedicineRecord");
        medicineRecord.implementsSerializable();
        medicineRecord.addLongProperty("id").notNull().primaryKey();
        medicineRecord.addStringProperty("patientId").notNull();
        medicineRecord.addLongProperty("startTime");
        medicineRecord.addLongProperty("endTime");
        medicineRecord.addBooleanProperty("isActive").notNull();

    }

    /**
     * 创建 辅助用药 用药列表 表
     *
     * @param schema
     */
    private static void addMedicineRecord(Schema schema) {

        Entity medicineRecord = schema.addEntity("MedicineRecord");
        medicineRecord.implementsSerializable();
        medicineRecord.addLongProperty("id").notNull().primaryKey();
        medicineRecord.addLongProperty("status");
        medicineRecord.addLongProperty("medicineId");
        medicineRecord.addLongProperty("medicineItemId");//辅助用药 id
        medicineRecord.addStringProperty("patientId");
        medicineRecord.addLongProperty("startTime");
        medicineRecord.addLongProperty("endTime");
        medicineRecord.addStringProperty("chemicalName");
        medicineRecord.addStringProperty("medicineName");
        medicineRecord.addBooleanProperty("isActive").notNull();
    }

    /**
     * 创建 手术记录 表
     *
     * @param schema
     */
    private static void addSurgeryRecord(Schema schema) {
        Entity surgeryRecord = schema.addEntity("SurgeryRecord");
        surgeryRecord.implementsSerializable();
        surgeryRecord.addLongProperty("id").notNull().primaryKey();//手术ID
        surgeryRecord.addLongProperty("hospitalId").notNull();//医院ID
        surgeryRecord.addLongProperty("therapeuticId").notNull();
//        surgeryRecord.addLongProperty("doctorId").notNull();//医生Id
        surgeryRecord.addLongProperty("time").notNull();
//        surgeryRecord.addLongProperty("createTime");
//        surgeryRecord.addLongProperty("updateTime");
        surgeryRecord.addLongProperty("hospitalRecordId");//住院ID
        surgeryRecord.addStringProperty("notes");
        surgeryRecord.addStringProperty("doctorName");
        surgeryRecord.addStringProperty("therapeuticName");
        surgeryRecord.addStringProperty("patientId");
        surgeryRecord.addStringProperty("hospitalName");
        surgeryRecord.addBooleanProperty("isActive").notNull();
    }

    /**
     * 创建 手术其他项目 表
     *
     * @param schema
     */
    private static void addSurgeryMethods(Schema schema) {
        Entity surgeryMethods = schema.addEntity("SurgeryMethods");
        surgeryMethods.implementsSerializable();
        surgeryMethods.addIdProperty();
        surgeryMethods.addStringProperty("surgeryMethodName");//手术其他项名字
        surgeryMethods.addLongProperty("surgeryMethodId");//手术其他项ID
        surgeryMethods.addLongProperty("surgeryRecordId");//手术id
    }

    /**
     * 创建 住院 表
     *
     * @param schema
     */
    private static void addHospitalized(Schema schema) {
        Entity hospitalized = schema.addEntity("Hospitalized");
        hospitalized.implementsSerializable();
        hospitalized.addLongProperty("id").notNull().primaryKey();//住院ID
        hospitalized.addLongProperty("inTime").notNull();//入院时间
        hospitalized.addIntProperty("type").notNull();
        hospitalized.addIntProperty("status");
        hospitalized.addStringProperty("dayCount");//住院天数
        hospitalized.addStringProperty("patientId").notNull();
        hospitalized.addStringProperty("description");
        hospitalized.addStringProperty("hospitalName");
        hospitalized.addLongProperty("hospitalId").notNull();
        hospitalized.addStringProperty("doctorName");
        hospitalized.addBooleanProperty("isActive").notNull();
    }

    /**
     * 创建 放疗 表
     *
     * @param schema
     */
    private static void addRadiotherapy(Schema schema) {
        Entity radiotherapy = schema.addEntity("Radiotherapy");
        radiotherapy.implementsSerializable();
        radiotherapy.addLongProperty("id").notNull().primaryKey();
        radiotherapy.addLongProperty("startTime").notNull();
        radiotherapy.addIntProperty("resultType");//放疗结果 1疗程结束 2耐受终止 3手术准备 4疗程进行中
        radiotherapy.addStringProperty("patientId");
        radiotherapy.addStringProperty("predictDosage");//放疗剂量
        radiotherapy.addStringProperty("currentCount");//放疗次数
        radiotherapy.addStringProperty("weeksCount");//治疗时长
        radiotherapy.addStringProperty("notes");//治疗备注
        radiotherapy.addBooleanProperty("isActive");
    }

    /**
     * 创建 放疗 暂停记录表
     *
     * @param schema
     */
    private static void addRadiotherapySuspendedRecords(Schema schema) {
        Entity radiotherapySuspendedRecords = schema.addEntity("RadiotherapySuspendedRecords");
        radiotherapySuspendedRecords.implementsSerializable();
        radiotherapySuspendedRecords.addLongProperty("id").notNull().primaryKey();
        radiotherapySuspendedRecords.addLongProperty("radiotherapyRecordId");
        radiotherapySuspendedRecords.addLongProperty("startTime");
        radiotherapySuspendedRecords.addIntProperty("suspendDays");
        radiotherapySuspendedRecords.addIntProperty("reason");
        radiotherapySuspendedRecords.addStringProperty("patientId");
        radiotherapySuspendedRecords.addBooleanProperty("isActive");
    }

    /**
     * 创建 化疗 表
     *
     * @param schema
     */
    private static void addChemotherapy(Schema schema) {

        Entity chemotherapy = schema.addEntity("Chemotherapy");
        chemotherapy.implementsSerializable();
        chemotherapy.addLongProperty("id").notNull().primaryKey();
        chemotherapy.addLongProperty("startTime");
        chemotherapy.addIntProperty("status");
        chemotherapy.addIntProperty("chemotherapyAim");//化疗目的
        chemotherapy.addIntProperty("weeksCount");//化疗周期数
        chemotherapy.addIntProperty("notesType");//化疗备注
        chemotherapy.addIntProperty("times");
        chemotherapy.addStringProperty("notes");
        chemotherapy.addStringProperty("doctorName");//医生姓名
        chemotherapy.addStringProperty("patientId");
        chemotherapy.addBooleanProperty("isActive");
    }

    /**
     * 创建 化疗 用药 关系表
     *
     * @param schema
     */
    private static void addChemotherapyMedicine(Schema schema) {
        Entity chemotherapyMedicine = schema.addEntity("ChemotherapyMedicine");
        chemotherapyMedicine.implementsSerializable();
        chemotherapyMedicine.addIdProperty();
        chemotherapyMedicine.addLongProperty("medicineId");
        chemotherapyMedicine.addLongProperty("chemotherapyId");
        chemotherapyMedicine.addStringProperty("medicineName");//药品名称（已废弃）
        chemotherapyMedicine.addStringProperty("chemicalName");//化学名称
        chemotherapyMedicine.addStringProperty("showName");//展示名称
    }

//    /**
//     * 创建 化疗疗程 用药 关系表
//     *
//     * @param schema
//     */
//    private static void addChemotherapyCourseMedicine(Schema schema) {
//        Entity chemotherapyCourseMedicine = schema.addEntity("ChemotherapyCourseMedicine");
//        chemotherapyCourseMedicine.implementsSerializable();
//        chemotherapyCourseMedicine.addIdProperty();
//        chemotherapyCourseMedicine.addLongProperty("medicineId");
//        chemotherapyCourseMedicine.addLongProperty("chemotherapyCourseId");
//        chemotherapyCourseMedicine.addStringProperty("medicineName");
//    }

//    /**
//     * 创建 化疗 疗程 表
//     *
//     * @param schema
//     */
//    private static void addChemotherapyCourse(Schema schema) {
//        Entity chemotherapyCourse = schema.addEntity("ChemotherapyCourse");
//        chemotherapyCourse.implementsSerializable();
//        chemotherapyCourse.addLongProperty("id").notNull().primaryKey();
//        chemotherapyCourse.addLongProperty("chemotherapyRecordId");
//        chemotherapyCourse.addLongProperty("startTime");
//        chemotherapyCourse.addLongProperty("endTime");
//        chemotherapyCourse.addIntProperty("endReason");
//        chemotherapyCourse.addIntProperty("status");
//        chemotherapyCourse.addIntProperty("times");
//        chemotherapyCourse.addStringProperty("description");
//        chemotherapyCourse.addStringProperty("patientId");
//        chemotherapyCourse.addBooleanProperty("isActive");
//    }

    /**
     * 创建患者表
     *
     * @param schema
     */
    private static void addPatient(Schema schema) {
        Entity patient = schema.addEntity("Patient");
        patient.implementsSerializable();
        patient.addStringProperty("patientId").notNull().primaryKey();
        patient.addLongProperty("comfirmTime");
        patient.addLongProperty("birthday");
        patient.addLongProperty("diseaseId");
        patient.addIntProperty("relation");
        patient.addIntProperty("gender");
        patient.addIntProperty("status");
        patient.addIntProperty("medicineNum");
        patient.addStringProperty("diseaseName");
        patient.addStringProperty("userId");//用户ID
        patient.addStringProperty("avatar");//头像URL
        patient.addBooleanProperty("isActive");
    }

    /**
     * 关注提示表
     * @param schema
     */
    private static void addAttentionNotices(Schema schema) {
        Entity attentionNotices = schema.addEntity("AttentionNotices");
        attentionNotices.implementsSerializable();
        attentionNotices.addLongProperty("id").notNull().primaryKey();
        attentionNotices.addStringProperty("patientId");
        attentionNotices.addStringProperty("content");
        attentionNotices.addLongProperty("time");
        attentionNotices.addLongProperty("createTime");
        attentionNotices.addIntProperty("status");
        attentionNotices.addBooleanProperty("isActive");
    }

}
