package com.zhongmeban.net;

import com.zhongmeban.bean.AttentionNoticesBean;
import com.zhongmeban.bean.BannerTreatBean;
import com.zhongmeban.bean.ChemotherapyRecordBean;
import com.zhongmeban.bean.CityList;
import com.zhongmeban.bean.CreateOrDeleteBean;
import com.zhongmeban.bean.DiseaseDetail;
import com.zhongmeban.bean.DoctDetailMedicase;
import com.zhongmeban.bean.DoctorDetail;
import com.zhongmeban.bean.DoctorList;
import com.zhongmeban.bean.DoctorWorkTime;
import com.zhongmeban.bean.DrugOverview;
import com.zhongmeban.bean.DrugRelatedQA;
import com.zhongmeban.bean.FavoriteMedicineList;
import com.zhongmeban.bean.FavoriteTreatMethodLists;
import com.zhongmeban.bean.HospitalDetail;
import com.zhongmeban.bean.HospitalLists;
import com.zhongmeban.bean.HospitalRecordsBean;
import com.zhongmeban.bean.HttpResult;
import com.zhongmeban.bean.IndexRecords;
import com.zhongmeban.bean.IndiacatorDetail;
import com.zhongmeban.bean.IndiacatorList;
import com.zhongmeban.bean.InspectionDetail;
import com.zhongmeban.bean.InspectionLists;
import com.zhongmeban.bean.Login;
import com.zhongmeban.bean.LoginIdentifyingCode;
import com.zhongmeban.bean.MedicineFactoryDetail;
import com.zhongmeban.bean.MedicineFactoryLists;
import com.zhongmeban.bean.MedicineList;
import com.zhongmeban.bean.MedicineQuestionBean;
import com.zhongmeban.bean.NewsDetail;
import com.zhongmeban.bean.NewsLists;
import com.zhongmeban.bean.NounDetails;
import com.zhongmeban.bean.NounexplainLists;
import com.zhongmeban.bean.PatientList;
import com.zhongmeban.bean.RadiotherapyRecordsListBean;
import com.zhongmeban.bean.RecordlistBean;
import com.zhongmeban.bean.SameMeidicne;
import com.zhongmeban.bean.SimpleDoctorPage;
import com.zhongmeban.bean.SimpleHospitalPage;
import com.zhongmeban.bean.SurgeryMethodsByDiseaseId;
import com.zhongmeban.bean.SurgeryRecordsList;
import com.zhongmeban.bean.TherapeuticDetailBean;
import com.zhongmeban.bean.TreatCategoryLists;
import com.zhongmeban.bean.TreatMentShareBean;
import com.zhongmeban.bean.TreatMethodLists;
import com.zhongmeban.bean.TrumerLists;
import com.zhongmeban.bean.attentionbean.AssistMedicineListBean;
import com.zhongmeban.bean.attentionbean.postbody.CreatAssistMedicineBean;
import com.zhongmeban.bean.postbody.AddRadiotherapyDoseBody;
import com.zhongmeban.bean.postbody.AttentionMarkerIndexSelectBean;
import com.zhongmeban.bean.postbody.CreateIndexRecordBody;
import com.zhongmeban.bean.postbody.CreateOrUpdateChemotherapyBody;
import com.zhongmeban.bean.postbody.CreateOrUpdateHospitalRecordBody;
import com.zhongmeban.bean.postbody.CreateOrUpdateRadiotherapyBody;
import com.zhongmeban.bean.postbody.CreatePatientBody;
import com.zhongmeban.bean.postbody.CreateSurgeryRecordBody;
import com.zhongmeban.bean.postbody.DeleteFavoriteBody;
import com.zhongmeban.bean.postbody.DeleteIndexRecordBody;
import com.zhongmeban.bean.postbody.DeletePatientBody;
import com.zhongmeban.bean.postbody.DeleteRecordBody;
import com.zhongmeban.bean.postbody.IdentityCodeBody;
import com.zhongmeban.bean.postbody.LoginBody;
import com.zhongmeban.bean.postbody.MedicineQuestionBody;
import com.zhongmeban.bean.postbody.MedicineQuestionListBody;
import com.zhongmeban.bean.postbody.PauseRadiotherapyBody;
import com.zhongmeban.bean.postbody.RecordListBody;
import com.zhongmeban.bean.postbody.StopRadiotherapyBody;
import com.zhongmeban.bean.postbody.UpdateEndOrDeleteMedicineBody;
import com.zhongmeban.bean.postbody.UpdateNickNameBody;
import com.zhongmeban.bean.postbody.UpdateNoticeStatusBody;
import com.zhongmeban.bean.postbody.UpdateUserAvatorBody;
import com.zhongmeban.bean.postbody.UpdateUserNameBody;
import com.zhongmeban.bean.postbody.UserLogoutBody;
import com.zhongmeban.bean.treatbean.CityListBean;
import com.zhongmeban.bean.treatbean.DieaseTypeBean;
import com.zhongmeban.bean.treatbean.MedicineListBean;
import com.zhongmeban.bean.treatbean.MedicineTypeBean;
import com.zhongmeban.bean.treatbean.ProvinceListBean;
import com.zhongmeban.bean.treatbean.TreatDoctList;
import com.zhongmeban.bean.treatbean.TreatHospList;
import com.zhongmeban.bean.treatbean.TreatMethodCommonBean;
import com.zhongmeban.bean.treatbean.TreatTipsBean;
import java.util.List;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

