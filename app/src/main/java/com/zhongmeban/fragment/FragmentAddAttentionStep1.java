package com.zhongmeban.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.zhongmeban.MyApplication;
import com.zhongmeban.R;
import com.zhongmeban.activity.ActivityAddAttention;
import com.zhongmeban.base.BaseFragment;
import com.zhongmeban.utils.CircleTransform;
import com.zhongmeban.utils.ImageLoaderZMB;
import com.zhongmeban.utils.ImageUtil;
import com.zhongmeban.utils.ToastUtil;
import com.zhongmeban.utils.genericity.ImageUrl;
import com.zhongmeban.view.DatePicker.OnDatePickedListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.Tumor;
import de.greenrobot.dao.TumorDao;
import rx.functions.Action1;

/**
 * 新增患者Fragment第一步
 * Created by Chengbin He on 2016/6/20.
 */
public class FragmentAddAttentionStep1 extends BaseFragment implements View.OnClickListener{

    public static final int MALE = 1;
    public static final int FEMALE = 2;
//    protected static Uri tempUri;//相机图片URL
    protected static final int CHOOSE_PICTURE = 0;//从相册中选择
    protected static final int TAKE_PICTURE = 1;//相机拍摄
    protected static final int CROP_SMALL_PICTURE = 2;


    private Button button;
    private ActivityAddAttention parentActivity;
    private RelativeLayout rlMale;
    private RelativeLayout rlFemale;
    private RelativeLayout rlBornDate;//出生日期
    private RelativeLayout rlDiagnosisDate;//确诊日期
    private RelativeLayout rlType;//癌症类型
    private ImageView ivMale;
    private ImageView ivFemale;
    private ImageView ivIcon;//头像
    private TextView tvMale;
    private TextView tvFemale;
    private TextView tvBornDate;
    private TextView tvDiagnosisDate;
    private TextView tvType;

