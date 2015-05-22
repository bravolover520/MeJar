package me.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 * @author Jesus{931178805@qq.com}
 * 2014年7月8日
 */
public class StringUtils {

	private final static int BUFFER_SIZE = 4096;
	private final static String UTF8_ENCODING = "UTF-8";

	/**
	 * 输入流转String.
	 */
	public static String inputStream2String(InputStream in, String encoding) throws UnsupportedEncodingException,
			IOException {

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] data = new byte[BUFFER_SIZE];
		int count = -1;
		while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
			outStream.write(data, 0, count);

		data = null;
		return new String(outStream.toByteArray(), UTF8_ENCODING);
	}

	/**
	 * 字节流转String
	 */
	public static String bytes2String(byte[] b, String encoding) throws UnsupportedEncodingException {
		if (b == null) 
			return "";
		else 
			return new String(b, encoding);		
	}
	
	/**
	 * 字节流转String
	 */
	public static String bytes2String(byte[] b) {
		// On Android, the default charset is UTF-8.
		if (b == null) 
			return "";
		else 
			return new String(b);
	}
    
    /**
	 * 字符串转整数
	 * 
	 * @param str
	 * @param defValue
	 * @return
	 */
	public static double toDouble(String str, double defValue) {
		if (TextUtils.isEmpty(str))
			return defValue;
		try {
			return Double.parseDouble(str);
		} catch (Exception e) {
		}
		return defValue;
	}
	
	/**
	 * 对象转整数
	 * 
	 * @param obj
	 * @return 转换异常返回 0
	 */
	public static double toDouble(Object obj) {
		if (obj == null)
			return 0;
		return toDouble(obj.toString(), 0d);
	}
	
	/**
	 * 字符串转整数
	 * 
	 * @param str
	 * @param defValue
	 * @return
	 */
	public static int toInt(String str, int defValue) {
		if (TextUtils.isEmpty(str))
			return defValue;
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
		}
		return defValue;
	}

	/**
	 * 对象转整数
	 * 
	 * @param obj
	 * @return 转换异常返回 0
	 */
	public static int toInt(Object obj) {
		if (obj == null)
			return 0;
		return toInt(obj.toString(), 0);
	}

	/**
	 * 对象转整数
	 * 
	 * @param obj
	 * @return 转换异常返回 0
	 */
	public static long toLong(String obj) {
		try {
			return Long.parseLong(obj);
		} catch (Exception e) {
		}
		return 0;
	}

	/**
	 * 字符串转布尔值
	 * 
	 * @param b
	 * @return 转换异常返回 false
	 * </br>
	 * 一般在java中，用1来表示true,用0来表示false
	 */
	public static boolean toBool(String b) {
		if(b.equals("0") || b.equals("1")){
			if (b.equals("0")) return false;
			else return true;
		}
		try {
			return Boolean.parseBoolean(b);
		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
	 * 
	 * @param input
	 * @return boolean
	 */
	public static boolean isEmpty(String input) {
		if (input == null || "".equals(input))
			return true;

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
				return false;
			}
		}
		return true;
	}
	
	/**
     * compare two string
     * 
     * @param actual
     * @param expected
     */
    public static boolean isEquals(String actual, String expected) {
    	if (!TextUtils.isEmpty(actual) && !TextUtils.isEmpty(expected)) {
			if (actual.equals(expected)) return true;
		}
        return false;
    }
    
    /**
     * encoded in utf-8
     * 
     * <pre>
     * utf8Encode(null)        =   null
     * utf8Encode("")          =   "";
     * utf8Encode("aa")        =   "aa";
     * utf8Encode("啊啊啊啊")   = "%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A";
     * </pre>
     * 
     * @param str
     * @return
     * @throws UnsupportedEncodingException if an error occurs
     */
    public static String utf8Encode(String str) {
        if (!isEmpty(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("UnsupportedEncodingException occurred. ", e);
            }
        }
        return str;
    }
    
    /**
     * encoded in utf-8, if exception, return defultReturn
     * 
     * @param str
     * @param defultReturn
     * @return
     */
    public static String utf8Encode(String str, String defultReturn) {
        if (!isEmpty(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return defultReturn;
            }
        }
        return str;
    }
    
    /**
	 * 获取流编码类型
	 * @param is InputStream流
	 * @return 编码类型
	 * @throws java.io.IOException
	 */
	public static String getStreamEncoding(InputStream is) throws IOException{
		BufferedInputStream bis = new BufferedInputStream(is);
		bis.mark(2);
		byte[] first3bytes = new byte[3];
		bis.read(first3bytes);
		bis.reset();
		String encoding = null;
		if (first3bytes[0] == (byte) 0xEF && first3bytes[1] == (byte) 0xBB && first3bytes[2] == (byte) 0xBF) {
			encoding = "utf-8";
		} else if (first3bytes[0] == (byte) 0xFF && first3bytes[1] == (byte) 0xFE) {
			encoding = "unicode";
		} else if (first3bytes[0] == (byte) 0xFE && first3bytes[1] == (byte) 0xFF) {
			encoding = "utf-16be";
		} else if (first3bytes[0] == (byte) 0xFF && first3bytes[1] == (byte) 0xFF) {
			encoding = "utf-16le";
		} else {
			encoding = "GBK";
		}
		return encoding;
	}
	
	/**
	 * 获取HTML文本流的编码类型
	 * @param is HTML文本流
	 * @return 编码类型
	 * @throws java.io.IOException
	 */
	public static String getEncodingFromHTML(InputStream is) throws IOException{
		final int FIND_CHARSET_CACHE_SIZE = 4 * 1024;
		BufferedInputStream bis = new BufferedInputStream(is);
        bis.mark(FIND_CHARSET_CACHE_SIZE);
        byte[] cache = new byte[FIND_CHARSET_CACHE_SIZE];
        bis.read(cache);
        bis.reset();
        return getHtmlCharset(new String(cache));
	}

	/**
	 * 获取HTML文本字符集类型
	 * @param content HTML内容
	 * @return 字符集类型
	 */
	public static String getHtmlCharset(String content){
		String encoding = null;
		final String CHARSET_REGX = "<meta.*charset=\"?([a-zA-Z0-9-_/]+)\"?";
		Matcher m = Pattern.compile(CHARSET_REGX).matcher(content);
		if(m.find()){
			encoding = m.group(1);
		}
        return encoding;
	}
	
	/**
     * 判断字符串中是否有中文字符
     * @param str 需要判断的字符串
     * @return boolean
     */
    public static boolean hasChinese(String str) {
        int count = 0;
        String regEx = "[\\u4e00-\\u9fa5]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        while (m.find()) {
            int size = m.groupCount();
            for (int i = 0; i <= size; i++) {
                count = count + 1;
            }
        }
        return count > 0;
    }
	
	// @see http://android-developers.blogspot.com/2011/03/identifying-app-installations.html
    public static String getUUID() {
        return md5(UUID.randomUUID().toString());
    }
    
    /**
     * capitalize first letter
     * 
     * <pre>
     * capitalizeFirstLetter(null)     =   null;
     * capitalizeFirstLetter("")       =   "";
     * capitalizeFirstLetter("2ab")    =   "2ab"
     * capitalizeFirstLetter("a")      =   "A"
     * capitalizeFirstLetter("ab")     =   "Ab"
     * capitalizeFirstLetter("Abc")    =   "Abc"
     * </pre>
     * 
     * @param str
     * @return
     */
    public static String capitalizeFirstLetter(String str) {
        if (isEmpty(str)) {
            return str;
        }

        char c = str.charAt(0);
        return (!Character.isLetter(c) || Character.isUpperCase(c)) ? str : new StringBuilder(str.length())
                .append(Character.toUpperCase(c)).append(str.substring(1)).toString();
    }
    
    /**
     * get innerHtml from href
     * 
     * <pre>
     * getHrefInnerHtml(null)                                  = ""
     * getHrefInnerHtml("")                                    = ""
     * getHrefInnerHtml("mp3")                                 = "mp3";
     * getHrefInnerHtml("&lt;a innerHtml&lt;/a&gt;")                    = "&lt;a innerHtml&lt;/a&gt;";
     * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
     * getHrefInnerHtml("&lt;a&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
     * getHrefInnerHtml("&lt;a href="baidu.com"&gt;innerHtml&lt;/a&gt;")               = "innerHtml";
     * getHrefInnerHtml("&lt;a href="baidu.com" title="baidu"&gt;innerHtml&lt;/a&gt;") = "innerHtml";
     * getHrefInnerHtml("   &lt;a&gt;innerHtml&lt;/a&gt;  ")                           = "innerHtml";
     * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                      = "innerHtml";
     * getHrefInnerHtml("jack&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                  = "innerHtml";
     * getHrefInnerHtml("&lt;a&gt;innerHtml1&lt;/a&gt;&lt;a&gt;innerHtml2&lt;/a&gt;")        = "innerHtml2";
     * </pre>
     * 
     * @param href
     * @return <ul>
     *         <li>if href is null, return ""</li>
     *         <li>if not match regx, return source</li>
     *         <li>return the last string that match regx</li>
     *         </ul>
     */
    public static String getHrefInnerHtml(String href) {
        if (isEmpty(href)) {
            return "";
        }

        String hrefReg = ".*<[\\s]*a[\\s]*.*>(.+?)<[\\s]*/a[\\s]*>.*";
        Pattern hrefPattern = Pattern.compile(hrefReg, Pattern.CASE_INSENSITIVE);
        Matcher hrefMatcher = hrefPattern.matcher(href);
        if (hrefMatcher.matches()) {
            return hrefMatcher.group(1);
        }
        return href;
    }

