package me.utils;

import me.base.Config;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * 配置工具，全局 
 * <ul>
 * <strong>Preference Name</strong>
 * <li>you can change preference name by {@link #PREFERENCE_NAME}</li>
 * </ul>
 * <ul>
 * <strong>Put Value</strong>
 * <li>put string {@link #putString(Context, String, String)}</li>
 * <li>put int {@link #putInt(Context, String, int)}</li>
 * <li>put long {@link #putLong(Context, String, long)}</li>
 * <li>put float {@link #putFloat(Context, String, float)}</li>
 * <li>put boolean {@link #putBoolean(Context, String, boolean)}</li>
 * </ul>
 * <ul>
 * <strong>Get Value</strong>
 * <li>get string {@link #getString(Context, String)}, {@link #getString(Context, String, String)}</li>
 * <li>get int {@link #getInt(Context, String)}, {@link #getInt(Context, String, int)}</li>
 * <li>get long {@link #getLong(Context, String)}, {@link #getLong(Context, String, long)}</li>
 * <li>get float {@link #getFloat(Context, String)}, {@link #getFloat(Context, String, float)}</li>
 * <li>get boolean {@link #getBoolean(Context, String)}, {@link #getBoolean(Context, String, boolean)}</li>
 * </ul>
 * @author Jesus{931178805@qq.com}
 * 2014年7月8日
 */
public class PreferencesUtils {

    public static String PREFERENCE_NAME = "_preferences";
    
    private static final String LOG_INIT_CONFIG = "Initialize CrashHandler with configuration.";
	private static final String ERROR_INIT_CONFIG_WITH_NULL = "CrashHandler configuration can not be initialized with null.";
	private static final String WARNING_RE_INIT_CONFIG = "Try to initialize CrashHandler which had already been initialized before.";
    
	// 配置
 	private static Config config;
 	
 	private volatile static PreferencesUtils instance;
 	
 	/** Returns singleton class instance */
	public static PreferencesUtils getInstance() {
		if (instance == null) {
			synchronized (FileUtils.class) {
				if (instance == null) {
					instance = new PreferencesUtils();
				}
			}
		}
		return instance;
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
    	if (!TextUtils.isEmpty(cfg.preferName)) 
    		PREFERENCE_NAME = cfg.preferName;
    }
    
    /**
     * 得到设置的配置, 如果为null,则得到系统的
     * @param c
     * @param pref
     * @return
     */
  	public static SharedPreferences getDefaultPreferences(Context c, SharedPreferences pref) {
  		if (pref == null) return getDefaultPreferences(c);
  		return pref;
  	}
  	
	/**
	 * 得到默认的系统
	 */
  	public static SharedPreferences getDefaultPreferences(Context c) {
  		return PreferenceManager.getDefaultSharedPreferences(c);
  	}

    /**
     * put string preferences
     * 
     * @param context
     * @param key The name of the preference to modify
     * @param value The new value for the preference
     * @return True if the new values were successfully written to persistent storage.
     */
    public static boolean putString(Context context, String key, String value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    /**
     * get string preferences
     * 
     * @param context
     * @param key The name of the preference to retrieve
     * @return The preference value if it exists, or null. Throws ClassCastException if there is a preference with this
     * name that is not a string
     * @see #getString(Context, String, String)
     */
    public static String getString(Context context, String key) {
        return getString(context, key, null);
    }

    /**
     * get string preferences
     * 
     * @param context
     * @param key The name of the preference to retrieve
     * @param defaultValue Value to return if this preference does not exist
     * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
     * this name that is not a string
     */
    public static String getString(Context context, String key, String defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getString(key, defaultValue);
    }
    
    /**
     * 得到不为empty的值
     * @param pref
     * @param key
     * @param def
     * @return
     */
 	public static String getNonEmptyString(final SharedPreferences pref, final String key, final String def) {
 		if (pref == null) return def;
 		final String val = pref.getString(key, def);
 		return TextUtils.isEmpty(val) ? def : val;
 	}

 	/**
 	 * 设置String
 	 * @param pref
 	 * @param key
 	 * @param value
 	 */
 	public static void setString(final SharedPreferences pref, final String key, final String value) {
 		if (pref == null) return;
 		pref.edit().putString(key, value).commit();
 	}

    /**
     * put int preferences
     * 
     * @param context
     * @param key The name of the preference to modify
     * @param value The new value for the preference
     * @return True if the new values were successfully written to persistent storage.
     */
    public static boolean putInt(Context context, String key, int value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    /**
     * get int preferences
     * 
     * @param context
     * @param key The name of the preference to retrieve
     * @return The preference value if it exists, or -1. Throws ClassCastException if there is a preference with this
     * name that is not a int
     * @see #getInt(Context, String, int)
     */
    public static int getInt(Context context, String key) {
        return getInt(context, key, -1);
    }

    /**
     * get int preferences
     * 
     * @param context
     * @param key The name of the preference to retrieve
     * @param defaultValue Value to return if this preference does not exist
     * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
     * this name that is not a int
     */
    public static int getInt(Context context, String key, int defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getInt(key, defaultValue);
    }
    
    /**
     * 得到不为empty的值
     * @param pref
     * @param key
     * @param def
     * @return
     */
 	public static int getNonEmptyInt(final SharedPreferences pref, final String key, final int def) {
 		if (pref == null) return def;
 		final int val = pref.getInt(key, def);
 		return val;
 	}

 	/**
 	 * 设置int
 	 * @param pref
 	 * @param key
 	 * @param value
 	 */
 	public static void setInt(final SharedPreferences pref, final String key, final int value) {
 		if (pref == null) return;
 		pref.edit().putInt(key, value).commit();
 	}

    /**
     * put long preferences
     * 
     * @param context
     * @param key The name of the preference to modify
     * @param value The new value for the preference
     * @return True if the new values were successfully written to persistent storage.
     */
    public static boolean putLong(Context context, String key, long value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(key, value);
        return editor.commit();
    }

    /**
     * get long preferences
     * 
     * @param context
     * @param key The name of the preference to retrieve
     * @return The preference value if it exists, or -1. Throws ClassCastException if there is a preference with this
     * name that is not a long
     * @see #getLong(Context, String, long)
     */
    public static long getLong(Context context, String key) {
        return getLong(context, key, -1);
    }

    /**
     * get long preferences
     * 
     * @param context
     * @param key The name of the preference to retrieve
     * @param defaultValue Value to return if this preference does not exist
     * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
     * this name that is not a long
     */
    public static long getLong(Context context, String key, long defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getLong(key, defaultValue);
    }
    
    /**
     * 得到不为empty的值
     * @param pref
     * @param key
     * @param def
     * @return
     */
 	public static long getNonEmptyLong(final SharedPreferences pref, final String key, final long def) {
 		if (pref == null) return def;
 		final long val = pref.getLong(key, def);
 		return val;
 	}

 	/**
 	 * 设置boolean
 	 * @param pref
 	 * @param key
 	 * @param value
 	 */
 	public static void setLong(final SharedPreferences pref, final String key, final long value) {
 		if (pref == null) return;
 		pref.edit().putLong(key, value).commit();
 	}

    /**
     * put float preferences
     * 
     * @param context
     * @param key The name of the preference to modify
     * @param value The new value for the preference
     * @return True if the new values were successfully written to persistent storage.
     */
    public static boolean putFloat(Context context, String key, float value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat(key, value);
        return editor.commit();
    }

    /**
     * get float preferences
     * 
     * @param context
     * @param key The name of the preference to retrieve
     * @return The preference value if it exists, or -1. Throws ClassCastException if there is a preference with this
     * name that is not a float
     * @see #getFloat(Context, String, float)
     */
    public static float getFloat(Context context, String key) {
        return getFloat(context, key, -1);
    }

    /**
     * get float preferences
     * 
     * @param context
     * @param key The name of the preference to retrieve
     * @param defaultValue Value to return if this preference does not exist
     * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
     * this name that is not a float
     */
    public static float getFloat(Context context, String key, float defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getFloat(key, defaultValue);
    }
    
    /**
     * 得到不为empty的值
     * @param pref
     * @param key
     * @param def
     * @return
     */
 	public static float getNonEmptyFloat(final SharedPreferences pref, final String key, final float def) {
 		if (pref == null) return def;
 		final float val = pref.getFloat(key, def);
 		return val;
 	}

 	/**
 	 * 设置boolean
 	 * @param pref
 	 * @param key
 	 * @param value
 	 */
 	public static void setFloat(final SharedPreferences pref, final String key, final float value) {
 		if (pref == null) return;
 		pref.edit().putFloat(key, value).commit();
 	}

    /**
     * put boolean preferences
     * 
     * @param context
     * @param key The name of the preference to modify
     * @param value The new value for the preference
     * @return True if the new values were successfully written to persistent storage.
     */
    public static boolean putBoolean(Context context, String key, boolean value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    /**
     * get boolean preferences, default is false
     * 
     * @param context
     * @param key The name of the preference to retrieve
     * @return The preference value if it exists, or false. Throws ClassCastException if there is a preference with this
     * name that is not a boolean
     * @see #getBoolean(Context, String, boolean)
     */
    public static boolean getBoolean(Context context, String key) {
        return getBoolean(context, key, false);
    }

    /**
     * get boolean preferences
     * 
     * @param context
     * @param key The name of the preference to retrieve
     * @param defaultValue Value to return if this preference does not exist
     * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
     * this name that is not a boolean
     */
    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getBoolean(key, defaultValue);
    }
    
    /**
     * 得到不为empty的值
     * @param pref
     * @param key
     * @param def
     * @return
     */
 	public static boolean getNonEmptyBoolean(final SharedPreferences pref, final String key, final boolean def) {
 		if (pref == null) return def;
 		final boolean val = pref.getBoolean(key, def);
 		return val;
 	}

 	/**
 	 * 设置boolean
 	 * @param pref
 	 * @param key
 	 * @param value
 	 */
 	public static void setBoolean(final SharedPreferences pref, final String key, final boolean value) {
 		if (pref == null) return;
 		pref.edit().putBoolean(key, value).commit();
 	}
}