    private List<Tumor> dbList = new ArrayList<Tumor>();//肿瘤List


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentActivity = (ActivityAddAttention) context;
    }

    public static  FragmentAddAttentionStep1 newInstance() {
        
        Bundle args = new Bundle();
        
        FragmentAddAttentionStep1 fragment = new FragmentAddAttentionStep1();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_attention_step1,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDBTumot();

        ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
        ivIcon.setOnClickListener(this);
        if (parentActivity.photo!=null){
            ivIcon.setImageBitmap(parentActivity.photo);
        }else if (!TextUtils.isEmpty(parentActivity.iconDbUrl)){
            ImageLoaderZMB.getInstance().loadTransImage(parentActivity,
                    ImageUrl.BaseImageUrl+parentActivity.iconDbUrl,
                    R.drawable.add_icon,new CircleTransform(),ivIcon);
        }

        button = (Button) view.findViewById(R.id.bt_next);
        button.setOnClickListener(this);
        if(parentActivity.gender>0&&parentActivity.diseaseId>0&&parentActivity.birthday!=0
                &&parentActivity.comfirmTime>0){
            button.setEnabled(true);
            button.setTextColor(getResources().getColor(R.color.white));
        }else {
            button.setEnabled(false);
            button.setTextColor(getResources().getColor(R.color.content_two));
        }

        rlMale = (RelativeLayout) view.findViewById(R.id.rl_male);
        rlMale.setOnClickListener(this);
        rlFemale = (RelativeLayout) view.findViewById(R.id.rl_female);
        rlFemale.setOnClickListener(this);
        ivMale = (ImageView) view.findViewById(R.id.iv_male);
        ivFemale = (ImageView) view.findViewById(R.id.iv_female);
        tvMale = (TextView) view.findViewById(R.id.tv_male);
        tvFemale = (TextView) view.findViewById(R.id.tv_female);

        rlBornDate = (RelativeLayout) view.findViewById(R.id.rl_born_date);
        rlBornDate.setOnClickListener(this);
        rlDiagnosisDate = (RelativeLayout) view.findViewById(R.id.rl_diagnosis_date);
        rlDiagnosisDate.setOnClickListener(this);
        rlType = (RelativeLayout) view.findViewById(R.id.rl_type);
        rlType.setOnClickListener(this);
        tvBornDate = (TextView) view.findViewById(R.id.tv_born_date);
        if (parentActivity.birthday!=0){
            tvBornDate.setText(changeDateToString(parentActivity.birthday));
        }
        tvDiagnosisDate = (TextView) view.findViewById(R.id.tv_diagnosis_date);
        if (parentActivity.comfirmTime>0){
            tvDiagnosisDate.setText(changeDateToString(parentActivity.comfirmTime));
        }
        tvType = (TextView) view.findViewById(R.id.tv_type);
        if (parentActivity.diseaseId>0){
            tvType.setText(parentActivity.diseaseName);
        }

        chooseSex(parentActivity.gender);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_next:
                parentActivity.replaceFragment(2);
                if(parentActivity.gender>0&&parentActivity.diseaseId>0){
                    button.setEnabled(true);
                    button.setTextColor(getResources().getColor(R.color.white));
                }else {
                    button.setEnabled(false);
                    button.setTextColor(getResources().getColor(R.color.content_two));
                }
                break;
            case R.id.rl_male:
                chooseSex(MALE);
                parentActivity.gender = 1;
                parentActivity.relation = 0;
                if(parentActivity.gender>0&&parentActivity.diseaseId>0&&parentActivity.birthday!=0
                        &&parentActivity.comfirmTime>0){
                    button.setEnabled(true);
                    button.setTextColor(getResources().getColor(R.color.white));
                }else {
                    button.setEnabled(false);
                    button.setTextColor(getResources().getColor(R.color.content_two));
                }
                break;
            case R.id.rl_female:
                chooseSex(FEMALE);
                parentActivity.gender = 2;
                parentActivity.relation = 0;
                if(parentActivity.gender>0&&parentActivity.diseaseId>0&&parentActivity.birthday!=0
                        &&parentActivity.comfirmTime>0){
                    button.setEnabled(true);
                    button.setTextColor(getResources().getColor(R.color.white));
                }else {
                    button.setEnabled(false);
                    button.setTextColor(getResources().getColor(R.color.content_two));
                }
                break;
            case R.id.rl_born_date://出生日期
                showDatePicker(parentActivity, new OnDatePickedListener() {
                    @Override
                    public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
                        parentActivity.birthday = changeDateToLong(dateDesc);
                        tvBornDate.setText(year+"年"+month+"月"+day+"日");
                        if(parentActivity.gender>0&&parentActivity.diseaseId>0&&parentActivity.birthday!=0
                                &&parentActivity.comfirmTime>0){
                            button.setEnabled(true);
                            button.setTextColor(getResources().getColor(R.color.white));
                        }else {
                            button.setEnabled(false);
                            button.setTextColor(getResources().getColor(R.color.content_two));
                        }
                    }
                },getTodayData(),1920,2018);
                break;
            case R.id.rl_diagnosis_date://确诊日期
                showDatePicker(parentActivity, new OnDatePickedListener() {
                    @Override
                    public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
                        if (changeDateToLong(dateDesc)<=changeDateToLong(getTodayData())){
                            parentActivity.comfirmTime = changeDateToLong(dateDesc);
                            tvDiagnosisDate.setText(year+"年"+month+"月"+day+"日");
                            if(parentActivity.gender>0&&parentActivity.diseaseId>0&&parentActivity.birthday!=0
                                    &&parentActivity.comfirmTime>0){
                                button.setEnabled(true);
                                button.setTextColor(getResources().getColor(R.color.white));
                            }else {
                                button.setEnabled(false);
                                button.setTextColor(getResources().getColor(R.color.content_two));
                            }
                        }else {
                            ToastUtil.showText(parentActivity,"确诊日期不能大于今天");
                        }

                    }
                },getTodayData());
                break;
            case R.id.rl_type:
                showDiseaseDialog();
                break;
            case R.id.iv_icon://添加头像
                RxPermissions.getInstance(parentActivity).request(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(new Action1<Boolean>() {
                            @Override
                            public void call(Boolean aBoolean) {
                                if (aBoolean){
                                    showNormalDialog(parentActivity,iconPositiveCallback,iconNegativeCallback,"上传头像",
                                            "从相册中选择","相机拍摄");
                                }else {
                                    parentActivity.showMissingPermissionDialog();
                                }
                            }
                        });


                break;
        }
    }

    //图片选择，从相册中选择
    MaterialDialog.SingleButtonCallback iconPositiveCallback = new MaterialDialog.SingleButtonCallback(){

        @Override
        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            Intent openAlbumIntent = new Intent(Intent.ACTION_PICK);
            openAlbumIntent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
        }
    };

    //图片选择，相机拍摄
    MaterialDialog.SingleButtonCallback iconNegativeCallback = new MaterialDialog.SingleButtonCallback(){

        @Override
        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            Intent openCameraIntent = new Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE);
            parentActivity.tempUri = Uri.fromFile(new File(Environment
                    .getExternalStorageDirectory(), "image.jpg"));
            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, parentActivity.tempUri);
            startActivityForResult(openCameraIntent, TAKE_PICTURE);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case TAKE_PICTURE://相机拍摄
                    startPhotoZoom(parentActivity.tempUri); // 开始对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE://从相册选择
                    startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }
    }


    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    protected void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
        }
        parentActivity.tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 260);
        intent.putExtra("outputY", 260);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }


    /**
     * 保存裁剪之后的图片数据
     *
     */
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            parentActivity.photo = ImageUtil.toRoundBitmap(photo, parentActivity.tempUri); // 这个时候的图片已经被处理成圆形的了
            ivIcon.setImageBitmap(parentActivity.photo);
