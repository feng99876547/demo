package com.fxc.entity;

import java.io.Serializable;

import com.fxc.admin.jdbc.annotation.Custom;

/**
 * 方便代码操作统一Id类型String 但是数据库可以使用long 在执行查询语句时会强转
 * @author fxc
 */
public class UUIDEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
