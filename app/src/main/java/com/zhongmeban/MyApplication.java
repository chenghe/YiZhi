package com.zhongmeban;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.multidex.MultiDex;
import android.util.Log;
import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.bugtags.library.Bugtags;
import com.bugtags.library.BugtagsCallback;
import com.bugtags.library.BugtagsOptions;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.zhongmeban.net.HttpShopMethods;
import com.zhongmeban.net.okhttp.OkHttpUtils;
import com.zhongmeban.utils.OkHttp3Downloader;
import de.greenrobot.dao.DaoMaster;
import de.greenrobot.dao.DaoSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Chengbin He on 2016/4/21.
 */
public class MyApplication extends Application {

    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private DaoMaster.OpenHelper helper;
    private SQLiteDatabase db;

    private de.greenrobot.dao.attention.DaoMaster attentionDaoMaster;
    private de.greenrobot.dao.attention.DaoSession attentionDaoSession;
    private de.greenrobot.dao.attention.DaoMaster.OpenHelper attentionHelper;
    private SQLiteDatabase attentionDb;

    private boolean isCreatData = false;//预制数据 ，本地数据库开关
    public static boolean isTest = true;// 网络测试开关，控制baseurl、加密解密、bugtags、蒲公英更新，其中httpurl 的前缀需要手动切换
    public static int AttentionSchemaVersion = 2;
    public static MyApplication app;
    private String DBDir;
    private String DBName = "data.db";


    //各个平台的配置，建议放在全局Application或者程序入口
    {
        PlatformConfig.setWeixin("wx199ada22efb9ec57", "d3f80ba96c296f87f44ac91c0903ca5e");
        PlatformConfig.setQQZone("1105768517", "ji73TXAgBEqA2QJk");
        //新浪微博
        PlatformConfig.setSinaWeibo("27777153", "c5ab8270d46ab285c5c9c689620f2601");
        Config.isUmengSina = true;

    }


    @Override
    public void onCreate() {
        super.onCreate();

        app = this;
        Log.i("hcbtest", "MyApplication onCreate start");
        UMShareAPI.get(this);//友盟分享

        FeedbackAPI.init(this, "23525238");//阿里百川

        DBDir = getFilesDir().getParent() + "/databases/";
        //预制数据，创建数据库
        if (isCreatData) {
            if (helper == null) {
                //            治疗模块数据库
                helper = new DaoMaster.DevOpenHelper(MyApplication.this, DBName, null);
            }
        } else {
            //本地预制数据
            copyDBFile();
        }

        //关注模块数据库
        attentionHelper = new de.greenrobot.dao.attention.DaoMaster.DevOpenHelper(
            MyApplication.this,
            "attention.db", null);

        if (isTest) {
            if (!BuildConfig.IS_DEBUG) {
                initBugtags();
                //初始化logger
                Logger.init("zmb").setLogLevel(LogLevel.FULL);
            } else {
                //初始化logger
                Logger.init("zmb").setLogLevel(LogLevel.FULL);
            }
        } else {
/*            if (BuildConfig.IS_DEBUG) {
                initBugtags();
                //初始化logger
                Logger.init("zmb").setLogLevel(LogLevel.FULL);
            } else {
                //初始化logger
                Logger.init("zmb").setLogLevel(LogLevel.NONE);
            }*/

            //正式版改为none
            Logger.init("zmb").setLogLevel(LogLevel.NONE);
        }

        Picasso picasso = new Picasso.Builder(this)
            .downloader(new OkHttp3Downloader(HttpShopMethods.getInstance().getClient()))
            .build();
        Picasso.setSingletonInstance(picasso);

        OkHttpUtils.initClient(HttpShopMethods.getInstance().getClient());
    }


    @Override protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    private void initBugtags() {
        //customizable init option
        BugtagsOptions options = new BugtagsOptions.Builder().
            trackingLocation(true).//是否获取位置
            trackingCrashLog(true).//是否收集crash
            trackingConsoleLog(true).//是否收集console log
            trackingUserSteps(true).//是否收集用户操作步骤
            build();
        Bugtags.start("f8ade0f3e35f7fd8bb5d23c52441590f", this, Bugtags.BTGInvocationEventBubble,
            options);

        Bugtags.setBeforeSendingCallback(new BugtagsCallback() {
            @Override
            public void run() {
                Bugtags.log("before");
            }
        });

        Bugtags.setAfterSendingCallback(new BugtagsCallback() {
            @Override
            public void run() {
                Bugtags.log("after");
            }
        });
    }


    /**
     * 取得DaoMaster
     *
     * @return DaoMaster
     */
    public DaoMaster getDaoMaster() {
        if (daoMaster == null) {
            if (isCreatData) {

                //创建数据库
                db = helper.getWritableDatabase();
            } else {

                //获取预制数据库
                db = SQLiteDatabase.openDatabase(DBDir + DBName,
                    null, SQLiteDatabase.OPEN_READWRITE);
            }
            daoMaster = new DaoMaster(db);
        }
        return daoMaster;
    }


    /**
     * 取得关注DaoMaster
     *
     * @return DaoMaster
     */
    public de.greenrobot.dao.attention.DaoMaster getAttentionDaoMaster() {
        if (attentionDaoMaster == null) {
            //创建数据库
            attentionDb = attentionHelper.getWritableDatabase();
            attentionDaoMaster = new de.greenrobot.dao.attention.DaoMaster(attentionDb);
        }
        return attentionDaoMaster;
    }


    public void copyDBFile() {
        File dir = new File(DBDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File dbFile = new File(DBDir + DBName);
        if (!dbFile.exists()) {
            InputStream is;
            OutputStream os;
            try {
                is = MyApplication.this.getResources().getAssets().open(DBName);
                os = new FileOutputStream(dbFile);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer, 0, buffer.length)) > 0) {
                    os.write(buffer, 0, length);
                }
                os.flush();
                os.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 关闭数据库连接
     *
     * @return DaoSession
     */

    public void closeDB() {
        if (db != null) {
            db.close();
            daoMaster = null;
            daoSession = null;
        }
    }


    /**
     * 关闭关注数据库连接
     *
     * @return DaoSession
     */

    public void closeAttentionDB() {
        if (attentionDb != null) {
            attentionDb.close();
            attentionDaoMaster = null;
            attentionDaoSession = null;
        }
    }


    /**
     * 取得DaoSession
     *
     * @return DaoSession
     */
    public DaoSession getDaoSession() {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster();
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }


    /**
     * 取得attentionDaoSession
     * 对应关注模块
     *
     * @return attentionDaoSession
     */
    public de.greenrobot.dao.attention.DaoSession getAttentionDaoSession() {
        if (attentionDaoSession == null) {
            if (attentionDaoSession == null) {
                attentionDaoMaster = getAttentionDaoMaster();
            }
            attentionDaoSession = attentionDaoMaster.newSession();
        }
        return attentionDaoSession;
    }

}
