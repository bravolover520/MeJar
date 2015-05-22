package me.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * 基类，继承此类
 * @author Jesus{931178805@qq.com}
 * 2014年7月9日
 */
public abstract class BaseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//添加Activity到堆栈
		AppManager.getAppManager().addActivity(this);
	}
	
	@Override
	protected void onDestroy() {
		//结束Activity&从堆栈中移除
		AppManager.getAppManager().finishActivity(this);
		super.onDestroy();
	}
	
	// 结果与(...)findViewById(R.id....)一模一样，采用$作为方法名称，借鉴自jQuery
	@SuppressWarnings("unchecked")
	public <T extends View> T $(int id) {
		return (T) super.findViewById(id);
	}
}
