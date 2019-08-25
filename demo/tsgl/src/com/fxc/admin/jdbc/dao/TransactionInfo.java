package com.fxc.admin.jdbc.dao;

import java.sql.Connection;

public class TransactionInfo {

	private String begin;
	
	private Connection conn;
	
	private MyDataSource wd;

	public String getBegin() {
		return begin;
	}

	public void setBegin(String begin) {
		this.begin = begin;
	}

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	public MyDataSource getWd() {
		return wd;
	}

	public void setWd(MyDataSource wd) {
		this.wd = wd;
	}
	
}
