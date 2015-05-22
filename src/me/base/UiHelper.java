package me.base;

import me.utils.TextUtils;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.ResultReceiver;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 常用显示辅助类
 * @author Jesus{931178805@qq.com}
 * 2014年8月1日
 */
public class UiHelper {
	
	/**
	 * 显示Toast
	 * @param context
	 * @param message
	 * @param duration
	 */
	public static void showToast(Context context, String message, int duration) {
		android.widget.Toast.makeText(context, message, duration).show();
    }

	/**
	 * 显示Toast
	 * @param context
	 * @param message
	 */
    public static void showShortToast(Context context, String message) {
        showToast(context, message, android.widget.Toast.LENGTH_SHORT);
    }
    
    /**
	 * 显示Toast
	 * @param context
	 * @param message
	 */
    public static void showShortToast(Context context, int message) {
        showShortToast(context, context.getResources().getString(message));
    }

    /**
	 * 显示Toast
	 * @param context
	 * @param message
	 */
    public static void showLongToast(Context context, String message) {
        showToast(context, message, android.widget.Toast.LENGTH_LONG);
    }
    
    /**
	 * 显示Toast
	 * @param context
	 * @param message
	 */
    public static void showLongToast(Context context, int message) {
        showLongToast(context, context.getResources().getString(message));
    }
	
	/**
	 * @param context
	 * <p><b> < uses-permission android:name="android.permission.DISABLE_KEYGUARD" /> </b></p>
	 * @return	当前是否是锁屏界面
	 */
	public static boolean isLocked(Context context) {
        KeyguardManager myKM = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        if (myKM.inKeyguardRestrictedInputMode()) {
            return true;
        } else {
            return false;
        }
    }
	
	/**
	 * 是否锁屏了
	 * 
	 * @param context	上下文
	 * @return true锁屏， false
	 */
	public static boolean isScreenOn(Context ctx) {
		PowerManager powerManager = (PowerManager) ctx.getApplicationContext().getSystemService(Context.POWER_SERVICE);
		return powerManager.isScreenOn();
	}
	
	/**
	 * 屏幕方向
	 * @author Jesus{931178805@qq.com}
	 * 2014年8月1日
	 */
	public static enum Orientation {
        Portrait, Landscape
    }
	
	/**
	 * 得到当前屏幕的方向
	 * @param c
	 * @return
	 */
	public static Orientation getScreenOrientation(Context c) {
        Display display = ((WindowManager) c.getSystemService(
                Context.WINDOW_SERVICE)).getDefaultDisplay();

        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        if (metrics.widthPixels > metrics.heightPixels) {
            return Orientation.Landscape;
        } else {
            return Orientation.Portrait;
        }
    }

	/**
	 * 隐藏键盘
	 * @param view
	 */
	public static void hideSoftKeyboard(View view) {
		if (null == view) return;
		InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null) {
			IBinder localIBinder = view.getWindowToken();
			if (localIBinder != null)
				imm.hideSoftInputFromWindow(localIBinder, 0);
		}
	}
	
	/**
	 * 隐藏软键盘
	 * </br> 附android:windowSoftInputMode参数,各个值之间用'|'分开.
	 * <ul>
	 * <li>【A】stateUnspecified：软键盘的状态并没有指定，系统将选择一个合适的状态或依赖于主题的设置
	 * <li>【B】stateUnchanged：当这个activity出现时，软键盘将一直保持在上一个activity里的状态，无论是隐藏还是显示
	 * <li>【C】stateHidden：用户选择activity时，软键盘总是被隐藏
	 * <li>【D】stateAlwaysHidden：当该Activity主窗口获取焦点时，软键盘也总是被隐藏的
	 * <li>【E】stateVisible：软键盘通常是可见的
	 * <li>【F】stateAlwaysVisible：用户选择activity时，软键盘总是显示的状态
	 * <li>【G】adjustUnspecified：默认设置，通常由系统自行决定是隐藏还是显示
	 * <li>【H】adjustResize：该Activity总是调整屏幕的大小以便留出软键盘的空间
	 * <li>【I】adjustPan：当前窗口的内容将自动移动以便当前焦点从不被键盘覆盖和用户能总是看到输入内容的部分
	 * </ul>
	 * @param activity
	 */
	public static void hideSoftKeyboard(Activity activity) {
		if (null == activity) return;
		InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		View currentFocus = activity.getCurrentFocus();
		if (null != currentFocus) {
			imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
	
	/**
	 * 显示键盘
	 * @param view
	 */
	public static void showSoftkeyboard(View view) {
		showSoftkeyboard(view, null);
	}

	public static void showSoftkeyboard(View view, ResultReceiver resultReceiver) {
		Configuration config = view.getContext().getResources().getConfiguration();
		if (config.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
			InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(
					Context.INPUT_METHOD_SERVICE);

			if (resultReceiver != null) {
				imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT, resultReceiver);
			} else {
				imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
			}
		}
	}
	
	/**
	 * 得到EditText的字符串
	 * @param et
	 * @return
	 */
	public static String getText(EditText et) {
		String text = et.getText().toString();
		if (!TextUtils.isEmpty(text)) {
			return text;
		} else {
			return "";
		}
	}
	
	/**
	 * 光标显示在最后
	 * @param et
	 */
	public static void setSelection(EditText et) {
		String text = getText(et);
		if (!TextUtils.isEmpty(text)) {
			et.setSelection(text.length());
		}
	}
}
