package com.fxc.admin.jdbc.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.sjb.util.Log;

public class DefaultWriteDao implements MyDataSource{

	private String jdbcDriver = null;//数据库驱动
	
	private String dbUrl = null;//数据url
	
	private String dbUsername = null;//账号
	
	private String dbPassword = null;//密码
	
	private int busiconnCount = 0;//连接池中空闲连接数
	
	private int initConnCount = 10;//最大的空闲连接数

	private int totalConnCount = 0;//连接池中总链接数
	
	private int maxConnCount = 50;//连接池最大链接数
	
	private int timeout = 3;//链接超时时间
	
	/** 空闲连接池 */
	private ArrayBlockingQueue<Connection> connPool = null;//和initConnCount一起初始化
	
	/** 允许的最大连接池 用户拿连接时先向队列插入this,插入成功说明可以获取连接 在关闭连接后从队列移除*/
	private ArrayBlockingQueue<MyDataSource> maxConnPoll = null;//和maxConnCount

	public DefaultWriteDao() {
		super();
	}

	
	/**
	 * 返回新建的数据库连接
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private Connection createConnection() throws ClassNotFoundException, SQLException{
		if(dbUrl != null && dbUsername != null && dbPassword !=null){
			Connection conn = null;
			Class.forName(jdbcDriver);
			conn = DriverManager.getConnection(dbUrl,dbUsername,dbPassword);
			return conn;
		}else{
			//到时候换成err日志不要打到bus去
			Log.error("[SQL]数据库连接失败 dbUrl和dbUsername和dbPassoword其中有一个为空","");
			return null;
		}
	}
	
	/**
	 * 获取数据库连接
	 * @return
	 * @throws Exception 
	 */
	public Connection getConn() throws Exception{
		//插入值不要使用null 因为有些方法取不到值返回就是null
		if(this.maxConnPoll.offer(this,timeout,TimeUnit.SECONDS)){//插入成功说明连接池没满
			Connection c = connPool.poll();
			if(c==null){
				return createConnection();
			}else{
				return c;
			}
		}else{
			throw new Exception("连接池已经满了");
		}
	}
	
	public void close(Connection conn){
		if(!connPool.offer(conn)){
			try {
				if(conn!=null){
					conn.close();
					conn = null;
					maxConnPoll.remove();
				}
			} catch (SQLException e) {
				Log.error("[SQL]数据库连接关闭异常!",e);
			}
		}else{
			maxConnPoll.remove();
		}
	}
	
	public String getJdbcDriver() {
		return jdbcDriver;
	}

	public void setJdbcDriver(String jdbcDriver) {
		this.jdbcDriver = jdbcDriver;
	}

	public String getDbUrl() {
		return dbUrl;
	}

	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}

	public String getDbUsername() {
		return dbUsername;
	}

	public void setDbUsername(String dbUsername) {
		this.dbUsername = dbUsername;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}

	public int getBusiconnCount() {
		return busiconnCount;
	}

//	public void setBusiconnCount(int busiconnCount) {
//		this.busiconnCount = busiconnCount;
//	}

	public int getInitConnCount() {
		return initConnCount;
	}

	public void setInitConnCount(int initConnCount) {
		this.initConnCount = initConnCount;
	}

	public int getTotalConnCount() {
		return totalConnCount;
	}

//	public void setTotalConnCount(int totalConnCount) {
//		this.totalConnCount = totalConnCount;
//	}

	public int getMaxConnCount() {
		return maxConnCount;
	}

	public void setMaxConnCount(int maxConnCount) {
		this.maxConnCount = maxConnCount;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public ArrayBlockingQueue<Connection> getConnPool() {
		return connPool;
	}

	public void setConnPool(ArrayBlockingQueue<Connection> connPool) {
		this.connPool = connPool;
	}

	public ArrayBlockingQueue<MyDataSource> getMaxConnPoll() {
		return maxConnPoll;
	}

	public void setMaxConnPoll(ArrayBlockingQueue<MyDataSource> maxConnPoll) {
		this.maxConnPoll = maxConnPoll;
	}

	
	
}
