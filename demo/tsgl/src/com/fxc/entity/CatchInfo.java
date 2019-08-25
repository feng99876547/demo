package com.fxc.entity;

import java.util.HashMap;

public class CatchInfo{
	private String table;//数据库的表名
	
	private String alias;//别名
	
	private String idKey;//统一都用id不就完了
	
	private HashMap<String,Associated>	ass;//实体类中会用到的关联对象集合 map key使用的是字段的name
	
	private String sqlPrefix;//sql select前缀
	
	
	public String getIdKey() {
		return idKey;
	}
	public void setIdKey(String idKey) {
		this.idKey = idKey;
	}
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public HashMap<String, Associated> getAss() {
		return ass;
	}
	public void setAss(HashMap<String, Associated> ass) {
		this.ass = ass;
	}
	public String getSqlPrefix() {
		return sqlPrefix;
	}
	public void setSqlPrefix(String sqlPrefix) {
		this.sqlPrefix = sqlPrefix;
	}
	public CatchInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
}
