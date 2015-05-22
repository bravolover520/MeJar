package me.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.storage.StorageManager;

/**
 * 存储器的工具类，因为4.0以上好多机型不再是mnt/sdcard.
 * @author Jesus{931178805@qq.com}
 * 2014年7月8日
 */
public class StorageUtils {
	
	
	/**
	 * 获取当前手机的存储路径集合,机型不适配只能自己写
	 * {@link android.os.Environment#getExternalStorageDirectory()}
	 * <ul> sample:
	 * <li> /mnt/sdcard
	 * <li> /storage/emulated/0
	 * <li> /storage/etSdCard
	 * <li> /storage/UsbDriveA
	 * </ul>
	 */
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public static String[] getVolumePaths(Context ctx) {         
        if (null == ctx) return null;
        
        String[] paths = null;  
		Method methodGetPaths = null;
        StorageManager sm = null;
        
        try {
        	sm = (StorageManager)ctx.getSystemService(Context.STORAGE_SERVICE);
        	methodGetPaths = sm.getClass().getMethod("getVolumePaths"); 
        	paths = (String[]) methodGetPaths.invoke(sm); 
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) { 
            e.printStackTrace(); 
        } catch (IllegalAccessException e) { 
            e.printStackTrace(); 
        } catch (InvocationTargetException e) { 
            e.printStackTrace(); 
        } 
        return paths; 
    } 
	
	/**
	 * 获取当前手机的主存储路径
	 * {@link android.os.Environment#getExternalStorageDirectory()}
	 */
	public static String getMainVolumePath(Context ctx) {
		String[] paths = getVolumePaths(ctx);
		if (null != paths) return paths[0];
		return null;
	}

}
