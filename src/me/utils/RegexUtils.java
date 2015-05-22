package me.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式工具类
 * @author Jesus{931178805@qq.com}
 * 2014年7月8日
 */
public class RegexUtils {

	/**
	 * 判断号码的输入合法性
	 */
	public static boolean isValidPhoneNum(String phoneNum) {
		if (TextUtils.isEmpty(phoneNum)) return false;
		return (phoneNum.startsWith("0") || phoneNum.startsWith("1"));
	}

	/**
	 * 邮箱正则表达式
	 */
	private final static Pattern emailer = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
	/**
	 * 判断是不是一个合法的电子邮件地址
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		if (email == null || email.trim().length() == 0)
			return false;
		return emailer.matcher(email).matches();
	}
	
	/**
	 * 手机号正则表达式
	 * 故先要整清楚现在已经开放了多少个号码段，国家号码段分配如下：
	 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
	 * 联通：130、131、132、152、155、156、185、186
	 * 电信：133、153、180、189、（1349卫通）
	 * 三网: 170-179
	 * 自己: 2
	 * 
	 * "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$"
	 * ^[1]([3][0-9]{1}|59|58|88|89)[0-9]{8}$
	 */
	private final static Pattern phoner = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9])|(17[0-9])|(2[0-9][0-9]))\\d{8}$");
	/**
	 * 判断是不是一个合法的手机号
	 * @param email
	 * @return
	 */
	public static boolean isPhone(String phone) {
		if (phone == null || phone.trim().length() == 0)
			return false;
		return phoner.matcher(phone).matches();
	}
	
	/**
	 * 判断+86,并去除
	 */
	/**
	 * user java reg to check phone number and replace 86 or +86 only check
	 * start with "+86" or "86" ex +8615911119999 13100009999 replace +86 or 86
	 * with ""
	 * 
	 * @param phoneNum
	 * @return
	 * @throws Exception
	 */
	public static String checkPhoneNum(String phoneNum) {
		phoneNum = phoneNum.trim().replaceAll(" ", "").replaceAll("-", "");
		Pattern p1 = Pattern.compile("^((\\+{0,1}86){0,1})1[0-9]{10}");
		Matcher m1 = p1.matcher(phoneNum);
		if (m1.matches()) {
			Pattern p2 = Pattern.compile("^((\\+{0,1}86){0,1})");
			Matcher m2 = p2.matcher(phoneNum);
			StringBuffer sb = new StringBuffer();
			while (m2.find()) {
				m2.appendReplacement(sb, "");
			}
			m2.appendTail(sb);
			return sb.toString();
		} 
		return phoneNum;
	}

	/** 
     * 使用java正则表达式去掉多余的.与0 
     * @param s 
     * @return  
     */  
    public static String subZeroAndDot(String s){  
    	if (TextUtils.isEmpty(s)) return "";
        if(s.indexOf(".") > 0){  
            s = s.replaceAll("0+?$", "");//去掉多余的0  
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉  
        }  
        return s;  
    }  
    
    /**
     * @return 去掉多余的.与0
     */
    public static String subZeroAndDot(double d){  
    	return subZeroAndDot(String.valueOf(d));
    } 
	
}
