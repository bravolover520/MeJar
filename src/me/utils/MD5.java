package me.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5工具
 * @author Jesus{931178805@qq.com}
 * 2014年7月8日
 */
public final class MD5 {

	/**
	 * 转MD5
	 * @param bytes
	 * @return
	 */
	public static String toMd5(byte[] bytes) {
		try {
			MessageDigest algorithm = MessageDigest.getInstance("MD5");
			algorithm.reset();
			algorithm.update(bytes);
			return toHexString(algorithm.digest(), "");
		} catch (NoSuchAlgorithmException e) {
			L.e("toMd5(): ", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * 转16进制String
	 * @param bytes
	 * @param separator
	 * @return
	 */
	private static String toHexString(byte[] bytes, String separator) {
		StringBuilder hexString = new StringBuilder();
		for (byte b : bytes) {
			String hex = Integer.toHexString(0xFF & b);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex).append(separator);
		}
		return hexString.toString();
	}
}
