package me.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;

/**
 * 时间工具类
 * @author Jesus{931178805@qq.com}
 * 2014年7月8日
 */
@SuppressLint("SimpleDateFormat")
public class DateUtils {

	public static String JUST_NOW = "刚刚";
    public static String MIN = "分";
    public static String HOUR = "时";
    public static String DAY = "天";
    public static String MONTH = "月";
    public static String YEAR = "年";
    
    public static String YESTER_DAY = "昨天";
    public static String THE_DAY_BEFORE_YESTER_DAY = "前天";
    public static String TODAY = "今天";
    
    private static Calendar cal = Calendar.getInstance();
    private static Calendar newCal = Calendar.getInstance();
    
    private final static ThreadLocal<SimpleDateFormat> DEFAULT_FORMAT = new ThreadLocal<SimpleDateFormat>() {
    	protected SimpleDateFormat initialValue() {
    		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	};
    };
    
    private final static ThreadLocal<SimpleDateFormat> DEFAULT_FULL_FORMAT = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
		}
	};
	
	private final static ThreadLocal<SimpleDateFormat> DEFAULT_FILE_FORMAT = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd-HH_mm_ss_SSS");
		}
	};
	
	/**
	 * 得到当前的时间戳
	 */
	public static String getCurrentTime() {
		return DEFAULT_FORMAT.get().format(new Date());
	}
	
	public static Calendar getCalendar() {
		cal.setTimeInMillis(System.currentTimeMillis());
		return cal;
	}
    
	public static Calendar getNewCalendar() {
		newCal.setTimeInMillis(System.currentTimeMillis());
		return newCal;
	}

	/**
	 * 判断是否是同一天
	 * 
	 * @param timeMillis
	 * @return
	 */
	public static boolean isToday(long timeMillis) {
		Date time = new Date(timeMillis);
		return isToday(time);
	}

	/**
	 * 判断是否是同一天
	 * 
	 * @param time
	 * @return
	 */
	public static boolean isToday(Date time) {
		Calendar svrCal = getCalendar();
		Calendar comCal = getNewCalendar();
		comCal.setTime(time);
		if (svrCal.get(Calendar.YEAR) != comCal.get(Calendar.YEAR)) {
			return false;
		}
		if (svrCal.get(Calendar.MONTH) != comCal.get(Calendar.MONTH)) {
			return false;
		}
		if (svrCal.get(Calendar.DAY_OF_MONTH) != comCal.get(Calendar.DAY_OF_MONTH)) {
			return false;
		}
		return true;
	}

	/**
	 * 判断是否是同一年
	 * 
	 * @param timeMillis
	 * @return
	 */
	public static boolean isThisYear(long timeMillis) {
		Date time = new Date(timeMillis);
		return isThisYear(time);
	}

	/**
	 * 判断是否是同一年
	 * 
	 * @param time
	 * @return
	 */
	public static boolean isThisYear(Date time) {
		Calendar svrCal = getCalendar();
		Calendar comCal = getNewCalendar();
		comCal.setTime(time);
		if (svrCal.get(Calendar.YEAR) != comCal.get(Calendar.YEAR)) {
			return false;
		}
		return true;
	}
	
	/**
	 * 根据生日获取星座
	 * 
	 * @param birthdayStr
	 * @return
	 */
	public static String getConstellation(String birthdayStr, String dateFormat) {
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		try {
			Date birth = formatter.parse(birthdayStr);
			return getConstellation(birth.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return "";
	}
	
	/**
	 * 根据生日获取星座
	 * 
	 * @param birthdayStr	默认的时间格式是"yyyy-MM-dd"
	 * @return
	 */
	public static String getConstellation(String birthdayStr) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date birth = formatter.parse(birthdayStr);
			return getConstellation(birth.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return "";
	}
	
	/**
	 * 根据生日获取星座
	 * 
	 * @param birthdayMills
	 * @return
	 */
	public static String getConstellation(long birthdayMills) {

		Calendar birthdayCalendar = getNewCalendar();
		birthdayCalendar.setTimeInMillis(birthdayMills);
		int month = birthdayCalendar.get(Calendar.MONTH) + 1;
		int day = birthdayCalendar.get(Calendar.DATE);

		return getConstellation(month, day);
	}
	
	/**
	 * 通过日期判断星座
	 * 
	 * @param monthOfYear
	 * @param dayOfMonth
	 * @return
	 */
	public static String getConstellation(int monthOfYear, int dayOfMonth) {
		int total = monthOfYear * 31 + dayOfMonth;
		String chinaNm = "";
		if (total >= 32 && total <= 50 || total >= 394 && total <= 403) {
			chinaNm = "魔羯座";
		} else if (total >= 51 && total <= 80) {
			chinaNm = "水瓶座";
		} else if (total >= 81 && total <= 113) {
			chinaNm = "双鱼座";
		} else if (total >= 114 && total <= 144) {
			chinaNm = "白羊座";
		} else if (total >= 145 && total <= 175) {
			chinaNm = "金牛座";
		} else if (total >= 176 && total <= 207) {
			chinaNm = "双子座";
		} else if (total >= 208 && total <= 239) {
			chinaNm = "巨蟹座";
		} else if (total >= 240 && total <= 270) {
			chinaNm = "狮子座";
		} else if (total >= 271 && total <= 301) {
			chinaNm = "处女座";
		} else if (total >= 302 && total <= 333) {
			chinaNm = "天秤座";
		} else if (total >= 334 && total <= 362) {
			chinaNm = "天蝎座";
		} else if (total >= 363 && total <= 393) {
			chinaNm = "射手座";
		}
		return chinaNm;
	}
	
	/**
	 * 根据出生日期,计算生日
	 * 
	 * @param birthdayMill
	 * @return
	 */
	public static int getAge(long birthdayMill) {
		int startYear = 1970;
		Calendar cal = getNewCalendar();
		long ageMill = cal.getTimeInMillis() - birthdayMill;
		cal.setTimeInMillis(ageMill);
		int ageYear = cal.get(Calendar.YEAR) - startYear;
		if (0 > ageYear) {
			ageYear = 0;
		}
		return ageYear;
	}
	
	/**
	 * 判断一个月有多少天,月份从1~~12.
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static int getDaysOfMonth(int year, int month) {
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			return 31;
		case 4:
		case 6:
		case 9:
		case 11:
			return 30;
		case 2:
			if (0 == year % 4) {
				return 29;// 闰年
			} else {
				return 28;// 非闰年
			}
		default:
			return 0;
		}
	}
	
	/**
	 * 得到日志时间
	 */
	public static String getErrorLogTime() {
		return DEFAULT_FULL_FORMAT.get().format(new Date());
	}
	
	/**
	 * 得到日志文件名
	 */
	public static String getTime(long time) {
		return DEFAULT_FILE_FORMAT.get().format(new Date(time));
	}
	
}
