package com.zhongmeban.utils.genericity;

/**
 * 功能描述：
 * 作者：ysf on 2016/12/19 14:12
 */
public class ApiConfig {

    public static final String AttentionRelase = "zhongmeban/api/";// 正式环境，其中httpurl 的前缀需要手动切换
    public static final String AttentionTest = "";// rap环境专用，其中httpurl 的前缀需要手动切换

    public static final String Attention = AttentionRelase;

    /**
     * 治疗模块部分接口
     */
    public static final String BANNER_LIST_URL = Attention + "information/getRecommendList";
    //获取轮播图列表URL
    public static final String NEWS_LIST_URL = Attention + "information/getInformationPage";
    //治疗新闻资讯列表16.11.2
    public static final String NEWS_DETAIL_URL = Attention + "information/getInformationContent";
    //治疗新闻资讯详情详情16.11.2
    //String NEWS_LIST_URL = Attention + "information/getNewsPage";//获取咨询列表URL
    //String NEWS_DETAIL_URL = Attention + "information/getNewsContent";//获取咨询详情URL
    //String BANNER_LIST_URL = Attention+ "information/getInformationPage";//治疗新闻资讯列表16.11.2
    //String BANNER_DETAIL_URL = Attention + "information/getInformationContent";//治疗新闻资讯详情详情16.11.2
    public static final String TIP_LIST_URL = Attention + "information/getTipsPage";// 10.17 新小贴士列表URL

    public static final String INDICATOR_LIST_URL = Attention + "disease/getIndexs";//指标解读列表

    public static final String INDICATOR_DETAIL_URL = Attention + "disease/getIndexDetail";//指标解读详情

    public static final String INSPECTION_LIST_URL = Attention + "disease/getInspections";//检查项目列表URL

    public static final String INSPECTION_DETAIL_URL = Attention + "disease/getInspectionDetail";//检查项目详情URL

    public static final String INTERPRETATION_LIST_URL = Attention + "information/getInterpretations";//名词解释列表URL

    public static final String INTERPRETATION_DETAIL_URL = Attention + "information/getInterpretationContent";//名词解释详情URL

    public static final String TUMOR_LIST_URL = Attention + "disease/getDiseases";//认识肿瘤列表URL

    public static final String TUMOR_DETAIL_URL = Attention + "disease/getDiseaseDetail";//认识肿瘤详情URL

    public static final String HOSPITAL_LIST_URL = Attention + "hospital/getHospitalPage";//医院列表URL

    public static final String HOSPITAL_DETAIL_URL = Attention + "hospital/getHospitalDetail";//医院详情URL

    public static final String MEDICINE_LIST_URL = Attention + "medicine/getMedicines";//药箱子列表索引部分URL

    public static final String MEDICINE_DETAIL_URL = Attention + "medicine/getMedicineDetail";//药箱子详情部分URL

    public static final String MEDICINE_MEDICINEQANDAPAGE_URL = Attention + "medicine/getMedicineQAndAPage";//药品问答URL

    public static final String MEDICINE_SAMEMEDICINE_URL = Attention + "medicine/getSameMedicines";//获取相同药品

    public static final String MEDICINE_MEDICINEQANDA_URL = Attention + "medicine/createMedicineQAndA";//药品提问URL

    public static final String MEDICINEICON_LIST_URL = Attention + "medicine/getManufacturerPage";//药箱子药厂ICON列表部分URL

    public static final String MEDICINEICON_DETAIL_URL = Attention + "medicine/getManufacturerDetail";//药箱子药厂ICON详情部分URL

    public static final String DOCTOR_LIST_URL = Attention + "doctor/getDoctorPage";//获取医生列表URL

    public static final String DOCTOR_DETAIL_URL = Attention + "doctor/getDoctorDetail";//获取医生简介URL

    public static final String DOCTOR_CASE_URL = Attention + "doctor/getDoctorCase";//获取医生案例URL

    public static final String DOCTOR_WORKTIME_URL = Attention + "doctor/getDoctorWorkTime";//获取医生工作时间表URL

    public static final String THERAPEUTICBYDISID_LIST_URL = Attention + "disease/getTherapeuticByDisId";//获取某种癌症的某种治疗方法分类的所有治疗方法

    public static final String THERAPEUTICBYDISEASEID_LIST_URL = Attention + "disease/getTherapeuticCategorysByDiseaseId";//获取癌症相关的治疗方法分类列表

    public static final String THERAPEUTIC_DETAIL_URL = Attention + "disease/getTherapeuticDetail";//获取治疗方法详情URL

    public static final String CITY_LIST_URL = Attention + "common/getCity";//获取城市列表

