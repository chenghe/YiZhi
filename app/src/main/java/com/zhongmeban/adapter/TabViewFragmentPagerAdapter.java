package com.zhongmeban.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Description:TabView + ViewPager + Fragment 通用Adapter
 * Created by Chengbin He on 2016/3/31.
 */
public class TabViewFragmentPagerAdapter extends FragmentPagerAdapter {

    private int mScrollY;
    private List<Fragment> mFragments;
    private String[] TITLES;

    public TabViewFragmentPagerAdapter(FragmentManager fm ,
                                       List<Fragment> mFragments,String[] TITLES) {
        super(fm);
        this.mFragments = mFragments;
        this.TITLES = TITLES;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }


    @Override public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }

    public int getmScrollY(){
        return mScrollY;
    }
    public void setmScrollY(int mScrollY){
        this.mScrollY = mScrollY;
    }
}
