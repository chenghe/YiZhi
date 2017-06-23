package com.zhongmeban.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import com.zhongmeban.R;
import com.zhongmeban.base.BaseActivity;
import com.zhongmeban.base.BaseFragment;
import com.zhongmeban.fragment.FragmentRecyclerView;
import com.zhongmeban.utils.LayoutActivityTitle;
import com.zhongmeban.utils.OnTabSelectListener;
import com.zhongmeban.utils.SlidingTabLayout;

import java.util.ArrayList;

/**
 * Created by Chengbin He on 2016/3/23.
 */
public class ActivityCollected extends BaseActivity implements OnTabSelectListener {

    private LayoutActivityTitle title;
    private BaseFragment hospitalFragment;
    private BaseFragment tumourFragment;
    private BaseFragment nameFragment;
    private BaseFragment inspectionFragment;
    private BaseFragment cureFragment;
    private BaseFragment medicineFragment;
    private BaseFragment normFragment;

    private final String[] mTitles = {"医院","肿瘤","名称","检查","治疗","药品","指标"};
    private Context mContext = ActivityCollected.this;
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    public String userId;
    public String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collected);

        SharedPreferences userSp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        userId = userSp.getString("userId","");
        token = userSp.getString("token","");

        initTitle();
        initView();
    }

    private void initView() {
        hospitalFragment = new FragmentRecyclerView();
        tumourFragment = new FragmentRecyclerView();
        nameFragment = new FragmentRecyclerView();
        inspectionFragment = new FragmentRecyclerView();
        cureFragment = new FragmentRecyclerView();
        medicineFragment = new FragmentRecyclerView();

        final Bundle medicineBundle = new Bundle(1);//设置Bundle 动态改变Fragment里Adapter;
        medicineBundle.putString("FragmentRecyclerView", "medicine");
        medicineFragment.setArguments(medicineBundle);

        normFragment = new FragmentRecyclerView();
        mFragments.add(hospitalFragment);
        mFragments.add(tumourFragment);
        mFragments.add(nameFragment);
        mFragments.add(inspectionFragment);
        mFragments.add(cureFragment);
        mFragments.add(medicineFragment);
        mFragments.add(normFragment);

//        View decorView = getWindow().getDecorView();
        ViewPager vp = (ViewPager) findViewById(R.id.vp);
        vp.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        SlidingTabLayout tabLayout = (SlidingTabLayout) findViewById(R.id.tl);
        tabLayout.setViewPager(vp);
        tabLayout.setOnTabSelectListener(this);
    }

    protected void initTitle() {
        title = new LayoutActivityTitle(findViewById(R.id.layout_top));
        title.slideCentertext("我的收藏");
        title.slideRighttext("编辑", onClickListener);
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
            switch (v.getId()){

            }
        }
    };


    @Override
    public void onTabSelect(int position) {
    }

    @Override
    public void onTabReselect(int position) {

    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }
}
