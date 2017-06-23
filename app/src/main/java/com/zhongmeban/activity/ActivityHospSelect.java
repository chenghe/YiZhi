package com.zhongmeban.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmeban.R;
import com.zhongmeban.base.BaseActivity;
import com.zhongmeban.utils.LayoutActivityTitle;

/**
 * 医院(医生)筛选Activity
 * Created by Chengbin He on 2016/5/23.
 */
public class ActivityHospSelect extends BaseActivity {

    private RelativeLayout itemCity;//城市item
    private RelativeLayout itemLevel;//医院等级item
    private RelativeLayout itemCancer;//肿瘤类型item
    private RelativeLayout itemDoctLevel;//医生类别item
    private RelativeLayout itemIsOperation;//是否可手术item
    private TextView tvLevelSelect;
    private TextView tvCitySelect;
    private TextView tvDoctLevel;
    private TextView tvIsOperation;
    private TextView tvCancer;
    private Activity mContxt = ActivityHospSelect.this;
    private Boolean DOCTSELECT = false;//医生筛选标记位
    private String level = "";
    private String levelName = "";
    private String titleName = "";
    private String cityName = "";
    private String cityId = "";
    private String cancerName = "";
    private String diseaseId ="";
    private String doctLevelName = "";
    private String doctLevel = "";
    private String operationLevelName = "";
    private String operationLevel = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hosp_select);

        Intent dataIntent = getIntent();
        DOCTSELECT = dataIntent.getBooleanExtra("doctselect",false);
        levelName = dataIntent.getStringExtra("levelName");
        cityName = dataIntent.getStringExtra("cityName");
        cancerName = dataIntent.getStringExtra("cancerName");
        doctLevelName = dataIntent.getStringExtra("doctLevelName");
        operationLevelName = dataIntent.getStringExtra("operationLevelName");

        initView();
        initTitle();
    }

    private void initView() {
        itemCity = (RelativeLayout) findViewById(R.id.rl_item1);
        itemCity.setOnClickListener(onClickListener);
        itemLevel = (RelativeLayout) findViewById(R.id.rl_item2);
        itemLevel.setOnClickListener(onClickListener);
        tvLevelSelect = (TextView) findViewById(R.id.tv_level_select);
        tvLevelSelect.setText(levelName);
        tvCitySelect = (TextView) findViewById(R.id.tv_city_select);
        tvCitySelect.setText(cityName);

        if (DOCTSELECT){
            titleName = "筛选医生";
            itemCancer = (RelativeLayout) findViewById(R.id.rl_item3);
            itemDoctLevel = (RelativeLayout) findViewById(R.id.rl_item4);
            itemIsOperation = (RelativeLayout) findViewById(R.id.rl_item5);
            itemCancer.setVisibility(View.VISIBLE);
            itemDoctLevel.setVisibility(View.VISIBLE);
            itemIsOperation.setVisibility(View.VISIBLE);
            itemCancer.setOnClickListener(onClickListener);
            itemDoctLevel.setOnClickListener(onClickListener);
            itemIsOperation.setOnClickListener(onClickListener);
            tvCancer = (TextView) findViewById(R.id.tv_cancer_select);
            tvCancer.setText(cancerName);
            tvDoctLevel = (TextView) findViewById(R.id.tv_doctlevel_select);
            tvDoctLevel.setText(doctLevelName);
            tvIsOperation = (TextView) findViewById(R.id.tv_isoperation_select);
            tvIsOperation.setText(operationLevelName);
        }else {
            titleName = "筛选医院";
        }

    }

    /**
     * 初始化Title
     */
    @Override
    protected void initTitle() {
        title = new LayoutActivityTitle(findViewById(R.id.layout_top));
        title.slideCentertext(titleName);

        title.backSlid(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ivTitleBtnback://back 图标
                    finish();
                    break;

                case R.id.right_button: //title右侧Button
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("level",level);
                    resultIntent.putExtra("levelName",levelName);
                    resultIntent.putExtra("cityId",cityId);
                    resultIntent.putExtra("cityName",cityName);
                    if (DOCTSELECT){
                        resultIntent.putExtra("cancerName",cancerName);
                        resultIntent.putExtra("diseaseId",diseaseId);
                        resultIntent.putExtra("doctLevelName",doctLevelName);
                        resultIntent.putExtra("doctLevel",doctLevel);
                        resultIntent.putExtra("operationLevelName",operationLevelName);
                        resultIntent.putExtra("operationLevel",operationLevel);
                        mContxt.setResult(200,resultIntent);//医院筛选

                    }else{
                        mContxt.setResult(100,resultIntent);//医院筛选
                    }

                    mContxt.finish();
                    break;

                case R.id.rl_item1://治疗城市筛选
                    Intent cityIntent = new Intent(mContxt,ActivityCitySelect.class);
                    startActivityForResult(cityIntent,1);
                    break;

                case R.id.rl_item2://医院等级筛选
                    Intent levelIntent = new Intent(mContxt,ActivityHospLevel.class);
                    startActivityForResult(levelIntent,1);
                    break;
                case R.id.rl_item3://肿瘤类型
                    Intent cancerIntent = new Intent(mContxt,ActivityCancerSelect.class);
                    startActivityForResult(cancerIntent,1);
                    break;
                case R.id.rl_item4://医生类别
                    Intent doctlevelIntent = new Intent(mContxt,ActivityDoctLevelSelect.class);
                    startActivityForResult(doctlevelIntent,1);
                    break;
                case R.id.rl_item5://是否可手术
                    Intent operationIntent = new Intent(mContxt,ActivityOperationSelect.class);
                    startActivityForResult(operationIntent,1);
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode){
            case 100://城市筛选
                title.slideRighttext("确定", onClickListener);
                cityId = data.getStringExtra("cityId");
                cityName = data.getStringExtra("cityName");
                tvCitySelect.setText(cityName);
                break;

            case 200://医院等级筛选
                title.slideRighttext("确定", onClickListener);
                level = data.getStringExtra("resultLevel");
                levelName = data.getStringExtra("resultLevelName");
                tvLevelSelect.setText(levelName);
                break;

            case 300://肿瘤类型
                title.slideRighttext("确定", onClickListener);
                cancerName = data.getStringExtra("cancerName");
                diseaseId = data.getStringExtra("diseaseId");
                tvCancer.setText(cancerName);
                break;

            case 400://医生等级筛选
                title.slideRighttext("确定", onClickListener);
                doctLevelName = data.getStringExtra("doctLevelName");
                doctLevel = data.getStringExtra("doctLevel");
                tvDoctLevel.setText(doctLevelName);
                break;

            case 500://是否可手术
                title.slideRighttext("确定", onClickListener);
                operationLevelName = data.getStringExtra("operationLevelName");
                operationLevel = data.getStringExtra("operationLevel");
                tvIsOperation.setText(operationLevelName);
                break;
        }
    }
}
