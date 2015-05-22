package me.base;

import me.utils.L;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;

/**
 * 根据签名判断apk的状态
 * <ul>
 * <li>正在开发:1</li>
 * <li>正式发布:0</li>
 * <li>被篡改:-1</li>
 * </ul>
 * @author Jesus{931178805@qq.com}
 * 2014年7月31日
 */
public class AppState {		
	
	/**被篡改*/
	public static final int STATUS_TAMPER = -1;
	/**正在开发：{日志:打印, 捕获异常:打印、保存, 未捕获异常:打印、保存}*/
	public static final int STATUS_DEVELOP = 1;
	/**正式发布：{日志:不打印不保存, 捕获异常:不打印不保存, 未捕获异常:不打印但保存}*/
	public static final int STATUS_RELEASE = 0;
	
	/**
     * <font color=red>需要使用同一的keystory<font/>
     */
	private static final int SHA1_DEVELOP = 197583638, SHA1_RELEASE = -624722122;
		
	/**App状态*/
	public static int STATE = -2;
	
	public static int getState() {
		if (STATE == -2)
			initAppState();
		return STATE;
	}
	
	/**
	 * 正在开发
	 * @return
	 */
	public static boolean isDeveloping(){
		return STATUS_DEVELOP == STATE;
	}
	/**
	 * 正式发布
	 * @return
	 */
	public static boolean isReleased(){
		return STATUS_RELEASE == STATE;
	}
    /**
	 * 被篡改
	 * @return
	 */
	public static boolean isTampered(){
		return STATUS_TAMPER == STATE;
	}
	
	/**
	 * 初始化App状态:开发or发布or被篡改
	 * */
	private static void initAppState() {		
		Context context = getAppContext();
		PackageInfo packageInfo;
		try {
			packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
			Signature sigs = packageInfo.signatures[0];
			int hashCode = sigs.hashCode();
			L.i("hashCode>" + hashCode);
			
			switch (sigs.hashCode()) {
			case SHA1_DEVELOP:
				STATE = STATUS_DEVELOP;
				break;
			case SHA1_RELEASE:
				STATE = STATUS_RELEASE;
				break;
			default:
				STATE = STATUS_TAMPER;
				break;
			}
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
     *  获取AppConteaxt
     * */
	private static Context getAppContext() {
		return me.base.BaseApplication.globalContext;
	}

}
