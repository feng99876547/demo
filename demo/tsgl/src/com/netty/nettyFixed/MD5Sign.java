package com.netty.nettyFixed;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.*;



public class MD5Sign {
	
	private static final  String MD5_KEY = "e10adc3949ba59abbe56e057f20f883e";
	
	//返回签名串
	public static String getSign(Map<String,Object> map) {
        String param = sort(map);
        return md5(param);
    }

    //接收数据验签
    public static boolean verifySign(Map<String,Object> data){
        String sign = null;
        try {
            sign = data.get("sign").toString();
            String param = sort(data);
            String md5Str = md5(param);
            if (md5Str.equals(sign)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println("数据加密异常");
            e.getMessage();
            return false;
        }
    }

    //按键名升序排列
    private static String sort (Map<String,Object> data) {
    	 Set<String> keySet = data.keySet();
         String[] keyArray = keySet.toArray(new String[keySet.size()]);
         Arrays.sort(keyArray);
         StringBuilder sb = new StringBuilder();
         for (String k : keyArray) {
             if (k.equals("sign")) {
                 continue;
             }
             if (data.get(k).toString().trim().length() > 0) // 参数值为空，则不参与签名
                 sb.append(k).append("=").append(data.get(k).toString().trim()).append("&");
         }
         sb.append("key=").append(MD5_KEY);
         return sb.toString();
    }

    //返回 32 为小写MD5串
    public static String md5(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
//            Log.error(e,"数据加密异常" + e.getMessage());
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
//            Log.error(e,"数据加密异常" + e.getMessage());
            return null;
        }

        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++)  {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        return md5StrBuff.toString();
    }
}
