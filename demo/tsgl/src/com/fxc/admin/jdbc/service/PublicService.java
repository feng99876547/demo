package com.fxc.admin.jdbc.service;


import java.util.List;

import com.fxc.entity.QueryInfo;
import com.fxc.entity.Result;


/**
 * 定义service 公共接口
 * @author fxc
 */
public  interface PublicService<T,PK,DAO>{
	
	
	/**
	 * 获取DaoBean实现
	 * @return
	 */
	public DAO getDao();
	
	/**
	 * 新增
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public  Result<T> add(QueryInfo qi)throws Exception;
	
	/**
	 * 删除
	 * @return
	 * @throws Exception
	 */
	public Result<List<T>> del(QueryInfo qi)throws Exception;
	
	/**
	 * 修改
	 * @return
	 * @throws Exception
	 */
	public Result<T> update(QueryInfo qi)throws Exception;
	
	
	/**
	 * id查找
	 * @param qi
	 * @return
	 * @throws Exception
	 */
	public T get(PK ID)throws Exception;
	
	/**
	 * 条件搜索
	 * @param qi
	 * @return
	 * @throws Exception
	 */
	public Result<T> get(QueryInfo qi)throws Exception;
	
	/**
	 * 条件搜索
	 * 不对search进行操作
	 * @param qi
	 * @return
	 * @throws Exception
	 */
	public Result<T> freeGet(QueryInfo qi)throws Exception;
	
	
	
	/**
	 * 查询所有
	 * @param qi
	 * @return
	 * @throws Exception
	 */
	public Result<List<T>> gets()throws Exception;
	
	/**
	 * 条件搜索
	 * @param qi
	 * @return
	 * @throws Exception
	 */
	public Result<List<T>> gets(QueryInfo qi)throws Exception;
	
	/**
	 * 条件搜索 
	 * 不对search进行操作
	 * @param qi
	 * @return
	 * @throws Exception
	 */
	public Result<List<T>> freeGets(QueryInfo qi) throws Exception;
	
}