import static com.zhongmeban.utils.genericity.ApiConfig.BANNER_LIST_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.CITY_LIST_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.CREATECHEMOTHERAPYRECORD_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.CREATEHOSPITALRECORD_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.CREATEINDEXRECORD_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.CREATEMEDICINERECORDLIST_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.CREATEPATIENT_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.CREATERADIOTHERAPYRECORD_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.CREATERADIOTHERAPYSUSPENDEDRECORD_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.CREATESURGERYRECORD_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.CREATEUSERFAVORITE_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.DELETECHEMOTHERAPYCOURSERECORD_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.DELETECHEMOTHERAPYRECORD_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.DELETEHOSPITALRECORD_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.DELETEINDEXRECORD_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.DELETEMEDICINE_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.DELETEPATIENT_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.DELETERADIOTHERAPYRECORD_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.DELETERADIOTHERAPYSUSPENDEDRECORD_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.DELETESURGERYRECORD_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.DELETEUSERFAVORITE_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.DOCTOR_CASE_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.DOCTOR_DETAIL_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.DOCTOR_LIST_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.DOCTOR_WORKTIME_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.GETCHEMOTHERAPYRECORDS_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.GETFAVORITEDOCTOR_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.GETFAVORITEINFORMATION_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.GETFAVORITEMEDICINE_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.GETFAVORITETHERAPEUTIC_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.GETHOSPITALRECORDS_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.GETMEDICINERECORD_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.GETRADIOTHERAPYRECORDS_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.GET_DISEASE_TYPE_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.GET_MARKER_SELECT_LIST;
import static com.zhongmeban.utils.genericity.ApiConfig.GET_MEDICINE_LIST_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.GET_MEDICINE_TYPE_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.GET_TREAT_METHOD_CHEMOTHERAPY_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.GET_TREAT_METHOD_RADIOTHERAPY_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.GET_TREAT_METHOD_SURGERY_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.GET_TREAT_METHOD_TARGET_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.HOSPITAL_DETAIL_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.HOSPITAL_LIST_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.INDEXRECORDS_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.INDICATOR_DETAIL_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.INDICATOR_LIST_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.INSPECTION_DETAIL_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.INSPECTION_LIST_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.INTERPRETATION_DETAIL_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.INTERPRETATION_LIST_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.ISFAVORITE_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.LOGIN_GETCODE_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.LOGIN_LOGIN_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.MEDICINEICON_DETAIL_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.MEDICINEICON_LIST_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.MEDICINE_DETAIL_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.MEDICINE_LIST_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.MEDICINE_MEDICINEQANDAPAGE_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.MEDICINE_MEDICINEQANDA_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.MEDICINE_SAMEMEDICINE_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.NEWS_DETAIL_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.NEWS_LIST_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.NOTICESLIST_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.PATIENTICON_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.PATIENTLIST_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.PROVINCECITY_LIST_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.PROVINCE_LIST_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.RECORDLIST_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.SIMPLEDOCTORPAGE_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.SIMPLEHOSPITALPAGE_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.SURGERYMETHODSBYDISEASEID_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.SURGERYRECORDSLIST_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.THERAPEUTICBYDISEASEID_LIST_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.THERAPEUTICBYDISID_LIST_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.THERAPEUTIC_DETAIL_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.TIP_LIST_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.TUMOR_DETAIL_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.TUMOR_LIST_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.UPDATECHEMOTHERAPYRECORD_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.UPDATEENDMEDICINE_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.UPDATEHOSPITALRECORD_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.UPDATEINDEXRECORD_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.UPDATENICKNAME_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.UPDATENOTICESTATUS_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.UPDATEPATIENT_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.UPDATERADIOTHERAPYRECORDADDDOSAGE_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.UPDATERADIOTHERAPYRECORDEND_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.UPDATERADIOTHERAPYRECORD_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.UPDATESURGERYRECORD_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.UPDATEUSERAVATAR_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.UPDATEUSERNAME_URL;
import static com.zhongmeban.utils.genericity.ApiConfig.USERLOGOUT_URL;

