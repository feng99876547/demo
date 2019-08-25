package com.fxc.admin.jdbc.service.jdbc;

import com.fxc.admin.jdbc.service.PublicAPI;
import com.fxc.entity.QueryInfo;
import com.fxc.entity.Result;

public interface JdbcUpdate<T,PK,DB> {
	
	public Result<T> update(QueryInfo qi,PublicAPI<T,PK,DB> api,Result<T> result) throws Exception;
	
}
