package com.fxc.admin.jdbc.dao;

import java.sql.Connection;

public interface MyDataSource {
	
	public void setJdbcDriver(String Driver);
	
	public void setDbUrl(String dbUrl);
	
	public void setDbPassword(String DbPassword);
	
	public void setDbUsername(String DbUsername);
	
	public void setInitConnCount(int initConnCount);
	
	public void setMaxConnCount(int maxConnCount);

	public Connection getConn() throws Exception;
	
	public void close(Connection conn) throws Exception;
}
