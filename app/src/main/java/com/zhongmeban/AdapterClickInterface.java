package com.zhongmeban;

import android.view.View;


/**
 * @Description: RecylcerView click事件回调接口
 * Created by Chengbin He on 2016/3/21.
 */
public interface AdapterClickInterface {
    void onItemClick(View v , int position);
    void onItemLongClick(View v, int position);
}
