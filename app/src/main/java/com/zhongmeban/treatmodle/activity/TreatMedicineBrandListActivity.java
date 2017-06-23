package com.zhongmeban.treatmodle.activity;

import com.zhongmeban.R;
import com.zhongmeban.base.BaseActivityToolBar;

/**
 * 功能描述： 药品列博
 * 作者：ysf on 2016/12/2 10:52
 */
public class TreatMedicineBrandListActivity extends BaseActivityToolBar {

    @Override protected int getLayoutId() {
        return R.layout.act_treat_medicine_brand_list;
    }


    @Override protected void initToolbar() {
        initToolbarCustom("品牌专区","");
    }


    @Override protected void initView() {

    }
}
