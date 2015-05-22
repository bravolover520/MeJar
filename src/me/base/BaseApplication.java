package me.base;

import me.utils.FileUtils;
import me.utils.L;
import me.utils.PreferencesUtils;
import android.content.Context;

/**
 * 程序Application继承此类
 * @author Jesus{931178805@qq.com}
 * 2014年7月8日
 */
public abstract class BaseApplication extends android.app.Application implements me.base.CrashHandler.OnSendReportListener{
	
	/**单例得到全局 Context**/
    public static Context globalContext = null;
    /**公共配置包**/
    public Config config = null;
	/**全局捕获工具类**/
    public static CrashHandler crashHandler = null;
    /**文件工具包**/
    public static FileUtils fileUtils = null;
    /**xml配置文件**/
    public static PreferencesUtils preferencesUtils = null;

	@Override
	public void onCreate() {
		super.onCreate();
		globalContext = this;
		init();
		init(this);
		if (null != config) {
			fileUtils.init(config);
			crashHandler.init(config);
			preferencesUtils.init(config);
			L.i(FileUtils.system());  //打印日志
		}		
	}
	
	@Override
	public void onLowMemory() {
		/**加上低内存的时候回收不需要的内存**/
    	System.gc();
		super.onLowMemory();
	}
	
	@Override
	public void onTerminate() {
		super.onTerminate();
	}
	
	public static Context getContext() {
		return globalContext.getApplicationContext();
	}
	
	private void init() {
		globalContext = this;	
		fileUtils = FileUtils.getInstance();
		//注册App异常崩溃处理类
    	//捕捉运行时异常
    	crashHandler = CrashHandler.getInstance();
    	crashHandler.init(getContext()); 
        /**错误日志上传回调接口**/
    	crashHandler.setOnSendReportListener(this);
    	preferencesUtils = PreferencesUtils.getInstance();
	}
	
	@Override
	public void onSendReport(String content) {
		sendReport(content);
	}
	
	public abstract void init(Context context);
	public abstract void sendReport(String content);
	
}