/**
 * @Description: 联网URL
 * Created by Chengbin He on 2016/4/19.
 */
public interface HttpUrl {
    /**
     * 新闻是否收藏
     */
    @GET(ISFAVORITE_URL)
    Observable<HttpResult<Boolean>> getInformationIsFavorite(@Query("favoriteId") int favoriteId,
                                                             @Query("type") int type,
                                                             @Query("token") String token);

    /**
     * 获取全部省列表
     */
    @GET(PROVINCE_LIST_URL)
    Observable<HttpResult<List<ProvinceListBean>>> getProvinceList();

    /**
     * 获取全部省列表
     */
    @GET(PROVINCECITY_LIST_URL)
    Observable<HttpResult<List<CityListBean>>> getProvinceCityList(
        @Query("provinceCode") String provinceCode);

    /**
     * 获取收藏的药品列表
     */
    @GET(GETFAVORITEMEDICINE_URL)
    Observable<FavoriteMedicineList> getFavoriteMedicine(@Query("pageIndex") int pageIndex,
                                                         @Query("pageSize") int pageSize,
                                                         @Query("userId") String userId,
                                                         @Query("token") String token);

    /**
     * 获取收藏的治疗方式列表
     */
    @GET(GETFAVORITETHERAPEUTIC_URL)
    Observable<FavoriteTreatMethodLists> getFavoriteTherapeutic(@Query("pageIndex") int pageIndex,
                                                                @Query("pageSize") int pageSize,
                                                                @Query("userId") String userId,
                                                                @Query("token") String token);

    /**
     * 获取收藏的消息
     */
    @GET(GETFAVORITEINFORMATION_URL)
    Observable<NewsLists> getFavoriteInformation(@Query("pageIndex") int pageIndex,
                                                 @Query("pageSize") int pageSize,
                                                 @Query("userId") String userId,
                                                 @Query("token") String token);

    /**
     * 删除关注内容
     */
    @POST(DELETEUSERFAVORITE_URL)
    Observable<CreateOrDeleteBean> postDeleteFavorite(
        @Body DeleteFavoriteBody body, @Query("token") String token);

    /**
     * 更新用户头像
     */
    @POST(UPDATEUSERAVATAR_URL)
    Observable<CreateOrDeleteBean> postUpdateUserAvatar(
        @Body UpdateUserAvatorBody body, @Query("token") String token);

    /**
     * 更新用户名
     */
    @POST(UPDATEUSERNAME_URL)
    Observable<CreateOrDeleteBean> postUpdateUserName(
        @Body UpdateUserNameBody body, @Query("token") String token);

    /**
     * 更新用户昵称
     */
    @POST(UPDATENICKNAME_URL)
    Observable<CreateOrDeleteBean> postUpdateNickName(
        @Body UpdateNickNameBody body, @Query("token") String token);

    /**
     * 用户退出
     */
    @POST(USERLOGOUT_URL)
    Observable<CreateOrDeleteBean> postUserLogOut(
        @Body UserLogoutBody body, @Query("token") String token);

    /**
     * 上传用户头像
     */
    @Multipart
    @POST(PATIENTICON_URL)
    Observable<CreateOrDeleteBean> postUploadPatientIcon(
        @Query("key") int key, @Part MultipartBody.Part file);

    /**
     * 删除化疗疗程信息
     */
    @POST(DELETECHEMOTHERAPYCOURSERECORD_URL)
    Observable<CreateOrDeleteBean> posteleteChemotherapyCourseRecord(@Body DeleteRecordBody body,
                                                                     @Query("token") String token);

    /**
     * 删除放疗暂停记录
     */
    @POST(DELETERADIOTHERAPYSUSPENDEDRECORD_URL)
    Observable<CreateOrDeleteBean> postDeleteRadiotherapySuspendedRecord(
        @Body DeleteRecordBody body,
        @Query("token") String token);

