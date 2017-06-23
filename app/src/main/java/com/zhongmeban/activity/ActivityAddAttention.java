package com.zhongmeban.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import com.zhongmeban.MyApplication;
import com.zhongmeban.R;
import com.zhongmeban.base.BaseActivity;
import com.zhongmeban.bean.postbody.CreatePatientBody;
import com.zhongmeban.fragment.FragmentAddAttentionStep1;
import com.zhongmeban.fragment.FragmentAddAttentionStep2;
import com.zhongmeban.utils.ImageLoaderZMB;
import com.zhongmeban.utils.ImageUtil;
import com.zhongmeban.utils.LayoutActivityTitle;
import com.zhongmeban.utils.genericity.ImageUrl;
import de.greenrobot.dao.attention.Patient;
import de.greenrobot.dao.attention.PatientDao;
import java.util.List;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;


/**
 * 新增关注
 * Created by Chengbin He on 2016/6/17.
 */
public class ActivityAddAttention extends BaseActivity {

    //    private TextView tvAddtion;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private FragmentAddAttentionStep1 fragmentAddAttentionStep1;
    private FragmentAddAttentionStep2 fragmentAddAttentionStep2;
    //    private FragmentAddAttentionStep3 fragmentAddAttentionStep3;
    private int step = 1;//当前步骤
    public static final int CROP_SMALL_PICTURE = 2;


