package com.fxc.admin.jdbc.service.listener.serviceSection;

import com.fxc.entity.QueryInfo;
import com.fxc.entity.Result;

public interface BeforeGetListener<T> {
	public boolean onAOP(Result<T> result,QueryInfo query)throws Exception;
}