    public static final String LOGIN_GETCODE_URL = Attention + "login/getCode";//登录获取验证码接口

    public static final String LOGIN_LOGIN_URL = Attention + "login/login";//登录接口

    public static final String CREATEUSERFAVORITE_URL = Attention + "user/createUserFavorite";//创建用户收藏

    public static final String DELETEUSERFAVORITE_URL = Attention + "user/deleteUserFavorite";//删除用户收藏

    public static final String GETFAVORITEDOCTOR_URL = Attention + "user/getFavoriteDoctor";//获取收藏医生列表

    public static final String UPDATEUSERAVATAR_URL = Attention + "user/updateUserAvatar";//修改用户头像

    public static final String UPDATEUSERNAME_URL = Attention + "user/updateUserName";//修改用户名

    public static final String UPDATENICKNAME_URL = Attention + "user/updateNickname";//修改昵称

    public static final String ISFAVORITE_URL = Attention + "user/getIsFavorite";//获取收藏

    public static final String USERLOGOUT_URL = Attention + "user/logout";//用户登出

    public static final String GETFAVORITEINFORMATION_URL = Attention + "user/getFavoriteInformation";//获取收藏的文章接口

    public static final String GETFAVORITETHERAPEUTIC_URL = Attention + "user/getFavoriteTherapeutic";//获取收藏的治疗方式

    public static final String GETFAVORITEMEDICINE_URL = Attention + "user/getFavoriteMedicine";//获取收藏的药品

    public static final String GET_MEDICINE_TYPE_URL = Attention + "medicine/getMedicineTypeList";//获取药品分类

    public static final String GET_MEDICINE_LIST_URL = Attention + "medicine/getMedicinePage";//获取药品列表

    public static final String GET_MEDICINE_SEARCH_URL = Attention + "medicine/getMedicinePage";//搜索药品

    public static final String GET_DISEASE_TYPE_URL = Attention + "disease/getDiseaseList";//癌症分类

    public static final String GET_TREAT_METHOD_SURGERY_URL = Attention + "disease/getSurgeryList";//手术

    public static final String GET_TREAT_METHOD_CHEMOTHERAPY_URL = Attention + "disease/getChemotherapyList";//化疗

    public static final String GET_TREAT_METHOD_RADIOTHERAPY_URL = Attention + "disease/getRadiotherapyList";//放疗

    public static final String GET_TREAT_METHOD_TARGET_URL = Attention + "disease/getTargetedTherapyList";//靶向治疗

    public static final String PROVINCE_LIST_URL = Attention + "common/getProvinceList";//获取省份（全量获取）

    public static final String PROVINCECITY_LIST_URL = Attention + "common/getCityList";//获取省内城市（全量获取）

    public static final String INFOFAVORITE_URL = Attention + "user/getInformationIsFavorite";//新闻是否收藏
    /**
     * 关注模块部分接口
     */
    public static final String PATIENTLIST_URL = Attention + "user/patient/getPatientList";//获取关注人列表

    public static final String CREATEPATIENT_URL = Attention + "user/patient/createPatient";//创建关注人

    public static final String INDEXRECORDS_URL = Attention + "user/patient/getIndexRecords";//获取指标检查记录

    public static final String CREATEINDEXRECORD_URL = Attention + "user/patient/createIndexRecord";//创建指标检查记录

    public static final String GET_MARKER_SELECT_LIST = Attention + "disease/getIndexPage";//添加标志物选择标志物列表

    public static final String SIMPLEHOSPITALPAGE_URL = Attention + "hospital/getSimpleHospitalPage";//获取医院简单信息列表信息，分页获取

    public static final String SIMPLEDOCTORPAGE_URL = Attention + "doctor/getSimpleDoctorPage";//获取医生简单信息列表，分页获取

    public static final String DELETEINDEXRECORD_URL = Attention + "user/patient/deleteIndexRecord";//删除指标记录

    public static final String UPDATEINDEXRECORD_URL = Attention + "user/patient/updateIndexRecord";//修改指标检查记录

    public static final String GETMEDICINERECORD_URL = Attention + "user/patient/getMedicineRecord";//获取用药记录

    public static final String CREATEMEDICINERECORDLIST_URL = Attention + "user/patient/createMedicineRecordList";//创建用药记录，批量提交

    public static final String UPDATEENDMEDICINE_URL = Attention + "user/patient/updateEndMedicine";//停止用药

    public static final String DELETEMEDICINE_URL = Attention + "user/patient/deleteMedicine";//删除用药记录

    public static final String SURGERYRECORDSLIST_URL = Attention + "user/patient/getSurgeryRecords";//获取手术记录

    public static final String CREATESURGERYRECORD_URL = Attention + "user/patient/createSurgeryRecord";//创建手术记录

