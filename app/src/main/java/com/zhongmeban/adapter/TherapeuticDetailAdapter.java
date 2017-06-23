package com.zhongmeban.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.zhongmeban.R;
import com.zhongmeban.bean.TherapeuticDetailTypeBean;
import java.util.List;

/**
 * 癌症分期部分Adapter
 * Created by Chengbin He on 2016/5/18.
 */
public class TherapeuticDetailAdapter extends BaseAdapter{
    private Context mContext;
    private List<TherapeuticDetailTypeBean> mDatas;

    private SparseBooleanArray answerShow;
    public TherapeuticDetailAdapter(Context mContext, List<TherapeuticDetailTypeBean> list){
        this.mContext = mContext;
        this.mDatas = list;
        answerShow = new SparseBooleanArray();
        for (int i = 0; i < mDatas.size(); i++) {
            answerShow.put(i,false);
        }
    }


    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }


    @Override public int getItemViewType(int position) {
        return mDatas.get(position).getType();
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        TherapeuticDetailTypeBean tnmDbsBean = mDatas.get(position);
        if (getItemViewType(position)==0){

            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_therapeutic_detail_title, parent, false);

            ImageView iv = (ImageView) convertView.findViewById(R.id.id_item_therapeutic_iv);
            TextView tv_title = (TextView) convertView.findViewById(R.id.id_item_therapeutic_tv_title);
            tv_title.setText(tnmDbsBean.getTitle());
            iv.setImageResource(tnmDbsBean.getImgId());
        } else if (getItemViewType(position)==1){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_therapeutic_detail_question, parent, false);
            TextView tv_qus = (TextView) convertView.findViewById(R.id.id_item_therapeutic_tv_question);
            final ImageView arrow = (ImageView) convertView.findViewById(R.id.id_item_therapeutic_iv_question);
            final TextView tv_answer = (TextView) convertView.findViewById(R.id.id_item_therapeutic_tv_answer);
            tv_qus.setText(tnmDbsBean.getQuestion());
            tv_answer.setText(tnmDbsBean.getAnswer());

            LinearLayout  layout= (LinearLayout) convertView.findViewById(R.id.id_item_therapeutic_ll_question);

            if (answerShow.get(position)){
                tv_answer.setVisibility(View.VISIBLE);
                arrow.setImageResource(R.drawable.ic_arrow_down);
            } else {
                tv_answer.setVisibility(View.GONE);
                arrow.setImageResource(R.drawable.ic_arrow_right);
            }

            layout.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    if (answerShow.get(position)){
                        tv_answer.setVisibility(View.GONE);
                        arrow.setImageResource(R.drawable.ic_arrow_right);
                        answerShow.put(position,false);
                    } else {
                        tv_answer.setVisibility(View.VISIBLE);
                        arrow.setImageResource(R.drawable.ic_arrow_down);
                        answerShow.put(position,true);
                    }

                    //notifyDataSetChanged();
                }
            });


        }
        return convertView;
    }
}
