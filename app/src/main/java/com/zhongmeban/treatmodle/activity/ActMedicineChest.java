package com.zhongmeban.treatmodle.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import com.zhongmeban.R;
import com.zhongmeban.adapter.TabViewFragmentPagerAdapter;
import com.zhongmeban.base.BaseActivity;
import com.zhongmeban.treatmodle.fragment.FragMedChestBrandZone;
import com.zhongmeban.treatmodle.fragment.FragMedChestDrugIndex;
import com.zhongmeban.utils.LayoutActivityTitle;
import com.zhongmeban.utils.OnTabSelectListener;
import com.zhongmeban.utils.SlidingTabLayout;
import java.util.ArrayList;

/**
 * Description:药箱子容器，两个子frag:药品索引，品牌专区
 * user: chenghe
 */
public class ActMedicineChest extends BaseActivity
    implements OnTabSelectListener, ViewPager.OnPageChangeListener {

    private LayoutActivityTitle title;
    private SlidingTabLayout tabLayout;
    private ViewPager viewPager;
    private TabViewFragmentPagerAdapter mPagerAdapter;
    private final String[] mTitles = { "全部药品", "品牌专区" };//Tab title
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private Context mContext = ActMedicineChest.this;
    private FragMedChestDrugIndex fDrugIndex;
    private FragMedChestBrandZone fBrandZone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_medicine_chest);

        //初始化Fragment
        fDrugIndex = new FragMedChestDrugIndex();//药品索引
        fBrandZone = new FragMedChestBrandZone();//品牌专区
        mFragments.add(fDrugIndex);
        mFragments.add(fBrandZone);
        mPagerAdapter = new TabViewFragmentPagerAdapter(getSupportFragmentManager(), mFragments,
            mTitles);
        tabLayout = (SlidingTabLayout) findViewById(R.id.tl);
        viewPager = (ViewPager) findViewById(R.id.vp);
        viewPager.setAdapter(mPagerAdapter);
        viewPager.setOnPageChangeListener(this);
        tabLayout.setViewPager(viewPager);
        tabLayout.setOnTabSelectListener(this);

        //隱藏 tab切换标签，现在只需要展示一个概述就行，viewpager修改后禁止滑动
        initTitle();
    }


    protected void initTitle() {
        title = new LayoutActivityTitle(findViewById(R.id.layout_top));
        title.slideCentertext("药品列表");
        title.backSlid(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    public void onTabSelect(int position) {
        Log.i("hcb", "ActMedicineChest onTabSelect start position is " + position);
        if (position == 1) {
            fBrandZone.getHttpData();
        }
    }


    @Override
    public void onTabReselect(int position) {

    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }


    @Override
    public void onPageSelected(int position) {
        Log.i("hcb", "ActMedicineChest onPageSelected start position is " + position);
        if (position == 1) {
            fBrandZone.getHttpData();
        }
    }


    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_DRAGGING) {
            //正在滑动   pager处于正在拖拽中
            if(fDrugIndex!=null)
            fDrugIndex.isShowOverlay(false);

        } else if (state == ViewPager.SCROLL_STATE_SETTLING) {
            //pager正在自动沉降，相当于松手后，pager恢复到一个完整pager的过程

        } else if (state == ViewPager.SCROLL_STATE_IDLE) {
            //空闲状态  pager处于空闲状态
        }

    }
}
