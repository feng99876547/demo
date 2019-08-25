package com.fxc.business;

import java.util.ArrayList;
import java.util.List;

import com.fxc.admin.jdbc.service.DispatchTask;
import com.fxc.admin.jdbc.service.PublicAPI;
import com.fxc.entity.QueryInfo;
import com.fxc.entity.Result;

public interface BusinessDispatch<T,PK,DB> {
	
	public Result<T> AddDispatchTask(ArrayList<DispatchTask> list,QueryInfo qi,PublicAPI<T,PK,DB> api) throws Exception;
	
	public Result<List<T>> delDispatchTask(ArrayList<DispatchTask> list,QueryInfo qi,PublicAPI<T,PK,DB> api) throws Exception;
	
	public Result<T> updateDispatchTask(ArrayList<DispatchTask> list,QueryInfo qi,PublicAPI<T,PK,DB> api) throws Exception;
	
	public Result<T> getDispatchTask(ArrayList<DispatchTask> list,QueryInfo qi,PublicAPI<T,PK,DB> api) throws Exception;
	
	public Result<List<T>> getsDispatchTask(ArrayList<DispatchTask> list,QueryInfo qi,PublicAPI<T,PK,DB> api) throws Exception;
	
}
