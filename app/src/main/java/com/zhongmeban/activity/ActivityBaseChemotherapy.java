package com.zhongmeban.activity;

import com.zhongmeban.base.BaseActivity;
import com.zhongmeban.fragment.FragmentChemotherapyMedicine;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.AttentionMedicine;

/**
 * 化疗基类Activity 带用药Fragment
 * Created by Chengbin He on 2016/8/26.
 */
public class ActivityBaseChemotherapy extends BaseActivity{
    public FragmentChemotherapyMedicine fragmentChemotherapyMedicine;//化疗用药
    public boolean ISSAVE;//判断是否为保存
    public List<AttentionMedicine> chooseMedicineList = new ArrayList<AttentionMedicine>();//显示用list
    @Override
    protected void initTitle() {
    }

    public void backChemotherapyFragment(){
        fragmentChemotherapyMedicine.clearSearch();
    }
}