    /**
     * 添加放疗次数剂量
     */
    @POST(UPDATERADIOTHERAPYRECORDADDDOSAGE_URL)
    Observable<CreateOrDeleteBean> postUpdateRadiotherapyRecordAddDosage(
        @Body AddRadiotherapyDoseBody body,
        @Query("token") String token);

    /**
     * 暂停放疗
     */
    @POST(CREATERADIOTHERAPYSUSPENDEDRECORD_URL)
    Observable<CreateOrDeleteBean> postPauseRadiotherapy(@Body PauseRadiotherapyBody body,
                                                         @Query("token") String token);

    /**
     * 终止放疗
     */
    @POST(UPDATERADIOTHERAPYRECORDEND_URL)
    Observable<CreateOrDeleteBean> postStopRadiotherapy(@Body StopRadiotherapyBody body,
                                                        @Query("token") String token);

    /**
     * 删除放疗记录
     */
    @POST(DELETERADIOTHERAPYRECORD_URL)
    Observable<CreateOrDeleteBean> postDeleteRadiotherapy(@Body DeleteRecordBody body,
                                                          @Query("token") String token);

    /**
     * 修改通知状态
     */
    @POST(UPDATENOTICESTATUS_URL)
    Observable<CreateOrDeleteBean> postUpdateNoticeStatus(@Body UpdateNoticeStatusBody body,
                                                          @Query("token") String token);

    /**
     * 提醒列表
     */
    @GET(NOTICESLIST_URL)
    Observable<AttentionNoticesBean> getAttentionNotices(@Query("serverTime") String serverTime,
                                                         @Query("patientId") String patientId,
                                                         @Query("token") String token);

    /**
     * 修改关注人
     */
    @POST(UPDATEPATIENT_URL)
    Observable<CreateOrDeleteBean> postUpdatePatient(@Body CreatePatientBody createPatientBody,
                                                     @Query("token") String token);

    /**
     * 删除关注人
     */
    @POST(DELETEPATIENT_URL)
    Observable<CreateOrDeleteBean> postDeletePatient(@Body DeletePatientBody body,
                                                     @Query("token") String token);

    /**
     * 获取所有记录信息
     */
    @POST(RECORDLIST_URL)
    Observable<RecordlistBean> postGetRecordList(@Body RecordListBody body,
                                                 @Query("token") String token);

    /**
     * 修改化疗疗程
     */
    //@POST(UPDATECHEMOTHERAPYCOURSERECORD_URL)
    //Observable<CreateOrDeleteBean> postUpdateChemotherapyCourseRecord(
    //    @Body CreateOrUpdateChemotherapyCourseBody body,
    //    @Query("token") String token);

    /**
     * 修改化疗记录
     */
    @POST(UPDATECHEMOTHERAPYRECORD_URL)
    Observable<HttpResult> postUpdateChemotherapyRecord(
        @Body CreateOrUpdateChemotherapyBody body);

    /**
     * 结束本次化疗
     */
    //@POST(UPDATECHEMOTHERAPYRECORDEND_URL)
    //Observable<CreateOrDeleteBean> postUpdateChemotherapyRecordEnd(@Body StopChemotherapyBody body,
    //                                                               @Query("token") String token);

    /**
     * 删除化疗记录
     */
    @POST(DELETECHEMOTHERAPYRECORD_URL)
    Observable<HttpResult> postDeleteChemotherapyRecord(@Body DeleteRecordBody body);

    /**
     * 获取化疗疗程记录
     */
    //@GET(CHEMOTHERAPYCOURSERECORDSLIST_URL)
    //Observable<ChemotherapyRecordBean> getChemotherapyCourseRecords(
    //    @Query("serverTime") String serverTime,
    //    @Query("patientId") String patientId,
    //    @Query("token") String token);

    /**
     * 创建化疗疗程记录
     */
    //@POST(CREATECHEMOTHERAPYCOURSERECORD_URL)
    //Observable<CreateOrDeleteBean> postCreateChemotherapyCourseRecord(
    //    @Body CreateOrUpdateChemotherapyCourseBody body,
    //    @Query("token") String token);

    /**
     * 获取化疗记录
     */
    @GET(GETCHEMOTHERAPYRECORDS_URL)
    Observable<HttpResult<ChemotherapyRecordBean>> getChemotherapyRecords(
        @Query("serverTime") String serverTime,
        @Query("patientId") String patientId);

