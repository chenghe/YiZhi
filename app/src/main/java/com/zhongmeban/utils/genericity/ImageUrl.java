package com.zhongmeban.utils.genericity;

import com.zhongmeban.MyApplication;

/**
 * Created by Chengbin He on 2016/10/27.
 */

public class ImageUrl {

    /**
     * 上传头像用户KEY
     */
    public static final int UserKey = 6;
    /**
     * 上传头像关注人KEY
     */
    public static final int PatientKey = 7;


    public static final String BaseImageTestUrl = "http://zhongmeban-picture-bucket.oss-cn-beijing.aliyuncs.com/";
    public static final String BaseImageReleaseUrl = "http://zmb-bucket.oss-cn-beijing.aliyuncs.com/";
    public static final String BaseImageUrl = MyApplication.isTest?BaseImageTestUrl:BaseImageReleaseUrl;
//    public static final String BaseImageUrl = "";

    public static final String BaseVideoTestUrl = "http://zhongmeban-picture-bucket.oss-cn-beijing.aliyuncs.com/";
    public static final String BaseVideoReleaseUrl = "http://zmb-bucket.oss-cn-beijing.aliyuncs.com/";
    public static final String BaseVideoUrl = MyApplication.isTest?BaseVideoTestUrl:BaseVideoReleaseUrl;
}
