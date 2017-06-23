package com.zhongmeban.treatmodle.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import com.zhongmeban.R;
import com.zhongmeban.base.BaseActivityToolBar;
import com.zhongmeban.bean.treatbean.DieaseTypeBean;
import com.zhongmeban.treatmodle.adapter.TreatTreatMethodPagerAdapter;
import com.zhongmeban.treatmodle.fragment.TreatChemotherapyFragment;
import com.zhongmeban.treatmodle.fragment.TreatOperationFragment;
import com.zhongmeban.treatmodle.fragment.TreatRadiotherapyFragment;
import com.zhongmeban.treatmodle.fragment.TreatTargetFragment;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述：治疗方式
 * 0  * 作者：ysf on 2016/12/2 10:52
 */
public class TreatTreatMethodActivity extends BaseActivityToolBar {

    public static final String EXTRA_CANCER_TYPE_BEAN = "extra_cancer_type_bean";

    private ViewPager vp;
    private TabLayout tbyt;
    private TreatTreatMethodPagerAdapter adapterTreat;
    private TreatOperationFragment fragOpertion;

    private String[] titles = { "手术", "化疗", "放疗", "靶向治疗" };
    private List<Fragment> fragmentList = new ArrayList<>();

    private DieaseTypeBean cancerTypeBean;


    @Override protected int getLayoutId() {
        return R.layout.act_treat_treat_method;
    }


    @Override protected void initToolbar() {
        cancerTypeBean = (DieaseTypeBean) getIntent().getSerializableExtra(EXTRA_CANCER_TYPE_BEAN);
        initToolbarCustom(cancerTypeBean.getName(), "");
    }


    @Override protected void initView() {
        vp = (ViewPager) findViewById(R.id.vp_treat_method);
        tbyt = (TabLayout) findViewById(R.id.tbyt_treat_method);

        fragmentList.add(TreatOperationFragment.newInstance(cancerTypeBean.getId()));
        fragmentList.add(TreatChemotherapyFragment.newInstance(cancerTypeBean.getId()));
        fragmentList.add(TreatRadiotherapyFragment.newInstance(cancerTypeBean.getId()));
        fragmentList.add(TreatTargetFragment.newInstance(cancerTypeBean.getId()));

        adapterTreat = new TreatTreatMethodPagerAdapter(getSupportFragmentManager(), fragmentList,
            titles);
        vp.setAdapter(adapterTreat);
        tbyt.setupWithViewPager(vp);

    }
}
