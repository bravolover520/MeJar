package me.utils;

/**
 * Log 日志类，类似android.util.Log。
 * 现在只需要写输出内容即可，不需要写TAG,不需要考虑 Null
 * tag自动产生，格式: customTagPrefix:className.methodName(L:lineNumber),
 * customTagPrefix为空时只输出：className.methodName(L:lineNumber)。
 * @author Jesus{931178805@qq.com}
 * 2014年7月8日
 */
public class L {

	private static String customTagPrefix = "";
	
	private static boolean allowD = true;
    private static boolean allowE = true;
    private static boolean allowI = true;
    private static boolean allowV = true;
    private static boolean allowW = true;
    private static boolean allowWtf = true;
    
    public static void setDebug(boolean debug) {
		allowD = debug;
		allowE = debug;
		allowI = debug;
		allowV = debug;
		allowW = debug;
		allowWtf = debug;
	}
    
    public static void setCustomTagPrefix(String tag) {
    	customTagPrefix = tag;
    }
    
	//仅仅得到类名
    private static String generateTag(StackTraceElement caller) {
        return generateTag(caller, false);
    }
    
    //得到所有的详细数据
    private static String generateTag(StackTraceElement caller, boolean isDetail) {
    	String tag = "%s.%s(L:%d)";
    	//com.zhiduan.yewen.activity.login.SignInActivity$1//com.zhiduan.yewen.activity.login.SignInActivity$d
    	//com.zhiduan.yewen.AccountManager
        String callerClazzName = caller.getClassName();	
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);	//SignInActivity$1
        if (isDetail)
        	tag = String.format(tag, callerClazzName, caller.getMethodName(), caller.getLineNumber());
        else{
        	int index = callerClazzName.indexOf("$");
        	if (index > -1) tag = callerClazzName.substring(0, index);
			else tag = callerClazzName;
        }
        tag = TextUtils.isEmpty(customTagPrefix) ? tag : customTagPrefix + ":" + tag;
        return tag;
    }
    
    private static CustomLogger customLogger;

    public interface CustomLogger {
        void d(String tag, String content);

        void d(String tag, String content, Throwable tr);

        void e(String tag, String content);

        void e(String tag, String content, Throwable tr);

        void i(String tag, String content);

        void i(String tag, String content, Throwable tr);

        void v(String tag, String content);

        void v(String tag, String content, Throwable tr);

        void w(String tag, String content);

        void w(String tag, String content, Throwable tr);

        void w(String tag, Throwable tr);

        void wtf(String tag, String content);

        void wtf(String tag, String content, Throwable tr);

        void wtf(String tag, Throwable tr);
    }
    
    public static void d(String content) {
        if (!allowD) return;
        if (TextUtils.isNull(content)) content = "null, please check";
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.d(tag, content);
        } else {
            android.util.Log.d(tag, content);
        }
    }
    
    public static void d(String content, Throwable tr) {
        if (!allowD) return;
        if (TextUtils.isNull(content)) content = "null, please check";
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.d(tag, content, tr);
        } else {
        	android.util.Log.d(tag, content, tr);
        }
    }

    public static void e(String content) {
        if (!allowE) return;
        if (TextUtils.isNull(content)) content = "null, please check";
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.e(tag, content);
        } else {
        	android.util.Log.e(tag, content);
        }
    }

    public static void e(String content, Throwable tr) {
        if (!allowE) return;
        if (TextUtils.isNull(content)) content = "null, please check";
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.e(tag, content, tr);
        } else {
        	android.util.Log.e(tag, content, tr);
        }
    }
    
    public static void i(String content) {
        if (!allowI) return;
        if (TextUtils.isNull(content)) content = "null, please check";
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);
        if (customLogger != null) {
            customLogger.i(tag, content);
        } else {
        	android.util.Log.i(tag, content);
        }
    }
    
    public static void i(String content, Throwable tr) {
        if (!allowI) return;
        if (TextUtils.isNull(content)) content = "null, please check";
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);
        
        if (customLogger != null) {
            customLogger.i(tag, content, tr);
        } else {
        	android.util.Log.i(tag, content, tr);
        }
    }

    public static void v(String content) {
        if (!allowV) return;
        if (TextUtils.isNull(content)) content = "null, please check";
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.v(tag, content);
        } else {
        	android.util.Log.v(tag, content);
        }
    }

    public static void v(String content, Throwable tr) {
        if (!allowV) return;
        if (TextUtils.isNull(content)) content = "null, please check";
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.v(tag, content, tr);
        } else {
        	android.util.Log.v(tag, content, tr);
        }
    }
    
    public static void w(String content) {
        if (!allowW) return;
        if (TextUtils.isNull(content)) content = "null, please check";
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.w(tag, content);
        } else {
        	android.util.Log.w(tag, content);
        }
    }

    public static void w(String content, Throwable tr) {
        if (!allowW) return;
        if (TextUtils.isNull(content)) content = "null, please check";
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.w(tag, content, tr);
        } else {
        	android.util.Log.w(tag, content, tr);
        }
    }

    public static void w(Throwable tr) {
        if (!allowW) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.w(tag, tr);
        } else {
        	android.util.Log.w(tag, tr);
        }
    }

    public static void wtf(String content) {
        if (!allowWtf) return;
        if (TextUtils.isNull(content)) content = "null, please check";
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.wtf(tag, content);
        } else {
        	android.util.Log.wtf(tag, content);
        }
    }

    public static void wtf(String content, Throwable tr) {
        if (!allowWtf) return;
        if (TextUtils.isNull(content)) content = "null, please check";
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.wtf(tag, content, tr);
        } else {
        	android.util.Log.wtf(tag, content, tr);
        }
    }

    public static void wtf(Throwable tr) {
        if (!allowWtf) return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (customLogger != null) {
            customLogger.wtf(tag, tr);
        } else {
        	android.util.Log.wtf(tag, tr);
        }
    }
    
    private static StackTraceElement getCallerStackTraceElement() {
        return Thread.currentThread().getStackTrace()[4];
    }
}
