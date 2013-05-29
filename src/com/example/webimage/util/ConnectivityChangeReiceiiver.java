package com.example.webimage.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ConnectivityChangeReiceiiver extends BroadcastReceiver{
	public void onReceive(Context context, Intent intent){
		 boolean hasConn=ConnectivityUtil.hasconnected(context);
		 if(!hasConn){
			 Toast.makeText(context, "网络连接已断开", Toast.LENGTH_SHORT).show();
		 }
	 }
}
