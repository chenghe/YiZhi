package com.zhongmeban.shopmodle.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.zhongmeban.shopmodle.presenter.ShopStorePresenter;
import com.zhongmeban.R;
import com.zhongmeban.shopmodle.activity.ActProductDetail;
import com.zhongmeban.shopmodle.activity.ActShopBannerInfo;
import com.zhongmeban.shopmodle.activity.ActShoppingCart;
import com.zhongmeban.base.BaseFragmentRefresh;
import com.zhongmeban.base.baseadapter.CommonAdapter;
import com.zhongmeban.base.baseadapter.base.ViewHolder;
import com.zhongmeban.base.baseadapter.wrapper.HeaderAndFooterWrapper;
import com.zhongmeban.base.baseadapter.wrapper.LoadMoreWrapper;
import com.zhongmeban.bean.HttpResult;
import com.zhongmeban.bean.shop.ProductBean;
import com.zhongmeban.bean.shop.PromotionBean;
import com.zhongmeban.bean.shop.ShopCartQuantity;
import com.zhongmeban.bean.shop.post.AddShopCartBody;
import com.zhongmeban.utils.ImageLoaderZMB;
import com.zhongmeban.utils.NetworkImageHolderView;
import com.zhongmeban.utils.ScrollView.ScrollUtils;
import com.zhongmeban.utils.ToastUtil;
import com.zhongmeban.view.ShopCarLandList;
import com.zhongmeban.viewinterface.IAddShopToCart;
import com.zhongmeban.viewinterface.IGetShopBanner;
import com.zhongmeban.viewinterface.IGetShopCartQuantity;
import com.zhongmeban.viewinterface.IGetShopProductHot;
import com.zhongmeban.viewinterface.IGetShopProductList;
import java.util.ArrayList;
import java.util.List;

