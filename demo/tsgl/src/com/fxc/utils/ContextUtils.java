package com.fxc.utils;

import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


public class ContextUtils implements ApplicationContextAware{
	
	public static ServletContext servletContext;
	public static ApplicationContext appContext;
	
	public static Map<String, String> globalPathMap;//全局参数 1
	
	public final static int XS = 1;//状态显示
	
	public final static int YC = 2;//状态隐藏
	
	public final static int WSC = 1;//未删除
	
	public final static int YSC = 5;//以删除
	
	public final static int WFB = 1;//状态 未发布 或取消发布
	
	public final static int FB = 2;//状态 发布
	
	public ContextUtils(){
	}


	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		appContext = applicationContext;
	}
	
}