    /**
     * 获取化疗疗程记录
     */
    //@GET(GETCHEMOTHERAPYCOURSERECORDS_URL)
    //Observable<ChemotherapyRecordBean> getChemotherapyCordsRecords(
    //    @Query("serverTime") String serverTime,
    //    @Query("patientId") String patientId,
    //    @Query("token") String token);

    /**
     * 创建化疗记录
     */
    @POST(CREATECHEMOTHERAPYRECORD_URL)
    Observable<HttpResult> postCreateChemotherapyRecord(
        @Body CreateOrUpdateChemotherapyBody body);

    /**
     * 获取放疗记录
     */
    @GET(GETRADIOTHERAPYRECORDS_URL)
    Observable<RadiotherapyRecordsListBean> getRadiotherapyRecordsList(
        @Query("serverTime") String serverTime,
        @Query("patientId") String patientId,
        @Query("token") String token);

    /**
     * 修改放疗
     */
    @POST(UPDATERADIOTHERAPYRECORD_URL)
    Observable<CreateOrDeleteBean> postUpdateRadiotherapyRecord(
        @Body CreateOrUpdateRadiotherapyBody body,
        @Query("token") String token);

    /**
     * 创建放疗记录
     */
    @POST(CREATERADIOTHERAPYRECORD_URL)
    Observable<CreateOrDeleteBean> postCreateRadiotherapyRecord(
        @Body CreateOrUpdateRadiotherapyBody body,
        @Query("token") String token);

    /**
     * 修改住院信息
     */
    @POST(UPDATEHOSPITALRECORD_URL)
    Observable<CreateOrDeleteBean> postUpdateHospitalRecord(
        @Body CreateOrUpdateHospitalRecordBody body,
        @Query("token") String token);

    /**
     * 删除出院记录
     */
    @POST(DELETEHOSPITALRECORD_URL)
    Observable<CreateOrDeleteBean> postDeleteHospitalRecord(@Body DeleteRecordBody body,
                                                            @Query("token") String token);

    /**
     * 获取住院记录
     */
    @GET(GETHOSPITALRECORDS_URL)
    Observable<HospitalRecordsBean> getHospitalRecords(@Query("serverTime") String serverTime,
                                                       @Query("patientId") String patientId,
                                                       @Query("token") String token);

    /**
     * 创建住院记录
     */
    @POST(CREATEHOSPITALRECORD_URL)
    Observable<CreateOrDeleteBean> postCreateHospitalRecord(
        @Body CreateOrUpdateHospitalRecordBody body,
        @Query("token") String token);

    /**
     * 修改手术记录
     */
    @POST(UPDATESURGERYRECORD_URL)
    Observable<CreateOrDeleteBean> postUpdateSurgeryRecord(@Body CreateSurgeryRecordBody body,
                                                           @Query("token") String token);

    /**
     * 删除手术记录
     */
    @POST(DELETESURGERYRECORD_URL)
    Observable<CreateOrDeleteBean> postDeleteSurgeryRecord(@Body DeleteRecordBody body,
                                                           @Query("token") String token);

    /**
     * 创建手术记录
     */
    @POST(CREATESURGERYRECORD_URL)
    Observable<CreateOrDeleteBean> postCreateSurgeryRecord(@Body CreateSurgeryRecordBody body,
                                                           @Query("token") String token);

    /**
     * 获取某种癌症的所有手术的手术方法
     */
    @GET(SURGERYMETHODSBYDISEASEID_URL)
    Observable<SurgeryMethodsByDiseaseId> getSurgeryMethodsByDiseaseId(
        @Query("diseaseId") int diseaseId);

    /**
     * 获取手术记录
     */
    @GET(SURGERYRECORDSLIST_URL)
    Observable<SurgeryRecordsList> getSurgeryRecords(@Query("serverTime") String serverTime,
                                                     @Query("patientId") String patientId,
                                                     @Query("token") String token);

    /**
     * 删除用药记录
     */
    @POST(DELETEMEDICINE_URL)
    Observable<HttpResult> deleteMedicine(@Body UpdateEndOrDeleteMedicineBody body);

    /**
     * 停止用药
     */
    @POST(UPDATEENDMEDICINE_URL)
    Observable<HttpResult> updateEndMedicine(
        @Body UpdateEndOrDeleteMedicineBody body);

    /**
     * 创建用药记录
     */
    @POST(CREATEMEDICINERECORDLIST_URL)
    Observable<HttpResult> createMedicineRecordList(
        @Body CreatAssistMedicineBean body);

