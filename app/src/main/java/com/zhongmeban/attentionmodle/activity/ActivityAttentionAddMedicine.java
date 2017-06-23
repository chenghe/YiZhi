package com.zhongmeban.attentionmodle.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.zhongmeban.R;
import com.zhongmeban.base.BaseActivity;
import com.zhongmeban.bean.postbody.CreateMedicineRecordListBody;
import com.zhongmeban.attentionmodle.fragment.FragmentAttentionMedicineList;
import com.zhongmeban.attentionmodle.fragment.FragmentAttentionVerifyMedicine;

import java.util.ArrayList;
import java.util.List;

/**
 * 添加用药Activity
 * Created by Chengbin He on 2016/7/14.
 */
public class ActivityAttentionAddMedicine extends BaseActivity{

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private FragmentAttentionMedicineList fragmentAttentionMedicineList;//药品列表Fragment
    private FragmentAttentionVerifyMedicine fragmentAttentionVerifyMedicine;//核对清单Fragment
    private int step = 1;//确定目前所在Fragment

    public List<CreateMedicineRecordListBody> medicineRecordList =
            new ArrayList<CreateMedicineRecordListBody>();//上传用List
    public boolean ISNOW;
    public String token;
    public String patientId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_no_title);

        medicineRecordList.clear();

        Intent intent = getIntent();
        ISNOW = intent.getBooleanExtra("ISNOW",true);

        SharedPreferences sp = getSharedPreferences("userInfo",MODE_PRIVATE);
        token = sp.getString("token","");
        patientId = sp.getString("patientId","");

        fragmentAttentionMedicineList = new FragmentAttentionMedicineList();
        fragmentAttentionVerifyMedicine = new FragmentAttentionVerifyMedicine();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fl_content,fragmentAttentionMedicineList);
        fragmentTransaction.commit();
    }

    public void startVerifyMedicine(){
        step = 2;
        fragmentAttentionMedicineList.clearSearche();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_content,fragmentAttentionVerifyMedicine);
        fragmentTransaction.commit();
    }

    public void backMedicineList(){
        step = 1;
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_content,fragmentAttentionMedicineList);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (step == 2){
            medicineRecordList.clear();
            medicineRecordList.addAll(fragmentAttentionVerifyMedicine.mMedicineRecordList);
            backMedicineList();
        }else {
            finish();
        }
    }

    @Override
    protected void initTitle() {
    }
}
