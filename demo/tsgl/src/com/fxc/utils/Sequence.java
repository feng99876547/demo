package com.fxc.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * 排序管理
 * @author fxc
 *
 */
@Component
@Transactional(propagation = Propagation.REQUIRED)
public class Sequence {

	@Autowired
	private  JdbcTemplate jdbcTemplate;
	
	public void setSequence(Object position,String table,String field) throws Exception{
		String sq = "select count(*) from "+table+" where "+field+"= ? ";
		int count = jdbcTemplate.queryForObject(sq,new Object[]{position},Integer.class); 
		if(count>0){//如果存在在修改
			String sql = "update "+table+" set "+field+" = "+field+"+1 where position >= "+position;
			jdbcTemplate.update(sql);
		}
	}
	
	public int exSql(String sql,Object id) throws Exception{
		return jdbcTemplate.update(sql,new Object[]{id});
	}
}
