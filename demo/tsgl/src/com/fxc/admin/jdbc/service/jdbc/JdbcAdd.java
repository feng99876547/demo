package com.fxc.admin.jdbc.service.jdbc;

import com.fxc.admin.jdbc.service.PublicAPI;
import com.fxc.entity.QueryInfo;
import com.fxc.entity.Result;

public interface JdbcAdd<T,PK,DB> {
	
	public Result<T> add(QueryInfo qi,PublicAPI<T,PK,DB> api,Result<T> result) throws Exception;
}
