package com.fxc.admin.jdbc.service.jdbc;

import java.util.List;

import com.fxc.admin.jdbc.service.PublicAPI;
import com.fxc.entity.QueryInfo;
import com.fxc.entity.Result;

public interface JdbcGet<T,PK,DB> {
	
	public T get(PK id,PublicAPI<T,PK,DB> api) throws Exception;
	
//	public Result<T> get(QueryInfo qi,PublicAPI<T,PK,DB> api,Result<T> result) throws Exception;
	
	/**
	 * 不带条件差全表
	 */
	public Result<List<T>> gets(PublicAPI<T,PK,DB> api) throws Exception;
	
//	public Result<List<T>> gets(QueryInfo qi,PublicAPI<T,PK,DB> api,Result<List<T>> result) throws Exception;
	
	public Result<T> freeGet(QueryInfo qi,PublicAPI<T,PK,DB> api,Result<T> result) throws Exception;
	
	public Result<List<T>> freeGets(QueryInfo qi,PublicAPI<T,PK,DB> api,Result<List<T>> result) throws Exception;
}