    public static final String UPDATESURGERYRECORD_URL = Attention + "user/patient/updateSurgeryRecord";//修改手术记录

    public static final String DELETESURGERYRECORD_URL = Attention + "user/patient/deleteSurgeryRecord";//删除手术记录

    public static final String SURGERYMETHODSBYDISEASEID_URL = Attention + "disease/getSurgeryMethodsByDiseaseId";//获取某种癌症的所有手术的手术方法

    public static final String CREATEHOSPITALRECORD_URL = Attention + "user/patient/createHospitalRecord";//创建住院记录

    public static final String UPDATEHOSPITALRECORD_URL = Attention + "user/patient/updateHospitalRecord";//修改住院信息

    public static final String GETHOSPITALRECORDS_URL = Attention + "user/patient/getHospitalRecords";//获取住院记录

    public static final String DELETEHOSPITALRECORD_URL = Attention + "user/patient/deleteHospitalRecord";//删除出院记录

    public static final String CREATERADIOTHERAPYRECORD_URL = Attention + "user/patient/createRadiotherapyRecord";//创建放疗记录

    public static final String UPDATERADIOTHERAPYRECORD_URL = Attention + "user/patient/updateRadiotherapyRecord";//修改放疗

    public static final String GETRADIOTHERAPYRECORDS_URL = Attention + "user/patient/getRadiotherapyRecords";//获取放疗记录

    public static final String CREATECHEMOTHERAPYRECORD_URL = Attention + "user/patient/createChemotherapyRecord";//创建化疗记录

    public static final String GETCHEMOTHERAPYRECORDS_URL = Attention + "user/patient/getChemotherapyRecords";//获取化疗记录

    public static final String GETCHEMOTHERAPYCOURSERECORDS_URL = Attention + "user/patient/getChemotherapyCourseRecords";//获取化疗疗程记录

    public static final String CREATECHEMOTHERAPYCOURSERECORD_URL = Attention + "user/patient/createChemotherapyCourseRecord";//创建化疗疗程记录

    public static final String CHEMOTHERAPYCOURSERECORDSLIST_URL = Attention + "user/patient/getChemotherapyCourseRecords";//获取化疗疗程记录

    public static final String DELETECHEMOTHERAPYRECORD_URL = Attention + "user/patient/deleteChemotherapyRecord";//删除化疗记录

    public static final String UPDATECHEMOTHERAPYRECORDEND_URL = Attention + "user/patient/updateChemotherapyRecordEnd";//结束本次化疗

    public static final String UPDATECHEMOTHERAPYRECORD_URL = Attention + "user/patient/updateChemotherapyRecord";//修改化疗记录

    public static final String UPDATECHEMOTHERAPYCOURSERECORD_URL = Attention + "user/patient/updateChemotherapyCourseRecord";//修改化疗疗程

    public static final String DELETECHEMOTHERAPYCOURSERECORD_URL = Attention + "user/patient/deleteChemotherapyCourseRecord";//删除化疗疗程信息

    public static final String RECORDLIST_URL = Attention + "user/patient/getRecordList";//获取所有记录信息

    public static final String DELETEPATIENT_URL = Attention + "user/patient/deletePatient";//删除关注人

    public static final String UPDATEPATIENT_URL = Attention + "user/patient/updatePatient";//修改关注人

    public static final String NOTICESLIST_URL = Attention + "user/patient/getNotices";//提醒列表

    public static final String UPDATENOTICESTATUS_URL = Attention + "user/patient/updateNoticeStatus";//修改通知状态

    public static final String DELETERADIOTHERAPYRECORD_URL = Attention + "user/patient/deleteRadiotherapyRecord";//删除放疗记录

    public static final String UPDATERADIOTHERAPYRECORDEND_URL = Attention + "user/patient/updateRadiotherapyRecordEnd";//终止放疗

    public static final String CREATERADIOTHERAPYSUSPENDEDRECORD_URL = Attention + "user/patient/createRadiotherapySuspendedRecord";//暂停放疗

    public static final String UPDATERADIOTHERAPYRECORDADDDOSAGE_URL = Attention + "user/patient/updateRadiotherapyRecordAddDosage";//添加放疗次数剂量

    public static final String DELETERADIOTHERAPYSUSPENDEDRECORD_URL = Attention + "user/patient/deleteRadiotherapySuspendedRecord";//删除放疗暂停记录

    public static final String PATIENTICON_URL = "zhongmeban/" + "upload/uploadPictureOss";//上传用户头像
    //    String PATIENTICON_URL = "zhongmeban/" + "upload/uploadPictureOss?key=6";//上传用户头像
}
