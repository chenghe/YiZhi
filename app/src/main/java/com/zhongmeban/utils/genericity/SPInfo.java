package com.zhongmeban.utils.genericity;

/**
 * Created by Chengbin He on 2016/11/7.
 */

public interface SPInfo {

    /**
     * 用户Sp
     */
    String SPUserInfo = "userInfo";
    /**
     * ( "userId","" )userId String
     */
    String UserKey_userId = "userId";
    /**
     * ("token","")token String
     */
    String UserKey_token = "token";
    /**
     * ("phone","")用户手机号码 String
     */
    String UserKey_phone = "phone";
    /**
     * ("patientId","")关注人Id Sting
     */
    String UserKey_patientId = "patientId";
    /**
     * （"patientDiseaseId",0）关注人癌症Id int
     */
    String UserKey_patientDiseaseId = "patientDiseaseId";
    /**
     * ("userAvatar","")用户头像 String
     */
    String UserKey_userAvatar = "userAvatar";
    /**
     * ( "userName","" )用户名称 String
     */
    String UserKey_userName = "userName";
    /**
     * ( "nickname","" )用户名称 String
     */
    String UserKey_nickname = "nickname";
    /**
     * ("UPDATEPATION",true)是否更新 boolean
     */
    String UserKey_UPDATEPATION = "UPDATEPATION";
    /**
     * ("takingMedicine",0)增在服用的药品 int
     */
    String UserKey_takingMedicine = "takingMedicine";
    /**
     * ("recentlyCheck",0)最近一次检查时间 long
     */
    String UserKey_recentlyCheck = "recentlyCheck";
    /**
     * ("newNotices",0)新提示 int
     */
    String UserKey_newNotices = "newNotices";
    /**
     * ("markerListBack",false)新提示 boolean
     */
    String UserKey_markerListBack = "markerListBack";

    /**
     * 增量更新ServerTimeSp
     */
    String SPServerTime = "serverTime";

//    long ServerTimeValue_tumorServerTime = 0;//认识肿瘤预制servertime
//    long ServerTimeValue_interpretationServerTime= 0;//名词解释预制servertime
//    long ServerTimeValue_inspectionServerTime = 0;//检查项目预制servertime
//    long ServerTimeValue_indicatorServerTime = 0;//指标解读预制servertime
//    long ServerTimeValue_medicineServerTime = 0;//药箱子预制servertime

    long ServerTimeValue_tumorServerTime = 1480307207838L;//认识肿瘤预制servertime
    long ServerTimeValue_interpretationServerTime= 1480307253049L;//名词解释预制servertime
    long ServerTimeValue_inspectionServerTime = 1480307468534L;//检查项目预制servertime
    long ServerTimeValue_indicatorServerTime = 1480307261761L;//指标解读预制servertime
    long ServerTimeValue_medicineServerTime = 1480307238187L;//药箱子预制servertime

    String ServerTimeDefaultValue = "0";//servertime 默认值

    /**
     * ("pationListServerTime","0");关注人列表ServerTime
     */
    String ServerTimeKey_pationListServerTime = "pationListServerTime";
    /**
     * ("markerServerTime","0");标志物记录ServerTime
     */
    String ServerTimeKey_markerServerTime = "markerServerTime";
    /**
     * ("medicineRecordServerTime","0");辅助用药ServerTime
     */
    String ServerTimeKey_medicineRecordServerTime = "medicineRecordServerTime";
    /**
     * ("surgeryServerTime","0");手术记录ServerTime
     */
    String ServerTimeKey_surgeryServerTime = "surgeryServerTime";
    /**
     * ("hospitalizedServerTime","0");住院记录ServerTime
     */
    String ServerTimeKey_hospitalizedServerTime = "hospitalizedServerTime";
    /**
     * ("radiotherapyServerTime","0");放疗记录ServerTime
     */
    String ServerTimeKey_radiotherapyServerTime = "radiotherapyServerTime";
    /**
     * ("chemotherapyServerTime","0");化疗记录ServerTime
     */
    String ServerTimeKey_chemotherapyServerTime = "chemotherapyServerTime";
    /**
     * ("chemotherapyCourseServerTime","0");放疗周期记录ServerTime
     */
    String ServerTimeKey_chemotherapyCourseServerTime = "chemotherapyCourseServerTime";
    /**
     * ("attentionNoticesServerTime","0");提示ServerTime
     */
    String ServerTimeKey_attentionNoticesServerTime = "attentionNoticesServerTime";

    /**
     * ("indicatorServerTime",0);指标解读ServerTime
     */
    String ServerTimeKey_indicatorServerTime = "indicatorServerTime";
    /**
     * ("inspectionServerTime",0);检查项目ServerTime
     */
    String ServerTimeKey_inspectionServerTime = "inspectionServerTime";
    /**
     * ("medicineServerTime",0);药箱子ServerTime
     */
    String ServerTimeKey_medicineServerTime = "medicineServerTime";
    /**
     *    ("tumorServerTime",0);认识肿瘤ServerTime
     */
    String ServerTimeKey_tumorServerTime = "tumorServerTime";
    /**
     *    ("interpretationServerTime",0);名词解释ServerTime
     */
    String ServerTimeKey_interpretationServerTime = "interpretationServerTime";
    /**
     *    ("cityServerTime",0);城市ServerTime
     */
    String ServerTimeKey_cityServerTime = "cityServerTime";



    //////////////////////////////////SPUtil////////////////////////////////////
    String FILE_NAME = "app_data";
    String Tip_Read_Set = "tip_read_set";//小贴士阅读记录
    String Key_attentionDBVersion = "Key_attentionDBVersion";
}
