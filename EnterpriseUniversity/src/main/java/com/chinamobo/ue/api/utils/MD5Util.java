package com.chinamobo.ue.api.utils;

import java.security.MessageDigest;

public class MD5Util {

	/**
	 * 
	 * 功能描述：[MD5]
	 *
	 * 创建者：wjx
	 * 创建时间: 2016年10月13日 上午11:22:58
	 * @param s
	 * @return
	 */
	public final static String MD5(String s) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};       
        try {
            byte[] btInput = s.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
       }
    }
	
	public static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000).substring(0, 10);
    }

}
