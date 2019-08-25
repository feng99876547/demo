package com.fxc.admin.jdbc.service.jdbc;

import java.util.List;

import com.fxc.admin.jdbc.service.PublicAPI;
import com.fxc.entity.QueryInfo;
import com.fxc.entity.Result;

public interface JdbcDel<T,PK,DB> {
	
	public Result<List<T>> del(QueryInfo qi,PublicAPI<T,PK,DB> api,Result<List<T>> result) throws Exception;
	
}
