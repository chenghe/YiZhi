package com.zhongmeban.shopmodle.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.orhanobut.logger.Logger;
import com.zhongmeban.R;
import com.zhongmeban.base.BaseActivityToolBar;
import com.zhongmeban.base.baseadapterlist.CommonAdapter;
import com.zhongmeban.base.baseadapterlist.ViewHolder;
import com.zhongmeban.bean.HttpResult;
import com.zhongmeban.bean.shop.FirmOrderBean;
import com.zhongmeban.net.HttpShopMethods;
import com.zhongmeban.utils.SmoothCheckBox;
import com.zhongmeban.utils.ToastUtil;
import com.zhongmeban.utils.pay.AlipayAPI;
import com.zhongmeban.utils.pay.IPayResult;
import com.zhongmeban.utils.pay.PayResult;
import it.sephiroth.android.library.widget.AdapterView;
import it.sephiroth.android.library.widget.HListView;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import rx.Subscriber;

public class ActShoppingCheckout extends BaseActivityToolBar
    implements View.OnClickListener {

    public static final String EXTRA_CARTID = "cartId";
    public static final String EXTRA_ORDER_SIZE = "extra_order_size";


    private TextView mCheckOut;
    private SmoothCheckBox mCheckZhi;
    private SmoothCheckBox mCheckWeiXin;

    private TextView mTvAddressName;
    private TextView mTvAddressPhone;
    private TextView mTvAddressDetail;

    private TextView mTvOrderListSize;
    private TextView mTvOrderAmount;
    private TextView mTvOrderFreight;
    private TextView mTvOrderTotal;
    private AlipayAPI alipayAPI;
    private RelativeLayout mLayoutProductList;//多个商品列表展示的父窗体
    private RelativeLayout mLayoutAddressList;//收货地址父窗体，点击后进入地址列表
    private LinearLayout  mLayoutProductOne;//收货地址父窗体，点击后进入地址列表
    private HListView mHListView;
    private CommonAdapter mAdapter;

    private List<FirmOrderBean.OrderItemsBean> mDataOrders = new ArrayList<>();
    private FirmOrderBean mOrderInfoBean;

    private Long cartId;
    private int orderSize;

    @Override protected int getLayoutId() {
        return R.layout.activity_act_shopping_chenkout;
    }


    @Override protected void initToolbar() {

        initToolbarCustom("收银台或者订单详情", "");
    }


    @Override protected void initView() {

        mCheckOut = (TextView) findViewById(R.id.id_act_checkout_order);
        mTvAddressName = (TextView) findViewById(R.id.id_act_checkout_order_name);
        mTvAddressPhone= (TextView) findViewById(R.id.id_act_checkout_order_phone);
        mTvAddressDetail= (TextView) findViewById(R.id.id_act_checkout_order_address);

        mTvOrderListSize= (TextView) findViewById(R.id.id_act_checkout_order_list_size);
        mTvOrderAmount= (TextView) findViewById(R.id.id_act_checkout_order_amount);
        mTvOrderFreight= (TextView) findViewById(R.id.id_act_checkout_order_freight);
        mTvOrderTotal= (TextView) findViewById(R.id.id_act_checkout_order_total);

        mCheckZhi = (SmoothCheckBox) findViewById(R.id.id_act_checkout_pay_zhi);
        mCheckWeiXin = (SmoothCheckBox) findViewById(R.id.id_act_checkout_pay_weixin);
        mLayoutProductList = (RelativeLayout) findViewById(
            R.id.id_act_checkout_order_layout_product_list);
        mLayoutAddressList = (RelativeLayout) findViewById(
            R.id.id_act_checkout_order_layout_address_list);
        mLayoutProductOne = (LinearLayout) findViewById(
            R.id.id_act_checkout_order_layout_product_one);
        mHListView = (HListView) findViewById(R.id.id_act_checkout_order_listview_product_list);

        mCheckOut.setOnClickListener(this);
        mCheckZhi.setOnClickListener(this);
        mCheckWeiXin.setOnClickListener(this);
        mLayoutProductList.setOnClickListener(this);
        mLayoutAddressList.setOnClickListener(this);
        mCheckZhi.setChecked(true);
        mCheckWeiXin.setChecked(false);

        mHListView.setAdapter(
            mAdapter =new CommonAdapter<FirmOrderBean.OrderItemsBean>(this, R.layout.item_checkout_product_img, mDataOrders) {
                @Override protected void convert(ViewHolder viewHolder, FirmOrderBean.OrderItemsBean item, int position) {

                }
            });

        mHListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mLayoutProductList.performClick();
            }
        });

        getCheckOutInfo(10l,1l);
        cartId = getIntent().getLongExtra(EXTRA_CARTID,0);
        orderSize = getIntent().getIntExtra(EXTRA_ORDER_SIZE,0);
        showOrderListOrOne(orderSize);
    }


    @Override protected void initTitle() {

    }

    private void getCheckOutInfo(Long cartId,Long memId){
        HttpShopMethods.getInstance().getCheckOrderInfo(memId, cartId,
            new Subscriber<HttpResult<FirmOrderBean>>() {
                @Override public void onCompleted() {
                }

                @Override public void onError(Throwable e) {

                }
                @Override public void onNext(HttpResult<FirmOrderBean> firmOrderBeanHttpResult) {
                    if (firmOrderBeanHttpResult!=null&&firmOrderBeanHttpResult.errorCode==0){
                        mOrderInfoBean = firmOrderBeanHttpResult.data;
                        mDataOrders.addAll(firmOrderBeanHttpResult.data.getOrderItems());

                        updateUIInfo(mOrderInfoBean);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            });
    }

    private void updateUIInfo(FirmOrderBean firmOrderBean){
        mTvAddressName.setText(firmOrderBean.getConsignee());
        mTvAddressPhone.setText(firmOrderBean.getPhone());
        mTvAddressDetail.setText(firmOrderBean.getAreaName());


    }

    private void showOrderListOrOne(int ordersize){
        if (ordersize==1){
            mLayoutProductOne.setVisibility(View.VISIBLE);
            mLayoutProductList.setVisibility(View.GONE);

        } else{
            mLayoutProductOne.setVisibility(View.GONE);
            mLayoutProductList.setVisibility(View.VISIBLE);
            mTvOrderListSize.setText("共"+ordersize+"件");
        }
    }

    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_act_checkout_order:
                if (mCheckZhi.isChecked()) {
                    Logger.d("支付宝");
                } else {
                    Logger.v("weixin");
                }

                aLiPay();
                break;
            case R.id.id_act_checkout_pay_zhi:
                selectPayMode(true);
                break;
            case R.id.id_act_checkout_pay_weixin:
                selectPayMode(false);
                break;
            case R.id.id_act_checkout_order_layout_product_list://商品清单父布局点击事件
                startActivity(new Intent(ActShoppingCheckout.this, ActOrderProductList.class));
                break;
            case R.id.id_act_checkout_order_layout_address_list://收货地址父布局点击事件
                startActivity(new Intent(ActShoppingCheckout.this, ActAddressSelect.class));
                break;

        }
    }


    public void selectPayMode(boolean isZhiFuBao) {
        if (isZhiFuBao) {
            if (mCheckZhi.isChecked()) {
                return;
            } else {
                mCheckZhi.setChecked(true, true);
                mCheckWeiXin.setChecked(false, true);
            }
        } else {
            if (mCheckWeiXin.isChecked()) {
                return;
            } else {
                mCheckZhi.setChecked(false, true);
                mCheckWeiXin.setChecked(true, true);
            }
        }
    }


    public void aLiPay() {
        if (alipayAPI == null) alipayAPI = new AlipayAPI(this);
        alipayAPI.pay(new IPayResult() {
            @Override public void payResult(Map<String, String> result) {

                PayResult payResult = new PayResult(result);
                /**
                 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                 */
                String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                String resultStatus = payResult.getResultStatus();
                // 判断resultStatus 为9000则代表支付成功
                if (TextUtils.equals(resultStatus, "9000")) {
                    // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                    ToastUtil.showText(ActShoppingCheckout.this, "支付成功");
                } else {
                    // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                    ToastUtil.showText(ActShoppingCheckout.this, "支付失败");
                }

            }

        });
    }

}
