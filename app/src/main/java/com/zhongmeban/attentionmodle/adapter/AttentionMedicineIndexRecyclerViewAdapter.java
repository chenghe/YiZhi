package com.zhongmeban.attentionmodle.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmeban.R;
import com.zhongmeban.base.BaseRecyclerViewAdapter;
import com.zhongmeban.utils.PinyinUtils;
import com.zhongmeban.utils.SmoothCheckBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.dao.Medicine;

/**
 * Created by Chengbin He on 2016/7/21.
 */
public class AttentionMedicineIndexRecyclerViewAdapter extends BaseRecyclerViewAdapter {

    private Context mContext;
    private List<Medicine> list;
    private HashMap<String, Integer> letterIndexes;
    private String[] sections;


    public AttentionMedicineIndexRecyclerViewAdapter(Context mContext, List<Medicine> list) {
        this.mContext = mContext;
        this.list = list;
        if (list == null) {
            list = new ArrayList<>();
        }
        int size = list.size();
        letterIndexes = new HashMap<>();
        sections = new String[size];
        for (int index = 0; index < size; index++) {
            //当前拼音首字母
            String currentLetter = PinyinUtils.getFirstLetter(list.get(index).getPinyinName());
            //上个首字母，如果不存在设为""
            String previousLetter = index >= 1 ? PinyinUtils.getFirstLetter(list.get(index - 1).getPinyinName()) : "";
            if (!TextUtils.equals(currentLetter, previousLetter)) {
                letterIndexes.put(currentLetter, index);
                sections[index] = currentLetter;
            }
        }
    }

    public void changeData(List<Medicine> list) {
        if (list == null) {
            this.list = list;
        } else {
            this.list.clear();
            this.list = list;
        }
        notifyDataSetChanged();
    }

    /**
     * 获取字母索引的位置
     *
     * @param letter
     * @return
     */
    public int getLetterPosition(String letter) {
        Integer integer = letterIndexes.get(letter);
        return integer == null ? -1 : integer;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.medicine_item_checkbox_index_listview, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        final String name = list.get(position).getMedicineName();
        String desc = list.get(position).getChemicalName();
        myViewHolder.name.setText(name);
        if (TextUtils.isEmpty(desc)){
            desc = "";
        }
        myViewHolder.describe.setText(desc);
        //设置医保是否显示
        //if (list.get(position).getMedicalInsurance()){
        //    myViewHolder.medicalInsurance.setVisibility(View.VISIBLE);
        //}else {
        //    myViewHolder.medicalInsurance.setVisibility(View.GONE);
        //}
        String currentLetter = PinyinUtils.getFirstLetter(list.get(position).getPinyinName());
        if (position == 0){
            myViewHolder.letter.setVisibility(View.VISIBLE);
            myViewHolder.letter.setText(currentLetter);
        }else{
            String previousLetter = position >= 1 ? PinyinUtils
                    .getFirstLetter(list.get(position - 1).getPinyinName()) : "";
            if (!TextUtils.equals(currentLetter, previousLetter)) {
                myViewHolder.letter.setVisibility(View.VISIBLE);
                myViewHolder.letter.setText(currentLetter);
            } else {
                myViewHolder.letter.setVisibility(View.GONE);
            }
        }
        myViewHolder.rl_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClick != null) {
                    mClick.onItemClick(v,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView letter;
        TextView name;
        TextView cost;
        TextView describe;
        TextView medicalInsurance;
        RelativeLayout rl_content;
        SmoothCheckBox checkBox;

        public MyViewHolder(View view) {
            super(view);
            letter = (TextView) view.findViewById(R.id.tv_letter);
            name = (TextView) view.findViewById(R.id.tv_name);
            cost = (TextView) view.findViewById(R.id.tv_cost);
            describe = (TextView) view.findViewById(R.id.tv_desc);
            medicalInsurance = (TextView) view.findViewById(R.id.tv_medicalInsurance);
            rl_content = (RelativeLayout) view.findViewById(R.id.rl_content);
            checkBox = (SmoothCheckBox) view.findViewById(R.id.checkbox);
        }
    }
}
