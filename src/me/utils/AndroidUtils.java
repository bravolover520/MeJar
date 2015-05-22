package me.utils;

import java.io.File;
import java.util.List;

import me.utils.ShellUtils.CommandResult;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;

/**
 * Android 自身工具包
 * @author Jesus{931178805@qq.com}
 * 2014年7月8日
 */
@TargetApi(android.os.Build.VERSION_CODES.GINGERBREAD)
public class AndroidUtils {
	
	/**
	 * 检查程序是否安装了
	 * @param ctx
	 * @param pkgName		程序包名
	 * @return
	 */
	public static boolean isAppInstalled(Context ctx, String pkgName) {
        PackageInfo packageInfo;
        try {
            packageInfo = ctx.getPackageManager().getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }

        return (packageInfo == null) ? false : true;
    }
	
	/**
	 * 检查自己的应用是否有设定的权限,一般用于检查有才能用.
	 * Whether had this permission.
	 * @param context
	 * @param permission
	 * @return
	 */
	public static boolean hasPermission(Context context, String permission) {
		int perm = context.checkCallingOrSelfPermission(permission);//检查自己或者其它调用者是否有 permission 权限
		//结果为0则表示使用了该权限，-1则表求没有使用该权限
		if (perm == PackageManager.PERMISSION_DENIED)
			throw new RuntimeException("Please add the permission " + permission + " to the manifest");
		return perm == PackageManager.PERMISSION_GRANTED;
	}
	
	/**
	 * 检查给定的包是否有此权限.
	 * Whether had this permission.
	 * @param context
	 * @param permission
	 * @return
	 */
	public static boolean hasPermission(Context context, String pkgName, String permission) {
		int perm = context.getPackageManager().checkPermission(permission, pkgName);
		return perm == PackageManager.PERMISSION_GRANTED;
	}
	
	/**
	 * get myself permission.
	 * @param c
	 * @return
	 */
	public static String getPermission(Context c) {
		return getPermission(c, null);
	}
	
	/**
	 * Get permission info by packageName, if packageName is null, then get myself packageName.
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static String getPermission(Context context, String packageName) {
		StringBuilder sb = new StringBuilder();
		
		if (packageName == null) 
			packageName = context.getPackageName();
		
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(packageName, 0);  
            // 得到自己的包名  
            String pkgName = pi.packageName; 
            PackageInfo pkgInfo = pm.getPackageInfo(pkgName,  
                    PackageManager.GET_PERMISSIONS);//通过包名，返回包信息  
            String sharedPkgList[] = pkgInfo.requestedPermissions;//得到权限列表
            
            for (int i = 0; i < sharedPkgList.length; i++) {  
                String permName = sharedPkgList[i];  
  
                PermissionInfo tmpPermInfo = pm.getPermissionInfo(permName, 0);//通过permName得到该权限的详细信息  
                PermissionGroupInfo pgi = pm.getPermissionGroupInfo(tmpPermInfo.group, 0);//权限分为不同的群组，通过权限名，我们得到该权限属于什么类型的权限。  
                  
                sb.append(i + "-" + permName + "\n");  
                sb.append(i + "-" + pgi.loadLabel(pm).toString() + "\n");  
                sb.append(i + "-" + tmpPermInfo.loadLabel(pm).toString()+ "\n");  
                sb.append(i + "-" + tmpPermInfo.loadDescription(pm).toString()+ "\n");  
            } 
		} catch (NameNotFoundException e) {
			L.e("Could'nt retrieve permissions for package", e);
		}
        return sb.toString();
	}
	
	/**
	 * 判断activity是否在最上层
	 * @param ctx
	 * @param className
	 * @return
	 */
	public static boolean isTopActivity(Context ctx, String className) {
		ActivityManager am = (ActivityManager)ctx.getSystemService(Context.ACTIVITY_SERVICE);
		ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
		if (cn.getClassName().contains(className)) 
			return true;
		return false;
	}
	
	/**
	 * 在应用市场中查找该应用
	 * @param ctx
	 * @param pkg		在市场上的包名
	 */
	public static void rateMyApp(Context ctx, String pkg) {
		String addres = "market://details?id=" + pkg;
		Intent marketIntent = new Intent("android.intent.action.VIEW");
		marketIntent.setData(Uri.parse(addres));
		ctx.startActivity(marketIntent);
	}

