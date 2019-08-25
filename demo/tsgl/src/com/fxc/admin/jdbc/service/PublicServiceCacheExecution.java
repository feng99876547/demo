package com.fxc.admin.jdbc.service;

import com.fxc.admin.jdbc.service.jdbc.PublicServiceJdbcExecution;

/**
 * 缓存执行过程继承了PublicServiceJdbc执行过程
 * @author fxc
 *
 * @param <T>
 * @param <PK>
 * @param <DB>
 */
public interface PublicServiceCacheExecution<T,PK,DB> extends PublicServiceJdbcExecution<T,PK,DB>{
	

	/**
	 * @return 生成key的策略
	 * @throws Exception
	 */
	public default String getKey(String key,String value, Class<?> clas) throws Exception {
		return clas.getName()+":"+key+":"+value;
	}
	/**
	 * 返回需要使用的缓存配置
	 * @return
	 * @throws Exception
	 */
	public abstract Object getCache() throws Exception;
	

	
}
