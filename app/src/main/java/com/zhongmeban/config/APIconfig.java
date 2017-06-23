package com.zhongmeban.config;

/**
 * Description:
 * user: Chong Chen
 * time：2016/1/12 14:17
 * 邮箱：cchen@ideabinder.com
 */
public class APIconfig {

    public static String BASE_URL = "http://123.56.182.156:8080/zhongmeban/api/";

    public static String IMAGE_URL = "http://123.56.182.156:8080";

    /** 获取医生列表-post */
    public static String URL_GET_DOCTOR_LISTS = BASE_URL + "doctor/getDoctorPage";

    /** 获取医生简介 */
    public static String URL_GET_DOCTOR_INTRODUCE = BASE_URL + "doctor/getDoctorDetail?doctorId=%s";//参数：医生ID

    /** 获取医生案例 */
    public static String URL_GET_DOCTOR_CASE = BASE_URL + "doctor/getDoctorCase";

    /**
     * 获取医生工作时间表
     */
    public static String URL_GET_DOCTOR_WORKTIME = BASE_URL + "doctor/getDoctorWorkTime?doctorId=1&year=2016&month=1";

    /**
     * 提交医生评价
     */
    public static String URL_GET_DOCTOR_COMMENT = BASE_URL + "doctor/createDoctorCase";

    /** 获取医院列表 */
    public static String URL_GET_HOSPITAL_LISTS = BASE_URL + "hospital/getHospitalPage";

    /**
     * 医院详情-get
     */
    public static String URL_GET_HOSPITAL_DETAIL = BASE_URL + "hospital/getHospitalDetail?hospitalId=%s";//参数是医生ID

    /** 获取医院科室列表 */
    public static String URL_GET_HOSPITAL_DEPARTMENT = BASE_URL + "hospital/getDepartment?hospitalId=%s";//参数：医院ID

    /** 获取药品列表 */
    public static String URL_GET_DRUG_LISTS = BASE_URL + "medicine/getMedicinePage";

    /** 获取药品详情 */
    public static String URL_GET_DRUG_DETAIL = BASE_URL + "medicine/getMedicineDetail?medicineId=%s";//参数：药品ID

    /**  获取相关同类药品 */
    public static String URL_GET_RELATIVEDRUGS = BASE_URL + "medicine/getSameMedicines?medicineId=%s";

    /**  药品详情之问答 */
    public static String URL_GET_DRUG_RELATIVEQA = BASE_URL + "medicine/getMedicineQAndAPage";//药品的相关问答

    /** 药品详情-提问问题 */
    public static String URL_POST_DRUG_COMMENT = BASE_URL + "medicine/createMedicineQAndA";

    /**
     * 通过药品ID获取相关文献
     */
    public static String URL_GET_DOCUMENT_BY_DRUGID = BASE_URL + "information/getLiteraturePage";

    /** 药厂药品列表 */
    public static String URL_GET_FACTORY_DRUG_LISTS = BASE_URL + "medicine/getMedicinesInManufacturer";

    /** 药厂列表 */
    public static String URL_GET_FACTORY_LISTS = BASE_URL + "medicine/getManufacturerPage";

    /** 获取药厂详情 */
    public static String URL_GET_FACTORY_DETAIL = BASE_URL + "medicine/getManufacturerDetail?manufacturerId=%s";//参数：药厂ID



    /**
     * 疾病/肿瘤列表
     */
    public static String URL_GET_DISEASE_LISTS = BASE_URL + "disease/getDiseases?serverTime=%s";

    /**  肿瘤详情 */
    public static String URL_GET_DISEASE_DETAILS = BASE_URL + "disease/getDiseaseDetail?diseaseId=%s";

    /** 检查列表 */
    public static String URL_GET_INSPECTION_LISTS = BASE_URL + "disease/getInspections?serverTime=0";

    /** 检查详情 */
    public static String URL_GET_INSPECTION_DETAIL = BASE_URL + "disease/getInspectionDetail?inspectionId=%s";//参数：检查项目ID

    /** 治疗方法列表1  */
    public static String URL_GET_TREATMENT_LISTS = BASE_URL + "disease/getTherapeuticCategorys?serverTime=0";

    /** 治疗方法列表2 */
    public static String URL_GET_TREATMENT_LISTS_TWO = BASE_URL + "disease/getTherapeutics?diseaseId=%s&therapeuticCategoryId=%s";//参数：疾病ID，种类ID

    /** 通过疾病ID获取治疗方法  */
    public static String URL_GET_TREATMENT_LIST_BYID = BASE_URL + "disease/getTherapeuticCategorysByDiseaseId?diseaseId=%s";

    /**  治疗详情 */
    public static String URL_GET_TREATMENT_DETAIL = BASE_URL + "disease/getTherapeuticDetail?therapeuticId=%s";

    /** 名词解释列表 */
    public static String URL_GET_NOUNEXPLAINING_LIST = BASE_URL + "information/getInterpretationPage";

    /** 名词解释详情 */
    public static String URL_GET_NOUNEXPLAIN_DETAIL = BASE_URL + "information/getInterpretationContent?interpretationId=%s";




    /**
     * 指标列表
     */
    public static String URL_GET_INDIACATOR_LISTS = BASE_URL + "disease/getIndexs?serverTime=0";

    /**
     * 指标详情
     */
    public static String URL_GET_INDIACATOR_DETAIL = BASE_URL + "disease/getIndexDetail?indexId=%s";//参数：指标ID

    /**
     * 获取小贴士内容列表-post
     */
    public static String URL_GET_TIPS_LISTS = BASE_URL + "information/getInformationPage";

    /**
     * 获取到小贴士详情-post
     */
    public static String URL_GET_TIPS_DETAILS = BASE_URL + "information/getInformationContent?informationId=%s";

    /**
     * 获取资讯列表
     */
    public static String URL_GET_NEWS_LISTS = BASE_URL + "information/getNewsPage";

    /**
     * 获取资讯详情
     */
    public static String URL_GET_NEWS_DETAILS = BASE_URL + "information/getNewsContent?newsId=%s";

    /***
     * 获取城市列表
     */
    public static String URL_GET_CITY_LISTS = BASE_URL + "common/getCity"; //post请求，传参是省份id


}
