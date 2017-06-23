package com.zhongmeban.attentionmodle.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.zhongmeban.R;
import com.zhongmeban.activity.ActHome;
import com.zhongmeban.base.BaseActivity;

/**
 * 异常提示首页弹框Activity
 * Created by Chengbin He on 2016/9/8.
 */
public class ActivityAttentionNoticePop extends BaseActivity implements View.OnClickListener {

    private TextView tvLater;//稍后查看
    private TextView tvCheck;//查看
    private TextView tvUnRead;
    private TextView tvNewMessage;
    private String patientId;
    private int unReadNotices;//未读信息条数

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attentionnotice_pop);

        Intent intent = getIntent();
        patientId = intent.getStringExtra("patientId");
        unReadNotices = intent.getIntExtra("unReadNotices",0);

        SharedPreferences sp = getSharedPreferences("userInfo",MODE_PRIVATE);
        int message = sp.getInt("newNotices",0);

        tvNewMessage = (TextView) findViewById(R.id.tv_new_message);
        tvNewMessage.setText(message + "条新提示");
        tvLater = (TextView) findViewById(R.id.tv_later);
        tvLater.setOnClickListener(this);
        tvCheck = (TextView) findViewById(R.id.tv_read);
        tvCheck.setOnClickListener(this);
        tvUnRead = (TextView) findViewById(R.id.tv_unread_message);
        tvUnRead.setText(unReadNotices+"条未读提示");

    }

    @Override
    protected void initTitle() {

    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.tv_later:
                intent = new Intent(this,ActHome.class);
                intent.putExtra("later",true);
                setResult(300,intent);
                finish();
                break;
            case R.id.tv_read:
                intent = new Intent(this,ActivityAttentionNoticesList.class);
                intent.putExtra("patientId",patientId);
                startActivity(intent);
                finish();
                break;
        }

    }

    @Override
    public void onBackPressed() {
    }
}
