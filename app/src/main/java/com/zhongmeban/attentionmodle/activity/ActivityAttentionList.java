package com.zhongmeban.attentionmodle.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import com.zhongmeban.R;
import com.zhongmeban.activity.ActHome;
import com.zhongmeban.attentionmodle.fragment.FragmentHospitalizedList;
import com.zhongmeban.attentionmodle.fragment.FragmentOperationList;
import com.zhongmeban.attentionmodle.fragment.FragmentRadiotherapyList;
import com.zhongmeban.attentionmodle.presenter.AttentionHospitalizedListPresenter;
import com.zhongmeban.attentionmodle.presenter.AttentionOperationListPresenter;
import com.zhongmeban.attentionmodle.presenter.AttentionRadiotherapyListPresenter;
import com.zhongmeban.base.BaseActivity;
import com.zhongmeban.fragment.FragmentAssistMedicineList;
import com.zhongmeban.fragment.FragmentChemotherapyList;
import com.zhongmeban.fragment.FragmentMarkerList;
import com.zhongmeban.utils.LayoutActivityTitle;
import com.zhongmeban.utils.genericity.SPInfo;

/**
 * 关注部分 listitem Activity
 * Created by Chengbin He on 2016/6/28.
 */
public class ActivityAttentionList extends BaseActivity {

    public static final String EXTRA_ATTENTION_TYPE = "attentionType";
    private Activity mContext = ActivityAttentionList.this;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private FragmentMarkerList fragmentMarkerList;//标志物记录Fragment
    private FragmentOperationList fragmentOperationList;//手术记录Fragment
    private FragmentHospitalizedList fragmentHospitalizedList;//住院记录Fragment
    private FragmentRadiotherapyList fragmentRadiotherapyList;//放疗记录Fragemt
    private FragmentChemotherapyList fragmentChemotherapyList;//化疗记录Fragment
    private FragmentAssistMedicineList fragmentMedicineList;//辅助用药Fragment
    private int type;
    private SharedPreferences sp;

    public String token;
    public String patientId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attention_list_common);
        Intent intent = getIntent();
        type = intent.getIntExtra(EXTRA_ATTENTION_TYPE, 0);

        SharedPreferences sp = getSharedPreferences("userInfo",MODE_PRIVATE);
        token = sp.getString("token","");
        patientId = sp.getString("patientId","");
        if (type == 0){
            sp.edit().putBoolean(SPInfo.UserKey_markerListBack,true).apply();
        }else {
            sp.edit().putBoolean(SPInfo.UserKey_markerListBack,false).apply();
        }

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        switch (type) {
            case 0:
                //标志物记录Fragment
                fragmentMarkerList = FragmentMarkerList.newInstance();
                fragmentTransaction.add(R.id.fl_content, fragmentMarkerList);
                break;

            case 1:
                //住院记录Fragment
                fragmentHospitalizedList = FragmentHospitalizedList.newInstance();
                fragmentTransaction.add(R.id.fl_content, fragmentHospitalizedList);
                new AttentionHospitalizedListPresenter(mContext,fragmentHospitalizedList);
                break;

            case 2:
                //手术记录Fragment
                fragmentOperationList = FragmentOperationList.newInstance();
                fragmentTransaction.add(R.id.fl_content, fragmentOperationList);
                new AttentionOperationListPresenter(mContext,fragmentOperationList);
                break;

            case 3:
                //化疗记录Fragemt
                fragmentChemotherapyList = FragmentChemotherapyList.newInstance();
                fragmentTransaction.add(R.id.fl_content, fragmentChemotherapyList);

                break;

            case 4:
                //放疗记录Fragment
                fragmentRadiotherapyList = FragmentRadiotherapyList.newInstance();
                fragmentTransaction.add(R.id.fl_content, fragmentRadiotherapyList);
                new AttentionRadiotherapyListPresenter(mContext,fragmentRadiotherapyList);
                break;
            case 5:
                //辅助用药aFragment
                fragmentMedicineList = FragmentAssistMedicineList.newInstance();
                fragmentTransaction.add(R.id.fl_content, fragmentMedicineList);
                break;

            default:
                fragmentMarkerList = FragmentMarkerList.newInstance();
                fragmentTransaction.add(R.id.fl_content, fragmentMarkerList);
                break;
        }


        fragmentTransaction.commit();
        initTitle();
    }

    @Override
    public void onBackPressed() {
        if (type ==0){
            Intent intent = new Intent(mContext,ActHome.class);
            setResult(FragmentMarkerList.FragmentMarkerListBack);
            finish();
        }else {
            finish();
        }
    }

    @Override
    protected void initTitle() {
        title = new LayoutActivityTitle(findViewById(R.id.layout_top));
        String titleName = "";
        switch (type) {
            case 0:
                titleName = "标志物记录";
                break;
            case 1:
                titleName = "住院记录";
                break;
            case 2:
                titleName = "手术记录";
                break;
            case 3:
                titleName = "化疗记录";
                break;
            case 4:
                titleName = "放疗记录";
                break;
            case 5:
                titleName = "辅助用药";
                break;
        }
        title.slideCentertext(titleName);
        title.backSlid(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
    }


}
