package com.zhongmeban.treatmodle.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.zhongmeban.R;
import com.zhongmeban.activity.ActPharmaceutiaclDetail;
import com.zhongmeban.adapter.TherapeuticDetailAdapter;
import com.zhongmeban.base.BaseFragment;
import com.zhongmeban.bean.TherapeuticDetailBean;
import com.zhongmeban.bean.TherapeuticDetailTypeBean;
import com.zhongmeban.utils.Constants;
import com.zhongmeban.view.ScrollLinearLayout;
import com.zhongmeban.view.TextViewExpandableAnimation;
import java.util.ArrayList;
import java.util.List;
import me.next.tagview.TagCloudView;

/**
 * Description: FragTherapeuticDetail----2 代表化疗
 * user: Chong Chen
 * time：2016/1/7 15:50
 * 邮箱：cchen@ideabinder.com
 */
public class FragTherapeuticDetail2 extends BaseFragment {

    public static final String KEY = "shoushu";

    private TherapeuticDetailBean detailBean;
    private ScrollLinearLayout scrollLinearLayout1;
    private ScrollLinearLayout scrollLinearLayout2;
    private ScrollLinearLayout scrollLinearLayout3;

    private TextViewExpandableAnimation mTvSummary;
    private TextView mTvPlan;
    private TagCloudView tagCloudView;
    private List<String > tagList = new ArrayList<>();


    private List<TherapeuticDetailBean.DataBean.QasBean> list1 = new ArrayList<>();
    private List<TherapeuticDetailBean.DataBean.QasBean> list2 = new ArrayList<>();
    private List<TherapeuticDetailBean.DataBean.QasBean> list3 = new ArrayList<>();

    private List<TherapeuticDetailTypeBean> mDatas1 = new ArrayList<>();
    private List<TherapeuticDetailTypeBean> mDatas2 = new ArrayList<>();
    private List<TherapeuticDetailTypeBean> mDatas3 = new ArrayList<>();

    public static FragTherapeuticDetail2 newInstance(TherapeuticDetailBean therapeuticDetail) {
        FragTherapeuticDetail2 fragment = new FragTherapeuticDetail2();
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
        View view = inflater.inflate(R.layout.frag_therapeutic_detail2, container, false);

        initWidget(view);

        initData();
        return view;
    }


    public void initWidget(View view) {

        scrollLinearLayout1 = (ScrollLinearLayout) view.findViewById(R.id.id_list_therapeutic_detail2_preper);
        scrollLinearLayout2 = (ScrollLinearLayout) view.findViewById(R.id.id_list_therapeutic_detail2_effect);
        scrollLinearLayout3 = (ScrollLinearLayout) view.findViewById(R.id.id_list_therapeutic_detail2_attention);
        mTvSummary  = (TextViewExpandableAnimation) view.findViewById(R.id.id_frag_therapeutic_summary);
        mTvPlan  = (TextView) view.findViewById(R.id.id_frag_therapeutic_plan);
        tagCloudView = (TagCloudView) view.findViewById(R.id.id_frag_therapeutic_tag);

        mTvSummary.setText(detailBean.getData().getNotes());
        mTvPlan.setText(detailBean.getData().getCaseType()+"线方案");

        for (TherapeuticDetailBean.DataBean.MedicineDbsBean bean:detailBean.getData().getMedicineDbs()) {
            tagList.add(bean.getMedicineName());
        }
        tagCloudView.setTags(tagList);
        tagCloudView.setOnTagClickListener(onTagClickListener);

    }

    /**
     * Tag 点击事件 方案用药的点击事件，数据有好几个id不知道 用哪个
     */
    TagCloudView.OnTagClickListener onTagClickListener = new TagCloudView.OnTagClickListener() {
        @Override
        public void onTagClick(int position) {

            String inspectionId = detailBean.getData().getMedicineDbs().get(position).getMedicineId()+"";

            Intent intent = new Intent(getActivity(), ActPharmaceutiaclDetail.class);
            intent.putExtra(ActPharmaceutiaclDetail.EXTRA_MEDICAL_ID, inspectionId);
            startActivity(intent);
        }
    };

    private void initData() {

        for (TherapeuticDetailBean.DataBean.QasBean bean :detailBean.getData().getQas()) {
            if (bean.getCategory()== Constants.TherapeuticQATypeTreatPrepare){
                list1.add(bean);
            } else if (bean.getCategory()==Constants.TherapeuticQATypeTreatAttention){
                list3.add(bean);
            }else if (bean.getCategory()==Constants.TherapeuticQATypeBadReaction){
                list2.add(bean);
            }
        }


        for (TherapeuticDetailBean.DataBean.QasBean bean :list1) {
            mDatas1.add(new TherapeuticDetailTypeBean("",bean.getQuestion(),bean.getAnswer(),0,1) );
        }
        for (TherapeuticDetailBean.DataBean.QasBean bean :list2) {
            mDatas2.add(new TherapeuticDetailTypeBean("",bean.getQuestion(),bean.getAnswer(),0,1) );
        }
        for (TherapeuticDetailBean.DataBean.QasBean bean :list3) {
            mDatas3.add(new TherapeuticDetailTypeBean("",bean.getQuestion(),bean.getAnswer(),0,1) );
        }


        scrollLinearLayout1.setAdapter(new TherapeuticDetailAdapter(getActivity(),mDatas1));
        scrollLinearLayout2.setAdapter(new TherapeuticDetailAdapter(getActivity(),mDatas2));
        scrollLinearLayout3.setAdapter(new TherapeuticDetailAdapter(getActivity(),mDatas3));
    }




}
