package com.zhongmeban.mymodle.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zhongmeban.R;
import com.zhongmeban.activity.ActivityAttenDoctor;
import com.zhongmeban.activity.ActivityCollected;
import com.zhongmeban.activity.ActivityQuestion;
import com.zhongmeban.activity.ActivitySetting;
import com.zhongmeban.base.BaseActivity;
import com.zhongmeban.utils.LayoutActivityTitle;

/**
 * @Description: 个人页面
 * Created by Chengbin He on 2016/3/21.
 */
public class ActivityMy extends BaseActivity {
    private LayoutActivityTitle title;
    private LinearLayout ll_mine;
    private RelativeLayout rl_atten;
    private RelativeLayout rl_myarticle;
    private RelativeLayout rl_mycollected;
    private RelativeLayout rl_setting;
    private RelativeLayout rl_question;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("hcb", "ActivityMy onCreate ");
        setContentView(R.layout.activity_my);

        initView();
        initTitle();
    }

    private void initView() {
        ll_mine = (LinearLayout) findViewById(R.id.ll_mine);
        ll_mine.setOnClickListener(onClickListener);
        rl_atten = (RelativeLayout) findViewById(R.id.rl_atten);
        rl_atten.setOnClickListener(onClickListener);
        rl_myarticle = (RelativeLayout) findViewById(R.id.rl_myarticle);
        rl_myarticle.setOnClickListener(onClickListener);
        rl_mycollected = (RelativeLayout) findViewById(R.id.rl_mycollected);
        rl_mycollected.setOnClickListener(onClickListener);
        rl_setting = (RelativeLayout) findViewById(R.id.rl_setting);
        rl_setting.setOnClickListener(onClickListener);
        rl_question = (RelativeLayout) findViewById(R.id.rl_question);
        rl_question.setOnClickListener(onClickListener);
    }

    protected void initTitle() {
        title = new LayoutActivityTitle(findViewById(R.id.layout_top));
        title.slideCentertext("我");
        title.backSlid(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            switch (v.getId()) {
                case R.id.ll_mine://登陆
                    Log.i("hcb", "R.id.ll_mine: click");

                    break;

                case R.id.rl_atten://关注的医生
                    intent.setClass(ActivityMy.this, ActivityAttenDoctor.class);
                    startActivity(intent);
                    Log.i("hcb","R.id.rl_atten: click");
                    break;

                case R.id.rl_myarticle://我的文章
                    intent.setClass(ActivityMy.this, ActivityMyArticle.class);
                    startActivity(intent);
                    Log.i("hcb","R.id.rl_myarticle: click");
                    break;

                case R.id.rl_question://我的提问
                    intent.setClass(ActivityMy.this, ActivityQuestion.class);
                    startActivity(intent);
                    Log.i("hcb","R.id.rl_mycollected: click");
                    break;

                case R.id.rl_mycollected://我的收藏
                    Log.i("hcb","R.id.rl_mycollected: click");
                    intent.setClass(ActivityMy.this, ActivityCollected.class);
                    startActivity(intent);
                    break;

                case R.id.rl_setting://应用设置
                    Log.i("hcb","R.id.rl_setting: click");
                    intent.setClass(ActivityMy.this, ActivitySetting.class);
                    startActivity(intent);
                    break;
            }

        }
    };
}
