package com.zhongmeban.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * 网络工具类
* @ClassName: NetWorkUtils 
* @Description: 网络工具类
* @author ChenChong
* @date 2015-12-15 下午3:00:04 
*
 */
public class NetWorkUtils {

	//  isAvailable 是否可用；本连接应该指的是物理上的连接，实际不可用
	//  isConnected 是否连接，本连接指的是数据上的连接，即实际可用
	//  首先应该先判断是WiFi还是手机网络，当是手机网络的时候，再去判断一下，是快速网络还是渣网络
	
	
	/**
	 * 
	* @Title: checkNetworkAvailable 
	* @Description: 检测网络是否连接（物理连接）
	* @param 
	* @return boolean  返回类型 ，true连接，false未连接
	* @throws
	 */
	public static boolean isNetworkConnected(Context context) {
		ConnectivityManager cm =
			(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
	}


	 /**
	  * 
	 * @Title: isWifiDataEnable 
	 * @Description: 单独判断WiFi数据是否可用
	 * @param 
	 * @return boolean    返回类型 
	 * @throws
	  */
    public static boolean isWifiDataEnable(Context context,ConnectivityManager connect){
    	boolean isWifiDataEnable = false;
        isWifiDataEnable = connect.getNetworkInfo(  
                ConnectivityManager.TYPE_WIFI).isConnected();
        return isWifiDataEnable;  
    }
    
    /**
     * 
    * @Title: isMobileDataEnable 
    * @Description: 单独判断手机数据是否可用
    * @param 
    * @return boolean    返回类型 
    * @throws
     */
    public static boolean isMobileDataEnable(Context context,ConnectivityManager connect){
    	boolean isMobileDataEnable = false;
    	isMobileDataEnable = connect.getNetworkInfo(  
                ConnectivityManager.TYPE_MOBILE).isConnected();
        return isMobileDataEnable;  
    }
    
    /**
    * @Title: isFastMobileNetwork 
    * @Description: 判断当前的网络是否是快速网络(暂且把3G及以上的网络定义为快速网络)
    * @param 
    * @return boolean    返回类型 
    * @throws
     */
    public static boolean isFastMobileNetwork(Context context) {
    	TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
    	    switch (telephonyManager.getNetworkType()) {
    	        case TelephonyManager.NETWORK_TYPE_1xRTT:
    	            return false; // ~ 50-100 kbps
    	        case TelephonyManager.NETWORK_TYPE_CDMA:
    	            return false; // ~ 14-64 kbps
    	        case TelephonyManager.NETWORK_TYPE_EDGE:
    	            return false; // ~ 50-100 kbps
    	        case TelephonyManager.NETWORK_TYPE_EVDO_0:
    	            return true; // ~ 400-1000 kbps
    	        case TelephonyManager.NETWORK_TYPE_EVDO_A:
    	            return true; // ~ 600-1400 kbps
    	        case TelephonyManager.NETWORK_TYPE_GPRS:
    	            return false; // ~ 100 kbps
    	        case TelephonyManager.NETWORK_TYPE_HSDPA:
    	            return true; // ~ 2-14 Mbps
    	        case TelephonyManager.NETWORK_TYPE_HSPA:
    	            return true; // ~ 700-1700 kbps
    	        case TelephonyManager.NETWORK_TYPE_HSUPA:
    	            return true; // ~ 1-tab2_sel Mbps
    	        case TelephonyManager.NETWORK_TYPE_UMTS:
    	            return true; // ~ 400-7000 kbps
    	        case TelephonyManager.NETWORK_TYPE_EHRPD:
    	            return true; // ~ 1-2 Mbps
    	        case TelephonyManager.NETWORK_TYPE_EVDO_B:
    	            return true; // ~ 5 Mbps
    	        case TelephonyManager.NETWORK_TYPE_HSPAP:
    	            return true; // ~ 10-tab1_un Mbps
    	        case TelephonyManager.NETWORK_TYPE_IDEN:
    	            return false; // ~25 kbps
    	        case TelephonyManager.NETWORK_TYPE_LTE:
    	            return true; // ~ 10+ Mbps
    	        case TelephonyManager.NETWORK_TYPE_UNKNOWN:
    	            return false;
    	        default:
    	            return false;
    	   }
    }
    
}
