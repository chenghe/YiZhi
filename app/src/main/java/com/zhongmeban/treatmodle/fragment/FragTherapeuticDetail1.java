package com.zhongmeban.treatmodle.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;
import com.zhongmeban.R;
import com.zhongmeban.adapter.TherapeuticDetailAdapter;
import com.zhongmeban.base.BaseFragment;
import com.zhongmeban.bean.TherapeuticDetailBean;
import com.zhongmeban.bean.TherapeuticDetailTypeBean;
import com.zhongmeban.utils.Constants;
import com.zhongmeban.utils.DisplayUtil;
import com.zhongmeban.utils.ZMBUtil;
import com.zhongmeban.utils.genericity.ImageUrl;
import com.zhongmeban.view.ScrollLinearLayout;
import com.zhongmeban.view.TextViewExpandableAnimation;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: 1 代表手術詳情
 * user: Chong Chen
 * time：2016/1/7 15:50
 * 邮箱：cchen@ideabinder.com
 */
public class FragTherapeuticDetail1 extends BaseFragment {

    public static final String KEY = "shoushu";

    private TherapeuticDetailBean detailBean;
    private ScrollLinearLayout scrollLinearLayout;

    private TextViewExpandableAnimation mTvSummary;
    private TextView mTvName;
    private LinearLayout mLayoutVideo;

    public JCVideoPlayerStandard jcVideoPlayerStandard;
    private List<TherapeuticDetailBean.DataBean.QasBean> list1 = new ArrayList<>();
    private List<TherapeuticDetailBean.DataBean.QasBean> list2 = new ArrayList<>();
    private List<TherapeuticDetailBean.DataBean.QasBean> list3 = new ArrayList<>();
    private List<TherapeuticDetailBean.DataBean.QasBean> list4 = new ArrayList<>();
    private List<TherapeuticDetailBean.DataBean.QasBean> list5 = new ArrayList<>();
    private List<TherapeuticDetailBean.DataBean.QasBean> list6 = new ArrayList<>();
    private List<TherapeuticDetailBean.DataBean.QasBean> list7 = new ArrayList<>();
    private List<TherapeuticDetailBean.DataBean.QasBean> list8 = new ArrayList<>();
    private List<TherapeuticDetailTypeBean> mDatas = new ArrayList<>();


    public static FragTherapeuticDetail1 newInstance(TherapeuticDetailBean therapeuticDetail) {
        FragTherapeuticDetail1 fragment = new FragTherapeuticDetail1();
        Bundle args = new Bundle();
        args.putSerializable(KEY, therapeuticDetail);
        fragment.setArguments(args);
        Logger.i("NewFragment----------------");
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
        View view = inflater.inflate(R.layout.layout_treatment_content, container, false);

        initWidget(view);

        initData();
        return view;
    }


    public void initWidget(View view) {

        scrollLinearLayout = (ScrollLinearLayout) view.findViewById(
            R.id.id_list_therapeutic_detail);
        mTvSummary = (TextViewExpandableAnimation) view.findViewById(R.id.id_tv_summary);
        mLayoutVideo = (LinearLayout) view.findViewById(R.id.id_frag_interpretation_video_layout);
        mTvSummary.setText(detailBean.getData().getNotes());//概述
        TextViewExpandableAnimation tx = (TextViewExpandableAnimation) view.findViewById(R.id.id_tv_doctor_expend);
        mTvName = (TextView) view.findViewById(R.id.id_id_frag_interpretation_name);

        jcVideoPlayerStandard = (JCVideoPlayerStandard) view.findViewById(
            R.id.custom_videoplayer_standard);
        int dp16 = DisplayUtil.dip2px(getActivity(),16);

        float videoHeight = 9 * 1.0f / 16 * (ZMBUtil.getWidth()- dp16*2);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, (int) videoHeight);
        layoutParams.setMargins(dp16,dp16,dp16,dp16);
        jcVideoPlayerStandard.setLayoutParams(layoutParams);

        Logger.d("视频高度===" + videoHeight);

