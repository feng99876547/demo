package com.fxc.business;

import java.util.ArrayList;
import java.util.List;

import com.fxc.admin.jdbc.service.DispatchTask;
import com.fxc.admin.jdbc.service.PublicAPI;
import com.fxc.entity.QueryInfo;
import com.fxc.entity.Result;

public interface Busines<T,PK,DB> {
	
	public  boolean add(QueryInfo qi,PublicAPI<T,PK,DB> api,Result<T> result) throws Exception;
	
	public void addCallback(QueryInfo qi,PublicAPI<T,PK,DB> api,Result<T> result)throws Exception;
	
	public  boolean del(QueryInfo qi,PublicAPI<T,PK,DB> api,Result<List<T>> result) throws Exception;
	
	public void delCallback(QueryInfo qi,PublicAPI<T,PK,DB> api,Result<List<T>> result)throws Exception;
	
	public  boolean update(QueryInfo qi,PublicAPI<T,PK,DB> api,Result<T> result) throws Exception;
	
	public void  updateCallback(QueryInfo qi,PublicAPI<T,PK,DB> api,Result<T> result)throws Exception;
	
	public  boolean gets(QueryInfo qi,PublicAPI<T,PK,DB> api,Result<List<T>> result) throws Exception;
	
	public void  getsCallback(QueryInfo qi,PublicAPI<T,PK,DB> api,Result<List<T>> result)throws Exception;
	
	public  boolean get(QueryInfo qi,PublicAPI<T,PK,DB> api,Result<T> result,ArrayList<DispatchTask> list,int index) throws Exception;
	
	public void  getCallback(QueryInfo qi,PublicAPI<T,PK,DB> api,Result<T> result)throws Exception;
	
	@SuppressWarnings("unchecked")
	public default void next(QueryInfo qi, PublicAPI<T, PK, DB> api, Result<T> result,ArrayList<DispatchTask> list,int index) throws Exception{
		index++;
		if(index>=list.size()){
			api.getJcbcGet().freeGet(qi,api,result);
			return;
		}else{
			Busines<T,PK,DB> bus = (Busines<T,PK,DB>) BusinessDispatchImpl.map.get(list.get(index).getKey());
			bus.get(qi, api, result, list, index);
		}
	}
}
