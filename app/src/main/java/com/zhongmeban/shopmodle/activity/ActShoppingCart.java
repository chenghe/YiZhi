package com.zhongmeban.shopmodle.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;
import com.zhongmeban.R;
import com.zhongmeban.base.BaseActivityRefresh;
import com.zhongmeban.base.baseadapter.CommonAdapter;
import com.zhongmeban.base.baseadapter.base.ViewHolder;
import com.zhongmeban.bean.HttpResult;
import com.zhongmeban.bean.shop.ShopCartInfoBean;
import com.zhongmeban.bean.shop.post.DeleteShopCartListBody;
import com.zhongmeban.bean.shop.post.UpdateShopCartCheckBody;
import com.zhongmeban.bean.shop.post.UpdateShopCartQuantityBody;
import com.zhongmeban.net.HttpShopMethods;
import com.zhongmeban.utils.SmoothCheckBox;
import com.zhongmeban.utils.ToastUtil;
import com.zhongmeban.view.QuantityView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * 购物车页面
 */

public class ActShoppingCart extends BaseActivityRefresh implements View.OnClickListener {

    private TextView mTvEdit;
    private TextView mTvFinish;
    private TextView mTvDelete;
    private TextView mTvAccounts;
    private SmoothCheckBox mCheckBox;
    private Toolbar mToolbar;
    private RecyclerView mRvList;
    private CommonAdapter mAdapter;
    private List<ShopCartInfoBean.CartItemsBean> mDatasCartItem = new ArrayList<>();
    private List<ShopCartInfoBean.CartItemsBean> mDatasCartListCopy = new ArrayList<>();

    private boolean isEditMode = false;

    private ShopCartInfoBean mShopCartBean;
    private List<Long> selectList = new ArrayList<>();


    @Override protected int getLayoutIdRefresh() {
        return R.layout.activity_act_shopping_cart;
    }


