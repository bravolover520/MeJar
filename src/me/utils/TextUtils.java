package me.utils;

import android.content.Context;
import android.text.ClipboardManager;

/**
 * 因为Andorid 自2.3以后有的此类，所以此类以后作为字符的处理，包括转码等
 * @author Jesus{931178805@qq.com}
 * 2014年7月8日
 */
//过期的 @SuppressWarnings("deprecation")表示不检测过期的方法
public class TextUtils {

    /**
     * Returns true if the string is null or 0-length.
     * @param str the string to be examined
     * @return true if str is null or zero length
     */
    public static boolean isEmpty(CharSequence str) {
        if (null == str || str.length() == 0)
            return true;
        else
            return false;
    }
    
    /**
     * is null or its length is 0 or it is made by space
     * 
     * <pre>
     * isBlank(null) = true;
     * isBlank(&quot;&quot;) = true;
     * isBlank(&quot;  &quot;) = true;
     * isBlank(&quot;a&quot;) = false;
     * isBlank(&quot;a &quot;) = false;
     * isBlank(&quot; a&quot;) = false;
     * isBlank(&quot;a b&quot;) = false;
     * </pre>
     * 
     * @param str
     * @return if string is null or its size is 0 or it is made by space, return true, else return false.
     */
    public static boolean isBlank(String str) {
        return (null == str || str.trim().length() == 0);
    }
    
    /**
     * str == null
     * @return
     */
    public static boolean isNull(CharSequence str) {
    	return null == str;
	}
    
    /**
     * @return is 'null' char, default false.
     */
    public static boolean isNullChar(CharSequence str) {
    	if (isEmpty(str)) return false;
    	else 
    		if("null".equalsIgnoreCase(str.toString())) 
    			return true;
    		else
    			return false;
    }

    /**
     * Returns the length that the specified CharSequence would have if
     * spaces and control characters were trimmed from the start and end,
     * as by {@link String#trim}.
     */
    public static int getTrimmedLength(CharSequence s) {
        int len = s.length();

        int start = 0;
        while (start < len && s.charAt(start) <= ' ') {
            start++;
        }

        int end = len;
        while (end > start && s.charAt(end - 1) <= ' ') {
            end--;
        }

        return end - start;
    }

    /**
     * Returns true if a and b are equal, including if they are both null.
     * <p><i>Note: In platform versions 1.1 and earlier, this method only worked well if
     * both the arguments were instances of String.</i></p>
     * @param a first CharSequence to check
     * @param b second CharSequence to check
     * @return true if a and b are equal
     */
    public static boolean equals(CharSequence a, CharSequence b) {
        if (a == b) return true;
        int length;
        if (a != null && b != null && (length = a.length()) == b.length()) {
            if (a instanceof String && b instanceof String) {
                return a.equals(b);
            } else {
                for (int i = 0; i < length; i++) {
                    if (a.charAt(i) != b.charAt(i)) return false;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Html-encode the string.
     * @param s the string to be encoded
     * @return the encoded string
     */
    public static String htmlEncode(String s) {
        StringBuilder sb = new StringBuilder();
        char c;
        for (int i = 0; i < s.length(); i++) {
            c = s.charAt(i);
            switch (c) {
            case '<':
                sb.append("&lt;"); //$NON-NLS-1$
                break;
            case '>':
                sb.append("&gt;"); //$NON-NLS-1$
                break;
            case '&':
                sb.append("&amp;"); //$NON-NLS-1$
                break;
            case '\'':
                //http://www.w3.org/TR/xhtml1
                // The named character reference &apos; (the apostrophe, U+0027) was introduced in
                // XML 1.0 but does not appear in HTML. Authors should therefore use &#39; instead
                // of &apos; to work as expected in HTML 4 user agents.
                sb.append("&#39;"); //$NON-NLS-1$
                break;
            case '"':
                sb.append("&quot;"); //$NON-NLS-1$
                break;
            default:
                sb.append(c);
            }
        }
        return sb.toString();
    }
    
    /**
	 * Copy
	 * @return
	 */
	public static CharSequence getText(Context c) {
		return ((ClipboardManager)c.getSystemService(android.content.Context.CLIPBOARD_SERVICE)).getText();
	}

	/**
	 * Paste
	 * @param text
	 * @return
	 */
	public static boolean setText(Context c, CharSequence text) {
		((ClipboardManager) c.getSystemService(android.content.Context.CLIPBOARD_SERVICE)).setText(text != null ? text : "");
		return true;
	}
}