	/**
	 * @return imsi
	 */
	public static String getIMSI(Context c) {
		return ((TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId();	
	}
	
	/**
	 * @return imei
	 */
	public static String getIMEI(Context c) {	
		return ((TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();	
	}
	
	/**
	 * @return phoneNumber
	 */
	public static String getPhoneNumber(Context c) {
		return ((TelephonyManager)c.getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number();	
	}
	
	/**
	 * 获取MAC地址
	 * @param context
	 * @return
	 */
	public static String getMacAddress(Context context){
		return ((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).getConnectionInfo().getMacAddress();
	}
	
	/**
     * 蓝牙地址
     * @return
     */
    public static String BlueToothMAC(){
        try {
            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
            String BT_MAC = adapter.getAddress();
            return null == BT_MAC ? "" : BT_MAC;
        }catch (Exception e){
            return "";
        }
    }
	
	/**
	 * 返回主板信息
	 */
	public static String getBoard() {
		return android.os.Build.BOARD;
	}

	/**
	 * device factory name, e.g: Samsung
	 * </br>Android系统定制商   
	 * @return the vENDOR
	 */
	public static String getVendor() {
		return android.os.Build.BRAND;
	}

	/**
	 * device model name, e.g: GT-I9100
	 * </br>返回手机型号
	 * @return the user_Agent
	 */
	public static String getDevice() {
		return android.os.Build.MODEL;
	}

	/**
	 * @return the OS version
	 * </br>固件版本
	 */
	public static String getOSVersion() {
		return android.os.Build.VERSION.RELEASE;
	}
	
	/**
	 * 基带版本
	 */
	public static String getOSIncremental() {
		return android.os.Build.VERSION.INCREMENTAL;
	}

	/**
	 * @return the SDK version
	 * </br>返回SDK版本
	 */
	public static int getSDKVersion() {
		return android.os.Build.VERSION.SDK_INT;
	}
	
	/**
	 * 检查当前手机的系统版本是否支持新版本的功能
	 * @param sdkVersionCode
	 * @return 如果支持则返回true
	 */
	public static boolean checkSDKVersion(int sdkVersionCode) {
		return getSDKVersion() >= sdkVersionCode;
	}

	/**
	 * Retrieves application's version number from the manifest
	 * @return versionName
	 */
	public static String getVersion(Context c) {
		String version = "-1";
		try {
			PackageInfo packageInfo = c.getPackageManager().getPackageInfo(c.getPackageName(), 0);
			version = packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return version;
	}

	/**
	 * Retrieves application's version code from the manifest
	 * @return versionCode
	 */
	public static int getVersionCode(Context c) {
		int code = 1;
		try {
			PackageInfo packageInfo = c.getPackageManager().getPackageInfo(c.getPackageName(), 0);
			code = packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return code;
	}
	
	/**
	 * Launch apk.
	 */
	public static void startApp(Context context, String packageName) {
		Intent intent = new Intent(Intent.ACTION_MAIN, null);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		PackageManager pm = context.getPackageManager();
		List<ResolveInfo> listInfos = pm.queryIntentActivities(intent, 0);
		String className = null;
		for (ResolveInfo info : listInfos) {
			if (packageName.equals(info.activityInfo.packageName)) {
				className = info.activityInfo.name;
				break;
			}
		}
		if (className != null && className.length() > 0) {
			intent.setComponent(new ComponentName(packageName, className));
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
			context.startActivity(intent);
		}
	}

	/**
	 * @return Get applicationName mysely.
	 */
	public static String getAppName(Context c) {
		return getAppName(c, null);
	}

	/**
	 * @return Get applicationName by packageName.
	 */
	public static String getAppName(Context c, String packageName) {
		String applicationName = null;

		if (packageName == null) 
			packageName = c.getPackageName();

		try {
			PackageManager packageManager = c.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
			applicationName = c.getString(packageInfo.applicationInfo.labelRes);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return applicationName;
	}
	
	/**
	 * 通过apk文件得到当前apk的包信息
	 * @param ctx
	 * @param apkFilePath	apk文件绝对目录
	 * @return
	 */
	public static PackageInfo getApkFilePackageInfo(Context ctx, String apkFilePath) {
		PackageManager pm = ctx.getPackageManager();      
        return pm.getPackageArchiveInfo(apkFilePath, PackageManager.GET_ACTIVITIES);
        //ApplicationInfo appInfo = info.applicationInfo;  
	}
	
	/**
	 * 通过apk文件得到当前apk的包名
	 * @param ctx
	 * @param apkFilePath		apk文件绝对目录
	 * @return
	 */
	public static String getApkFilePackageName(Context ctx, String apkFilePath) {
		PackageInfo info = getApkFilePackageInfo(ctx, apkFilePath);
		if (null != info)
			return info.packageName;
		return null;
	}	
	
	/**
     * install according conditions
     * <ul>
     * <li>if system application or rooted, see {@link #installSilent(Context, String)}</li>
     * <li>else see {@link #installNormal(Context, String)}</li>
     * </ul>
     * 
     * @param context
     * @param filePath
     * @return
     */
    public static final int install(Context context, String filePath) {
        if (isSystemApplication(context) || ShellUtils.checkRootPermission()) {
            return installSilent(context, filePath);
        }
        return installNormal(context, filePath) ? INSTALL_SUCCEEDED : INSTALL_FAILED_INVALID_URI;
    }

    /**
     * install package normal by system intent
     * 
     * @param context
     * @param filePath file path of package
     * @return whether apk exist
     */
    public static boolean installNormal(Context context, String filePath) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        File file = new File(filePath);
        if (file == null || !file.exists() || !file.isFile() || file.length() <= 0) {
            return false;
        }

        i.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        return true;
    }

    /**
     * install package silent by root
     * <ul>
     * <strong>Attentions:</strong>
     * <li>Don't call this on the ui thread, it may costs some times.</li>
     * <li>You should add <strong>android.permission.INSTALL_PACKAGES</strong> in manifest, so no need to request root
     * permission, if you are system app.</li>
     * <li>Default pm install params is "-r".</li>
     * </ul>
     * 
     * @param context
     * @param filePath file path of package
     * @return {@link PackageUtils#INSTALL_SUCCEEDED} means install success, other means failed. details see
     * {@link PackageUtils}.INSTALL_FAILED_*. same to {@link PackageManager}.INSTALL_*
     * @see #installSilent(Context, String, String)
     */
    public static int installSilent(Context context, String filePath) {
        return installSilent(context, filePath, "-r");
    }

    /**
     * install package silent by root
     * <ul>
     * <strong>Attentions:</strong>
     * <li>Don't call this on the ui thread, it may costs some times.</li>
     * <li>You should add <strong>android.permission.INSTALL_PACKAGES</strong> in manifest, so no need to request root
     * permission, if you are system app.</li>
     * </ul>
     * 
     * @param context
     * @param filePath file path of package
     * @param pmParams pm install params
     * @return {@link PackageUtils#INSTALL_SUCCEEDED} means install success, other means failed. details see
     * {@link PackageUtils}.INSTALL_FAILED_*. same to {@link PackageManager}.INSTALL_*
     */
    public static int installSilent(Context context, String filePath, String pmParams) {
        if (filePath == null || filePath.length() == 0) {
            return INSTALL_FAILED_INVALID_URI;
        }

        File file = new File(filePath);
        if (file == null || file.length() <= 0 || !file.exists() || !file.isFile()) {
            return INSTALL_FAILED_INVALID_URI;
        }

        /**
         * if context is system app, don't need root permission, but should add <uses-permission
         * android:name="android.permission.INSTALL_PACKAGES" /> in mainfest
         **/
        StringBuilder command = new StringBuilder().append("LD_LIBRARY_PATH=/vendor/lib:/system/lib pm install ")
                                                   .append(pmParams == null ? "" : pmParams).append(" ")
                                                   .append(filePath.replace(" ", "\\ "));
        CommandResult commandResult = ShellUtils.execCommand(command.toString(), !isSystemApplication(context), true);
        if (commandResult.successMsg != null
            && (commandResult.successMsg.contains("Success") || commandResult.successMsg.contains("success"))) {
            return INSTALL_SUCCEEDED;
        }

        L.e(new StringBuilder().append("installSilent successMsg:").append(commandResult.successMsg)
                                 .append(", ErrorMsg:").append(commandResult.errorMsg).toString());
        if (commandResult.errorMsg == null) {
            return INSTALL_FAILED_OTHER;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_ALREADY_EXISTS")) {
            return INSTALL_FAILED_ALREADY_EXISTS;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_INVALID_APK")) {
            return INSTALL_FAILED_INVALID_APK;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_INVALID_URI")) {
            return INSTALL_FAILED_INVALID_URI;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_INSUFFICIENT_STORAGE")) {
            return INSTALL_FAILED_INSUFFICIENT_STORAGE;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_DUPLICATE_PACKAGE")) {
            return INSTALL_FAILED_DUPLICATE_PACKAGE;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_NO_SHARED_USER")) {
            return INSTALL_FAILED_NO_SHARED_USER;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_UPDATE_INCOMPATIBLE")) {
            return INSTALL_FAILED_UPDATE_INCOMPATIBLE;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_SHARED_USER_INCOMPATIBLE")) {
            return INSTALL_FAILED_SHARED_USER_INCOMPATIBLE;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_MISSING_SHARED_LIBRARY")) {
            return INSTALL_FAILED_MISSING_SHARED_LIBRARY;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_REPLACE_COULDNT_DELETE")) {
            return INSTALL_FAILED_REPLACE_COULDNT_DELETE;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_DEXOPT")) {
            return INSTALL_FAILED_DEXOPT;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_OLDER_SDK")) {
            return INSTALL_FAILED_OLDER_SDK;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_CONFLICTING_PROVIDER")) {
            return INSTALL_FAILED_CONFLICTING_PROVIDER;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_NEWER_SDK")) {
            return INSTALL_FAILED_NEWER_SDK;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_TEST_ONLY")) {
            return INSTALL_FAILED_TEST_ONLY;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_CPU_ABI_INCOMPATIBLE")) {
            return INSTALL_FAILED_CPU_ABI_INCOMPATIBLE;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_MISSING_FEATURE")) {
            return INSTALL_FAILED_MISSING_FEATURE;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_CONTAINER_ERROR")) {
            return INSTALL_FAILED_CONTAINER_ERROR;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_INVALID_INSTALL_LOCATION")) {
            return INSTALL_FAILED_INVALID_INSTALL_LOCATION;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_MEDIA_UNAVAILABLE")) {
            return INSTALL_FAILED_MEDIA_UNAVAILABLE;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_VERIFICATION_TIMEOUT")) {
            return INSTALL_FAILED_VERIFICATION_TIMEOUT;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_VERIFICATION_FAILURE")) {
            return INSTALL_FAILED_VERIFICATION_FAILURE;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_PACKAGE_CHANGED")) {
            return INSTALL_FAILED_PACKAGE_CHANGED;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_UID_CHANGED")) {
            return INSTALL_FAILED_UID_CHANGED;
        }
        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_NOT_APK")) {
            return INSTALL_PARSE_FAILED_NOT_APK;
        }
        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_BAD_MANIFEST")) {
            return INSTALL_PARSE_FAILED_BAD_MANIFEST;
        }
        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION")) {
            return INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION;
        }
        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_NO_CERTIFICATES")) {
            return INSTALL_PARSE_FAILED_NO_CERTIFICATES;
        }
        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_INCONSISTENT_CERTIFICATES")) {
            return INSTALL_PARSE_FAILED_INCONSISTENT_CERTIFICATES;
        }
        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_CERTIFICATE_ENCODING")) {
            return INSTALL_PARSE_FAILED_CERTIFICATE_ENCODING;
        }
        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME")) {
            return INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME;
        }
        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_BAD_SHARED_USER_ID")) {
            return INSTALL_PARSE_FAILED_BAD_SHARED_USER_ID;
        }
        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_MANIFEST_MALFORMED")) {
            return INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
        }
        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_MANIFEST_EMPTY")) {
            return INSTALL_PARSE_FAILED_MANIFEST_EMPTY;
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_INTERNAL_ERROR")) {
            return INSTALL_FAILED_INTERNAL_ERROR;
        }
        return INSTALL_FAILED_OTHER;
    }

    /**
     * uninstall according conditions
     * <ul>
     * <li>if system application or rooted, see {@link #uninstallSilent(Context, String)}</li>
     * <li>else see {@link #uninstallNormal(Context, String)}</li>
     * </ul>
     * 
     * @param context
     * @param packageName package name of app
     * @return whether package name is empty
     * @return
     */
    public static final int uninstall(Context context, String packageName) {
        if (isSystemApplication(context) || ShellUtils.checkRootPermission()) {
            return uninstallSilent(context, packageName);
        }
        return uninstallNormal(context, packageName) ? DELETE_SUCCEEDED : DELETE_FAILED_INVALID_PACKAGE;
    }

    /**
     * uninstall package normal by system intent
     * 
     * @param context
     * @param packageName package name of app
     * @return whether package name is empty
     */
    public static boolean uninstallNormal(Context context, String packageName) {
        if (packageName == null || packageName.length() == 0) {
            return false;
        }

        Intent i = new Intent(Intent.ACTION_DELETE, Uri.parse(new StringBuilder(32).append("package:")
                                                                                   .append(packageName).toString()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        return true;
    }

    /**
     * uninstall package and clear data of app silent by root
     * 
     * @param context
     * @param packageName package name of app
     * @return
     * @see #uninstallSilent(Context, String, boolean)
     */
    public static int uninstallSilent(Context context, String packageName) {
        return uninstallSilent(context, packageName, true);
    }

    /**
     * uninstall package silent by root
     * <ul>
     * <strong>Attentions:</strong>
     * <li>Don't call this on the ui thread, it may costs some times.</li>
     * <li>You should add <strong>android.permission.DELETE_PACKAGES</strong> in manifest, so no need to request root
     * permission, if you are system app.</li>
     * </ul>
     * 
     * @param context file path of package
     * @param packageName package name of app
     * @param isKeepData whether keep the data and cache directories around after package removal
     * @return <ul>
     * <li>{@link #DELETE_SUCCEEDED} means uninstall success</li>
     * <li>{@link #DELETE_FAILED_INTERNAL_ERROR} means internal error</li>
     * <li>{@link #DELETE_FAILED_INVALID_PACKAGE} means package name error</li>
     * <li>{@link #DELETE_FAILED_PERMISSION_DENIED} means permission denied</li>
     */
    public static int uninstallSilent(Context context, String packageName, boolean isKeepData) {
        if (packageName == null || packageName.length() == 0) {
            return DELETE_FAILED_INVALID_PACKAGE;
        }

        /**
         * if context is system app, don't need root permission, but should add <uses-permission
         * android:name="android.permission.DELETE_PACKAGES" /> in mainfest
         **/
        StringBuilder command = new StringBuilder().append("LD_LIBRARY_PATH=/vendor/lib:/system/lib pm uninstall")
                                                   .append(isKeepData ? " -k " : " ")
                                                   .append(packageName.replace(" ", "\\ "));
        CommandResult commandResult = ShellUtils.execCommand(command.toString(), !isSystemApplication(context), true);
        if (commandResult.successMsg != null
            && (commandResult.successMsg.contains("Success") || commandResult.successMsg.contains("success"))) {
            return DELETE_SUCCEEDED;
        }
		L.e(new StringBuilder().append("uninstallSilent successMsg:").append(commandResult.successMsg)
				.append(", ErrorMsg:").append(commandResult.errorMsg).toString());
        if (commandResult.errorMsg == null) {
            return DELETE_FAILED_INTERNAL_ERROR;
        }
        if (commandResult.errorMsg.contains("Permission denied")) {
            return DELETE_FAILED_PERMISSION_DENIED;
        }
        return DELETE_FAILED_INTERNAL_ERROR;
    }

    /**
     * whether context is system application
     * 
     * @param context
     * @return
     */
    public static boolean isSystemApplication(Context context) {
        if (context == null) {
            return false;
        }

        return isSystemApplication(context, context.getPackageName());
    }

    /**
     * whether packageName is system application
     * 
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isSystemApplication(Context context, String packageName) {
        if (context == null) {
            return false;
        }

        return isSystemApplication(context.getPackageManager(), packageName);
    }

    /**
     * whether packageName is system application
     * 
     * @param packageManager
     * @param packageName
     * @return <ul>
     * <li>if packageManager is null, return false</li>
     * <li>if package name is null or is empty, return false</li>
     * <li>if package name not exit, return false</li>
     * <li>if package name exit, but not system app, return false</li>
     * <li>else return true</li>
     * </ul>
     */
    public static boolean isSystemApplication(PackageManager packageManager, String packageName) {
        if (packageManager == null || packageName == null || packageName.length() == 0) {
            return false;
        }

        try {
            ApplicationInfo app = packageManager.getApplicationInfo(packageName, 0);
            return (app != null && (app.flags & ApplicationInfo.FLAG_SYSTEM) > 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * start InstalledAppDetails Activity
     * 
     * @param context
     * @param packageName
     */
    public static void startInstalledAppDetails(Context context, String packageName) {
        Intent intent = new Intent();
        int sdkVersion = android.os.Build.VERSION.SDK_INT;
        if (sdkVersion >= 9) {
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.fromParts("package", packageName, null));
        } else {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra((sdkVersion == 8 ? "pkg" : "com.android.settings.ApplicationPkgName"), packageName);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    
    /**
     * Installation return code<br/>
     * install success.
     */
    public static final int INSTALL_SUCCEEDED                              = 1;
    /**
     * Installation return code<br/>
     * the package is already installed.
     */
    public static final int INSTALL_FAILED_ALREADY_EXISTS                  = -1;

    /**
     * Installation return code<br/>
     * the package archive file is invalid.
     */
    public static final int INSTALL_FAILED_INVALID_APK                     = -2;

    /**
     * Installation return code<br/>
     * the URI passed in is invalid.
     */
    public static final int INSTALL_FAILED_INVALID_URI                     = -3;

    /**
     * Installation return code<br/>
     * the package manager service found that the device didn't have enough storage space to install the app.
     */
    public static final int INSTALL_FAILED_INSUFFICIENT_STORAGE            = -4;

    /**
     * Installation return code<br/>
     * a package is already installed with the same name.
     */
    public static final int INSTALL_FAILED_DUPLICATE_PACKAGE               = -5;

    /**
     * Installation return code<br/>
     * the requested shared user does not exist.
     */
    public static final int INSTALL_FAILED_NO_SHARED_USER                  = -6;

    /**
     * Installation return code<br/>
     * a previously installed package of the same name has a different signature than the new package (and the old
     * package's data was not removed).
     */
    public static final int INSTALL_FAILED_UPDATE_INCOMPATIBLE             = -7;

    /**
     * Installation return code<br/>
     * the new package is requested a shared user which is already installed on the device and does not have matching
     * signature.
     */
    public static final int INSTALL_FAILED_SHARED_USER_INCOMPATIBLE        = -8;

    /**
     * Installation return code<br/>
     * the new package uses a shared library that is not available.
     */
    public static final int INSTALL_FAILED_MISSING_SHARED_LIBRARY          = -9;

    /**
     * Installation return code<br/>
     * the new package uses a shared library that is not available.
     */
    public static final int INSTALL_FAILED_REPLACE_COULDNT_DELETE          = -10;

    /**
     * Installation return code<br/>
     * the new package failed while optimizing and validating its dex files, either because there was not enough storage
     * or the validation failed.
     */
    public static final int INSTALL_FAILED_DEXOPT                          = -11;

    /**
     * Installation return code<br/>
     * the new package failed because the current SDK version is older than that required by the package.
     */
    public static final int INSTALL_FAILED_OLDER_SDK                       = -12;

    /**
     * Installation return code<br/>
     * the new package failed because it contains a content provider with the same authority as a provider already
     * installed in the system.
     */
    public static final int INSTALL_FAILED_CONFLICTING_PROVIDER            = -13;

    /**
     * Installation return code<br/>
     * the new package failed because the current SDK version is newer than that required by the package.
     */
    public static final int INSTALL_FAILED_NEWER_SDK                       = -14;

    /**
     * Installation return code<br/>
     * the new package failed because it has specified that it is a test-only package and the caller has not supplied
     * the {@link #INSTALL_ALLOW_TEST} flag.
     */
    public static final int INSTALL_FAILED_TEST_ONLY                       = -15;

    /**
     * Installation return code<br/>
     * the package being installed contains native code, but none that is compatible with the the device's CPU_ABI.
     */
    public static final int INSTALL_FAILED_CPU_ABI_INCOMPATIBLE            = -16;

    /**
     * Installation return code<br/>
     * the new package uses a feature that is not available.
     */
    public static final int INSTALL_FAILED_MISSING_FEATURE                 = -17;

    /**
     * Installation return code<br/>
     * a secure container mount point couldn't be accessed on external media.
     */
    public static final int INSTALL_FAILED_CONTAINER_ERROR                 = -18;

    /**
     * Installation return code<br/>
     * the new package couldn't be installed in the specified install location.
     */
    public static final int INSTALL_FAILED_INVALID_INSTALL_LOCATION        = -19;

    /**
     * Installation return code<br/>
     * the new package couldn't be installed in the specified install location because the media is not available.
     */
    public static final int INSTALL_FAILED_MEDIA_UNAVAILABLE               = -20;

    /**
     * Installation return code<br/>
     * the new package couldn't be installed because the verification timed out.
     */
    public static final int INSTALL_FAILED_VERIFICATION_TIMEOUT            = -21;

    /**
     * Installation return code<br/>
     * the new package couldn't be installed because the verification did not succeed.
     */
    public static final int INSTALL_FAILED_VERIFICATION_FAILURE            = -22;

    /**
     * Installation return code<br/>
     * the package changed from what the calling program expected.
     */
    public static final int INSTALL_FAILED_PACKAGE_CHANGED                 = -23;

    /**
     * Installation return code<br/>
     * the new package is assigned a different UID than it previously held.
     */
    public static final int INSTALL_FAILED_UID_CHANGED                     = -24;

    /**
     * Installation return code<br/>
     * if the parser was given a path that is not a file, or does not end with the expected '.apk' extension.
     */
    public static final int INSTALL_PARSE_FAILED_NOT_APK                   = -100;

    /**
     * Installation return code<br/>
     * if the parser was unable to retrieve the AndroidManifest.xml file.
     */
    public static final int INSTALL_PARSE_FAILED_BAD_MANIFEST              = -101;

    /**
     * Installation return code<br/>
     * if the parser encountered an unexpected exception.
     */
    public static final int INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION      = -102;

    /**
     * Installation return code<br/>
     * if the parser did not find any certificates in the .apk.
     */
    public static final int INSTALL_PARSE_FAILED_NO_CERTIFICATES           = -103;

    /**
     * Installation return code<br/>
     * if the parser found inconsistent certificates on the files in the .apk.
     */
    public static final int INSTALL_PARSE_FAILED_INCONSISTENT_CERTIFICATES = -104;

    /**
     * Installation return code<br/>
     * if the parser encountered a CertificateEncodingException in one of the files in the .apk.
     */
    public static final int INSTALL_PARSE_FAILED_CERTIFICATE_ENCODING      = -105;

    /**
     * Installation return code<br/>
     * if the parser encountered a bad or missing package name in the manifest.
     */
    public static final int INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME          = -106;

    /**
     * Installation return code<br/>
     * if the parser encountered a bad shared user id name in the manifest.
     */
    public static final int INSTALL_PARSE_FAILED_BAD_SHARED_USER_ID        = -107;

    /**
     * Installation return code<br/>
     * if the parser encountered some structural problem in the manifest.
     */
    public static final int INSTALL_PARSE_FAILED_MANIFEST_MALFORMED        = -108;

    /**
     * Installation return code<br/>
     * if the parser did not find any actionable tags (instrumentation or application) in the manifest.
     */
    public static final int INSTALL_PARSE_FAILED_MANIFEST_EMPTY            = -109;

    /**
     * Installation return code<br/>
     * if the system failed to install the package because of system issues.
     */
    public static final int INSTALL_FAILED_INTERNAL_ERROR                  = -110;
    /**
     * Installation return code<br/>
     * other reason
     */
    public static final int INSTALL_FAILED_OTHER                           = -1000000;
    
    /**
     * Uninstall return code<br/>
     * uninstall success.
     */
    public static final int DELETE_SUCCEEDED                               = 1;

    /**
     * Uninstall return code<br/>
     * uninstall fail if the system failed to delete the package for an unspecified reason.
     */
    public static final int DELETE_FAILED_INTERNAL_ERROR                   = -1;

    /**
     * Uninstall return code<br/>
     * uninstall fail if the system failed to delete the package because it is the active DevicePolicy manager.
     */
    public static final int DELETE_FAILED_DEVICE_POLICY_MANAGER            = -2;

    /**
     * Uninstall return code<br/>
     * uninstall fail if pcakge name is invalid
     */
    public static final int DELETE_FAILED_INVALID_PACKAGE                  = -3;

    /**
     * Uninstall return code<br/>
     * uninstall fail if permission denied
     */
    public static final int DELETE_FAILED_PERMISSION_DENIED                = -4;
}
