package com.sjb.util;

/**
 * 正则替换
 * @author fxc
 *
 */
public class replace {

	//多个条件要用replaceAll不要用replace  如果字符串的开头或结尾匹配将匹配到的字符串替换为""
    String rsapublic = "-----BEGIN RSA PUBLIC KEY----- ssdcdsss21as12-----END RSA PUBLIC KEY-----"
    		.replaceAll("(^-----BEGIN RSA PUBLIC KEY-----|-----END RSA PUBLIC KEY-----$)", "");

}