public class ShopFragment extends BaseFragmentRefresh
    implements View.OnClickListener,
    IGetShopProductList,
    IGetShopProductHot,
    IGetShopBanner,
    IGetShopCartQuantity,
    IAddShopToCart {

    private RecyclerView mRecyView;

    private Toolbar mToolBar;
    private TextView mTitle;
    private ImageView mIvShopping;
    private TextView mTvShopCartQuantity;
    private List<ProductBean.ContentBean> mDatasList = new ArrayList<>();
    private List<ProductBean.ContentBean> mDatasHot = new ArrayList<>();
    private List<PromotionBean> mDatasPromotion = new ArrayList<>();
    private AppBarLayout app_bar;

    private CommonAdapter mAdapter;
    private ConvenientBanner convenientBanner;
    private List<String> networkImages = new ArrayList<String>();
    private ShopStorePresenter mPresenter;

    private int pageCount = 1;
    private int pageSize = 10;
    private int pageTotal = 10;
    private int orderBy = 1;
    private LoadMoreWrapper mLoadMoreWrapper;

    private Long memId = 1l;


    @Override public void onAttach(Context context) {
        super.onAttach(context);

    }


    @Override protected int getLayoutIdRefresh() {
        return R.layout.frag_shop_home;
    }


    @Override protected void initToolbarRefresh() {

    }


    @Override protected void initOnCreateView() {
        mRecyView = (RecyclerView) mRootView.findViewById(R.id.id_frag_shop_home_rv);
        mRecyView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mToolBar = (Toolbar) mRootView.findViewById(R.id.id_frag_shop_home_toolbar);
        app_bar = (AppBarLayout) mRootView.findViewById(R.id.app_bar);
        mTitle = (TextView) mRootView.findViewById(R.id.id_frag_shop_home_title);
        mTvShopCartQuantity = (TextView) mRootView.findViewById(
            R.id.id_frag_shop_home_tv_cart_quantity);
        mIvShopping = (ImageView) mRootView.findViewById(R.id.id_frag_shop_home_shopping);
        mIvShopping.setOnClickListener(this);
        convenientBanner = (ConvenientBanner) mRootView.findViewById(
            R.id.id_frag_shop_home_convenientBanner);
        initToolbar();
        if (mPresenter == null) {
            mPresenter = new ShopStorePresenter(getActivity());
        }

        initEmptyView();

        loadAllDatas();

        mAdapter = new CommonAdapter<ProductBean.ContentBean>(getActivity(),
            R.layout.item_shop_product,
            mDatasList) {

            @Override
            protected void convert(ViewHolder holder, final ProductBean.ContentBean bean, int position) {
                holder.setText(R.id.id_item_shop_home_product_tv_name, bean.getName());
                holder.setText(R.id.id_item_shop_home_product_tv_price, bean.getPrice() + "");

                ImageView img = holder.getView(R.id.id_item_shop_home_iv_land);
                //Picasso.with(getActivity()).load(bean.getImage()).into(img);
                ImageLoaderZMB.getInstance().loadImage(getActivity(),bean.getImage(),img);

                holder.setOnClickListener(R.id.id_item_shop_home_iv_land,
                    new View.OnClickListener() {
                        @Override public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), ActProductDetail.class);
                            intent.putExtra(ActProductDetail.EXTRA_GOODS_BEAN, bean);
                            startActivity(intent);
                        }
                    });

                //添加购物车
                holder.setOnClickListener(R.id.id_item_shop_home_product_iv_add,
                    new View.OnClickListener() {
                        @Override public void onClick(View v) {
                            mPresenter.addShopToCart(
                                new AddShopCartBody(bean.getId(), 1, memId, false),
                                ShopFragment.this);
                        }
                    });
            }
        };

        mRecyView.setAdapter(mAdapter);
    }


    private void initToolbar() {
        final int baseColor = getResources().getColor(R.color.toolbar_color);
        final int baseTextColor = getResources().getColor(R.color.top_text);
        app_bar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                float percent = Math.abs(
                    verticalOffset * 1.0F / appBarLayout.getTotalScrollRange());

                mToolBar.setBackgroundColor(ScrollUtils.getColorWithAlpha(percent, baseColor));
                mTitle.setTextColor(ScrollUtils.getColorWithAlpha(percent, baseTextColor));

            }
        });
    }


    public void loadAllDatas() {

        showEmptyLayoutState(LOADING_STATE_PEOGRESS);

        mPresenter.getShopProductList(this, pageCount, pageSize, orderBy, false, false);
        mPresenter.getShopProductHot(this, pageCount, 20, 1, true, true);
        mPresenter.getShopBanner(this);
        mPresenter.getShopCartQuantity(memId, this);

    }


    //初始化商城轮播促销图
    private void initBanner(List<PromotionBean> result) {

        mDatasPromotion.clear();
        mDatasPromotion.addAll(result);
        for (PromotionBean bean : result) {
            networkImages.add(bean.getImage());
        }
        convenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        }, networkImages)
            .setPageIndicator(
                new int[] { R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused })
            .setOnItemClickListener(new OnItemClickListener() {
                @Override public void onItemClick(int position) {

                    Intent intent = new Intent(getActivity(), ActShopBannerInfo.class);
                    intent.putExtra(ActShopBannerInfo.EXTRA_SHOP_BANNER,
                        mDatasPromotion.get(position));
                    startActivity(intent);
                }
            });
    }


    @Override
    public void onResume() {
        super.onResume();
        //开始自动翻页
        convenientBanner.startTurning(5000);
    }


    @Override
    public void onPause() {
        super.onPause();
        //停止翻页
        convenientBanner.stopTurning();
    }


    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_frag_shop_home_shopping:
                startActivity(new Intent(getActivity(), ActShoppingCart.class));
                break;
        }
    }


    @Override public void getShopBannerSuccess(List<PromotionBean> result) {

        initBanner(result);
    }


    @Override public void getShopBannerFail() {

    }


    @Override public void getShopProductHotSuccess(ProductBean result, boolean isLoadMore) {

        showEmptyLayoutState(LOADING_STATE_SUCESS);
        if (result == null || result.getContent() == null) return;
        ShopCarLandList hotView = new ShopCarLandList(getActivity());
        hotView.setmDatasHot(result.getContent());
        HeaderAndFooterWrapper mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mAdapter);
        mHeaderAndFooterWrapper.addHeaderView(hotView);

        mLoadMoreWrapper = new LoadMoreWrapper(mHeaderAndFooterWrapper);
        mLoadMoreWrapper.setLoadMoreView(
            LayoutInflater.from(getActivity()).inflate(R.layout.default_loading, mRecyView, false));
        mLoadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
            @Override public void onLoadMoreRequested() {

                mPresenter.getShopProductList(ShopFragment.this, pageCount, pageSize, orderBy,
                    false, true);
            }
        });

        mRecyView.setAdapter(mLoadMoreWrapper);

    }


    @Override public void getShopProductHotFail() {
        //showEmptyLayoutState(LOADING_STATE_FAIL);
    }


    @Override public void getShopProductFail() {
        //showEmptyLayoutState(LOADING_STATE_FAIL);
    }


    @Override public void getShopProductSuccess(ProductBean result, boolean isLoadMore) {
        if (result == null || result.getContent() == null) return;

        if (pageCount > pageTotal) {
            ToastUtil.showText(getActivity(), "再怎么滑也沒有啦");
            mLoadMoreWrapper.removeLoadMore();
            //mLoadMoreWrapper.notifyDataSetChanged();
            return;
        }

        if (isLoadMore) {
            mDatasList.addAll(result.getContent());
            pageCount = result.getPageNumber() + 1;
        } else {
            mDatasList.clear();
            mDatasList.addAll(result.getContent());
            pageCount = 2;
        }

        pageTotal = result.getTotalPages();
        if (mLoadMoreWrapper != null) {
            mLoadMoreWrapper.notifyDataSetChanged();
        }
    }


    //购物车数量小圆点展示
    @Override public void getShopQuantitySuccess(ShopCartQuantity shopCartQuantity) {
        mTvShopCartQuantity.setText(shopCartQuantity.getQuantity() + "");
    }


    @Override public void getShopQuantityFail() {
        mTvShopCartQuantity.setText("");
    }


    //添加购购物车的操作，成功之后需要更新右上角购物车的数量 _____________________________________暂时屏蔽此功能
    @Override public void addShopToCartSuccess(HttpResult httpResult) {
        if (httpResult.errorCode==0){
            ToastUtil.showText(getActivity(),"添加成功");

            mPresenter.getShopCartQuantity(memId,this);//更新右上角购物车的数量
        } else{
            ToastUtil.showText(getActivity(),"系统出现了一个意外");
        }
    }


    @Override public void addShopToCartFail() {
        ToastUtil.showText(getActivity(),"添加失败");
    }


    @Override public void clickLeft() {

    }


    @Override public void clickRight() {

    }
}
