package com.sjb.service;

import net.sf.ehcache.Element;

public class ObjectEHCache<T> extends ModelEHCache<T>{

	public ObjectEHCache(String name) {
		super(name);
	}

	@Override
	public T get(String cacheKey) throws Exception {
		Element e = cache.get(cacheKey);
		if (e == null) {
			return null;
		}
		return (T) e.getValue();
	}
	
	
	@Override
	public void put(String cacheKey, T result) throws Exception {
		Element element = new Element(cacheKey, result);
		cache.put(element);
	}
}