/**
     * process special char in html
     * 
     * <pre>
     * htmlEscapeCharsToString(null) = null;
     * htmlEscapeCharsToString("") = "";
     * htmlEscapeCharsToString("mp3") = "mp3";
     * htmlEscapeCharsToString("mp3&lt;") = "mp3<";
     * htmlEscapeCharsToString("mp3&gt;") = "mp3\>";
     * htmlEscapeCharsToString("mp3&amp;mp4") = "mp3&mp4";
     * htmlEscapeCharsToString("mp3&quot;mp4") = "mp3\"mp4";
     * htmlEscapeCharsToString("mp3&lt;&gt;&amp;&quot;mp4") = "mp3\<\>&\"mp4";
     * </pre>
     * 
     * @param source
     * @return
     */
    public static String htmlEscapeCharsToString(String source) {
        return StringUtils.isEmpty(source) ? source : source.replaceAll("&lt;", "<").replaceAll("&gt;", ">")
                .replaceAll("&amp;", "&").replaceAll("&quot;", "\"");
    }

    /**
     * transform half width char to full width char
     * 全角转半角
     * <pre>
     * fullWidthToHalfWidth(null) = null;
     * fullWidthToHalfWidth("") = "";
     * fullWidthToHalfWidth(new String(new char[] {12288})) = " ";
     * fullWidthToHalfWidth("！＂＃＄％＆) = "!\"#$%&";
     * </pre>
     * 
     * @param s
     * @return
     */
    public static String fullWidthToHalfWidth(String s) {
        if (isEmpty(s)) {
            return s;
        }

        char[] source = s.toCharArray();
        for (int i = 0; i < source.length; i++) {
            if (source[i] == 12288) {
                source[i] = ' ';
                // } else if (source[i] == 12290) {
                // source[i] = '.';
            } else if (source[i] >= 65281 && source[i] <= 65374) {
                source[i] = (char)(source[i] - 65248);
            } else {
                source[i] = source[i];
            }
        }
        return new String(source);
    }
    
    /**
     * transform full width char to half width char
     * 半角转全角
     * <pre>
     * halfWidthToFullWidth(null) = null;
     * halfWidthToFullWidth("") = "";
     * halfWidthToFullWidth(" ") = new String(new char[] {12288});
     * halfWidthToFullWidth("!\"#$%&) = "！＂＃＄％＆";
     * </pre>
     * 
     * @param s
     * @return
     */
    public static String halfWidthToFullWidth(String s) {
        if (isEmpty(s)) {
            return s;
        }

        char[] source = s.toCharArray();
        for (int i = 0; i < source.length; i++) {
            if (source[i] == ' ') {
                source[i] = (char)12288;
                // } else if (source[i] == '.') {
                // source[i] = (char)12290;
            } else if (source[i] >= 33 && source[i] <= 126) {
                source[i] = (char)(source[i] + 65248);
            } else {
                source[i] = source[i];
            }
        }
        return new String(source);
    }
	
	/**
	 * MD5编码值
	 * @param string
	 * @return
	 * @throws RuntimeException
	 */
	public static String md5(String string) throws RuntimeException {
        byte[] hash;

        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            int i = (b & 0xFF);
            if (i < 0x10) hex.append('0');
            hex.append(Integer.toHexString(i));
        }

        return hex.toString();
    }
	
	/**
	 * 去除字符串后面的".0"
	 * @param string
	 * @return
	 */
	public static String removePointZero(String string){
		if(!isEmpty(string)){
			boolean iscontains = string.contains(".0");
			if(iscontains){
				string = string.substring(0, string.indexOf(".0"));
			}
		}
		return string;
	}
}
