package com.zhongmeban.mymodle.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmeban.R;
import com.zhongmeban.activity.ActHome;
import com.zhongmeban.activity.ActivityCardLogin;
import com.zhongmeban.treatmodle.activity.ActivityMyCollectionInfo;
import com.zhongmeban.treatmodle.activity.ActivityMyDoctor;
import com.zhongmeban.mymodle.activity.ActivityMyInfo;
import com.zhongmeban.mymodle.activity.ActivityMyMedicine;
import com.zhongmeban.mymodle.activity.ActivityMyTreatment;
import com.zhongmeban.activity.ActivitySetting;
import com.zhongmeban.base.BaseFragment;
import com.zhongmeban.utils.CircleTransform;
import com.zhongmeban.utils.ImageLoaderZMB;
import com.zhongmeban.utils.genericity.ImageUrl;
import com.zhongmeban.utils.genericity.SPInfo;

public class FragmentMy extends BaseFragment {

//    private RelativeLayout rl_address;//地址管理
//    private RelativeLayout rl_mycommidity;//我的收藏的商品
//    private RelativeLayout rl_mycollected;//我收藏的内容
//    private RelativeLayout rl_shop_car;//购物车
//    private RelativeLayout rlMyOrder;//我的订单

    private RelativeLayout rlMyDoc;//关注医生
    private RelativeLayout rlMyMedicine;//收藏药品
    private RelativeLayout rlMyCollection;//关注医生
    private RelativeLayout rlMyTips;//关注医生
    private RelativeLayout rlSetting;//设置
    private RelativeLayout ll_mine;
    private ImageView ivUserIcon;
    private TextView tvName;
    private TextView tvPhone;
    private ActHome mContext;
    private String avatarUrl;
    private String nickname;
    private String phoneNum;
    private SharedPreferences userSp;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (ActHome)context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initData();
        initView(view);
    }


    private void initView(View view) {
        ll_mine = (RelativeLayout) view.findViewById(R.id.ll_mine);
        ll_mine.setOnClickListener(onClickListener);
        rlMyDoc = (RelativeLayout) view.findViewById(R.id.rl_doc);
        rlMyDoc.setOnClickListener(onClickListener);
        rlMyMedicine = (RelativeLayout) view.findViewById(R.id.rl_medicine);
        rlMyMedicine.setOnClickListener(onClickListener);
        rlMyCollection = (RelativeLayout) view.findViewById(R.id.rl_other);
        rlMyCollection.setOnClickListener(onClickListener);
        rlSetting = (RelativeLayout) view.findViewById(R.id.rl_setting);
        rlSetting.setOnClickListener(onClickListener);
        rlMyTips = (RelativeLayout) view.findViewById(R.id.rl_tips);
        rlMyTips.setOnClickListener(onClickListener);

        tvName = (TextView) view.findViewById(R.id.tv_name);
        if (TextUtils.isEmpty(nickname)){
            tvName.setText("注册/登录");
        }else {
            tvName.setText(nickname);
        }

        tvPhone = (TextView) view.findViewById(R.id.tv_phone);
        tvPhone.setText(phoneNum);
        ivUserIcon = (ImageView) view.findViewById(R.id.iv_user_icon);
        if (!TextUtils.isEmpty(avatarUrl)){
            String imageUrl = ImageUrl.BaseImageUrl + avatarUrl;
            Log.i("hcb","imageUrl"+imageUrl);
            ImageLoaderZMB.getInstance().loadTransImage(mContext,imageUrl,R.drawable.attention_icon,new CircleTransform(),ivUserIcon);
        }


    }

    private void initData(){
        userSp = mContext.getSharedPreferences("userInfo",Context.MODE_PRIVATE);
        avatarUrl = userSp.getString("userAvatar","");
        nickname = userSp.getString(SPInfo.UserKey_nickname,"");
        phoneNum = userSp.getString("phone","");
    }

    @Override
    public void onResume() {
        super.onResume();
        userSp = mContext.getSharedPreferences("userInfo",Context.MODE_PRIVATE);
        avatarUrl = userSp.getString("userAvatar","");
        nickname = userSp.getString(SPInfo.UserKey_nickname,"");
        phoneNum = userSp.getString("phone","");
        if (TextUtils.isEmpty(nickname)){
            tvName.setText("注册/登录");
        }else {
            tvName.setText(nickname);
        }

        if (!TextUtils.isEmpty(avatarUrl)){
            String imageUrl = ImageUrl.BaseImageUrl + avatarUrl;
            Log.i("hcb","imageUrl"+imageUrl);
            ImageLoaderZMB.getInstance().loadTransImage(mContext,imageUrl,R.drawable.attention_icon,new CircleTransform(),ivUserIcon);
        }
        tvPhone.setText(phoneNum);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            switch (v.getId()) {
                case R.id.ll_mine://我的信息
                    Log.i("hcb", "R.id.ll_mine: click");
                    if (TextUtils.isEmpty(phoneNum)){
                        //未登录需要重新登录
                        intent.setClass(mContext, ActivityCardLogin.class);
                    }else {
                        intent.setClass(mContext, ActivityMyInfo.class);
                    }
                    startActivityForResult(intent,1);
                    break;

                case R.id.rl_doc://关注的医生
                    Log.i("hcb","R.id.rl_mycollected: click");
                    if (TextUtils.isEmpty(phoneNum)){
                        //未登录需要重新登录
                        intent.setClass(mContext, ActivityCardLogin.class);
                        startActivityForResult(intent,1);
                    }else {
                        intent.setClass(mContext, ActivityMyDoctor.class);
                        startActivity(intent);
                    }

                    break;

                case R.id.rl_medicine://收藏的药品
                    Log.i("hcb","R.id.rl_setting: click");
                    if (TextUtils.isEmpty(phoneNum)){
                        //未登录需要重新登录
                        intent.setClass(mContext, ActivityCardLogin.class);
                        startActivityForResult(intent,1);
                    }else {
                        intent.setClass(mContext, ActivityMyMedicine.class);
                        startActivity(intent);
                    }

                    break;

                case R.id.rl_other://收藏治疗方式
                    if (TextUtils.isEmpty(phoneNum)){
                        //未登录需要重新登录
                        intent.setClass(mContext, ActivityCardLogin.class);
                        startActivityForResult(intent,1);
                    }else {
                        intent.setClass(mContext, ActivityMyTreatment.class);
                        startActivity(intent);
                    }
                    break;

                case R.id.rl_tips://收藏的内容
                    if (TextUtils.isEmpty(phoneNum)){
                        //未登录需要重新登录
                        intent.setClass(mContext, ActivityCardLogin.class);
                        startActivityForResult(intent,1);
                    }else {
                        intent.setClass(mContext, ActivityMyCollectionInfo.class);
                        startActivity(intent);
                    }
                    break;

                case R.id.rl_setting://应用设置

                    intent.setClass(mContext, ActivitySetting.class);
                    startActivityForResult(intent,1);
                    break;
            }

        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 200){
            //是否为退出
            boolean loginout = data.getBooleanExtra("isLoginOut",false);
            if (loginout){
                //成功退出
                avatarUrl = userSp.getString("userAvatar","");
                nickname = userSp.getString(SPInfo.UserKey_nickname,"");
                phoneNum = userSp.getString("phone","");
                ivUserIcon.setImageResource(R.drawable.attention_icon);
                tvName.setText("注册/登录");
                tvPhone.setText("");
                mContext.changeUser = true;
            }
        }else if (resultCode == Activity.RESULT_OK){
            //更改用户信息返回
            avatarUrl = userSp.getString("userAvatar","");
            nickname = userSp.getString("nickname","");
            phoneNum = userSp.getString("phone","");
            if (!TextUtils.isEmpty(avatarUrl)){
                String imageUrl = ImageUrl.BaseImageUrl + avatarUrl;
                Log.i("hcb","imageUrl"+imageUrl);
                ImageLoaderZMB.getInstance().loadTransImage(mContext,imageUrl,R.drawable.attention_icon,new CircleTransform(),ivUserIcon);
            }
            tvName.setText(nickname);
            tvPhone.setText(phoneNum);
        }
    }
}
