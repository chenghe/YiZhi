package com.zhongmeban.treatmodle.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.zhongmeban.R;
import com.zhongmeban.treatmodle.activity.ActDoctDetail;
import com.zhongmeban.treatmodle.activity.ActKnowTumorDetail;
import com.zhongmeban.utils.ScrollView.BaseScrollFragment;
import com.zhongmeban.utils.ScrollView.ObservableScrollView;
import com.zhongmeban.utils.ScrollView.ObservableScrollViewCallbacks;
import com.zhongmeban.view.TextViewExpandableAnimation;
import java.util.List;
import me.next.tagview.TagCloudView;

/**
 * 医生详情基本Fragment
 * Created by Chengbin He on 2016/5/10.
 */
public class FragmentDoctDetail extends BaseScrollFragment implements TagCloudView.OnTagClickListener{

    private TextViewExpandableAnimation tvDescription;//执业经历
    private TextView tvHospName;//执业地点
    private ActDoctDetail parentActivity;
    private TagCloudView  tagCloudView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentActivity = (ActDoctDetail) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doct_detail, container, false);
        final ObservableScrollView scrollView = (ObservableScrollView) view.findViewById(R.id.scroll);
        scrollView.setTouchInterceptionViewGroup((ViewGroup) parentActivity.findViewById(R.id.container));
        if (parentActivity instanceof ObservableScrollViewCallbacks) {
            scrollView.setScrollViewCallbacks((ObservableScrollViewCallbacks) parentActivity);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvDescription = (TextViewExpandableAnimation) view.findViewById(R.id.tv_content3);
        //tvDescription.setText(getString(R.string.expend_test_content));

        tvHospName = (TextView) view.findViewById(R.id.tv_content1);
        tagCloudView = (TagCloudView) view.findViewById(R.id.tag_cloud_view);
//        tagCloudView.setOnTagClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void setDescription(String description){
        tvDescription.setText(description);

    }

    public void setHospName(String hospName){
        tvHospName.setText(hospName);
    }

    public void setTags(List<String> list){
        tagCloudView.setTags(list);
    }

    /**
     * Tag 点击事件
     * @param position
     */
    @Override
    public void onTagClick(int position) {
        String diseaseId = parentActivity.diseasesList.get(position).getId()+"";
        Intent intent = new Intent(parentActivity, ActKnowTumorDetail.class);
        intent.putExtra("diseaseId",diseaseId);
        startActivity(intent);
    }
}
