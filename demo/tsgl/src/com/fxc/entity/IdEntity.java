package com.fxc.entity;

import java.io.Serializable;

/**
 * 注意出现java.lang.String cannot be cast to java.lang.Long等一些异常时 
 * 是因为使用泛型反射注入后不会强转 当数据库类型和实体类型不一致时会抛出类似异常
 * 
 * 如果int类型取出来是long 是因为该类型使用的是 unsigned 非负数 正数的值为正常的2倍 所有类型是int取出来的是long
 */
public  class IdEntity<PK> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private PK id;


	public IdEntity(){
		super();
	}
	
	
	public IdEntity(PK id){
		super();
		this.id = id;
	}
	
	public PK getId() {
		return id;
	}
	
	public void setId(PK id) {
		this.id = id;
	}
	
//	public abstract  String getIdName();
//	public String getIdName(){
//		return "id";
//	}
}
