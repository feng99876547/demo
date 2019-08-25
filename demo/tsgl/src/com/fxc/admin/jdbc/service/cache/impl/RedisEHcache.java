package com.fxc.admin.jdbc.service.cache.impl;

import org.springframework.stereotype.Component;

import com.fxc.admin.jdbc.service.cache.ExecutionCache;
import com.sjb.service.EhAllCache;
import com.sjb.service.cache.CacheMagger;

/**
 * 这边缓存的获取过程先像redis获取 获取失败在重本地二级缓存获取 
 * put也一样 put到redis在put到本地缓存
 * @author fxc
 *
 * @param <T>
 */
@Component
public class RedisEHcache<T> implements ExecutionCache<T>{

	public RedisEHcache(){
	}
	
	/**
	 * @return 生成key的策略
	 * @throws Exception
	 */
	public  String getKey(String key,String value, Class<?> clas) throws Exception {
		return clas.getName()+":"+key+":"+value;
	}

	/**
	 * 先像redis获取 没有获取到缓存 在从本地二级缓存获取 
	 * @param timeout 本地缓存用于做过期时间缓存分组用的
	 */
	@Override
	public T get(String key,int timeout) throws Exception {
		EhAllCache cache =  CacheMagger.getEhCache(timeout);//获取本地缓存
		T obj = (T) cache.get(key);
		return obj;
	}

	/**
	 * 更新到redis 在更新到本地缓存
	 * @param timeout 本地缓存用于做过期时间缓存分组用的
	 */
	@Override
	public void put(String cacheKey, T result,int timeout) throws Exception {
		EhAllCache cache =  CacheMagger.getEhCache(timeout);
		// TODO Auto-generated method stub
		
	}

	/**
	 * 移除redis在移除本地缓存
	 * @param timeout 本地缓存用于做过期时间缓存分组用的
	 */
	@Override
	public void remove(String key, int timeout) throws Exception {
		EhAllCache cache =  CacheMagger.getEhCache(timeout);
		// TODO Auto-generated method stub
		
	}

}
