package com.zhongmeban.utils.broadcast;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import com.zhongmeban.utils.ToastUtil;
import java.util.List;

/**
 * 功能描述：
 * 作者：ysf on 2016/12/6 16:27
 */
public class NetStateBroadCast extends BroadcastReceiver {

    @Override public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectManger = (ConnectivityManager) context.getSystemService(
            Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectManger.getActiveNetworkInfo();

        if (!isAppRunning(context)){
            //ToastUtil.showText(context, "app 没运行");
            return;
        }

        if (activeNetworkInfo == null || !activeNetworkInfo.isConnectedOrConnecting()) {
            ToastUtil.showText(context, "网络连接错误");
        }

    }


    private boolean isAppRunning(Context context) {
        String topActivityName = getTopActivityName(context);
        if (context.getPackageCodePath() != null && TextUtils.isEmpty(topActivityName) &&
            topActivityName.startsWith(context.getPackageName())) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 判断栈顶activity的名字,包括路径
     * @param context
     * @return
     */
    public String getTopActivityName(Context context) {
        String topActivityClassName = null;
        ActivityManager activityManager =
            (ActivityManager) (context.getSystemService(android.content.Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1);
        if (runningTaskInfos != null) {
            ComponentName f = runningTaskInfos.get(0).topActivity;
            topActivityClassName = f.getClassName();
        }
        return topActivityClassName;
    }

}