    public boolean ISADD;//判断是新增还是编辑标记位
    public SharedPreferences userSp;
    public long birthday;//出生日期
    public long comfirmTime;//确诊日期
    public long diseaseId;//癌症ID
    public int gender;//性别 1.男 2.女
    public int relation;
    public String patientId;
    public String userId;
    public String token;
    public String diseaseName;
    public String iconDbUrl;
    public  Uri tempUri;//相机图片URL
    public Bitmap photo;
    public CreatePatientBody createPatientBody = new CreatePatientBody();
    public int witch = -1;//病情选择dialog 位置

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addattention);



        fragmentManager = getSupportFragmentManager();
        initData();
        initView();
        initTitle();
    }

    private void initData() {
        userSp = getSharedPreferences("userInfo",MODE_PRIVATE);
        userId = userSp.getString("userId","");
        token = userSp.getString("token","");
        patientId = userSp.getString("patientId","");

        Intent intent = getIntent();
        ISADD = intent.getBooleanExtra("ISADD",false);
//        photo = intent.getParcelableExtra("photo");

    }

    private void initView() {
//        tvAddtion = (TextView) findViewById(R.id.tv_addticon);
        fragmentAddAttentionStep1 = FragmentAddAttentionStep1.newInstance();
        fragmentAddAttentionStep2 = FragmentAddAttentionStep2.newInstance();
//        fragmentAddAttentionStep3 = FragmentAddAttentionStep3.newInstance();
        fragmentTransaction = fragmentManager.beginTransaction();
        if (ISADD){
            fragmentTransaction.add(R.id.fl_content, fragmentAddAttentionStep1);
            fragmentTransaction.commit();
        }else {
            getDBData(patientId);
        }

    }

    @Override
    protected void initTitle() {
        title = new LayoutActivityTitle(findViewById(R.id.layout_top));
        String titleName = "";
        if (ISADD){
            titleName = "创建患者信息";
        }else {
            titleName = "修改患者信息";
        }
        title.slideCentertext(titleName);
        title.backSlid(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (step) {
                    case 1:
                        finish();
                        break;
                    case 2:
                        replaceFragment(1);
                        break;
                }
            }
        });
    }
    public String uploadPicUrl(Bitmap bitmap) {
        // 上传至服务器
        // ... 可以在这里把Bitmap转换成file，然后得到file的url，做文件上传操作
        // 注意这里得到的图片已经是圆形图片了
        // bitmap是没有做个圆形处理的，但已经被裁剪了
        String imagePath = "";
         imagePath = ImageUtil.savePhoto(bitmap, Environment
                .getExternalStorageDirectory().getAbsolutePath(), String
                .valueOf(System.currentTimeMillis()));
        Log.i("hcb", "imagePath "+imagePath+"");
        return imagePath;
    }
    /***
     * 更换fragment
     *
     * @param step 1.fragmentAddAttentionStep1 2.fragmentAddAttentionStep2 3.fragmentAddAttentionStep3
     */
    public void replaceFragment(int step) {
        this.step = step;
        fragmentTransaction = fragmentManager.beginTransaction();
        switch (step) {
            case 1:
                fragmentTransaction.replace(R.id.fl_content, fragmentAddAttentionStep1);
                fragmentTransaction.commit();
                break;
            case 2:
                fragmentTransaction.replace(R.id.fl_content, fragmentAddAttentionStep2);
                fragmentTransaction.commit();
                break;
            default:
                fragmentTransaction.replace(R.id.fl_content, fragmentAddAttentionStep1);
                fragmentTransaction.commit();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        switch (step) {
            case 1:
                finish();
                break;
            case 2:
                replaceFragment(1);
                break;
//            case 3:
//                replaceFragment(2);
//                break;
            default:
                finish();
                break;
        }
    }

//    /**
//     * 裁剪图片方法实现
//     *
//     * @param uri
//     */
//    public void startPhotoZoom(Uri uri) {
//        if (uri == null) {
//            Log.i("tag", "The uri is not exist.");
//        }
//        tempUri = uri;
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType(uri, "image/*");
//        // 设置裁剪
//        intent.putExtra("crop", "true");
//        // aspectX aspectY 是宽高的比例
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//        // outputX outputY 是裁剪图片宽高
//        intent.putExtra("outputX", 260);
//        intent.putExtra("outputY", 260);
//        intent.putExtra("return-data", true);
//        startActivityForResult(intent, CROP_SMALL_PICTURE);
//    }
//
//
//    /**
//     * 保存裁剪之后的图片数据
//     *
//     */
//    public void setImageToView(Intent data, ImageView ivIcon) {
//        Bundle extras = data.getExtras();
//        if (extras != null) {
//            Bitmap photo = extras.getParcelable("data");
//            photo = ImageUtil.toRoundBitmap(photo, tempUri); // 这个时候的图片已经被处理成圆形的了
//            ivIcon.setImageBitmap(photo);
////            uploadPic(photo);
//        }
//    }

    /**
     * 网络请求前 初始化Body
     */
    public void initPatientBody(){
        createPatientBody.setUserId(userId);
        createPatientBody.setRelation(relation);
        createPatientBody.setDiseaseId(diseaseId);
        createPatientBody.setGender(gender);
        createPatientBody.setBirthday(birthday);
        createPatientBody.setComfirmTime(comfirmTime);
        patientId = (TextUtils.isEmpty(patientId)?null:patientId);
        createPatientBody.setId(patientId);
        createPatientBody.setAvatar(iconDbUrl);
    }

    /**
     * 获取数据库数据
     * @param patientId
     */
    private void getDBData(final String patientId){
        Observable.create(new Observable.OnSubscribe<Patient>() {
            @Override
            public void call(Subscriber<? super Patient> subscriber) {

                PatientDao patientDao = ((MyApplication)getApplication())
                        .getAttentionDaoSession().getPatientDao();
                List<Patient> patientList = patientDao.queryBuilder()
                        .where(PatientDao.Properties.PatientId.eq(patientId)).list();

                if (patientList.size() > 0) {
                    Patient patient = patientList.get(0);
                    relation = patient.getRelation();
                    comfirmTime = patient.getComfirmTime();
                    birthday = patient.getBirthday();
                    diseaseId = patient.getDiseaseId();
                    gender = patient.getGender();
                    diseaseName = patient.getDiseaseName();
                    iconDbUrl = patient.getAvatar();//获取头像URL
                    if (iconDbUrl!=null&&!TextUtils.isEmpty(iconDbUrl)){
                        String imageUrl = ImageUrl.BaseImageUrl+iconDbUrl;
                        if(ImageLoaderZMB.getInstance().loadBitMapList(ActivityAddAttention.this,imageUrl).size()>0){
                            Bitmap mPhoto = (Bitmap) ImageLoaderZMB.getInstance().loadBitMapList(ActivityAddAttention.this,imageUrl).get(0);
                            photo = ImageUtil.toRoundBitmap(mPhoto);
                        }
                    }
                    subscriber.onNext(patient);
                }

                subscriber.onCompleted();
            }
        }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Patient>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb", "getDBData onCompleted()");
                        fragmentTransaction.add(R.id.fl_content, fragmentAddAttentionStep1);
                        fragmentTransaction.commit();

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb", "getDBData onError()");
                        Log.i("hcb", "e" + e);
                    }

                    @Override
                    public void onNext(Patient patient) {
                        Log.i("hcb", "getDBData onNext()");
//                        relation = patient.getRelation();
//                        comfirmTime = patient.getComfirmTime();
//                        birthday = patient.getBirthday();
//                        diseaseId = patient.getDiseaseId();
//                        gender = patient.getGender();
//                        diseaseName = patient.getDiseaseName();
//                        iconDbUrl = patient.getAvatar();//获取头像URL
//                        if (iconDbUrl!=null&&!TextUtils.isEmpty(iconDbUrl)){
//                            String imageUrl = ImageUrl.BaseImageUrl+iconDbUrl;
//                            Bitmap mPhoto = (Bitmap) ImageLoaderZMB.getInstance().loadBitMapList(ActivityAddAttention.this,imageUrl).get(0);
//                            photo = ImageUtil.toRoundBitmap(mPhoto);
//                        }
                    }
                });
    }
}
