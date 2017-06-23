package com.zhongmeban.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhongmeban.R;
import com.zhongmeban.bean.DoctDetailMedicase;
import com.zhongmeban.bean.DrugRelatedQA;

import java.util.List;


/**
 * Created by Chengbin He on 2016/5/26.
 */
public class MedicineQuestionAdapter extends BaseAdapter{

    private Context mContext;
    private List<DrugRelatedQA.DrugQASourceItem> list;

    public  MedicineQuestionAdapter(Context mContext,List<DrugRelatedQA.DrugQASourceItem> list){
        this.mContext = mContext;
        this.list = list;
    }

    public void notifyData(List<DrugRelatedQA.DrugQASourceItem> list){
        this.list = list;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder = null;
        if (holder == null) {
            holder = new MyViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_medicine_question,parent,false);
            holder.tvAnswer = (TextView) convertView.findViewById(R.id.tv_answer);
            holder.tvQuestion = (TextView) convertView.findViewById(R.id.tv_question);
            holder.tvDate = (TextView) convertView.findViewById(R.id.tv_time);
        }else{
            holder = (MyViewHolder) convertView.getTag();
        }

        initData(holder,position);
        return convertView;
    }

    private void initData( MyViewHolder holder,int position){
        String answer = list.get(position).getAnswerContent();
        if (TextUtils.isEmpty(answer)){
            answer = "该问题还未回答";
        }
        holder.tvAnswer.setText(answer);

        holder.tvQuestion.setText(list.get(position).getQuestionContent());
        holder.tvDate.setText(list.get(position).getQuestionTime()+"");
    }
    class MyViewHolder {
        public TextView tvAnswer;
        public TextView tvQuestion;
        public TextView tvDate;
    }
}
