package com.fxc.admin.jdbc.service.cache;

/**
 * 用于获取缓存的过程 一般的实现有本地获取 redis获取 和 redis结合本地获取
 * @author fxc
 *
 * @param <T>
 */
public interface ExecutionCache<T> {

	public T get(String key,int timeout) throws Exception;
	
	public void put(String key, T result,int timeout) throws Exception;
	
	public void remove(String key,int timeout) throws Exception;
}
