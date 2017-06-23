package com.zhongmeban.treatmodle.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.zhongmeban.R;
import com.zhongmeban.adapter.TherapeuticDetailAdapter;
import com.zhongmeban.base.BaseFragment;
import com.zhongmeban.bean.TherapeuticDetailBean;
import com.zhongmeban.bean.TherapeuticDetailTypeBean;
import com.zhongmeban.utils.Constants;
import com.zhongmeban.view.ScrollLinearLayout;
import com.zhongmeban.view.TextViewExpandableAnimation;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: 3 代表放疗
 * user: Chong Chen
 * time：2016/1/7 15:50
 * 邮箱：cchen@ideabinder.com
 */
public class FragTherapeuticDetail3 extends BaseFragment {

    public static final String KEY = "shoushu";

    private TherapeuticDetailBean detailBean;
    private ScrollLinearLayout scrollLinearLayout;

    private TextViewExpandableAnimation mTvSummary;


    private List<TherapeuticDetailBean.DataBean.QasBean> list1 = new ArrayList<>();
    private List<TherapeuticDetailBean.DataBean.QasBean> list2 = new ArrayList<>();
    private List<TherapeuticDetailBean.DataBean.QasBean> list3 = new ArrayList<>();
    private List<TherapeuticDetailBean.DataBean.QasBean> list4 = new ArrayList<>();
    private List<TherapeuticDetailTypeBean> mDatas = new ArrayList<>();

    public static FragTherapeuticDetail3 newInstance(TherapeuticDetailBean therapeuticDetail) {
        FragTherapeuticDetail3 fragment = new FragTherapeuticDetail3();
        Bundle args = new Bundle();
        args.putSerializable(KEY,therapeuticDetail);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            detailBean = (TherapeuticDetailBean) getArguments().getSerializable(KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_therapeutic_detail3, container, false);

        initWidget(view);

        initData();
        return view;
    }


    public void initWidget(View view) {

        scrollLinearLayout = (ScrollLinearLayout) view.findViewById(R.id.id_list_therapeutic_detail3);
        mTvSummary  = (TextViewExpandableAnimation) view.findViewById(R.id.id_tv_summary);

        mTvSummary.setText(detailBean.getData().getNotes());
    }

    private void initData() {

        for (TherapeuticDetailBean.DataBean.QasBean bean :detailBean.getData().getQas()) {
            if (bean.getCategory()== Constants.TherapeuticQATypeTreatPrepare){
                list1.add(bean);
            } else if (bean.getCategory()==Constants.TherapeuticQATypeTreatProcess){
                list2.add(bean);
            }else if (bean.getCategory()==Constants.TherapeuticQATypeTreatAttention){
                list3.add(bean);
            }else if (bean.getCategory()==Constants.TherapeuticQATypeBadReaction){
                list4.add(bean);
            }
        }

        mDatas.add(new TherapeuticDetailTypeBean("治疗准备","","",R.drawable.nail4,0));
        for (TherapeuticDetailBean.DataBean.QasBean bean :list1) {
            mDatas.add(new TherapeuticDetailTypeBean("",bean.getQuestion(),bean.getAnswer(),0,1) );
        }
        mDatas.add(new TherapeuticDetailTypeBean("治疗过程","","",R.drawable.nail3,0));
        for (TherapeuticDetailBean.DataBean.QasBean bean :list2) {
            mDatas.add(new TherapeuticDetailTypeBean("",bean.getQuestion(),bean.getAnswer(),0,1) );
        }
        mDatas.add(new TherapeuticDetailTypeBean("治疗注意","","",R.drawable.nail2,0));
        for (TherapeuticDetailBean.DataBean.QasBean bean :list3) {
            mDatas.add(new TherapeuticDetailTypeBean("",bean.getQuestion(),bean.getAnswer(),0,1) );
        }
        mDatas.add(new TherapeuticDetailTypeBean("不良反应","","",R.drawable.nail1,0));
        for (TherapeuticDetailBean.DataBean.QasBean bean :list4) {
            mDatas.add(new TherapeuticDetailTypeBean("",bean.getQuestion(),bean.getAnswer(),0,1) );
        }




        scrollLinearLayout.setAdapter(new TherapeuticDetailAdapter(getActivity(),mDatas));
    }




}
