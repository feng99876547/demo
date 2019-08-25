package com.fxc.admin.jdbc.dao;

import java.sql.Connection;

public interface JdbcWriteDao {

	public MyDataSource getWriteDao();
	
	public Connection getConnection()throws Exception;
	
}
