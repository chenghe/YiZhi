package com.zhongmeban.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.zhongmeban.R;
import com.zhongmeban.base.BaseRecyclerViewAdapter;
import com.zhongmeban.base.baseadapterlist.CommonAdapter;
import com.zhongmeban.base.baseadapterlist.ViewHolder;
import com.zhongmeban.utils.TimeUtils;
import com.zhongmeban.view.MyListView;
import de.greenrobot.dao.attention.MarkerSource;
import de.greenrobot.dao.attention.RecordIndex;
import java.util.List;

/**
 * 关注，标志物记录Adapter
 * Created by Chengbin He on 2016/6/28.
 */
public class MarkerListAdapter extends BaseRecyclerViewAdapter {

    private Context mContext;
    private List<MarkerSource> list;


    public MarkerListAdapter(Context mContext, List<MarkerSource> list) {
        this.mContext = mContext;
        this.list = list;
    }


    public void updateDate(List<MarkerSource> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_marker_new, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        MyHolder myHolder = (MyHolder) holder;
        List<RecordIndex> indexList = list.get(position).getIndexList();

        myHolder.tvMarkerHosName.setText(list.get(position).getIndexItem().getHospitalName());
        myHolder.tvTime.setText(TimeUtils.formatTime(list.get(position).getIndexItem().getTime()));

        if (indexList != null && indexList.size() > 3) {
            indexList = indexList.subList(0, 3);
        }
        myHolder.listView.setAdapter(
            new CommonAdapter<RecordIndex>(mContext, R.layout.item_item_marker_info, indexList) {
                @Override
                protected void convert(ViewHolder viewHolder, RecordIndex item, int pos) {
                    TextView tvAbnormal = viewHolder.getView(R.id.tv_marker_id_abnormal);
                    TextView tvName = viewHolder.getView(R.id.tv_marker_name);
                    tvName.setText(item.getIndexName());
                    viewHolder.setText(R.id.tv_marker_id_normal,item.getNormalMin() + "-" + item.getNormalMax() );
                    tvAbnormal.setText(item.getValue() + item.getUnitName());
                    tvAbnormal.setTextColor(item.getStatus() == 2  ? ContextCompat.getColor(mContext, R.color.text_red) : ContextCompat.getColor(mContext,
                                                R.color.content_two));

                    viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
                        @Override public void onClick(View v) {
                            if (mClick != null) {
                                mClick.onItemClick(v, position);
                            }
                        }
                    });
                }
            });
        //if (list.get(position).getStatusList().size()>0){
        //    myHolder.tvAbnormal.setVisibility(View.VISIBLE);
        //    myHolder.tvAbnormal.setText(list.get(position).getStatusList().size()+"项异常");
        //}else {
        //    myHolder.tvAbnormal.setVisibility(View.GONE);
        //}

        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClick != null) {
                    mClick.onItemClick(v, position);
                }
            }
        });
    }


    class MyHolder extends RecyclerView.ViewHolder {
        public TextView tvMarkerHosName;
        public TextView tvTime;
        private MyListView listView;


        public MyHolder(View itemView) {
            super(itemView);
            tvMarkerHosName = (TextView) itemView.findViewById(R.id.tv_marker_hos_name);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            listView = (MyListView) itemView.findViewById(R.id.lv_marker_info);
        }
    }

}
