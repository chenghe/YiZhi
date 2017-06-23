package com.zhongmeban.attentionmodle.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.zhongmeban.R;
import com.zhongmeban.adapter.OnIndexClickListener;
import com.zhongmeban.utils.PinyinUtils;
import com.zhongmeban.utils.SmoothCheckBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.dao.AttentionIndicator;
import de.greenrobot.dao.Indicator;
import de.greenrobot.dao.Medicine;

/**
 * 关注部分添加标志物Adapter
 * Created by Chengbin He on 2016/7/27.
 */
public class AttentionMarkerIndexAdapter extends BaseAdapter{
    private Context mContext;
    private List<AttentionIndicator> list;
    private HashMap<String, Integer> letterIndexes;
    private String[] sections;
    private OnIndexClickListener onIndexClickListener;

    public AttentionMarkerIndexAdapter(Context mContext, List<AttentionIndicator> list){

        this.mContext = mContext;
        this.list = list;
        if (list == null){
            list = new ArrayList<>();
        }
        int size = list.size();
        letterIndexes = new HashMap<>();
        sections = new String[size];
        for (int index = 0; index < size; index++){
            //当前拼音首字母
            String currentLetter = PinyinUtils.getFirstLetter(list.get(index).getIndicator().getPinyinName());
            //上个首字母，如果不存在设为""
            String previousLetter = index >= 1 ? PinyinUtils.getFirstLetter(list.get(index-1).getIndicator().getPinyinName()) : "";
            if (!TextUtils.equals(currentLetter, previousLetter)){
                letterIndexes.put(currentLetter, index);
                sections[index] = currentLetter;
            }
        }

    }
    public void changeData(List<AttentionIndicator> list){
//        if (list == null){
//            this.list = list;
//        }else{
//            this.list.clear();
            this.list = list;
//        }
        notifyDataSetChanged();
    }

    /**
     * 获取字母索引的位置
     * @param letter
     * @return
     */
    public int getLetterPosition(String letter){
        Integer integer = letterIndexes.get(letter);
        Log.i("hcbtest","letter "+letter);
        return integer == null ? -1 : integer;
    }

    @Override
    public int getCount() {
        return list== null ? 0: list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        MyViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.medicine_item_checkbox_index_listview, parent, false);
            holder = new MyViewHolder();
            holder.letter = (TextView) view.findViewById(R.id.tv_letter);
            holder.name = (TextView) view.findViewById(R.id.tv_name);
            holder.cost = (TextView) view.findViewById(R.id.tv_cost);
            holder.describe = (TextView) view.findViewById(R.id.tv_desc);
            holder.medicalInsurance = (TextView) view.findViewById(R.id.tv_medicalInsurance);
            holder.rl_content = (RelativeLayout) view.findViewById(R.id.rl_content);
            holder.checkBox = (SmoothCheckBox) view.findViewById(R.id.checkbox);
            holder.checkBox.setChecked(false);
//            holder.checkBox.setEnabled(false);
            holder.checkBox.setCanClick(false);
            view.setTag(holder);
        } else {
            holder = (MyViewHolder) view.getTag();
        }
        final String name = list.get(position).getIndicator().getName();
        String desc = list.get(position).getMarkerDose();
        holder.name.setText(name);
        if (TextUtils.isEmpty(desc)||desc == ""){
            desc = "";
            holder.describe.setText(desc);
        }else {
            String unit = "";
            if (list.get(position).getIndicator().getUnit()!=null){
                unit = list.get(position).getIndicator().getUnit();
            }
            holder.describe.setText(desc+" "+unit);
        }

//        //设置医保是否显示
//        if (list.get(position).getMedicalInsurance()){
//            holder.medicalInsurance.setVisibility(View.VISIBLE);
//        }else {
//            holder.medicalInsurance.setVisibility(View.GONE);
//        }
        String currentLetter = PinyinUtils.getFirstLetter(list.get(position).getIndicator().getPinyinName());
        if (position == 0){
            holder.letter.setVisibility(View.VISIBLE);
            holder.letter.setText(currentLetter);
        }else{
            String previousLetter = position >= 1 ? PinyinUtils
                    .getFirstLetter(list.get(position - 1).getIndicator().getPinyinName()) : "";
            if (!TextUtils.equals(currentLetter, previousLetter)) {
                holder.letter.setVisibility(View.VISIBLE);
                holder.letter.setText(currentLetter);
            } else {
                holder.letter.setVisibility(View.GONE);
            }
        }

        //判断是否选中
        if (list.get(position).getAttentionMarker()){
            holder.checkBox.setChecked(true);
        }else {
            holder.checkBox.setChecked(false);
        }
        holder.rl_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onIndexClickListener != null) {
                    onIndexClickListener.onNameClick(position);
                }
            }
        });


        return view;
    }


    public static class MyViewHolder{
        TextView letter;
        TextView name;
        TextView cost;
        TextView describe;
        TextView medicalInsurance;
        RelativeLayout rl_content;
        SmoothCheckBox checkBox;
    }
    public void setIndexClickListener(OnIndexClickListener listener){
        this.onIndexClickListener = listener;
    }
}