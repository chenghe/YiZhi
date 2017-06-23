package com.zhongmeban.base;

import android.content.Context;

/**
 *公共view 接口
 * Created by User on 2016/10/17.
 */

public interface IBaseView {

    /**
     * 显示可取消的进度条
     *
     * @param message 要显示的信息
     */
    void showProgress(String message);

    /**
     * 取消显示进度条
     */
    void cancelProgress();


    /**
     * 根据字符串弹出toast
     *
     * @param msg 提示内容
     */
    void showTheToast(String msg);

    /**
     * 获取当前上下文对象
     * @return
     */
    Context getContext();

    void onServerFail(String msg);
}
