package com.example.webimage.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectivityUtil {
	/**
	 * 判断当前设备是否具有可用的网络连接
	 * 
	 * @return
	 */
	public static boolean hasconnected(Context context){
		//获得连接管理器对象
		ConnectivityManager cm=(ConnectivityManager) context.getSystemService
				(Context.CONNECTIVITY_SERVICE);
		
		//获得当前可用的网络连接信息对象
		NetworkInfo info=cm.getActiveNetworkInfo();
		if(info != null){
			return info.isConnected();
		}
		return false;
	}
	/**
	 * 判断当前网络连接是否是wifi连接
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWifiConnection(Context context){
		// 获得连接类型为wifi的网络连接对象
		ConnectivityManager cm=(ConnectivityManager) context.getSystemService
				(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info=cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if(info!=null){
			return info.isConnected();
		}
		return false;	
	}
	/**
	 * 判断当前网络连接是否是移动运营商数据网络连接
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isMobileConnection(Context context){
		// 获得连接类型为wifi的网络连接对象
		ConnectivityManager cm=(ConnectivityManager) context.getSystemService
				(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info=cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if(info!=null){
			return info.isConnected();
		}
		return false;
	}
}
