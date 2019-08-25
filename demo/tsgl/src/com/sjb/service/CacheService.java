package com.sjb.service;


import com.fxc.admin.jdbc.service.PublicServiceCacheExecution;
import com.fxc.admin.jdbc.service.jdbc.JdbcAdd;
import com.fxc.admin.jdbc.service.jdbc.JdbcDel;
import com.fxc.admin.jdbc.service.jdbc.JdbcGet;
import com.fxc.admin.jdbc.service.jdbc.JdbcUpdate;

/**
 * 
 * @author fxc
 *
 * @param <T>
 * @param <PK>
 * @param <DB>
 */
public class CacheService<T,PK,DB> implements PublicServiceCacheExecution<T,PK,DB>{
	
	private JdbcAdd<T,PK,DB> jcbcAdd;
	
	private JdbcDel<T,PK,DB> jcbcDel;
	
	private JdbcUpdate<T,PK,DB> jcbcUpdate;
	
	private JdbcGet<T,PK,DB> jcbcGet;

	public void setJcbcAdd(JdbcAdd<T, PK, DB> jcbcAdd) {
		this.jcbcAdd = jcbcAdd;
	}

	public void setJcbcDel(JdbcDel<T, PK, DB> jcbcDel) {
		this.jcbcDel = jcbcDel;
	}

	public void setJcbcUpdate(JdbcUpdate<T, PK, DB> jcbcUpdate) {
		this.jcbcUpdate = jcbcUpdate;
	}

	public void setJcbcGet(JdbcGet<T, PK, DB> jcbcGet) {
		this.jcbcGet = jcbcGet;
	}

	@Override
	public JdbcAdd<T,PK,DB> getJcbcAdd() {
		return this.jcbcAdd;
	}

	@Override
	public JdbcDel<T,PK,DB> getJcbcDel() {
		return this.jcbcDel;
	}

	@Override
	public JdbcUpdate<T,PK,DB> getJcbcUpdate() {
		return this.jcbcUpdate;
	}

	@Override
	public JdbcGet<T, PK, DB> getJcbcGet() {
		return this.jcbcGet;
	}


	@Override
	public EhAllCache getCache() throws Exception {
		return null;
	}

}
