package com.zhongmeban.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.zhongmeban.R;
import com.zhongmeban.base.BaseFragment;
import com.zhongmeban.base.FragmentFactory;
import com.zhongmeban.utils.ZMBUtil;
import com.zhongmeban.view.XListView;
import com.zhongmeban.view.XListView.IXListViewListener;

/**
 * Description:frag之医生详情中的出诊
 * user: Chong Chen
 * time：2016/1/6 13:36
 * 邮箱：cchen@ideabinder.com
 */
public class FragDoctDetilVisit extends BaseFragment implements OnItemClickListener, IXListViewListener {

    public static final String TAG = FragDoctDetilVisit.class.getSimpleName();

    static {
        FragmentFactory.getInstance().addFragmen(TAG, FragDoctDetilVisit.class);
    }

    public static FragDoctDetilVisit newInstance() {
        return new FragDoctDetilVisit();
    }

    private XListView mContentList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_inspection, container, false);

        initWidget(view);
        return view;
    }


    public void initWidget(View view) {
        mContentList = (XListView) view.findViewById(R.id.content_list);
        mContentList.setPullLoadEnable(true);
        mContentList.setXListViewListener(this);
        mContentList.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //此处跳向详情页面
        //点击item可以跳act去显示详情


    }

    private void onLoad() {
        mContentList.stopRefresh();
        mContentList.stopLoadMore();
        mContentList.setRefreshTime(ZMBUtil.getLastUpdateTime());
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }
}
