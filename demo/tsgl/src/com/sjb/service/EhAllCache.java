package com.sjb.service;


public class EhAllCache extends ModelEHCache<Object>{
	
	public EhAllCache(){
		super("all");
	}
	
	public EhAllCache(int timeout){
		super(String.valueOf(timeout),timeout);
	}
}

