package com.w_angler.spider.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密
 * @author wangle
 *
 */
public class MD5 {
	private static final String hex="0123456789abcdef";
	
	public static String encrypt(String s){
		String code=null;
		try {
			MessageDigest md5=MessageDigest.getInstance("MD5");
			md5.update(s.getBytes("UTF-8"));
			byte[] array=md5.digest();
			code=getString(array);
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return code;
	}
	/**
	 * 将加密后的字节数组转化为字符串
	 * @param array 加密后的字节数组
	 * @return
	 */
	private static String getString(byte[] array){
		StringBuffer sb=new StringBuffer();
		for(byte b : array){
			int temp=b;
			if(temp<0){
				temp=temp+256;
			}
			int d1=temp/16;
			int d2=temp%16;
			sb.append(hex.charAt(d1)).append(hex.charAt(d2));
		}
		return sb.toString();
	}
}
