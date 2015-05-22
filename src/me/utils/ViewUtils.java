package me.utils;

import java.lang.reflect.Field;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * View 的工具类
 * <ul>
 * <li>View派生出的直接子类有： 
 * AnalogClock, ImageView, KeyboardView, ProgressBar, SurfaceView, Space, TextView, TextureView, ViewGroup, ViewStub
 * <br/>
 * <li>View派生出的直接子类有：
 * AbsSeekBar, AutoCompleteTextView, Button, CheckBox, CheckedTextView, Chronometer, CompoundButton, DateTimeView, 
 * DigitalClock, EditText, ImageButton, MultiAutoCompleteTextView, QuickContactBadge, RatingBar, RadioButton, 
 * Switch, SeekBar, TextClock, VideoView, ZoomButton
 * <br/>
 * <li>ViewGroup派生出的直接子类有： 
 * ActivityChooserView, AbsoluteLayout, AdapterView, FrameLayout, GridLayout, LinearLayout, RelativeLayout, SlidingDrawer
 * <br/>
 * <li>ViewGroup派生出的间接子类有：
 * AbsListView, AbsSpinner, AdapterViewAnimator, AdapterViewFlipper, AppWidgetHostView, CalendarView, DatePicker, DialerFilter, 
 * ExpandableListView, FragmentBreadCrumbs, Gallery, GestureOverlayView, GridView, HorizontalScrollView, ImageSwitcher, ListView, 
 * MediaController, NumberPicker, RadioGroup, ScrollView, SearchView, Spinner, StackView, TabHost, TableLayout, TableRow, 
 * TabWidget, TextSwitcher, TimePicker, TwoLineListItem, ViewAnimator, ViewFlipper, ViewSwitcher, ZoomControls
 * </ul>
 * 
 * @author Jesus{931178805@qq.com}
 * 2014年7月8日
 */
public class ViewUtils {
	
	/**
	 * 得到当前currentView在屏幕上当前的位置
	 * @param currentView
	 * @return
	 */
	public static Rect getLocation(View currentView) {
		if (null == currentView) return null;
		
		int[] location = new int[2];
		currentView.getLocationOnScreen(location);
		Rect locationRect = new Rect();
		locationRect.left = location[0];
		locationRect.top = location[1];
		locationRect.right = locationRect.left + currentView.getWidth();
		locationRect.bottom = locationRect.top + currentView.getHeight();
		return locationRect;
	}
	
	/**
	 * 为程序创建桌面快捷方式。
	 * 
	 * @param activity
	 *            指定当前的Activity为快捷方式启动的对象
	 * @param nameId
	 *            快捷方式的名称
	 * @param iconId
	 *            快捷方式的图标
	 * @param appendFlags
	 *            需要在快捷方式启动应用的Intent中附加的Flag
	 */
	public static void addShortcut(Activity activity, int nameId, int iconId,
			int appendFlags) {
		Intent shortcut = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");

		// 快捷方式的名称
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,
				activity.getString(nameId));
		shortcut.putExtra("duplicate", false); // 不允许重复创建

