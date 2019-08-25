package com.fxc.admin.jdbc.service;

public enum DispatchTask {
	CACHE("cache"),
	BUSINES("busines");

	private String key;
	
	DispatchTask(String key){
		this.key = key;
	}

	public String getKey() {
		return this.key;
	}
}
