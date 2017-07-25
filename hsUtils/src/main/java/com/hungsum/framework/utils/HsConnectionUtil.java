package com.hungsum.framework.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;

public class HsConnectionUtil
{
	private HsConnectionUtil util;
	
	private ConnectivityManager manager;
	
	public HsConnectionUtil getDefault(Context context)
	{
		if(util == null)
		{
			util = new HsConnectionUtil(context);
		}
		
		return util;

	}
	
	private HsConnectionUtil(Context context)
	{
		manager = (ConnectivityManager)context.getSystemService(Activity.CONNECTIVITY_SERVICE);
	}
	
	public boolean getWifiIsConnected()
	{
		return manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
	}
	
	public boolean getInternetIsConnected()
	{
		return manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected();
	}
	
	public boolean getNetworkIsConnected()
	{
		return getWifiIsConnected() || getInternetIsConnected();
	}

}
