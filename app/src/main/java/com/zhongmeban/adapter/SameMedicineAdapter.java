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
import com.zhongmeban.bean.SameMeidicne;
import com.zhongmeban.utils.Constants;
import com.zhongmeban.utils.ZMBUtil;
import java.util.List;

/**
 * Created by Chengbin He on 2016/6/6.
 */
public class SameMedicineAdapter extends BaseAdapter {

    private List<SameMeidicne.SameMeidicneData> list;
    private Context mContext;


    public SameMedicineAdapter(Context mContext, List<SameMeidicne.SameMeidicneData> list) {
        this.mContext = mContext;
        this.list = list;
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
    public View getView(int position, View view, ViewGroup parent) {
        MyViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_treat_medicine_info, parent, false);
            holder = new MyViewHolder();
            holder.name = (TextView) view.findViewById(R.id.tv_name);
            holder.cost = (TextView) view.findViewById(R.id.tv_cost);
            holder.describe = (TextView) view.findViewById(R.id.tv_desc);
            holder.tvLeft = (TextView) view.findViewById(R.id.tv_medicalInsurance);
            holder.tvRight = (TextView) view.findViewById(R.id.tv_is_medical_special);
            view.setTag(holder);
        } else {
            holder = (MyViewHolder) view.getTag();
        }

        SameMeidicne.SameMeidicneData medicineBean = list.get(position);
        final String name = list.get(position).getShowName();
        String desc = list.get(position).getChemicalName();
        holder.name.setText(name);
        if (TextUtils.isEmpty(desc)) {
            desc = "";
        }
        holder.describe.setText(desc);
        holder.cost.setText(ZMBUtil.getFormatPrice(medicineBean.getPriceMax()));

        boolean isInsurance = Constants.isMedicalInsurance(medicineBean.getInsurance());
        boolean isSpecial = Constants.isMedicalInsurance(medicineBean.getSpecial());
        if (isInsurance && isSpecial) {
            holder.tvLeft.setText("保");
            holder.tvRight.setText("特");
            holder.tvLeft.setVisibility(View.VISIBLE);
            holder.tvRight.setVisibility(View.VISIBLE);
        } else if (isInsurance) {
            holder.tvRight.setText("保");
            holder.tvLeft.setVisibility(View.GONE);
            holder.tvRight.setVisibility(View.VISIBLE);
        } else if (isSpecial) {
            holder.tvRight.setText("特");
            holder.tvLeft.setVisibility(View.GONE);
            holder.tvRight.setVisibility(View.VISIBLE);
        } else {
            holder.tvLeft.setVisibility(View.GONE);
            holder.tvRight.setVisibility(View.GONE);
        }

        return view;
    }


    public static class MyViewHolder {
        TextView letter;
        TextView name;
        TextView cost;
        TextView describe;
        TextView tvRight;
        TextView tvLeft;
        RelativeLayout rl_content;
        View line;
    }
}