		// 指定当前的Activity为快捷方式启动的对象
		ComponentName comp = new ComponentName(activity.getPackageName(),
				activity.getClass().getName());
		Intent intent = new Intent(Intent.ACTION_MAIN).setComponent(comp);
		if (appendFlags != 0) {
			intent.addFlags(appendFlags);
		}
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);

		// 快捷方式的图标
		ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(
				activity, iconId);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);

		activity.sendBroadcast(shortcut);
	}
	
	/***
	 * 截取当前屏幕
	 * @param activity
	 * @param hasStatusBar
	 * @return
	 */
	public static Bitmap printScreen(Activity activity, boolean hasStatusBar) {
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap b = view.getDrawingCache();
		Point screen = getScreenSize(activity);
		int top = hasStatusBar ? 0 : getStatusBarH(activity);
		int height = hasStatusBar ? screen.y : screen.y - getStatusBarH(activity);
		Bitmap bitmap = Bitmap.createBitmap(b, 0, top, screen.x, height);
        view.destroyDrawingCache();
        return bitmap;
	}
	
	/**
	 * 截图当前控件的
	 * @param v
	 * @param quality
	 * @return
	 */
	public static Bitmap printScreen(View v) {
		v.setDrawingCacheEnabled(true);
		v.buildDrawingCache();
		return v.getDrawingCache();
	}
	
	/**
	 * 获取屏幕的尺寸
	 * @param context
	 * @return
	 */
	public static Point getScreenSize(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		dm = context.getResources().getDisplayMetrics();
		Point size = new Point();
		size.set(dm.widthPixels, dm.heightPixels);
		return size;
	}	
	
	/**
	 * 获取系统状态栏高度
	 * @param activity Activity
	 * @return 状态栏高度
	 *
	 */
	public static int getStatusBarHeight(Context ctx){
		try {
			Class<?> clazz = Class.forName("com.android.internal.R$dimen");
			Object object = clazz.newInstance();
			Field field = clazz.getField("status_bar_height");
		    int dpHeight = Integer.parseInt(field.get(object).toString());
		    return ctx.getResources().getDimensionPixelSize(dpHeight);
		} catch (Exception e1) {
		    e1.printStackTrace();
		    return 0;
		} 
	}
	
	/**
	 * 获取状态栏的高度
	 * @param activity
	 * @return
	 */
	public static int getStatusBarH(Activity activity){
		Rect frame = new Rect();  
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);  
		return frame.top;
	}

	/**
	 * 切换全屏状态。
	 * @param activity Activity
	 * @param isFull 设置为true则全屏，否则非全屏
	 */
	public static void toggleFullScreen(Activity activity,boolean isFull){
		hideTitleBar(activity);
		Window window = activity.getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		if (isFull) {
			params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
			window.setAttributes(params);
			window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		} else {
			params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
			window.setAttributes(params);
			window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		}
	}
	
	/**
	 * 设置为全屏
	 * @param activity Activity
	 */
	public static void setFullScreen(Activity activity){
		toggleFullScreen(activity,true);
	}
	
	/**
	 * 隐藏Activity的系统默认标题栏
	 * @param activity Activity
	 */
	public static void hideTitleBar(Activity activity){
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	/**
	 * 得到密度
	 * @param c
	 * @return
	 */
	public static float getDensity(Context c) {
		DisplayMetrics dm = new DisplayMetrics();
		dm = c.getResources().getDisplayMetrics();
		return dm.density;
	}
	
	/**
	 * 得到屏宽
	 */
	public static int getScreenWidth(Context c) {
		DisplayMetrics dm = new DisplayMetrics();
		dm = c.getResources().getDisplayMetrics();
		return dm.widthPixels;
	}
	
	/**
	 * 得到屏高
	 */
	public static int getScreenHeight(Context c) {
		DisplayMetrics dm = new DisplayMetrics();
		dm = c.getResources().getDisplayMetrics();
		return dm.heightPixels;
	}
	
	/**
	 * 得到Dpi
	 */
	public static int getDensityDpi(Context c) {
		DisplayMetrics dm = new DisplayMetrics();
		dm = c.getResources().getDisplayMetrics();
		return dm.densityDpi;
	}
	
	/**
	 * dp 转 像素
	 * @param context
	 * @param dp
	 * @return
	 */
	public static float dpToPx(Context context, float dp) {
        if (context == null) {
            return -1;
        }
        return dp * context.getResources().getDisplayMetrics().density;
    }

	/**
	 * 像素 转 dp
	 * @param context
	 * @param px
	 * @return
	 */
    public static float pxToDp(Context context, float px) {
        if (context == null) {
            return -1;
        }
        return px / context.getResources().getDisplayMetrics().density;
    }
	
}
