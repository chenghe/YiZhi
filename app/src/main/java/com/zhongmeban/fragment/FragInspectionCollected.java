package com.zhongmeban.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongmeban.R;
import com.zhongmeban.treatmodle.activity.ActInspectionDetail;
import com.zhongmeban.base.BaseFragment;
import com.zhongmeban.base.FragmentFactory;
import com.zhongmeban.bean.InspectionLists;
import com.zhongmeban.utils.ZMBUtil;
import com.zhongmeban.view.XListView;
import com.zhongmeban.view.XListView.IXListViewListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:检查项目-收藏页面
 * user: Chong Chen
 * time：2016/1/5 19:num2
 * 邮箱：cchen@ideabinder.com
 */
public class FragInspectionCollected extends BaseFragment implements OnItemClickListener, IXListViewListener {

    public static final String TAG = FragInspectionCollected.class.getSimpleName();

    static {
        FragmentFactory.getInstance().addFragmen(TAG, FragInspectionCollected.class);
    }

    public static FragInspectionCollected newInstance() {
        return new FragInspectionCollected();
    }

    private XListView mContentList;
    private List<InspectionLists.InspectionScource> inspectionLists = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_inspection, container, false);
        initWidget(view);
        getInspectionData();
        return view;
    }

    private void getInspectionData() {
        //此方法从数据库中获取到收藏的InspectionScource对象的列表
        inspectionLists.clear();
    }

    public void initWidget(View view) {
        mContentList = (XListView) view.findViewById(R.id.content_list);
        mContentList.setPullLoadEnable(true);
        mAllAdapter = new MyAdapter(getContext());
        mContentList.setAdapter(mAllAdapter);
        mContentList.setXListViewListener(this);
        mContentList.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //点击item跳act去显示详情
        Intent intent = new Intent(getActivity(), ActInspectionDetail.class);
        intent.putExtra("title", "检查项目详情");
        startActivity(intent);
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

    private MyAdapter mAllAdapter;
    private class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater; //得到一个LayoutInfalter对象用来导入布局

        public MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return inspectionLists.size();
        }

        @Override
        public Object getItem(int position) {
            return inspectionLists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_inspection, null);
                holder = new ViewHolder();
                /*得到各个控件的对象*/
                holder.collect = (ImageView) convertView.findViewById(R.id.image_collect);
                holder.nounName = (TextView) convertView.findViewById(R.id.tv_inspection);

                convertView.setTag(holder); //绑定ViewHolder对象
            } else {
                holder = (ViewHolder) convertView.getTag(); //取出ViewHolder对象
            }

            InspectionLists.InspectionScource inspection = (InspectionLists.InspectionScource) getItem(position);
            holder.collect.setImageResource(R.drawable.collected);
            holder.nounName.setText(inspection.getName());
            holder.collect.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            return convertView;
        }
    }

    /*存放控件 的ViewHolder*/
    final class ViewHolder {
        ImageView collect;
        TextView nounName;
    }

}
