package me.utils;

import android.content.Context;
import android.os.SystemClock;

/**
 * 常用系统的工具
 * @author Jesus{931178805@qq.com} 2014年8月1日
 */
public class SystemUtils {
	
	/** recommend default thread pool size according to system available processors, {@link #getDefaultThreadPoolSize()} **/
    public static final int DEFAULT_THREAD_POOL_SIZE = getDefaultThreadPoolSize();

    /**
     * get recommend default thread pool size
     * 得到默认的线程池大小
     * @return if 2 * availableProcessors + 1 less than 8, return it, else return 8;
     * @see {@link #getDefaultThreadPoolSize(int)} max is 8
     */
    public static int getDefaultThreadPoolSize() {
        return getDefaultThreadPoolSize(8);
    }

    /**
     * get recommend default thread pool size
     * 
     * @param max
     * @return if 2 * availableProcessors + 1 less than max, return it, else return max;
     */
    public static int getDefaultThreadPoolSize(int max) {
        int availableProcessors = 2 * Runtime.getRuntime().availableProcessors() + 1;
        return availableProcessors > max ? max : availableProcessors;
    }

	/**
	 * <font color=red> 
	 * OSVersion 			系统版本 
	 * </br> VersionName 	程序版本名 
	 * </br> Vendor	 		Android系统定制商 
	 * </br> Device 		返回设备型号(e.g: GT-I9500)
	 * <font/>
	 */
	private static final String UA_TEMPLATE = "Android(OSVersion:%s; VersionName:%s; Vendor:%s; Device:%s)";

	/**
	 * 得到数据传输的UserAgent
	 * @param ctx
	 * @return
	 */
	public static String generateUserAgent(Context ctx) {
		return String.format(UA_TEMPLATE, AndroidUtils.getOSVersion(), AndroidUtils.getVersion(ctx), 
				AndroidUtils.getVendor(), AndroidUtils.getDevice());
	}
	
	/**
	 * 得到当前系统语言
	 * @return
	 */
	public static String getLocaleLanguage() {
		java.util.Locale l = java.util.Locale.getDefault();
		return String.format("%s-r%s", l.getLanguage(), l.getCountry().toUpperCase(l));
	}

	/**
	 * 检查内存释放有足够空间，如果不够则尝试释放
	 * @param size
	 * @return
	 */
	public static boolean checkMemory(long size) {
		Runtime runtime = Runtime.getRuntime();

		long freeMemory = runtime.freeMemory();// 空闲内存
		// long maxMemory = runtime.maxMemory();//总可以内存
		// long toatlMemory = Runtime.getRuntime().totalMemory();//已用内存
		if (size * 2 < freeMemory) {
			return true;
		} else {
			System.gc();
		}
		SystemClock.sleep(500);
		if (size * 2 < freeMemory) {
			return true;
		} else {
			return false;
		}
	}

}
