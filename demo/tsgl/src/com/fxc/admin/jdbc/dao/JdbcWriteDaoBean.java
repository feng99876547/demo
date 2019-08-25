package com.fxc.admin.jdbc.dao;

import java.sql.Connection;
import java.sql.SQLException;

import sun.rmi.runtime.Log;

//分片表可以使用计划任务每天判断时间 没个月的月末创建一个新表
public class JdbcWriteDaoBean implements JdbcWriteDao{
	
	private MyDataSource writeDao = null;
	
	public static final ThreadLocal<TransactionInfo> connPool = new ThreadLocal<TransactionInfo>();

	public JdbcWriteDaoBean(){
		super();
		//配置中配置的默认DataSource
//		writeDao = 
	}
	
	public JdbcWriteDaoBean(MyDataSource wd){
		super();
		writeDao = wd;
	}
	
	public MyDataSource getWriteDao() {
		return this.writeDao;
	}
	
	/**
	 * 获取连接
	 */
	public Connection getConnection() throws Exception {
		Connection conn = null;
		try{
			conn = connPool.get().getConn();
			if(conn!=null){
				return conn;
			}else{
				conn = getWriteDao().getConn();
				conn.setAutoCommit(false);
				connPool.get().setConn(conn);
				connPool.get().setWd(getWriteDao());
			}
			return conn;
		}catch(Exception e){
			if(conn!=null){
				conn.close();
			}
			throw new Exception(e);
		}
	}

}
