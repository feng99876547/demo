package com.sjb.service;


import com.fxc.admin.jdbc.dao.PublicDao;
import com.fxc.admin.jdbc.service.PublicServiceCacheExecution;
import com.fxc.admin.jdbc.service.jdbc.JdbcAdd;
import com.fxc.admin.jdbc.service.jdbc.JdbcDel;
import com.fxc.admin.jdbc.service.jdbc.JdbcGet;
import com.fxc.admin.jdbc.service.jdbc.JdbcUpdate;
import com.sjb.util.redis.RedisApi;

public class RedisCacheService<T,PK,DB extends PublicDao<T,PK>> implements PublicServiceCacheExecution<T,PK,DB>{
	
	private RedisApi redisApi;

	private JdbcAdd<T,PK,DB> jcbcAdd;
	
	private JdbcDel<T,PK,DB> jcbcDel;
	
	private JdbcUpdate<T,PK,DB> jcbcUpdate;
	
	private JdbcGet<T,PK,DB> jcbcGet;
	
	public JdbcAdd<T,PK,DB> getJcbcAdd() {
		return this.jcbcAdd;
	}

	public JdbcDel<T,PK,DB> getJcbcDel() {
		return this.jcbcDel;
	}

	public JdbcUpdate<T,PK,DB> getJcbcUpdate() {
		return this.jcbcUpdate;
	}

	public JdbcGet<T, PK, DB> getJcbcGet() {
		return this.jcbcGet;
	}

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
	public RedisApi getCache() throws Exception {
		if(redisApi == null)
			redisApi = new RedisApi();
		return redisApi;
	}

}
