package com.zhongmeban.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongmeban.R;
import com.zhongmeban.base.BaseActivity;
import com.zhongmeban.bean.CreateOrDeleteBean;
import com.zhongmeban.bean.postbody.UpdateNickNameBody;
import com.zhongmeban.bean.postbody.UpdateUserNameBody;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.utils.LayoutActivityTitle;
import com.zhongmeban.utils.ToastUtil;
import com.zhongmeban.utils.genericity.SPInfo;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 更改用户姓名Activity
 * Created by HeCheng on 2016/11/6.
 */

public class ActivityChangeUserName extends BaseActivity implements View.OnClickListener{

    private Context mContext = ActivityChangeUserName.this;
    private ImageView ivClear;
    private String name;
    private EditText edName;
    private String token;
    private String userId;
    private String avatarUrl;
    private String nickName;
    private SharedPreferences userSp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_my);
        initTitle();
        initData();
        ivClear = (ImageView) findViewById(R.id.iv_search_clear);
        ivClear.setOnClickListener(this);

        edName = (EditText) findViewById(R.id.et_name);
        if (!TextUtils.isEmpty(nickName)){
            edName.setText(nickName);
            ivClear.setVisibility(View.VISIBLE);
        }else {
            ivClear.setVisibility(View.GONE);
        }
        edName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String inName = s.toString();
               if (!TextUtils.isEmpty(inName)){
                   ivClear.setVisibility(View.VISIBLE);
               }else {
                   ivClear.setVisibility(View.GONE);
               }
            }
        });
    }

    private void initData() {
        userSp = getSharedPreferences("userInfo",MODE_PRIVATE);
        userId = userSp.getString("userId","");
        token = userSp.getString("token","");
        avatarUrl = userSp.getString("userAvatar","");
        nickName = userSp.getString(SPInfo.UserKey_nickname,"");
    }

    @Override
    protected void initTitle() {
        title = new LayoutActivityTitle(findViewById(R.id.layout_top));
        title.slideCentertext("名字");
        title.slideRighttext("完成", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edName.getText().toString();
                if (!TextUtils.isEmpty(name)){
                    nickName = name;
                    updateNickName(name,token);
                }else {
                    ToastUtil.showText(mContext,"请输入名字");
                }

            }
        });
        title.backSlid(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
    }

    private void updateNickName(String name,String token){
        UpdateNickNameBody body = new UpdateNickNameBody(userId,name);
        HttpService.getHttpService().postUpdateNickName(body,token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CreateOrDeleteBean>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb","updateUserName onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb","updateUserName onError");
                        Log.i("hcb","e"+e);
                    }

                    @Override
                    public void onNext(CreateOrDeleteBean createOrDeleteBean) {
                        Log.i("hcb","updateUserName onNext");
                        if (createOrDeleteBean.getResult()){
                            userSp.edit().putString(SPInfo.UserKey_nickname,nickName).commit();
                            setResult(200);
                            finish();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        edName.setText("");
    }
}
