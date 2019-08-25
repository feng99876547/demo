package com.sjb.service;


import com.fxc.entity.IdEntity;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * 添加和获取的方法只适合值是继承 IdEntity 的entity实体 和Object的区别在于存的key是非id多了一层引用指向idkey
 * @author fxc
 *
 * @param <T>
 */
public class ModelEHCache<T>{
	
	public static final CacheManager cacheManager = new CacheManager();
	
	protected Cache cache;
	
	public ModelEHCache(String CacheManager){
		if(ModelEHCache.cacheManager.cacheExists(CacheManager)){
			this.cache=cacheManager.getCache(CacheManager);
		}
	}
	
	public ModelEHCache(String CacheManager,int timeout){
		synchronized (ModelEHCache.class) {
			if(ModelEHCache.cacheManager.cacheExists(CacheManager)){
				cacheManager.addCacheIfAbsent(CacheManager);
				Cache ca = cacheManager.getCache(CacheManager);
				ca.getCacheConfiguration().setTimeToIdleSeconds(timeout);
				this.cache=ca;
			}else{
				this.cache = cacheManager.getCache(CacheManager);
			}
		}
	}
 
	public ModelEHCache(){
		super();
	}
	
	/**
	 * 通过名称从缓存中获取数据 如果获取的值是map或list之类的其他值可能会引起异常 
	 * 所以是其他值的使用objectEHCache
	 */
	public T get(String cacheKey) throws Exception {
		Element e = null;
		if(cacheKey.indexOf(":id:")>-1){
			e = cache.get(cacheKey);
		}else{//不是id 存的值是idkey的key
			e = cache.get(cacheKey);
			if(e == null){
				return null;
			}else{
				e = cache.get(e.getValue());
			}
		}
		if (e == null) {
			return null;
		}
		return (T) e.getValue();
	}
	
	/*
	 * 将对象添加到缓存中
	 */
	public void put(String cacheKey, T result) throws Exception {
		if(result instanceof IdEntity){
			if(cacheKey.indexOf(":id:")>-1){//说明是以id为key
				Element element = new Element(cacheKey, result);
				cache.put(element);
			}else{
//				String idkey = cacheKey.replaceAll("(:.*:)", ":id:");
				String idkey = cacheKey.split(":")[0]+":id:"+((IdEntity) result).getId().toString();
				Element idElement = new Element(idkey, result);
				cache.put(idElement);
				Element element = new Element(cacheKey, idkey);
				cache.put(element);
			}
		}else{
			Element element = new Element(cacheKey, result);
			cache.put(element);
		}
	}
	
	/**
	 * 删除缓存
	 * @param cacheKey
	 * @throws Exception
	 */
	public void remove(String cacheKey) throws Exception {
		cache.remove(cacheKey);
	}
 
	
}

