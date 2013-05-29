package com.example.webimage.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectivityUtil {
	/**
	 * �жϵ�ǰ�豸�Ƿ���п��õ���������
	 * 
	 * @return
	 */
	public static boolean hasconnected(Context context){
		//������ӹ���������
		ConnectivityManager cm=(ConnectivityManager) context.getSystemService
				(Context.CONNECTIVITY_SERVICE);
		
		//��õ�ǰ���õ�����������Ϣ����
		NetworkInfo info=cm.getActiveNetworkInfo();
		if(info != null){
			return info.isConnected();
		}
		return false;
	}
	/**
	 * �жϵ�ǰ���������Ƿ���wifi����
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWifiConnection(Context context){
		// �����������Ϊwifi���������Ӷ���
		ConnectivityManager cm=(ConnectivityManager) context.getSystemService
				(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info=cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if(info!=null){
			return info.isConnected();
		}
		return false;	
	}
	/**
	 * �жϵ�ǰ���������Ƿ����ƶ���Ӫ��������������
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isMobileConnection(Context context){
		// �����������Ϊwifi���������Ӷ���
		ConnectivityManager cm=(ConnectivityManager) context.getSystemService
				(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info=cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if(info!=null){
			return info.isConnected();
		}
		return false;
	}
}
