package me.base;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import me.utils.AndroidUtils;
import me.utils.DateUtils;
import me.utils.FileUtils;
import me.utils.L;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Looper;

/**
 * 捕获异常类
 * @author Jesus{931178805@qq.com}
 * 2014年7月8日
 */
@SuppressWarnings("serial")
public class CrashHandler extends Exception implements UncaughtExceptionHandler {
	
	private static final String LOG_INIT_CONFIG = "Initialize CrashHandler with configuration.";
	private static final String ERROR_INIT_CONFIG_WITH_NULL = "CrashHandler configuration can not be initialized with null.";
	private static final String WARNING_RE_INIT_CONFIG = "Try to initialize CrashHandler which had already been initialized before.";
	
	// 系统默认的UncaughtException处理类
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	// CrashHandler实例
	private static CrashHandler instance;
	// 配置
	private static Config config;
	// 程序的Context对象
	private Context mContext;
	
	// 用来存储设备信息和异常信息
	private Map<String, String> mInfos = new HashMap<String, String>();
	
	private OnSendReportListener listener;
	
	public interface OnSendReportListener {
		/**
		 * 异步发送错误日志(可以发送给自己建的服务器,也可以发送给我们的邮箱).
		 * @param content
		 * @return
		 */
		void onSendReport(String content);
	}
	
	public void setOnSendReportListener(OnSendReportListener l) {
		this.listener = l;
	}

	/** 获取CrashHandler实例 ,单例模式 */
	public static CrashHandler getInstance() {
		if (instance == null) {
			synchronized (CrashHandler.class){
				if (instance == null) {
					instance = new CrashHandler();
				}
			}
		}
		return instance;
	}
	
	/**
	 * 初始化,注册Context对象, 获取系统默认的UncaughtException处理器, 设置该CrashHandler为程序的默认处理器
	 */
	public void init(Context context){
		mContext = context;
		this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}
	
