package com.sjb.service.cache;

import java.util.HashMap;

import com.fxc.admin.jdbc.service.PublicAPI;
import com.sjb.service.EhAllCache;
import com.sjb.service.ModelEHCache;

public class CacheMagger {
	
	private static final HashMap<Integer,EhAllCache> cacheMagger = new HashMap<Integer,EhAllCache>();
	
	public static EhAllCache getEhCache(int timeout) throws Exception{
		if(timeout == PublicAPI.timeout){//默认统一的过期时间 配置不一样
			if(cacheMagger.get(timeout)==null){
				if(ModelEHCache.cacheManager.cacheExists("all")){
					EhAllCache cache = new EhAllCache();
					cacheMagger.put(timeout, cache);
				}else{
					throw new Exception("没有配置allcache");
				}
				
			}
		}else{//根据不同的过期时间创建缓存集合
			if(cacheMagger.get(timeout)==null){
				EhAllCache cache = new EhAllCache(timeout);
				cacheMagger.put(timeout, cache);
			}
		}
		return cacheMagger.get(timeout);
	}

}
