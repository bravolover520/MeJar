package me.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * 网路 工具类
 * @author Jesus{931178805@qq.com}
 * 2014年7月8日
 * 
 * 当用wifi上的时候  
 * 			getType 是WIFI
 * 			getExtraInfo是空的
 * 
 * 当用手机上的时候
 * 			getType 是MOBILE
 * 			用移动CMNET方式
 * 				getExtraInfo 的值是cmnet
 * 			用移动CMWAP方式
 * 				getExtraInfo 的值是cmwap,但是不在代理的情况下访问普通的网站访问不了
 * 			用联通3gwap方式
 * 				getExtraInfo 的值是3gwap
 * 			用联通3gnet方式
 * 				getExtraInfo 的值是3gnet
 * 			用联通uniwap方式
 * 				getExtraInfo 的值是uniwap
 * 			用联通uninet方式
 * 				getExtraInfo 的值是uninet
 */
public class NetworkUtils {

	public enum NetworkState {
		/**手机网络*/
		CMNET,
		/**wap代理*/
		CMWAP,
		/**无线网络*/
		WIFI,
		/**没网络*/
		NONE
	}
	
	/**
	 * 获取当前的网络状态 
	 * <ul>
	 * <li>-1：没有网络
	 * <li>0：wifi网络
	 * <li>1：net网络
	 * <li>2：wap 网络
	 * </ul>
	 * @param context
	 * @return
	 */
	@SuppressLint("DefaultLocale")
	public static NetworkState getAPNState(Context context) {
		ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (null == networkInfo) return NetworkState.NONE;
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			if (networkInfo.getExtraInfo().toLowerCase().equals("cmnet")) {
				return NetworkState.CMNET;
			} else
				return NetworkState.CMWAP;
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			return NetworkState.WIFI;
		}
		return NetworkState.NONE;
	}
	
	/**
	 * 使用GPRS上网时获取本机IP地址，设置用户上网权限
	 * <uses-permission android:name="android.permission.INTERNET" />
	 * @return null：没有网络连接
	 */
	public static String getLocalIpAddressOnGPRS() {
		try {
			NetworkInterface nerworkInterface;
			InetAddress inetAddress;
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				nerworkInterface = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = nerworkInterface.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
			return null;
		} catch (SocketException ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 使用WIFI时获取IP 设置用户权限
	 * </br>< uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
	 * </br>< uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
	 * </br>< uses-permission android:name="android.permission.WAKE_LOCK" />
	 * @param ctx
	 * @return
	 */
	public static String getLocalIpAddressOnWifi(Context ctx) {
		// 获取wifi服务
		WifiManager wifiManager = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
		// 判断wifi是否开启
		if (!wifiManager.isWifiEnabled())
			wifiManager.setWifiEnabled(true);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();
		return int2Ip(ipAddress);
	}
	
	/**
	 * 打开Wifi.需要权限
	 * </br>< uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
	 * </br>< uses-permission android:name="android.permission.CHANGE_WIFI_STATE"/>
	 * @param ctx
	 * @param enabled
	 */
	public static void toggleWifi(Context ctx, boolean enabled) {
		WifiManager wifiManager = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
		wifiManager.setWifiEnabled(enabled);
	}
	
	/**
	 * 移动网络.需要权限
	 * </br>< uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
	 * </br>< uses-permission android:name="android.permission.CHANGE_NETWORK_STATE"/>
	 * @param ctx
	 * @param enabled
	 */
	public static void toggleMobile(Context ctx, boolean enabled) {
		 ConnectivityManager conMgr = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		 Class<?> conMgrClass = null; 	// ConnectivityManager类 
		 Field iConMgrField = null; 	// ConnectivityManager类中的字段  
		 Object iConMgr = null; 		// IConnectivityManager类的引用  
		 Class<?> iConMgrClass = null; 	// IConnectivityManager类
		 Method setMobileDataEnabledMethod = null; 	// setMobileDataEnabled方法
		 try {
			 // 取得ConnectivityManager类
			 conMgrClass = Class.forName(conMgr.getClass().getName());  
			 // 取得ConnectivityManager类中的对象mService  
			 iConMgrField = conMgrClass.getDeclaredField("mService");
			 // 设置mService可访问  
			 iConMgrField.setAccessible(true); 
			 // 取得mService的实例化类IConnectivityManager  
			 iConMgr = iConMgrField.get(conMgr); 
			 // 取得IConnectivityManager类  
			 iConMgrClass = Class.forName(iConMgr.getClass().getName());  
			 // 取得IConnectivityManager类中的setMobileDataEnabled(boolean)方法  
			 setMobileDataEnabledMethod = iConMgrClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);  
			 // 设置setMobileDataEnabled方法可访问  
			 setMobileDataEnabledMethod.setAccessible(true);  
			 // 调用setMobileDataEnabled方法  
			 setMobileDataEnabledMethod.invoke(iConMgr, enabled); 
		 } catch (ClassNotFoundException e) {  
		    e.printStackTrace();  
		 } catch (NoSuchFieldException e) {  
		    e.printStackTrace();  
		 } catch (SecurityException e) {  
		    e.printStackTrace();  
		 } catch (NoSuchMethodException e) {  
		    e.printStackTrace();  
		 } catch (IllegalArgumentException e) {  
		    e.printStackTrace();  
		 } catch (IllegalAccessException e) {  
		    e.printStackTrace();  
		 } catch (InvocationTargetException e) {  
		    e.printStackTrace();  
		 } 
	}

	/**
	 * 返回网络是否可用。需要权限：
	 * </br>< uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
	 * 判断wifi是否可用
	 */
	public static boolean isWifiActive(Context c) {
		ConnectivityManager connectivity = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getTypeName().equalsIgnoreCase("WIFI") && info[i].isConnected()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 得到当前连接Wifi的 ssid
	 * @param context
	 * @return
	 */
	public static String getSSID(Context context) {
		if (isWifiActive(context)) {
			// 取得WifiManager对象
			WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			// 取得WifiInfo对象
			WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
			return mWifiInfo.getBSSID();
		}
		return "unknown";
	}

	/**
	 * 判断网络是否连通
	 * @param ctx
	 * @return
	 */
	public static boolean isNetworkAvailable(Context ctx) {
		ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		return (info != null && info.isAvailable());
	}

	/**
	 * int转IP
	 * @param ip int值的IP
	 * @return
	 */
	public static String int2Ip(int ip) {
		return (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "." + ((ip >> 24) & 0xFF);
	}

	/**
	 * 得到所有的网络连接名称
	 * @param context
	 * @return
	 */
	public static String[] getAllNetworkNames(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo[] networkInfo = cm.getAllNetworkInfo();
		String[] networkNames = new String[networkInfo.length];
		for (int i = 0; i < networkInfo.length; i++) {
			networkNames[i] = networkInfo[i].getTypeName();
		}
		return networkNames;
	}	
}
