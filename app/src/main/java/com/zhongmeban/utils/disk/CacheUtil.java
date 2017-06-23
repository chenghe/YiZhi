package com.zhongmeban.utils.disk;

import com.zhongmeban.MyApplication;
import com.zhongmeban.utils.FileHelper;
import java.io.File;
import java.io.IOException;

/**
 * 功能描述：
 * 作者：ysf on 2016/12/9 17:26
 */
public class CacheUtil {

    public DiskLruCacheHelper getCacheHelper() {
        return cacheHelper;
    }


    private DiskLruCacheHelper cacheHelper;


    public CacheUtil() {
        if (cacheHelper == null) {
            try {
                cacheHelper = new DiskLruCacheHelper(MyApplication.app,
                    new File(FileHelper.getInstance().getCacheDisk()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    //在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final CacheUtil INSTANCE = new CacheUtil();
    }


    public static CacheUtil getInstance() {
        return SingletonHolder.INSTANCE;
    }

}
