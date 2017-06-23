package com.zhongmeban.treatmodle.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhongmeban.R;
import com.zhongmeban.treatmodle.activity.ActTipsDetails;
import com.zhongmeban.base.BaseFragment;
import com.zhongmeban.base.FragmentFactory;
import com.zhongmeban.bean.TipsLists;
import com.zhongmeban.utils.StringUtils;
import com.zhongmeban.utils.ZMBUtil;
import com.zhongmeban.view.XListView;
import com.zhongmeban.view.XListView.IXListViewListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * user: Chong Chen
 * time：2016/1/7 18:38
 * 邮箱：cchen@ideabinder.com
 */
public class FragTipsCollected extends BaseFragment implements OnItemClickListener, IXListViewListener {

    public static final String TAG = FragTipsCollected.class.getSimpleName();

    static {
        FragmentFactory.getInstance().addFragmen(TAG, FragTipsCollected.class);
    }

    public static FragTipsCollected newInstance() {
        return new FragTipsCollected();
    }

    private XListView mContentList;
    private List<TipsLists.TipDataScourceItem> mDataLists = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_inspection, container, false);

        initWidget(view);
        return view;
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
        TipsLists.TipDataScourceItem tips = (TipsLists.TipDataScourceItem) parent.getAdapter().getItem(position);
        Intent intent = new Intent(getActivity(), ActTipsDetails.class);
        intent.putExtra("tips", tips);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.act_entry_from_right, R.anim.act_out_from_left);
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
            return mDataLists.size();
        }

        @Override
        public Object getItem(int position) {
            return mDataLists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_tips, null);
                holder = new ViewHolder();
                /*得到各个控件的对象*/
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
                holder.tvAbstract = (TextView) convertView.findViewById(R.id.tv_abstract);
                holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
                convertView.setTag(holder); //绑定ViewHolder对象
            }else {
                holder = (ViewHolder) convertView.getTag(); //取出ViewHolder对象
            }
            TipsLists.TipDataScourceItem tip = (TipsLists.TipDataScourceItem) getItem(position);
            holder.tvTitle.setText(tip.getTitle());
            holder.tvTime.setText(StringUtils.formateDate(tip.getTime()));
            return convertView;
        }
    }

    /*存放控件 的ViewHolder*/
    final class ViewHolder {
        TextView tvTitle;//标题
        TextView tvAbstract;//摘要
        TextView tvTime;//时间
    }
}
