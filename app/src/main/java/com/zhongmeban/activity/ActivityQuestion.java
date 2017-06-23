package com.zhongmeban.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import com.zhongmeban.R;
import com.zhongmeban.base.BaseActivity;
import com.zhongmeban.fragment.FragmentAnswer;
import com.zhongmeban.fragment.FragmentUnanswered;
import com.zhongmeban.utils.LayoutActivityTitle;
import com.zhongmeban.utils.OnTabSelectListener;
import com.zhongmeban.utils.SlidingTabLayout;

import java.util.ArrayList;

/**
 * @Description:我的提问Activity
 * Created by Chengbin He on 2016/3/23.
 */
public class ActivityQuestion extends BaseActivity implements OnTabSelectListener {

    private final String[] mTitles = {"已回复","未回复"};
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private Context mContext = ActivityQuestion.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        FragmentAnswer answer = new FragmentAnswer();
        FragmentUnanswered unanswered = new FragmentUnanswered();
        mFragments.add(answer);
        mFragments.add(unanswered);

//        View decorView = getWindow().getDecorView();
        ViewPager vp = (ViewPager) findViewById(R.id.vp);
        vp.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        SlidingTabLayout tabLayout = (SlidingTabLayout) findViewById(R.id.tl);
        tabLayout.setViewPager(vp);
        tabLayout.setOnTabSelectListener(this);

        initTitle();
    }
    protected void initTitle() {
        title = new LayoutActivityTitle(findViewById(R.id.layout_top));
        title.slideCentertext("我要提问");
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
        Toast.makeText(mContext, "onTabSelect&position--->" + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTabReselect(int position) {
        Toast.makeText(mContext, "onTabReselect&position--->" + position, Toast.LENGTH_SHORT).show();
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
