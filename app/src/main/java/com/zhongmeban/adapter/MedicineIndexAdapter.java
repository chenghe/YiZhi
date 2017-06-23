package com.zhongmeban.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.zhongmeban.R;
import com.zhongmeban.utils.Constants;
import com.zhongmeban.utils.PinyinUtils;
import de.greenrobot.dao.Medicine;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Description: 药箱子，带索引部分Adapter
 * Created by Chengbin He on 2016/4/20.
 */
public class MedicineIndexAdapter extends BaseAdapter {

    private Context mContext;
    private List<Medicine> list;
    private HashMap<String, Integer> letterIndexes;
    private String[] sections;
    //    private CharacterParser cp;
    private OnIndexClickListener onIndexClickListener;


    public MedicineIndexAdapter(Context mContext, List<Medicine> list) {

        this.mContext = mContext;
        this.list = list;
        if (list == null) {
            list = new ArrayList<>();
        }
        int size = list.size();
        letterIndexes = new HashMap<>();
        sections = new String[size];
        //        cp = new CharacterParser();
        for (int index = 0; index < size; index++) {
            //当前拼音首字母
            String currentLetter = PinyinUtils.getFirstLetter(list.get(index).getPinyinName());
            //上个首字母，如果不存在设为""
            String previousLetter = index >= 1 ? PinyinUtils.getFirstLetter(
                list.get(index - 1).getPinyinName()) : "";
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
     */
    public int getLetterPosition(String letter) {
        Integer integer = letterIndexes.get(letter);
        return integer == null ? -1 : integer;
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
        return position;
    }


    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        MyViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext)
                .inflate(R.layout.medicine_item_index_listview, parent, false);
            holder = new MyViewHolder();
            holder.letter = (TextView) view.findViewById(R.id.tv_letter);
            holder.name = (TextView) view.findViewById(R.id.tv_name);
            holder.cost = (TextView) view.findViewById(R.id.tv_cost);
            holder.describe = (TextView) view.findViewById(R.id.tv_desc);
            holder.medicalInsurance = (TextView) view.findViewById(R.id.tv_medicalInsurance);
            holder.medicalSpecial = (TextView) view.findViewById(R.id.tv_is_medical_special);
            holder.rl_content = (RelativeLayout) view.findViewById(R.id.rl_content);
            view.setTag(holder);
        } else {
            holder = (MyViewHolder) view.getTag();
        }

        Medicine medicine = list.get(position);
        final String name = list.get(position).getShowName();
        String desc = list.get(position).getChemicalName();
        holder.name.setText(name);
        if (TextUtils.isEmpty(desc)) {
            desc = "";
        }
        holder.describe.setText(desc);
        holder.cost.setText(TextUtils.isEmpty(medicine.getPriceMax())?"":(medicine.getPriceMax()+"元"));

        //设置医保是否显示
        if (Constants.isMedicalInsurance(medicine.getInsurance())) {
            holder.medicalInsurance.setVisibility(View.VISIBLE);
        } else {
            holder.medicalInsurance.setVisibility(View.GONE);
        }
        if (Constants.isSpicalMedical(medicine.getSpecial())) {
            holder.medicalSpecial.setVisibility(View.VISIBLE);
        } else {
            holder.medicalSpecial.setVisibility(View.GONE);
        }
        String currentLetter = PinyinUtils.getFirstLetter(list.get(position).getPinyinName());
        if (position == 0) {
            holder.letter.setVisibility(View.VISIBLE);
            holder.letter.setText(currentLetter);
        } else {
            String previousLetter = position >= 1 ? PinyinUtils
                .getFirstLetter(list.get(position - 1).getPinyinName()) : "";
            if (!TextUtils.equals(currentLetter, previousLetter)) {
                holder.letter.setVisibility(View.VISIBLE);
                holder.letter.setText(currentLetter);
            } else {
                holder.letter.setVisibility(View.GONE);
            }
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


    public static class MyViewHolder {
        TextView letter;
        TextView name;
        TextView cost;
        TextView describe;
        TextView medicalSpecial;
        TextView medicalInsurance;
        RelativeLayout rl_content;
    }


    public void setIndexClickListener(OnIndexClickListener listener) {
        this.onIndexClickListener = listener;
    }

}