//            uploadPic(photo);
        }
    }

    private void uploadPic(Bitmap bitmap) {
        // 上传至服务器
        // ... 可以在这里把Bitmap转换成file，然后得到file的url，做文件上传操作
        // 注意这里得到的图片已经是圆形图片了
        // bitmap是没有做个圆形处理的，但已经被裁剪了

        String imagePath = ImageUtil.savePhoto(bitmap, Environment
                .getExternalStorageDirectory().getAbsolutePath(), String
                .valueOf(System.currentTimeMillis()));
        Log.e("imagePath", imagePath+"");
        if(imagePath != null){

        }
    }

    @SuppressLint("NewApi")
    private void chooseSex(int sex){
        switch (sex){
            case MALE:
                rlMale.setBackground(parentActivity.getResources().getDrawable(R.drawable.ellipse_green_bg));
                ivMale.setImageResource(R.drawable.man_selected);
                tvMale.setTextColor(parentActivity.getResources().getColor(R.color.white));
                rlFemale.setBackground(parentActivity.getResources().getDrawable(R.drawable.marker_box_bg));
                ivFemale.setImageResource(R.drawable.woman_normal);
                tvFemale.setTextColor(parentActivity.getResources().getColor(R.color.content_two));
                break;

            case FEMALE:
                rlMale.setBackground(parentActivity.getResources().getDrawable(R.drawable.marker_box_bg));
                ivMale.setImageResource(R.drawable.man_normal);
                tvMale.setTextColor(parentActivity.getResources().getColor(R.color.content_two));
                rlFemale.setBackground(parentActivity.getResources().getDrawable(R.drawable.ellipse_green_bg));
                ivFemale.setImageResource(R.drawable.woman_seleted);
                tvFemale.setTextColor(parentActivity.getResources().getColor(R.color.white));
                break;

            default:
                rlMale.setBackground(parentActivity.getResources().getDrawable(R.drawable.marker_box_bg));
                ivMale.setImageResource(R.drawable.man_normal);
                tvMale.setTextColor(parentActivity.getResources().getColor(R.color.content_two));
                rlFemale.setBackground(parentActivity.getResources().getDrawable(R.drawable.marker_box_bg));
                ivFemale.setImageResource(R.drawable.woman_normal);
                tvFemale.setTextColor(parentActivity.getResources().getColor(R.color.content_two));
                break;
        }
    }

    private void showDiseaseDialog(){
        new MaterialDialog.Builder(parentActivity)
                .title("请选择癌症类型")
                .items(dbList)
                .itemsCallbackSingleChoice(parentActivity.witch, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        if (which>-1){
                            parentActivity.witch = which;
                            parentActivity.diseaseId = dbList.get(which).getId();
                            parentActivity.diseaseName = (String) text;
                            tvType.setText(text);
                            if(parentActivity.gender>0&&parentActivity.diseaseId>0&&parentActivity.birthday!=0
                                    &&parentActivity.comfirmTime>0){
                                button.setEnabled(true);
                                button.setTextColor(getResources().getColor(R.color.white));
                            }else {
                                button.setEnabled(false);
                                button.setTextColor(getResources().getColor(R.color.content_two));
                            }
                            return true;
                        }
                        return false;
                    }
                })
                .positiveText("确定")
                .show();
    }

    /**
     * 获取本地癌症数据库数据
     */
    private void getDBTumot(){

        TumorDao tumorDao = ((MyApplication)parentActivity.getApplication()).getDaoSession()
                .getTumorDao();
         dbList = tumorDao.queryBuilder().where(TumorDao.Properties.IsActive.eq(true)).list();
    }

}
