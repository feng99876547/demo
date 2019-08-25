package com.fxc.admin.jdbc.dao;

import java.util.HashMap;
import java.util.List;

import com.fxc.entity.QueryInfo;

/**
 * 通用jdbc操作接口
 * @author fxc
 */
public interface PublicDao<T,PK>{
	
	/**
	 * 新增
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public int add(T obj)throws Exception;
	
	/**
	 * 新增返回id
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public Object addAndId(T obj)throws Exception;
	
	/**
	 * 按id删除 id不能为空
	 * @return
	 * @throws Exception
	 */
	public int del(PK id)throws Exception;
	
	/**
	 * 按id删除多条 id不能为空
	 * @return
	 * @throws Exception
	 */
	public int dels(String id) throws Exception;
	
	/**
	 * 按条件删除
	 * @return
	 * @throws Exception
	 */
	public int del(HashMap<String,Object> ma)throws Exception;
	
	/**
	 * 按id修改（只修改obj的值不为 null 的字段）id不能为空
	 * @return
	 * @throws Exception
	 */
	public int update(T obj)throws Exception;
	
	/**
	 * 按查询条件修改obj中不为空的字段
	 * @return
	 * @throws Exception
	 */
	public int update(T obj,HashMap<String,Object> where)throws Exception;
	
	/**
	 * id查找
	 * @return
	 * @throws Exception
	 */
	public T get(PK ID)throws Exception;
	
	/**
	 * id查找 不做关联
	 * @return
	 * @throws Exception
	 */
	public T get(PK ID,boolean b)throws Exception;
	
	/**
	 * 条件搜索返回单条
	 * @param qi
	 * @return 
	 * @throws Exception
	 */
	public T get(QueryInfo qi)throws Exception;
	
	/**
	 * 单纯的按HashMap<String,Object> ma 不做任何多余操作
	 * @param ma
	 * @return
	 * @throws Exception
	 */
	public T get(HashMap<String,Object> ma) throws Exception;
	
	/**
	 * 查询所有
	 * @return
	 * @throws Exception
	 */
	public List<T> gets()throws Exception;
	
	/**
	 * 条件搜索返回多条
	 * @param qi
	 * @return 
	 * @throws Exception
	 */
	public List<T> gets(QueryInfo qi)throws Exception;
	
	/**
	 * 单纯的按HashMap<String,Object> ma 不做任何多余操作
	 * @param ma
	 * @return
	 * @throws Exception
	 */
	public List<T> gets(HashMap<String,Object> ma) throws Exception;
	
	
	/**
	 * 按条件查询 返回总行数
	 * @param searchs
	 * @return
	 */
	public int getCount(HashMap<String, Object> searchs) throws Exception;
	
	/**
	 * 返回所有总行数
	 * @return
	 */
	public int getCount() throws Exception;
}
