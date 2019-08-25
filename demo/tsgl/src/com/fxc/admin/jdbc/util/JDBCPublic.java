package com.fxc.admin.jdbc.util;


public class JDBCPublic {

	public final void printsSQL(String sql){
//		if(FxcJdbc.showSql)
		if(true)
			System.out.println(sql);
	}
}
