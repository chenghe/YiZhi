package com.zhongmeban.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhongmeban.R;
import com.zhongmeban.utils.PinyinUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.dao.BaseDao;
import de.greenrobot.dao.Indicator;

/**
 * Description: 预制数据，带索引部分Adapter
 * Created by Chengbin He on 2016/4/20.
 */
public class IndexAdapter extends BaseAdapter {

    private Context mContext;
    private List<?extends BaseDao> list;
    private HashMap<String, Integer> letterIndexes;
    private String[] sections;
//    private CharacterParser cp;
    private OnIndexClickListener onIndexClickListener;

    public IndexAdapter(Context mContext,List<?extends BaseDao> list){

        this.mContext = mContext;
        this.list = list;
        if (list == null){
            list = new ArrayList<>();
        }
        int size = list.size();
        Log.i("hcbtest","list.size() is " +list.size());
        letterIndexes = new HashMap<>();
        sections = new String[size];
//        cp = new CharacterParser();
        for (int index = 0; index < size; index++){
            //当前拼音首字母
            String currentLetter = PinyinUtils.getFirstLetter(list.get(index).pinyinName);
            //上个首字母，如果不存在设为""
            String previousLetter = index >= 1 ? PinyinUtils.getFirstLetter(list.get(index-1).pinyinName) : "";
            if (!TextUtils.equals(currentLetter, previousLetter)){
                letterIndexes.put(currentLetter, index);
                sections[index] = currentLetter;
            }
        }

    }

    public void changeData(List<?extends BaseDao> list){
        if (list == null){
            this.list = list;
        }else{
            this.list.clear();
            this.list = list;
        }
        notifyDataSetChanged();
    }

    /**
     * 获取字母索引的位置
     * @param letter
     * @return
     */
    public int getLetterPosition(String letter){
        Integer integer = letterIndexes.get(letter);
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
        Log.i("hcb","position is " +position);
        MyViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_index_listview, parent, false);
            holder = new MyViewHolder();
            holder.letter = (TextView) view.findViewById(R.id.tv_item_city_listview_letter);
            holder.name = (TextView) view.findViewById(R.id.tv_item_city_listview_name);
            view.setTag(holder);
        } else {
            holder = (MyViewHolder) view.getTag();
        }

        String currentLetter = PinyinUtils.getFirstLetter(list.get(position).pinyinName);
        final String name = list.get(position).name;
        if (position == 0){
            holder.letter.setVisibility(View.VISIBLE);
            holder.letter.setText(currentLetter);
            holder.name.setText(name);
        }else{
            if (position >= 1) {
                holder.name.setText(name);
                String previousLetter = position >= 1 ? PinyinUtils.getFirstLetter(list.get(position-1).pinyinName) : "";
                if (!TextUtils.equals(currentLetter, previousLetter)) {
                    holder.letter.setVisibility(View.VISIBLE);
                    holder.letter.setText(currentLetter);
                } else {
                    holder.letter.setVisibility(View.GONE);
                }
        }
        }
        //点击回调
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onIndexClickListener != null) {
                    onIndexClickListener.onNameClick(name,position);
                }
            }
        });
        return view;
    }

    public static class MyViewHolder{
        TextView letter;
        TextView name;
    }
    public void setIndexClickListener(OnIndexClickListener listener){
        this.onIndexClickListener = listener;
    }

    public interface OnIndexClickListener{
        void onNameClick(String name,int position);
        void onLocateClick();
    }
}