    @Override protected void initToolbarRefresh() {

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        initEmptyView();
        mEmptyLayout.setActionListener(this);
        showEmptyLayoutState(LOADING_STATE_SUCESS);
        //设置 toolbar 导航栏图片和点击事件
        mToolbar.setNavigationIcon(R.drawable.back_grey);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    @Override protected void initViewRefresh() {
        mTvEdit = (TextView) findViewById(R.id.id_act_shopcar_toolbar_edit);
        mTvFinish = (TextView) findViewById(R.id.id_act_shopcar_toolbar_finish);
        mTvDelete = (TextView) findViewById(R.id.id_act_shopping_cart_accounts_delete);
        mTvAccounts = (TextView) findViewById(R.id.id_act_shopping_cart_accounts);
        mCheckBox = (SmoothCheckBox) findViewById(R.id.id_shop_accounts_checkbox);

        mRvList = (RecyclerView) findViewById(R.id.id_act_shopping_cart_list);
        mRvList.setLayoutManager(new LinearLayoutManager(this));

        mTvEdit.setOnClickListener(this);
        mTvFinish.setOnClickListener(this);
        mTvDelete.setOnClickListener(this);
        mTvAccounts.setOnClickListener(this);

        mCheckBox.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                Logger.d("点击事件--" + isChecked);
            }
        });

        getShopCartList(1l);
        handListEvent();

        mCheckBox.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                if (isChecked) {
                    updateShopCartItemChenck(0L, mShopCartBean.getId(), true, true);
                } else {
                    updateShopCartItemChenck(0L, mShopCartBean.getId(), false, true);
                }
            }
        });
    }


    private void getShopCartList(Long memId) {
        HttpShopMethods.getInstance().getShopCartList(memId,
            new Subscriber<HttpResult<ShopCartInfoBean>>() {
                @Override public void onCompleted() {
                    mCheckBox.postDelayed(new Runnable() {
                        @Override public void run() {
                            mRefreshLayout.setRefreshing(false);
                        }
                    }, 1000);
                }


                @Override public void onError(Throwable e) {
                    mCheckBox.postDelayed(new Runnable() {
                        @Override public void run() {
                            mRefreshLayout.setRefreshing(false);
                        }
                    }, 1000);
                }


                @Override
                public void onNext(HttpResult<ShopCartInfoBean> httpResult) {

                    refreshList(httpResult);
                }
            });
    }


    /**
     * 购物车列表的点击事件处理
     */
    private void handListEvent() {

        selectList.clear();
        mRvList.setAdapter(
            mAdapter = new CommonAdapter<ShopCartInfoBean.CartItemsBean>(this,
                R.layout.item_shopping_cart_info, mDatasCartItem) {

                @Override
                protected void convert(ViewHolder holder, final ShopCartInfoBean.CartItemsBean cartItemsBean, final int position) {

                    ImageView ivImg = holder.getView(R.id.id_item_shop_home_iv_land);
                    ImageView ivCheck = holder.getView(R.id.id_item_shopcart_select);
                    TextView tvName = holder.getView(R.id.id_item_shop_home_product_tv_name);
                    TextView tvPrice = holder.getView(R.id.id_item_shop_home_product_tv_price);
                    QuantityView quantityView = holder.getView(
                        R.id.id_item_shop_home_product_tv_quantity);

                    Picasso.with(ActShoppingCart.this)
                        .load(cartItemsBean.getProduct().getImage())
                        .into(ivImg);
                    ivCheck.setImageResource(cartItemsBean.isIsChecked()
                                             ? R.drawable.choose_address_select
                                             : R.drawable.choose_address_normal);
                    tvName.setText(cartItemsBean.getProduct().getGoodsName());
                    tvPrice.setText("￥ " + cartItemsBean.getProduct().getPrice());
                    quantityView.setQuantity(cartItemsBean.getQuantity());

                    //修改 购物车每个商品数量
                    quantityView.setOnQuantityChangeListener(
                        new QuantityView.OnQuantityChangeListener() {
                            @Override
                            public void onQuantityChanged(int newQuantity, boolean programmatically) {
                                updateShopCartItemQuantity(cartItemsBean.getId(),
                                    mShopCartBean.getId(), newQuantity);
                            }


                            @Override public void onLimitReached() {
                            }
                        });

                    //修改 购物车商品是否选中
                    holder.setOnClickListener(R.id.id_item_shopcart_select,
                        new View.OnClickListener() {
                            @Override public void onClick(View v) {
                                if (isEditMode) {
                                    if (cartItemsBean.isIsChecked()) {
                                        cartItemsBean.setIsChecked(false);
                                        selectList.remove(Long.valueOf(cartItemsBean.getId()));
                                    } else {
                                        cartItemsBean.setIsChecked(true);
                                        selectList.add(Long.valueOf(cartItemsBean.getId()));
                                    }
                                    mAdapter.notifyItemChanged(position);
                                } else {
                                    updateShopCartItemChenck(cartItemsBean.getId(),
                                        mShopCartBean.getId(), !cartItemsBean.isIsChecked(), false);
                                }
                            }
                        });

                    holder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
                        @Override public boolean onLongClick(View v) {

                            new AlertDialog.Builder(ActShoppingCart.this).setTitle("是否删除当前商品")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();

                            return false;
                        }
                    });
                }
            });
    }


    boolean flag = false;


    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_act_shopcar_toolbar_edit:

                Iterator<ShopCartInfoBean.CartItemsBean> iterator = mDatasCartItem.iterator();
                while (iterator.hasNext()) {
                    ShopCartInfoBean.CartItemsBean next = iterator.next();
                    next.setIsChecked(false);
                }
                mAdapter.notifyDataSetChanged();
                selectList.clear();
                showLayoutEdit();
                break;
            case R.id.id_act_shopcar_toolbar_finish:

                //mDatasCartItem.clear();
                //mDatasCartItem.addAll(mDatasCartListCopy);
                //
                //mAdapter.notifyDataSetChanged();

                getShopCartList(1l);
                showLayoutAccounts();
                break;
            case R.id.id_act_shopping_cart_accounts:

                startOrderInfo();

                break;
            case R.id.id_act_shopping_cart_accounts_delete:
                new AlertDialog.Builder(ActShoppingCart.this).setTitle(
                    "是否删除选中的" + selectList.size() + "件商品")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteShopCartList(mShopCartBean.getId(), selectList);
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();

                break;

        }
    }


    /**
     *
     */
    private void startOrderInfo() {

        final List<String> selectProduct = new ArrayList<>();
        Observable.from(mDatasCartItem).filter(
            new Func1<ShopCartInfoBean.CartItemsBean, Boolean>() {
                @Override public Boolean call(ShopCartInfoBean.CartItemsBean cartItemsBean) {
                    return cartItemsBean.isIsChecked();
                }
            }).subscribe(new Action1<ShopCartInfoBean.CartItemsBean>() {
            @Override public void call(ShopCartInfoBean.CartItemsBean cartItemsBean) {
                selectProduct.add("");
            }
        });

        if (selectProduct.size()<1){
            ToastUtil.showText(this,"请至少选择一种商品");
            return;
        }

        Intent intent = new Intent(ActShoppingCart.this, ActShoppingCheckout.class);
        intent.putExtra(ActShoppingCheckout.EXTRA_CARTID,mShopCartBean.getId());
        intent.putExtra(ActShoppingCheckout.EXTRA_ORDER_SIZE,selectProduct.size());
        startActivity(intent);
    }


    private void updateShopCartItemQuantity(Long itemId, Long cartId, final int quantity) {
        HttpShopMethods.getInstance()
            .updateShopCartQuantity(new UpdateShopCartQuantityBody(itemId, quantity, cartId),
                new Subscriber<HttpResult<ShopCartInfoBean>>() {
                    @Override public void onCompleted() {
                    }


                    @Override public void onError(Throwable e) {
                        Logger.e("更新数量--" + quantity);
                    }


                    @Override
                    public void onNext(HttpResult<ShopCartInfoBean> shopCartInfoBeanHttpResult) {
                        if (!isEditMode){
                            refreshList(shopCartInfoBeanHttpResult);
                        }
                        Logger.v("更新onNext--" + shopCartInfoBeanHttpResult.errorCode);
                    }
                });
    }


    private void updateShopCartItemChenck(Long itemId, Long cartId, boolean isCheck, boolean isAll) {
        HttpShopMethods.getInstance()
            .updateShopCartCheck(new UpdateShopCartCheckBody(cartId, itemId, isCheck, isAll),
                new Subscriber<HttpResult<ShopCartInfoBean>>() {
                    @Override public void onCompleted() {
                    }


                    @Override public void onError(Throwable e) {
                    }


                    @Override
                    public void onNext(HttpResult<ShopCartInfoBean> shopCartInfoBeanHttpResult) {
                        refreshList(shopCartInfoBeanHttpResult);
                        Logger.v("更新onNext--" + shopCartInfoBeanHttpResult.toString());
                    }
                });
    }


    /**
     * 删除购物车
     */
    private void deleteShopCartList(Long cartId, List<Long> selectList) {
        HttpShopMethods.getInstance()
            .deleteShopCartList(new DeleteShopCartListBody(cartId, selectList),
                new Subscriber<HttpResult<ShopCartInfoBean>>() {
                    @Override public void onCompleted() {
                    }


                    @Override public void onError(Throwable e) {
                    }


                    @Override
                    public void onNext(HttpResult<ShopCartInfoBean> shopCartInfoBeanHttpResult) {
                        refreshList(shopCartInfoBeanHttpResult);
                        Logger.v("更新onNext--" + shopCartInfoBeanHttpResult.toString());

                        mTvFinish.performClick();
                    }
                });
    }


    /**
     * 购物车列表的变动后刷新动作
     */
    private void refreshList(HttpResult<ShopCartInfoBean> httpResult) {
        if (httpResult != null && httpResult.errorCode == 0) {

            mShopCartBean = httpResult.data;
            mDatasCartItem.clear();
            mDatasCartListCopy.clear();
            mDatasCartItem.addAll(httpResult.data.getCartItems());
            Collections.sort(mDatasCartItem);

            for (ShopCartInfoBean.CartItemsBean bean :
                mDatasCartItem) {
                ShopCartInfoBean.CartItemsBean newBean = new ShopCartInfoBean.CartItemsBean(
                    bean.getId(), bean.getQuantity(), bean.getProduct(), bean.isIsChecked());
                mDatasCartListCopy.add(newBean);
            }
            mAdapter.notifyDataSetChanged();
        }
    }


    @Override public void onRefresh() {
        super.onRefresh();

        getShopCartList(1l);
    }


    private void showLayoutAccounts() {

        isEditMode = false;
        mTvEdit.setVisibility(View.VISIBLE);
        mTvFinish.setVisibility(View.GONE);
        mTvAccounts.setVisibility(View.VISIBLE);
        mTvDelete.setVisibility(View.GONE);
    }


    private void showLayoutEdit() {

        isEditMode = true;
        mTvEdit.setVisibility(View.GONE);
        mTvFinish.setVisibility(View.VISIBLE);
        mTvAccounts.setVisibility(View.GONE);
        mTvDelete.setVisibility(View.VISIBLE);
    }


    // 去逛逛 执行的动作，去商城
    @Override public void onClickReLoadData() {

    }
}
