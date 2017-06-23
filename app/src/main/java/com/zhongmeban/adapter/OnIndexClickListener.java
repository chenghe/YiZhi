package com.zhongmeban.adapter;

/**
 * 带索引ListView 点击事件Interface
 * Created by Chengbin He on 2016/7/19.
 */
public interface OnIndexClickListener {
    /**
     * listview item click事件
     * @param position
     */
    void onNameClick(int position);
    void onLocateClick();
}