        //处理视频逻辑
        List<TherapeuticDetailBean.DataBean.ExtendDbsBean> beanList = detailBean.getData()
            .getExtendDbs();
        if (beanList == null || beanList.size() < 1||TextUtils.isEmpty(beanList.get(0).getUrl())) {

            mTvName.setVisibility(View.GONE);
            tx.setVisibility(View.GONE);

            jcVideoPlayerStandard.setUp("", JCVideoPlayerStandard.SCREEN_LAYOUT_LIST, " ");
            Picasso.with(getActivity())
                .load(R.drawable.video_comming)
                .fit()
                .into(jcVideoPlayerStandard.thumbImageView);
        } else {
            mTvName.setVisibility(View.VISIBLE);
            tx.setVisibility(View.VISIBLE);

            mTvName.setText(beanList.get(0).getTitle());
            tx.setText(beanList.get(0).getNotes());
            //tx.setText("我是很长的数据我是很长的数据我是很长的数据我是很长的数据我是很长的数据我是很长" +
            //    "的数据我是很长的数据我是很长的数据我是很长的数据我是很长的数据我是很长的数据我是很长的数据" +
            //    "我是很长的数据我是很长的数据我是很长的数据我是很长的数据我是很长的数据我是很长的数据我是很长的数据");

            TherapeuticDetailBean.DataBean.ExtendDbsBean bean = detailBean.getData()
                .getExtendDbs()
                .get(0);
            //tx.setText(bean.getNotes());
            //String mVideoUrl = bean.getUrl();
            //名医讲师 视频播放逻辑
            //String mVideoUrl = "http://7xt0mj.com2.z0.glb.clouddn.com/lianaidaren.v.640.480.mp4";


            String picUrl = beanList.get(0).getPicUrl();
            String urlVideo = beanList.get(0).getUrl();
            jcVideoPlayerStandard.setUp(TextUtils.isEmpty(urlVideo) ? "" : ImageUrl.BaseVideoUrl + urlVideo

                , JCVideoPlayerStandard.SCREEN_LAYOUT_LIST, " ");

            if (TextUtils.isEmpty(picUrl)) {
                Picasso.with(getActivity())
                    .load(R.drawable.video_default)
                    .fit()
                    .into(jcVideoPlayerStandard.thumbImageView);
            } else {
                Picasso.with(getActivity())
                    .load(ImageUrl.BaseVideoUrl+ beanList.get(0).getPicUrl())
                    .fit()
                    .into(jcVideoPlayerStandard.thumbImageView);
            }
        }

    }


    private void initData() {

        for (TherapeuticDetailBean.DataBean.QasBean bean : detailBean.getData().getQas()) {
            if (bean.getCategory() == Constants.TherapeuticQATypeResectionScope) {
                list1.add(bean);
            } else if (bean.getCategory() == Constants.TherapeuticQATypeAdvantage) {
                list2.add(bean);
            } else if (bean.getCategory() == Constants.TherapeuticQATypeDisadvantage) {
                list3.add(bean);
            } else if (bean.getCategory() == Constants.TherapeuticQATypeSurgeryPrepare) {
                list4.add(bean);
            } else if (bean.getCategory() == Constants.TherapeuticQATypeAnaesthesiaMethod) {
                list5.add(bean);
            } else if (bean.getCategory() == Constants.TherapeuticQATypeRoughlyFolw) {
                list6.add(bean);
            } else if (bean.getCategory() == Constants.TherapeuticQATypeSurgeryRecover) {
                list7.add(bean);
            } else if (bean.getCategory() == Constants.TherapeuticQATypeNurseCharacteristic) {
                list8.add(bean);
            }
        }

        mDatas.add(new TherapeuticDetailTypeBean("切除范围", "", "", R.drawable.nail3, 0));
        for (TherapeuticDetailBean.DataBean.QasBean bean : list1) {
            mDatas.add(
                new TherapeuticDetailTypeBean("", bean.getQuestion(), bean.getAnswer(), 0, 1));
        }
        mDatas.add(new TherapeuticDetailTypeBean("优势", "", "", R.drawable.nail2, 0));
        for (TherapeuticDetailBean.DataBean.QasBean bean : list2) {
            mDatas.add(
                new TherapeuticDetailTypeBean("", bean.getQuestion(), bean.getAnswer(), 0, 1));
        }
        mDatas.add(new TherapeuticDetailTypeBean("劣势", "", "", R.drawable.nail1, 0));
        for (TherapeuticDetailBean.DataBean.QasBean bean : list3) {
            mDatas.add(
                new TherapeuticDetailTypeBean("", bean.getQuestion(), bean.getAnswer(), 0, 1));
        }
        mDatas.add(new TherapeuticDetailTypeBean("术前准备", "", "", R.drawable.nail5, 0));
        for (TherapeuticDetailBean.DataBean.QasBean bean : list4) {
            mDatas.add(
                new TherapeuticDetailTypeBean("", bean.getQuestion(), bean.getAnswer(), 0, 1));
        }
        mDatas.add(new TherapeuticDetailTypeBean("麻醉方式", "", "", R.drawable.nail6, 0));
        for (TherapeuticDetailBean.DataBean.QasBean bean : list5) {
            mDatas.add(
                new TherapeuticDetailTypeBean("", bean.getQuestion(), bean.getAnswer(), 0, 1));
        }
        mDatas.add(new TherapeuticDetailTypeBean("大致流程", "", "", R.drawable.nail4, 0));
        for (TherapeuticDetailBean.DataBean.QasBean bean : list6) {
            mDatas.add(
                new TherapeuticDetailTypeBean("", bean.getQuestion(), bean.getAnswer(), 0, 1));
        }
        mDatas.add(new TherapeuticDetailTypeBean("术后恢复", "", "", R.drawable.nail3, 0));
        for (TherapeuticDetailBean.DataBean.QasBean bean : list7) {
            mDatas.add(
                new TherapeuticDetailTypeBean("", bean.getQuestion(), bean.getAnswer(), 0, 1));
        }
        mDatas.add(new TherapeuticDetailTypeBean("护理特点", "", "", R.drawable.nail2, 0));
        for (TherapeuticDetailBean.DataBean.QasBean bean : list8) {
            mDatas.add(
                new TherapeuticDetailTypeBean("", bean.getQuestion(), bean.getAnswer(), 0, 1));
        }

        scrollLinearLayout.setAdapter(new TherapeuticDetailAdapter(getActivity(), mDatas));
    }

}
