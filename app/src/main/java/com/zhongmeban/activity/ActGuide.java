package com.zhongmeban.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.rd.PageIndicatorView;
import com.squareup.picasso.Picasso;
import com.zhongmeban.R;
import com.zhongmeban.utils.DisplayUtil;
import com.zhongmeban.view.PinchImageView;

/**
 * Created by User on 2016/9/28.
 */

public class ActGuide extends Activity implements View.OnClickListener {
    private ViewPager mViewPager;
    private TextView mBtnStartHome;
    private PageIndicatorView mIndicator;
    private int[] mDatas = { R.drawable.app_guide0, R.drawable.app_guide1, R.drawable.app_guide2 };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_guide);
        initView();
    }


    private void initView() {

        mViewPager = (ViewPager) findViewById(R.id.id_guide_viewpager);
        mBtnStartHome = (TextView) findViewById(R.id.id_guide_start_home);
        mBtnStartHome.setVisibility(View.GONE);
        mIndicator = (PageIndicatorView) findViewById(R.id.id_guide_pageIndicator);

        mBtnStartHome.setOnClickListener(this);
        PhotoPagerAdapter minePagerAdapter = new PhotoPagerAdapter();
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(minePagerAdapter);
        mIndicator.setViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }


            @Override
            public void onPageSelected(int position) {
                if (position == mDatas.length - 1) {
                    mBtnStartHome.setVisibility(View.INVISIBLE);
                } else {
                    mBtnStartHome.setVisibility(View.INVISIBLE);
                }
            }


            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }


    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_guide_start_home:
                startHome();
                break;
        }
    }


    private void startHome() {

        mBtnStartHome.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(ActGuide.this, ActHome.class);
                startActivity(intent);
                finish();
            }
        }, 200);
    }


    class PhotoPagerAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            if (position == mDatas.length - 1) {
                ImageView img = new ImageView(ActGuide.this);
                img.setScaleType(ImageView.ScaleType.FIT_XY);
                Picasso.with(ActGuide.this).load(mDatas[position]).fit().into(img);
                img.setTag(position);
                TextView tvStart = new TextView(ActGuide.this);
                RelativeLayout layout = new RelativeLayout(ActGuide.this);
                ViewPager.LayoutParams layoutParams = new ViewPager.LayoutParams();
                layout.setLayoutParams(layoutParams);
                RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                img.setLayoutParams(layoutParams1);
                layout.addView(img);
                RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(
                    DisplayUtil.dip2px(ActGuide.this,115), DisplayUtil.dip2px(ActGuide.this,38));
                layoutParams2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                layoutParams2.addRule(RelativeLayout.CENTER_HORIZONTAL);
                layoutParams2.bottomMargin = DisplayUtil.dip2px(ActGuide.this,48);

                tvStart.setText("立即体验");
                tvStart.setPadding(DisplayUtil.dip2px(ActGuide.this,20),DisplayUtil.dip2px(ActGuide.this,8),DisplayUtil.dip2px(ActGuide.this,20),DisplayUtil.dip2px(ActGuide.this,8));
                tvStart.setTextSize(16);
                tvStart.setGravity(Gravity.CENTER);
                tvStart.setTextColor(getResources().getColor(R.color.guaid_btn));
                tvStart.setBackgroundResource(R.drawable.select_btn_edge_color_green);
                tvStart.setLayoutParams(layoutParams2);
                layout.addView(tvStart);
                container.addView(layout);

                tvStart.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        startHome();
                    }
                });
                return  layout;
            } else {
                ImageView img = new ImageView(ActGuide.this);
                img.setScaleType(ImageView.ScaleType.FIT_XY);
                Picasso.with(ActGuide.this).load(mDatas[position]).fit().into(img);
                img.setTag(position);
                container.addView(img);
                return img;
            }

            //img.setOnClickListener(new View.OnClickListener() {
            //    @Override public void onClick(View v) {
            //        if (position == mDatas.length - 1) {
            //
            //            //startHome();
            //        }
            //    }
            //});
            //return img;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            PinchImageView piv = (PinchImageView) object;
            container.removeView(piv);
        }


        @Override
        public int getCount() {

            return mDatas.length;
        }


        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

}