    /**
     * 获取用药记录
     */
    @GET(GETMEDICINERECORD_URL)
    Observable<HttpResult<AssistMedicineListBean>> getMedicineRecord(@Query("serverTime") String serverTime,
                                                                     @Query("patientId") String patientId);

    /**
     * 修改指标检查记录
     */
    @POST(UPDATEINDEXRECORD_URL)
    Observable<CreateOrDeleteBean> postUpdateIndexRecord(
        @Body CreateIndexRecordBody createIndexRecordBody,
        @Query("token") String token);

    /**
     * 创建指标检查记录
     */
    @POST(CREATEINDEXRECORD_URL)
    Observable<CreateOrDeleteBean> postCreateIndexRecord(
        @Body CreateIndexRecordBody createIndexRecordBody,
        @Query("token") String token);

    /**
     * 删除指标记录
     */
    @POST(DELETEINDEXRECORD_URL)
    Observable<CreateOrDeleteBean> getDeleteIndexRecord(@Body DeleteIndexRecordBody body,
                                                        @Query("token") String token);

    /**
     * 获取关注人列表
     */
    @GET(PATIENTLIST_URL)
    Observable<PatientList> getPatientList(@Query("serverTime") String serverTime,
                                           @Query("userId") String userId,
                                           @Query("token") String token);

    /**
     * 创建关注人
     */
    @POST(CREATEPATIENT_URL)
    Observable<CreateOrDeleteBean> postCreatePatient(@Body CreatePatientBody createPatientBody,
                                                     @Query("token") String token);

    /**
     * 获取指标检查记录
     */
    @GET(INDEXRECORDS_URL)
    Observable<IndexRecords> getIndexRecords(@Query("serverTime") String serverTime,
                                             @Query("patientId") String patientId,
                                             @Query("token") String token);

    /**
     * 获取医院简单信息列表信息，分页获取
     */
    @GET(SIMPLEHOSPITALPAGE_URL)
    Observable<SimpleHospitalPage> getSimpleHospitalPage(@Query("pageIndex") int pageIndex,
                                                         @Query("pageSize") int pageSize,
                                                         @Query("cityId") String cityId,
                                                         @Query("level") String level,
                                                         @Query("name") String name);

    /**
     * 获取医生简单信息列表，分页获取
     */
    @Headers("Cache-Control: public, max-age=0")
    @GET(SIMPLEDOCTORPAGE_URL)
    Observable<SimpleDoctorPage> getSimpleDoctorPage(@Query("name") String name,
                                                     @Query("diseaseId") String diseaseId,
                                                     @Query("cityId") String cityId,
                                                     @Query("hospitalId") String hospitalId,
                                                     @Query("hospitalLevel") String hospitalLevel,
                                                     @Query("doctorTitle") String doctorTitle,
                                                     @Query("isOperation") String isOperation,
                                                     @Query("pageIndex") int pageIndex,
                                                     @Query("pageSize") int pageSize);

    /**
     * 登录接口
     */
    @POST(LOGIN_LOGIN_URL)
    Observable<Login> login(@Body LoginBody loginBody);

    /**
     * 获取登录验证码
     */
    @POST(LOGIN_GETCODE_URL)
    Observable<LoginIdentifyingCode> getCode(@Body IdentityCodeBody body);

    /**
     * 获取城市列表
     */
    @GET(CITY_LIST_URL)
    Observable<CityList> getCityList();

