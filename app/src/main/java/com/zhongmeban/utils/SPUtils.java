package com.zhongmeban.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import java.util.Set;

/**
 * Created by User on 2016/9/22.
 */
public class SPUtils {
    private static String FILE_NAME = "app_data";


    public static void putApply(Context context, String key, Object object, String fileName) {

        FILE_NAME = (TextUtils.isEmpty(fileName) ? "app_data" : fileName);

        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        judgePutDataType(key, object, editor);
        editor.apply();
    }


    public static void putCommit(Context context, String key, Object object, String fileName) {

        FILE_NAME = (TextUtils.isEmpty(fileName) ? "app_data" : fileName);
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        judgePutDataType(key, object, editor);
        editor.commit();
    }


    public static SharedPreferences.Editor getSpEditorByName(Context context, String name) {

        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        return editor;
    }


    public static void clearSpByName(Context context, String name) {
        SharedPreferences.Editor editor = getSpEditorByName(context, name);
        editor.clear();
    }


    /**
     * 获取 sp 中的数据
     * @param context
     * @param key
     * @param defValue
     * @param fileName sp的名字，如果是空，就用默认的 app_data 这个文件
     * @return
     */
    public static Object getParams(Context context, String key, Object defValue, String fileName) {

        FILE_NAME = (TextUtils.isEmpty(fileName) ? "app_data" : fileName);
        SharedPreferences mSharedPreferences = context.getSharedPreferences(FILE_NAME,
            Context.MODE_PRIVATE);

        if (defValue instanceof String) {
            return mSharedPreferences.getString(key, (String) defValue);
        } else if (defValue instanceof Integer) {
            return mSharedPreferences.getInt(key, (Integer) defValue);
        } else if (defValue instanceof Boolean) {
            return mSharedPreferences.getBoolean(key, (Boolean) defValue);
        } else if (defValue instanceof Float) {
            return mSharedPreferences.getFloat(key, (Float) defValue);
        } else if (defValue instanceof Long) {
            return mSharedPreferences.getLong(key, (Long) defValue);
        } else if (defValue instanceof Set) {
            return mSharedPreferences.getStringSet(key, (Set<String>) defValue);
        } else {
            return null;
        }
    }


    private static void judgePutDataType(String key, Object object, SharedPreferences.Editor editor) {

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else if (object instanceof Set) {
            editor.putStringSet(key, (Set<String>) object);
        } else {
            editor.putString(key, object.toString());
        }

    }


/*    private void saveSearchHistory(String key) {
        //转到这个界面就表示 搜索成功 保存搜索记录
        HashSet<String> hashSet = (HashSet<String>) SPUtils.get(mContext, Constant.HISTORYTEXT, new HashSet<String>());
        //关键操作 需要在新的集合添加值 然后再提交修改
        Set<String> changeData = new HashSet<>(hashSet);
        changeData.add(key);

        boolean isSuccess = SPUtils.putCommit(mContext, Constant.HISTORYTEXT, changeData);
        Logger.d("isSuccess=" + isSuccess);
    }*/

}
