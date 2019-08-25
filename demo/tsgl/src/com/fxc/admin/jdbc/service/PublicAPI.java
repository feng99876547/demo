package com.fxc.admin.jdbc.service;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import com.fxc.admin.jdbc.service.entity.ServiceSection;
import com.fxc.admin.jdbc.service.jdbc.JdbcAdd;
import com.fxc.admin.jdbc.service.jdbc.JdbcDel;
import com.fxc.admin.jdbc.service.jdbc.JdbcGet;
import com.fxc.admin.jdbc.service.jdbc.JdbcUpdate;
import com.fxc.admin.jdbc.service.jdbc.PublicServiceJdbcExecution;

public interface PublicAPI<T,PK,DB> {
	
	public static int timeout = 1800;
	
	public Class<T> getClas() throws Exception;
	
	public DB getDao() throws Exception;
	
	public HashMap<String,ArrayList<Field>> getBusinessFields()throws Exception;
	
	public abstract ServiceSection<T> getServiceAop()throws Exception;
	
	public JdbcAdd<T,PK,DB> getJcbcAdd()throws Exception;
	
	public JdbcDel<T,PK,DB> getJcbcDel()throws Exception;
	
	public JdbcUpdate<T,PK,DB> getJcbcUpdate()throws Exception;
	
	public JdbcGet<T,PK,DB> getJcbcGet()throws Exception;
	
	/**
	 * 获取缓存的过期时间  设置值为0时表示永久
	 * @return
	 * @throws Exception
	 */
	public default int getTimeout()throws Exception{
		return timeout;
	};
	
	//如果redis有这样需要 需要存储到多太redis服务器 需要使用不同的缓存配置
//	public default String getCacheName(){
//		return "RedisApi";
//	}
	
	
	/**
	 * 获取dao层的实现
	 * @return
	 * @throws Exception
	 */
	public PublicServiceJdbcExecution<T,PK,DB> getPublicServiceJdbc() throws Exception;
}
