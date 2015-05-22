package me.base;

import java.util.LinkedList;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

/**
 * 应用程序Activity管理类：用于Activity管理和应用程序退出
 * @author Jesus{931178805@qq.com}
 * 2014年7月8日
 */
public class AppManager {

	protected static LinkedList<Activity> mQueue = new LinkedList<Activity>();

	private static AppManager instance;
	
	public static AppManager getAppManager() {
		if (instance == null) syncInit();
		return instance;
	}

	private static synchronized void syncInit() {
		if (instance == null) {
			instance = new AppManager();
		}
	}
	
	/**
	 * 添加Activity到堆栈
	 */
	public void addActivity(Activity activity) {
		if (mQueue == null) {
			mQueue = new LinkedList<Activity>();
		}
		if (!mQueue.contains(activity)) {
			mQueue.add(activity);
		}
	}

	/**
	 * 得到索引的Activity
	 * 
	 * @param index
	 */
	public static Activity getActivity(int index) {
		if (index < 0 || index >= mQueue.size())
			throw new IllegalArgumentException("out of queue");
		return mQueue.get(index);
	}

	/**
	 * 获取当前Activity（堆栈中最后一个压入的）
	 */
	public Activity currentActivity() {
		return mQueue.getLast();
	}

	/**
	 * 结束当前Activity（堆栈中最后一个压入的）
	 */
	public void finishCurrentActivity() {
		Activity activity = mQueue.getLast();
		finishActivity(activity);
	}

	/**
	 * 结束指定的Activity
	 */
	public void finishActivity(Activity activity) {
		if (activity != null) {
			mQueue.remove(activity);
			activity.finish();
			activity = null;
		}
	}
	
	/**
	 * 结束指定类名的Activity
	 */
	public void finishActivity(Class<?> cls) {
		for (Activity activity : mQueue) {
			if (activity.getClass().equals(cls)) {
				finishActivity(activity);
			}
		}
	}

	/**
	 * 结束所有Activity
	 */
	public void finishAllActivity() {
		while (mQueue.size() > 0) {
			finishCurrentActivity();
		}
		mQueue.clear();
	}

	/**
	 * 退出应用程序
	 */
	public void exitApp() {
		try {
			finishAllActivity();
			ActivityManager activityMgr = (ActivityManager) BaseApplication.getContext().getSystemService(
					Context.ACTIVITY_SERVICE);
			activityMgr.restartPackage(BaseApplication.getContext().getPackageName());
			System.exit(0);
		} catch (Exception e) {
		}
	}
}
