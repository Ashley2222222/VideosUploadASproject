package com.jiesai.camera;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 *网络连接检查器
 */
public class ConnectionDetector {

	private Context context;

	public ConnectionDetector(Context context) {
		this.context = context;
	}

	/**
	 * 检查是否有网络连接
	 */
	public boolean isConnectingToInternet() {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivity.getActiveNetworkInfo();
		if (info != null && info.isConnected()) {
			return true;
		} else {
			Toast.makeText(context, "无可用网络", Toast.LENGTH_LONG).show();
			return false;
		}
	}
	
	/**
	 * 检查指定ip地址是否有效/是否有连接
	 */	
	public boolean checkURL(String url){
		boolean result = false;
		try {
			HttpURLConnection conn=(HttpURLConnection)new URL(url).openConnection();
			conn.setConnectTimeout(30000);
			int code = conn.getResponseCode();
			if(code!=200){
				result=false;
				Toast.makeText(context, "无网络连接", Toast.LENGTH_LONG).show();
			   }else{
				result=true;
			   }
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

}
