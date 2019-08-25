package com.sjb.util;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;


/**
 * @author fxc
 *
 */
public class Log {

	public static Logger business = LoggerFactory.getLogger("business");
	
	public static Logger allError = LoggerFactory.getLogger("allerror");

	public static Logger syn = LoggerFactory.getLogger("syndata");
	
	public static void info(String text,String param){
		business.info("{},[{}]",toUTF(text),toUTF(param));
	}
	
	public static void info(Class<?> clas,String text,String param){
		business.info("[{}][{}],[{}]",clas.getName(),text,toUTF(param));
	}
	
	public static void error(String errText,String param){
		allError.error("{},[{}]",toUTF(errText),toUTF(param));
	}
	
	public static void error(String param,Exception e){
		allError.error("异常[{}],erroeMessage{}",toUTF(param),e.getMessage());
	}
	
	public static void error(String errText,String param,Exception e){
		allError.error("{},[{}],erroeMessage:{}",toUTF(errText),toUTF(param),e.getMessage());
	}
	
	public static void error(Class<?> clas,String errText,String param,Exception e){
		allError.error("[{}][{}],[{}],erroeMessage:{}",clas.getName(),toUTF(errText),toUTF(param),e.getMessage());
	}
	
	public static String toUTF(String str){
		return str;
	}
	
	
}
