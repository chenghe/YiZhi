package com.zhongmeban.treatmodle.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.zhongmeban.R;
import com.zhongmeban.treatmodle.activity.ActHospDetail;
import com.zhongmeban.utils.ScrollView.BaseScrollFragment;
import com.zhongmeban.utils.ScrollView.ObservableScrollView;
import com.zhongmeban.utils.ScrollView.ObservableScrollViewCallbacks;
import com.zhongmeban.view.TextViewExpandableAnimation;

/**
 * Description:医院详情 基本页Fragment
 * Created by Chengbin He on 2016/4/6.
 */
public class FragmentHospDetail extends BaseScrollFragment {

    public String hospLevel ="";
    public String hospAddress ="";
    private String hospitalId;
    private TextView tvHospLevel;//医院等级
    private TextView tvHospAddress;//医院地址
    private TextView tvHospInfo;//医院信息
    private TextViewExpandableAnimation tvHospDescription;//医院简介

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctview, container, false);

        final ObservableScrollView scrollView = (ObservableScrollView) view.findViewById(R.id.scroll);
        Activity parentActivity = getActivity();
        scrollView.setTouchInterceptionViewGroup((ViewGroup) parentActivity.findViewById(R.id.container));
        if (parentActivity instanceof ObservableScrollViewCallbacks) {
            scrollView.setScrollViewCallbacks((ObservableScrollViewCallbacks) parentActivity);
        }

            ActHospDetail mActivity = (ActHospDetail) parentActivity;
            hospLevel = mActivity.hospLevel;
            hospAddress = mActivity.hospAddress;

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvHospLevel = (TextView) view.findViewById(R.id.tv_content3);
        tvHospLevel.setText(hospLevel);
        tvHospAddress = (TextView) view.findViewById(R.id.tv_content1);
        tvHospAddress.setText(hospAddress);
        tvHospInfo = (TextView) view.findViewById(R.id.tv_content2);
        tvHospDescription = (TextViewExpandableAnimation) view.findViewById(R.id.tv_content4);

    }

    public void setHospInfo(String hospInfo){
        tvHospInfo.setText(hospInfo);
    }

    public void setHospDescription(String hospDescription ){
        tvHospDescription.setText(hospDescription);
    }


}
