package com.zhongmeban.treatmodle.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述：
 * 作者：ysf on 2016/12/2 14:19
 */
public class TreatTreatMethodPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList = new ArrayList<>();
    private String[] titles;


    public TreatTreatMethodPagerAdapter(FragmentManager fm, List<Fragment> fragmentList, String[] titles) {
        super(fm);
        this.fragmentList = fragmentList;
        this.titles = titles;
    }


    @Override public Fragment getItem(int position) {
        return fragmentList.get(position);
    }


    @Override public CharSequence getPageTitle(int position) {
        return titles[position];
    }


    @Override public int getCount() {
        return fragmentList.size();
    }
}
