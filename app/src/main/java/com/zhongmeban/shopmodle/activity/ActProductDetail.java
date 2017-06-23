package com.zhongmeban.shopmodle.activity;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhongmeban.R;
import com.zhongmeban.base.BaseActivity;
import com.zhongmeban.bean.HttpResult;
import com.zhongmeban.bean.shop.ProductBean;
import com.zhongmeban.bean.shop.ProductDetailBean;
import com.zhongmeban.bean.shop.ShopCartInfoBean;
import com.zhongmeban.bean.shop.ShopCartQuantity;
import com.zhongmeban.bean.shop.post.AddShopCartBody;
import com.zhongmeban.shopmodle.fragment.FragProductDetail;
import com.zhongmeban.shopmodle.fragment.FragProductInfo;
import com.zhongmeban.net.HttpShopMethods;
import com.zhongmeban.utils.SlidingTabLayout;
import rx.Subscriber;

public class ActProductDetail extends BaseActivity implements View.OnClickListener {

    public static final String EXTRA_GOODS_BEAN = "extra_goods_bean";
    private ViewPager mViewPager;
    private SlidingTabLayout mTab;
    private ImageView mIvLove;
    private ImageView mIvCar;

    private TextView mTvAddShoppingCart;
    private TextView mTvSubmitOrder;
    private TextView mTvShopCartQuantity;

    private ProductBean.ContentBean goodsBean;


    @Override protected void initTitle() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_product_detail);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        // 理论上如果使用toolbar需要加上下边代码，但是加上之后icon 右边会出现title文字，不加也可以
        //setSupportActionBar(mToolbar);

        //设置 toolbar 导航栏图片和点击事件
        mToolbar.setNavigationIcon(R.drawable.back_grey);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });

        goodsBean = (ProductBean.ContentBean) getIntent().getSerializableExtra(EXTRA_GOODS_BEAN);
        initView();

        getDataFromNet(goodsBean.getId());

    }


    private void initView() {

        mViewPager = (ViewPager) findViewById(R.id.id_act_product_detail_viewpager);
        mTab = (SlidingTabLayout) findViewById(R.id.id_act_product_detail_tabs);

        mIvCar = (ImageView) findViewById(R.id.id_act_product_detail_shop_cat);
        mIvLove = (ImageView) findViewById(R.id.id_act_product_detail_love);
        mTvAddShoppingCart = (TextView) findViewById(R.id.id_act_product_detail_add_shop_cat);
        mTvShopCartQuantity = (TextView) findViewById(R.id.id_act_product_detail_shop_cat_quantity);
        mTvSubmitOrder = (TextView) findViewById(R.id.id_act_product_detail_submit_order);
        mIvCar.setOnClickListener(this);
        mIvLove.setOnClickListener(this);
        mTvAddShoppingCart.setOnClickListener(this);
        mTvSubmitOrder.setOnClickListener(this);
    }


    private boolean isLove;


    private void getDataFromNet(Long goodsId) {
        HttpShopMethods.getInstance().getProductDetail(
            new Subscriber<HttpResult<ProductDetailBean>>() {
                @Override public void onCompleted() {

                }


                @Override public void onError(Throwable e) {

                }


                @Override public void onNext(HttpResult<ProductDetailBean> httpResult) {
                    if (httpResult.data != null) {
                        initFragment(httpResult.data);
                    }
                }
            }, goodsId);

    }


    private void initFragment(ProductDetailBean productDetailBean) {
        MinePagerAdapter minePagerAdapter = new MinePagerAdapter(getSupportFragmentManager(),
            productDetailBean);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(minePagerAdapter);
        mTab.setViewPager(mViewPager);

    }


    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_act_product_detail_love:
                if (isLove) {
                    mIvLove.setImageResource(R.drawable.ic_love_normal);
                    isLove = !isLove;
                    showAnim(mIvLove, 0.2f);
                } else {
                    mIvLove.setImageResource(R.drawable.ic_love_select);
                    isLove = !isLove;
                    showAnim(mIvLove, 1.8f);
                }
                break;
            //点击购物车图标
            case R.id.id_act_product_detail_shop_cat:

                break;
            //点击加入购物车按钮
            case R.id.id_act_product_detail_add_shop_cat:

                break;
            //点击提交订单按钮
            case R.id.id_act_product_detail_submit_order:
                startActivity(new Intent(this, ActShoppingCheckout.class));
                break;

        }
    }


    private void addShopToCart() {
        HttpShopMethods.getInstance().addShopCart(new AddShopCartBody(51l, 2, 1l, true),
            new Subscriber<HttpResult<ShopCartInfoBean>>() {
                @Override public void onCompleted() {

                }

                @Override public void onError(Throwable e) {

                }
                @Override
                public void onNext(HttpResult<ShopCartInfoBean> shopCartInfoBeanHttpResult) {

                }
            });
    }


    private void getShopCartQuantity(Long memId) {
        HttpShopMethods.getInstance().getShopCartQuantity(memId,
            new Subscriber<HttpResult<ShopCartQuantity>>() {
                @Override public void onCompleted() {

                }
                @Override public void onError(Throwable e) {

                }
                @Override
                public void onNext(HttpResult<ShopCartQuantity> shopCartResult) {
                    if (shopCartResult != null && shopCartResult.errorCode == 0) {

                        mTvShopCartQuantity.setText(shopCartResult.data.getQuantity() + "");
                    }
                }
            });
    }


    Fragment[] fragments;


    /**
     * ViewPager的PagerAdapter
     */
    public class MinePagerAdapter extends FragmentPagerAdapter {

        String[] titles = new String[] { "商品", "详情" };


        public MinePagerAdapter(FragmentManager fm, ProductDetailBean detailBean) {
            super(fm);
            fragments = new Fragment[] { FragProductInfo.newInstance(detailBean),
                FragProductDetail.newInstance(detailBean) };
        }


        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }


        @Override
        public int getCount() {
            return fragments.length;
        }
    }


    //收藏商品的动画
    public void showAnim(View love, float value) {
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("alpha", 1f,
            value, 1f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX", 1f,
            value, 1f);
        PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat("scaleY", 1f,
            value, 1f);
        ObjectAnimator.ofPropertyValuesHolder(love, pvhX, pvhY, pvhZ)
            .setDuration(500)
            .start();
    }
}