	public synchronized void init(Config cfg) {
    	if (cfg == null) {
			throw new IllegalArgumentException(ERROR_INIT_CONFIG_WITH_NULL);
		}
    	if (config == null) {
			if (cfg.writeLogs) L.d(LOG_INIT_CONFIG);
			config = cfg;
		} else {
			L.w(WARNING_RE_INIT_CONFIG);
		}
    }

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		// 当UncaughtException发生时会转入该函数来处理
		if (!handleException(ex) && mDefaultHandler != null) {
			// 如果用户没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			try {
				Thread.sleep(1000);
				android.os.Process.killProcess(android.os.Process.myPid());
				System.exit(0);
			} catch (InterruptedException e) {
				L.e("uncaughtException : ", e);
			}
		}
	}
	
	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 开发者可以根据自己的情况来自定义异常处理逻辑
	 * 
	 * @param ex
	 * @return true:如果处理了该异常信息;否则返回false
	 */
	private boolean handleException(final Throwable ex) {
		if (ex == null) return true;
		
		//显示异常信息
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				//备注:这里可以弹出对话框，提示出现的错误异常，让用户上传错误日志
				//Toast>"程序出现了错误" + ex.getMessage()
				L.e(ex.getLocalizedMessage(), ex);
				Looper.loop();
			}
		}.start();
		// 收集设备信息
		collectCrashDeviceInfo(mContext);
		// 保存错误报告文件
		saveCrashInfoToFile(ex);
		// 发送错误报告到服务器
		sendCrashReportsToServer(mContext);
		return true;
	}

	/**
	 * 在程序启动时候, 可以调用该函数来发送以前没有发送的报告
	 */
	public void sendPreviousReportsToServer() {
		sendCrashReportsToServer(mContext);
	}
	
	/**
	 * 把错误报告发送给服务器,包含新产生的和以前没发送的.
	 * @param ctx
	 */
	private void sendCrashReportsToServer(Context ctx) {
		String[] crFiles = getCrashReportFiles(ctx);
		if (crFiles != null && crFiles.length > 0) {
			TreeSet<String> sortedFiles = new TreeSet<String>();
			sortedFiles.addAll(Arrays.asList(crFiles));

			for (String fileName : sortedFiles) {
				File cr = null;
				cr = new File(FileUtils.getErrorLogPath(), fileName);
				if (null == cr || !cr.exists()) 
					cr = new File(ctx.getFilesDir(), fileName);
				if (cr != null) {
					//因为是异步的，所以不能做到在返回日志后才删除文件的
					postReport(cr);
					if (config.reportsDelete) cr.delete();// 删除已发送的报告
				}
			}
		}
	}
	
	/**
	 * 发送错误日志Log
	 * @param file
	 */
	private void postReport(File file) {
		//发送错误报告
		L.i("errFile:" + file.getAbsolutePath());
//		uploadCrash(FileUtils.readFile(file.getAbsolutePath(), "UTF-8").toString());
		if (null != listener) {
			listener.onSendReport(FileUtils.readFile(file.getAbsolutePath(), "UTF-8").toString());
		}
	}
		
	/**
	 * 获取错误报告文件名
	 * @param ctx
	 * @return
	 */
	private String[] getCrashReportFiles(Context ctx) {
		File filesDir = null;
		filesDir = new File(FileUtils.getErrorLogPath());
		if (null == filesDir || !filesDir.exists()) 
			filesDir = ctx.getFilesDir();
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(config.reportsExtension);
			}
		};
		return filesDir.list(filter);
	}
		
	/**
	 * 保存错误信息到文件中
	 * @param ex
	 * @return
	 */
	private String saveCrashInfoToFile(Throwable ex) {
		Writer info = new StringWriter();
		PrintWriter printWriter = new PrintWriter(info);
		ex.printStackTrace(printWriter);

		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}

		String result = info.toString();
		printWriter.close();
		mInfos.put(config.reportsTrace, result);

		try {
			long timestamp = System.currentTimeMillis();
			String fileName = "crash-" + DateUtils.getTime(timestamp) + config.reportsExtension;
			FileOutputStream trace = null;
			if (null != FileUtils.getSdPath()) {
				String path = FileUtils.getErrorLogPath();
				FileUtils.makeDirs(path);
				FileUtils.makeFile(path + File.separator + fileName);				
				trace = new FileOutputStream(path + fileName);
			} else
				trace = mContext.openFileOutput(fileName, Context.MODE_PRIVATE);
			
			StringBuffer sb = new StringBuffer();
			for (Map.Entry<String, String> entry : mInfos.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				sb.append(key + "=" + value + "\n");
			}
			trace.write(sb.toString().getBytes());
			
			trace.flush();
			trace.close();
			return fileName;
		} catch (Exception e) {
			L.e("an error occured while writing report file...", e);
		}
		return null;
	}
		
	/**
	 * 收集程序崩溃的设备信息
	 * @param ctx
	 */
	public void collectCrashDeviceInfo(Context ctx) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				mInfos.put("appVersionName", pi.versionName == null ? "not set" : pi.versionName);
				mInfos.put("appVersionCode", String.valueOf(pi.versionCode));
				mInfos.put("iMEI", AndroidUtils.getIMEI(ctx));
				mInfos.put("iMSI", AndroidUtils.getIMSI(ctx));
				mInfos.put("oSVersion", AndroidUtils.getOSVersion());
				mInfos.put("sDKVersionCode", String.valueOf(AndroidUtils.getSDKVersion()));
				mInfos.put("dAte", DateUtils.getErrorLogTime());
			}
		} catch (NameNotFoundException e) {
			L.e("Error while collect package info", e);
		}
		// 使用反射来收集设备信息.在Build类中包含各种设备信息,
		// 例如: 系统版本号,设备生产商 等帮助调试程序的有用信息
		java.lang.reflect.Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				mInfos.put(field.getName(), field.get(null).toString());
				L.i(field.getName() + " : " + field.get(null));
			} catch (Exception e) {
				L.e("Error while collect crash info", e);
			}
		}
	}
}