    /**
     * 获取治疗轮播图列表
     */
    @GET(BANNER_LIST_URL)
    Observable<HttpResult<List<BannerTreatBean>>> getNewsList(
        @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

    /**
     * 获取小贴士列表
     */
    @GET(TIP_LIST_URL)
    Observable<HttpResult<List<TreatTipsBean>>> getTipList(
        @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

    /**
     * 获取底部新闻资讯列表
     */
    @GET(NEWS_LIST_URL)
    Observable<NewsLists> getNewsListBottom(
        @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

    /**
     * 获取治疗新闻资讯详情
     */
    @GET(NEWS_DETAIL_URL)
    Observable<NewsDetail> getNewsDetail(
        @Query("informationId") String informationId, @Query("userId") String userId);

    /**
     * 获取指标解读列表
     */
    @GET(INDICATOR_LIST_URL)
    Observable<IndiacatorList> getIndicatorList(@Query("serverTime") long serverTime);

    /**
     * 获取指标详情列表
     */
    @GET(INDICATOR_DETAIL_URL)
    Observable<IndiacatorDetail> getIndicatorDetail(@Query("indexId") String indexId);

    /**
     * 获取检查项目列表
     */
    @GET(INSPECTION_LIST_URL)
    Observable<InspectionLists> getInspectionList(@Query("serverTime") long serverTime);

    /**
     * 获取检查项目详情
     */
    @GET(INSPECTION_DETAIL_URL)
    Observable<InspectionDetail> getInspectionDetail(@Query("inspectionId") String inspectionId);

    /**
     * 获取名词解释列表
     */
    @GET(INTERPRETATION_LIST_URL)
    Observable<NounexplainLists> getInterpretation(@Query("serverTime") long serverTime);

    /**
     * 获取名词解释详情
     */
    @GET(INTERPRETATION_DETAIL_URL)
    Observable<NounDetails> getInterpretationDetail(
        @Query("interpretationId") String interpretationId);

    /**
     * 获取认识肿瘤列表
     */
    @GET(TUMOR_LIST_URL)
    Observable<TrumerLists> getTumor(@Query("serverTime") long serverTime);

    /**
     * 获取认识肿瘤详情
     */
    @GET(TUMOR_DETAIL_URL)
    Observable<DiseaseDetail> getTumorDetail(@Query("diseaseId") String diseaseId);

    /**
     * 获取医院列表
     */
    @GET(HOSPITAL_LIST_URL)
    Observable<TreatHospList> getHospitalList(@Query("cityCode") String cityCode,//城市
                                              @Query("level") int level,//医院等级
                                              @Query("keywords") String keywords,//搜索
                                              @Query("pageIndex") int pageIndex,
                                              @Query("pageSize") int pageSize);

    /**
     * 获取医院列表
     */
    @GET(HOSPITAL_LIST_URL)
    @Headers("api-version: 1")
    Observable<HospitalLists> getOldHospitalList(@Query("cityId") String cityId,
                                                 @Query("level") String level,
                                                 @Query("name") String name,
                                                 @Query("pageIndex") int pageInde,
                                                 @Query("pageSize") int pageSize);

    /**
     * 获取医院详情
     */
    @POST(HOSPITAL_DETAIL_URL)
    Observable<HospitalDetail> getHospitalDetail(@Query("hospitalId") String hospitalId);

    /**
     * 获取药品列表
     */
    @GET(MEDICINE_LIST_URL)
    Observable<MedicineList> getMedicine(@Query("serverTime") long serverTime);

    /**
     * 获取同种药
     */
    @GET(MEDICINE_SAMEMEDICINE_URL)
    Observable<SameMeidicne> getSameMedicines(@Query("medicineId") String medicineId);

    /**
     * 获取药品问答列表
     */
    @POST(MEDICINE_MEDICINEQANDAPAGE_URL)
    Observable<DrugRelatedQA> postMedicineQAndAPage(@Body MedicineQuestionListBody body);

    /**
     * 药品提问
     */
    @POST(MEDICINE_MEDICINEQANDA_URL)
    Observable<MedicineQuestionBean> postMedicineQAndA(@Body MedicineQuestionBody body);

    /**
     * 获取药品详情概述
     */
    @GET(MEDICINE_DETAIL_URL)
    Observable<DrugOverview> getMedicineDetail(
        @Query("medicineId") String medicineId, @Query("userId") String userId);

    /**
     * 获取药厂Icon列表
     */
    @GET(MEDICINEICON_LIST_URL)
    //pageSize=50&indexPage=1
    Observable<MedicineFactoryLists> postMedicineIcon(@Query("pageIndex") int pageIndex,
                                                      @Query("pageSize") int pageSize);

    /**
     * 获取药厂详情
     */
    @GET(MEDICINEICON_DETAIL_URL)
    Observable<MedicineFactoryDetail> getMedicineIconDetail(
        @Query("manufacturerId") String manufacturerId);

    /**
     * 获取医生列表
     */
    @GET(DOCTOR_LIST_URL)
    Observable<TreatDoctList> getDoctorList(@Query("keywords") String keywords,
                                            @Query("cityCode") String cityCode,
                                            @Query("hospitalId") String hospitalId,
                                            @Query("hospitalLevel") int hospitalLevel,
                                            @Query("doctorTitle") int doctorTitle,
                                            @Query("pageIndex") int pageIndex,
                                            @Query("pageSize") int pageSize);

    /**
     * 获取医生列表
     */
    @GET(GETFAVORITEDOCTOR_URL)
    Observable<DoctorList> getFavoriteDoctor(@Query("pageIndex") int pageIndex,
                                             @Query("pageSize") int pageSize,
                                             @Query("userId") String userId,
                                             @Query("token") String token);

    /**
     * 获取医生简介
     */
    @POST(DOCTOR_DETAIL_URL)
    Observable<DoctorDetail> getDoctorDetail(
        @Query("doctorId") String doctorId, @Query("userId") String userId);

    /**
     * 获取医生案例
     */
    @GET(DOCTOR_CASE_URL)
    Observable<DoctDetailMedicase> getDoctorCase(@Query("doctorId") String doctorId,
                                                 @Query("pageSize") int pageSize,
                                                 @Query("pageIndex") int pageIndex);

    /**
     * 获取医生工作时间表
     */
    @POST(DOCTOR_WORKTIME_URL)
    Observable<DoctorWorkTime> getDoctorWorkTime(@Query("doctorId") String doctorId);

    /**
     * 获取癌症相关的治疗方法分类列表
     */
    @GET(THERAPEUTICBYDISEASEID_LIST_URL)
    Observable<TreatCategoryLists> getTherapeuticCategorysByDiseaseId(
        @Query("diseaseId") int diseaseId);

    /**
     * 获取某种癌症的某种治疗方法分类的所有治疗方法
     */
    @GET(THERAPEUTICBYDISID_LIST_URL)
    Observable<TreatMethodLists> getTherapeuticByDisId(@Query("diseaseId") String diseaseId
        , @Query("therapeuticCategoryId") String therapeuticCategoryId, @Query("name") String name);

    /**
     * 获取治疗方法详情
     */
    @GET(THERAPEUTIC_DETAIL_URL)
    Observable<TherapeuticDetailBean> getTherapeuticDetail(
        @Query("therapeuticId") String therapeuticId, @Query("userId") String userId);

    /**
     * 创建用户收藏
     */
    @POST(CREATEUSERFAVORITE_URL)
    Observable<CreateOrDeleteBean> postCreateUserFavorite(@Body TreatMentShareBean bean,
                                                          @Query("token") String token);

    /**
     * 获取药品分类
     */
    @GET(GET_MEDICINE_TYPE_URL)
    Observable<HttpResult<List<MedicineTypeBean>>> getMedicineTypeList();
    /**
     * 获取药品分类
     */
    @GET(GET_MEDICINE_TYPE_URL)
    Observable<HttpResult<List<MedicineTypeBean>>> getMedicineTypeListTest();

    /**
     * 获取药品列表  搜索
     */
    @Headers("Cache-Control:public, max-age=0")
    @GET(GET_MEDICINE_LIST_URL)
    Observable<HttpResult<List<MedicineListBean>>> getMedicinePage(
        @Query("keyword") String keyword,
        @Query("medicineTypeId") int medicineTypeId, @Query("pageIndex") int pageIndex,
        @Query("pageSize") int pageSize);

    /**
     * 癌症分类
     */
    @GET(GET_DISEASE_TYPE_URL)
    Observable<HttpResult<List<DieaseTypeBean>>> getDiseaseList();

    /**
     * 治疗方法--手术
     */
    @GET(GET_TREAT_METHOD_SURGERY_URL)
    Observable<HttpResult<List<TreatMethodCommonBean>>> getSurgeryList(
        @Query("diseaseId") int diseaseId);

    /**
     * 治疗方法--化疗
     */
    @GET(GET_TREAT_METHOD_CHEMOTHERAPY_URL)
    Observable<HttpResult<List<TreatMethodCommonBean>>> getChemotherapyList(
        @Query("diseaseId") int diseaseId);

    /**
     * 治疗方法--放疗
     */
    @GET(GET_TREAT_METHOD_RADIOTHERAPY_URL)
    Observable<HttpResult<List<TreatMethodCommonBean>>> getRadiotherapyList(
        @Query("diseaseId") int diseaseId);

    /**
     * 治疗方法--靶向治疗
     */
    @GET(GET_TREAT_METHOD_TARGET_URL)
    Observable<HttpResult<List<TreatMethodCommonBean>>> getTargetList(
        @Query("diseaseId") int diseaseId);

    /**
     * 治疗方法--靶向治疗
     */
    @GET(GET_MARKER_SELECT_LIST)
    Observable<HttpResult<List<AttentionMarkerIndexSelectBean>>> getMarkerSelectList(
        @Query("diseaseId") int diseaseId,
        @Query("isLandmark") boolean isLandmark, @Query("keywords")
            String keywords
    );

}
