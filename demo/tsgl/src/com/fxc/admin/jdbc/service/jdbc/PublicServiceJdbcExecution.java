package com.fxc.admin.jdbc.service.jdbc;


public interface PublicServiceJdbcExecution<T,PK,DB>{
	
	public JdbcAdd<T,PK,DB> getJcbcAdd();
	
	public JdbcDel<T,PK,DB> getJcbcDel();
	
	public JdbcUpdate<T,PK,DB> getJcbcUpdate();
	
	public JdbcGet<T,PK,DB> getJcbcGet();
}
