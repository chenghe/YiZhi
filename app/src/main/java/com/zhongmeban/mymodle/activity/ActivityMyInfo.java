package com.zhongmeban.mymodle.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.zhongmeban.R;
import com.zhongmeban.activity.ActivityChangeUserName;
import com.zhongmeban.base.BaseActivityToolBar;
import com.zhongmeban.bean.CreateOrDeleteBean;
import com.zhongmeban.bean.postbody.UpdateUserAvatorBody;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.utils.CircleTransform;
import com.zhongmeban.utils.ImageLoaderZMB;
import com.zhongmeban.utils.ImageUtil;
import com.zhongmeban.utils.ToastUtil;
import com.zhongmeban.utils.genericity.ImageUrl;
import com.zhongmeban.utils.genericity.SPInfo;
import java.io.File;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 我的信息Activity
 * Created by HeCheng on 2016/10/19.
 */

public class ActivityMyInfo extends BaseActivityToolBar implements View.OnClickListener {

    private Context mContext = ActivityMyInfo.this;
    private RelativeLayout rlIcon;
    private RelativeLayout rlName;
    private RelativeLayout rlNum;
    private ImageView ivMyIcon;
    private TextView tvName;//用户姓名
    private TextView tvPhone;// 用户手机号
    private Bitmap photo;
    private String token;
    private String userId;
    private String avatarUrl;
    private String nickname;
    private String phoneNum;
    private SharedPreferences userSp;

    public Uri tempUri;//相机图片URL
    protected static final int CHOOSE_PICTURE = 0;//从相册中选择
    protected static final int TAKE_PICTURE = 1;//相机拍摄
    protected static final int CROP_SMALL_PICTURE = 2;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_info;
    }

    @Override
    protected void initToolbar() {
        initToolbarCustom("我的信息", "");
    }

    @Override
    protected void initView() {
        initData();
        tvName = (TextView) findViewById(R.id.tv_name);
        tvPhone = (TextView) findViewById(R.id.tv_phone);
        tvName.setText(nickname);
        tvPhone.setText(phoneNum);
        rlIcon = (RelativeLayout) findViewById(R.id.rl_icon);
        rlIcon.setOnClickListener(this);
        rlName = (RelativeLayout) findViewById(R.id.rl_name);
        rlName.setOnClickListener(this);
        rlNum = (RelativeLayout) findViewById(R.id.rl_num);
        ivMyIcon = (ImageView) findViewById(R.id.my_icon);
        if (!TextUtils.isEmpty(avatarUrl)){
            String imageUrl = ImageUrl.BaseImageUrl + avatarUrl;
            Log.i("hcb","imageUrl"+imageUrl);
            ImageLoaderZMB.getInstance().loadTransImage(mContext,imageUrl,R.drawable.attention_icon,new CircleTransform(),ivMyIcon);

        }
    }

    private void initData(){
        userSp = getSharedPreferences("userInfo",MODE_PRIVATE);
        userId = userSp.getString("userId","");
        token = userSp.getString("token","");
        avatarUrl = userSp.getString("userAvatar","");
        nickname = userSp.getString(SPInfo.UserKey_nickname,"");
        phoneNum = userSp.getString("phone","");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_icon:
                RxPermissions.getInstance(this).request(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(new Action1<Boolean>() {
                            @Override
                            public void call(Boolean aBoolean) {
                                if (aBoolean){
                                    showNormalDialog(mContext, iconPositiveCallback, iconNegativeCallback, "上传头像",
                                            "从相册中选择", "相机拍摄");
                                }else {
                                    showMissingPermissionDialog();
                                }
                            }
                        });

                break;
            case R.id.rl_name:
                Intent intent = new Intent(mContext,ActivityChangeUserName.class);
                startActivityForResult(intent,1);
                break;
            case R.id.rl_num:
                break;
        }
    }


    //图片选择，从相册中选择
    MaterialDialog.SingleButtonCallback iconPositiveCallback = new MaterialDialog.SingleButtonCallback() {

        @Override
        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            Intent openAlbumIntent = new Intent(Intent.ACTION_PICK);
            openAlbumIntent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
        }
    };

    //图片选择，相机拍摄
    MaterialDialog.SingleButtonCallback iconNegativeCallback = new MaterialDialog.SingleButtonCallback() {

        @Override
        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            Intent openCameraIntent = new Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE);
            tempUri = Uri.fromFile(new File(Environment
                    .getExternalStorageDirectory(), "image.jpg"));
            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
            startActivityForResult(openCameraIntent, TAKE_PICTURE);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case TAKE_PICTURE://相机拍摄
                    startPhotoZoom(tempUri); // 开始对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE://从相册选择
                    startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                        String url = uploadPicUrl(photo);
                        uploadIcon(url);
                    }
                    break;
            }
        }else if (resultCode == 200){
            //改名回调
            nickname = userSp.getString(SPInfo.UserKey_nickname,"");
            tvName.setText(nickname);
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
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

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    protected void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
        }
        tempUri = uri;
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
            photo = extras.getParcelable("data");
            photo = ImageUtil.toRoundBitmap(photo, tempUri); // 这个时候的图片已经被处理成圆形的了
            ivMyIcon.setImageBitmap(photo);
//            uploadPic(photo);
        }
    }

    /**
     * 上传头像
     */
    private void uploadIcon(String iconUrl) {
        File file = new File(iconUrl);
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("11", "11.png", fileBody);
        HttpService.getHttpService().postUploadPatientIcon(ImageUrl.UserKey,part)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Subscriber<CreateOrDeleteBean>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb", " uploadIcon onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb", " uploadIcon onError");
                        Log.i("hcb", " e" + e);
                        ToastUtil.showText(mContext,"头像上传失败");
                    }

                    @Override
                    public void onNext(CreateOrDeleteBean createOrDeleteBean) {
                        Log.i("hcb", " uploadIcon onNext");
                        if (createOrDeleteBean.getResult()) {

                            avatarUrl = createOrDeleteBean.getData();
                            UpdateUserAvator(avatarUrl,token);
                        }
                    }
                });
    }

    /**
     * 修改用户头像
     */
    private void UpdateUserAvator(final String avatarUrl, String token){
        UpdateUserAvatorBody body = new UpdateUserAvatorBody(userId,avatarUrl);
        HttpService.getHttpService().postUpdateUserAvatar(body,token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CreateOrDeleteBean>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb","UpdateUserAvator onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb","UpdateUserAvator onError");
                        Log.i("hcb","e" + e);
                        ToastUtil.showText(mContext,"头像上传失败");
                    }

                    @Override
                    public void onNext(CreateOrDeleteBean createOrDeleteBean) {
                        Log.i("hcb","UpdateUserAvator onNext");
                        ToastUtil.showText(mContext,"头像上传成功");
                        userSp.edit().putString("userAvatar",avatarUrl).commit();

                    }
                });
    }
}