package com.zhongmeban.mymodle.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zhongmeban.R;
import com.zhongmeban.shopmodle.activity.ActAddressList;
import com.zhongmeban.activity.ActivityCollected;
import com.zhongmeban.mymodle.activity.ActivityMyInfo;
import com.zhongmeban.activity.ActivityOrderList;
import com.zhongmeban.activity.ActivitySetting;
import com.zhongmeban.base.BaseFragment;

public class FragmentMyShop extends BaseFragment {

    private RelativeLayout rl_address;//地址管理
    private RelativeLayout rl_mycommidity;//我的收藏的商品
    private RelativeLayout rl_mycollected;//我收藏的内容
    private RelativeLayout rl_setting;//设置
    private RelativeLayout rl_shop_car;//购物车
    private RelativeLayout rlMyOrder;//我的订单
    private LinearLayout ll_mine;
    private Context mContext;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getActivity();
        initView(view);
    }

    private void initView(View view) {
        ll_mine = (LinearLayout) view.findViewById(R.id.ll_mine);
        ll_mine.setOnClickListener(onClickListener);
        rl_address = (RelativeLayout) view.findViewById(R.id.rl_address);
        rl_address.setOnClickListener(onClickListener);
        rl_mycommidity = (RelativeLayout) view.findViewById(R.id.rl_my_commodity);
        rl_mycommidity.setOnClickListener(onClickListener);
        rl_mycollected = (RelativeLayout) view.findViewById(R.id.rl_mycollected);
        rl_mycollected.setOnClickListener(onClickListener);
        rl_setting = (RelativeLayout) view.findViewById(R.id.rl_setting);
        rl_setting.setOnClickListener(onClickListener);
        rl_shop_car = (RelativeLayout) view.findViewById(R.id.rl_shop_car);
        rl_shop_car.setOnClickListener(onClickListener);

        rlMyOrder = (RelativeLayout) view.findViewById(R.id.rl_my_order);
        rlMyOrder.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            switch (v.getId()) {
                case R.id.ll_mine://登陆
                    Log.i("hcb", "R.id.ll_mine: click");
                    intent.setClass(mContext, ActivityMyInfo.class);
                    startActivity(intent);
                    break;

                case R.id.rl_mycollected://我的收藏
                    Log.i("hcb","R.id.rl_mycollected: click");
                    intent.setClass(mContext, ActivityCollected.class);
                    startActivity(intent);
                    break;

                case R.id.rl_setting://应用设置
                    Log.i("hcb","R.id.rl_setting: click");
                    intent.setClass(mContext, ActivitySetting.class);
                    startActivity(intent);
                    break;

                case R.id.rl_my_order://我的订单
                    intent.setClass(mContext, ActivityOrderList.class);
                    startActivity(intent);
                    break;

                case R.id.rl_address://地址管理
                    intent.setClass(mContext, ActAddressList.class);
                    startActivity(intent);
                    break;
            }

        }
    };
}
