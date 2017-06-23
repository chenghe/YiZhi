package com.zhongmeban.shopmodle.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.zhongmeban.R;
import com.zhongmeban.base.BaseActivity;
import com.zhongmeban.utils.DisplayUtil;
import com.zhongmeban.utils.SpannableUtils;
import com.zhongmeban.view.PinchImageView;
import java.util.ArrayList;

/**
 * Created by User on 2016/9/28.
 */

public class ActProductBrowser extends BaseActivity implements View.OnClickListener {
    private ViewPager mViewPager;
    private TextView mTvInsiditor;
    private ArrayList<String> mDatas = new ArrayList<>();

    public static final String EXTRA_IMG_LIST = "extra_img_list";



    @Override protected void initTitle() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_product_browser);
        mDatas = getIntent().getStringArrayListExtra(EXTRA_IMG_LIST);

        initView();
    }


    private void initView() {

        mViewPager = (ViewPager) findViewById(R.id.id_act_product_browser_viewpager);
        mTvInsiditor = (TextView) findViewById(R.id.id_act_product_browser_indicator);

        mTvInsiditor.setText(
            SpannableUtils.setTextSize(1+"/"+mDatas.size(),0,1,
                DisplayUtil.sp2px(ActProductBrowser.this,20)));

        PhotoPagerAdapter minePagerAdapter = new PhotoPagerAdapter();
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(minePagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                mTvInsiditor.setText(
                    SpannableUtils.setTextSize((position+1)+"/"+mDatas.size(),0,1,
                        DisplayUtil.sp2px(ActProductBrowser.this,20)));
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


    }


    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_act_product_detail_shop_cat:
                break;
        }
    }


    class PhotoPagerAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            PinchImageView img = new PinchImageView(ActProductBrowser.this);
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);

            Picasso.with(ActProductBrowser.this).load(mDatas.get(position)).into(img);
            img.setTag(position);
            container.addView(img);

            img.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    ActProductBrowser.this.finish();
                }
            });
            return img;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            PinchImageView piv = (PinchImageView) object;
            container.removeView(piv);
        }

        @Override
        public int getItemPosition(Object object) {
            PinchImageView img = (PinchImageView) object;
            Object imgTag = img.getTag();

            Log.d("tag", "getItemPosition: " + imgTag);
/*            if (imgTag != null && imgTag instanceof Integer) {
                if ((Integer) imgTag == mCurrentPos) {
                    return POSITION_NONE;

                } else {
                    return super.getItemPosition(object);
                }
            } else {
                return POSITION_NONE;
            }*/
            //直接返回 none 也是可以的，但是会导致view重绘，instantiateItem会重新调用三（viewpager缓存的数量）次
            return POSITION_NONE;
        }

        @Override
        public int getCount() {

            return mDatas.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }


}
