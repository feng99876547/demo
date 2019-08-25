package com.sjb.service.cache;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.fxc.admin.jdbc.dao.PublicDaoImpl;
import com.fxc.admin.jdbc.service.DispatchTask;
import com.fxc.admin.jdbc.service.PublicAPI;
import com.fxc.admin.jdbc.service.PublicServiceCacheExecution;
import com.fxc.admin.jdbc.service.cache.ExecutionCache;
import com.fxc.admin.jdbc.service.cache.impl.RedisEHcache;
import com.fxc.admin.jdbc.service.jdbc.PublicServiceJdbcGet;
import com.fxc.business.Busines;
import com.fxc.business.BusinessDispatchImpl;
import com.fxc.entity.IdEntity;
import com.fxc.entity.QueryInfo;
import com.fxc.entity.Result;
import com.fxc.exception.MyCacheException;
import com.sjb.service.ModelEHCache;
import com.sjb.util.Log;
import com.sjb.service.EhAllCache;

import net.sf.ehcache.Cache;

/**
 * 为什么没用aop 因为aop的代理后需要创建新的对象 这样会占用更多的内存 所以架构之前可以不用aop尽量不用
 * @author Administrator
 *
 * @param <T>
 * @param <PK>
 * @param <DB>
 */
@SuppressWarnings({"unused"})
public class RedisEHCachePublicServiceJdbcGet<T extends IdEntity<PK>,PK,DB extends PublicDaoImpl<T,PK>>  implements Busines<T,PK,DB>{
	
	
//	private ConcurrentHashMap<String,String> lock = new ConcurrentHashMap<String,String>();
	
	private AtomicInteger ai=new AtomicInteger(0);
	
	private ExecutionCache<T> ec = new RedisEHcache<T>();
	

	/**
	 * @return 生成key的策略
	 * @throws Exception
	 */
	public  String getKey(String key,String value, Class<?> clas) throws Exception {
		return clas.getName()+":"+key+":"+value;
	}
	
	/**
	 * 使用本地缓存 时规则就是传入参数keyName和keyValue 
	 * 带有不同的查询条件需要和key绑定 如 name 和年龄都是查询条件 keyName = className:name:xxx,age:xxx
	 * 不带这两个参数则按正常流程走
	 * 返回false表示满足需求业务中断  true表示当前业务完毕需要继承往下走 不符合业务需求抛出自定义异常
	 */
	@Override
	public boolean get(QueryInfo qi, PublicAPI<T, PK, DB> api, Result<T> result,ArrayList<DispatchTask> list,int index) throws Exception {
		if(qi.getKeyName()!=null ){//代表使用缓存查询
			if(qi.getKeyValue() == null){
				throw new MyCacheException("使用缓存查询keyValue不能为空");
			}
			String key = getKey(qi.getKeyName(),qi.getKeyValue(), api.getClas());
			T obj = ec.get(key, api.getTimeout());
			if(obj != null){
				result.setRows(obj);
				System.out.println("拿到缓存:"+ai.getAndIncrement());
				return false;
			}else{
				//替换redis分布式锁
//				synchronized (getLock(api.getClas().getName())) { //这种小项目应该不会引起缓存雪奔所以锁的力度很细
					obj =  ec.get(key, api.getTimeout());
					if(obj != null){
						System.out.println("拿到缓存:"+ai.getAndIncrement());
						result.setRows(obj);
						return false;
					}
					System.out.println("拿到锁:"+ai.getAndIncrement());
					HashMap<String,Object> ma = new HashMap<String,Object>();
					ma.put(qi.getKeyName().split(",")[0], qi.getKeyValue());
					qi.setSearch(ma);//重新初始化查询条件 只使用key 做条件
					next(qi, api, result, list, index);
					getCallback(qi,api,result);
					return true;
//				}
			}
		}else{//代表使用非缓存查询 那就直接跳过
			return true;
		}
	}
	
	@Override
	public void getCallback(QueryInfo qi, PublicAPI<T, PK, DB> api, Result<T> result) throws Exception {
		if(qi.getKeyName()!=null ){
			if(result.isSuccess()){//添加缓存
				String key = getKey(qi.getKeyName(),qi.getKeyValue(), api.getClas());
				ec.put(key, result.getRows(),api.getTimeout());
			}
		}
	}
	
	@Override
	public boolean add(QueryInfo qi, PublicAPI<T, PK, DB> api, Result<T> result) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}


	@Override
	public void addCallback(QueryInfo qi, PublicAPI<T, PK, DB> api, Result<T> result) throws Exception {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean del(QueryInfo qi, PublicAPI<T, PK, DB> api, Result<List<T>> result) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}


	@Override
	public void delCallback(QueryInfo qi, PublicAPI<T, PK, DB> api, Result<List<T>> result) throws Exception {
		if(result.isSuccess()){
			//删除缓存 还处于事物范围内
			String id = (String) result.getIds();
			String[] ids = id.trim().split(",");
			for(int i=0;i<ids.length;i++){
				String key = getKey("id",ids[i], api.getClas());
				ec.remove(key,api.getTimeout());
			}
		}
	}


	@Override
	public boolean update(QueryInfo qi, PublicAPI<T, PK, DB> api, Result<T> result) throws Exception {
		
		return true;
	}


	@Override
	public void updateCallback(QueryInfo qi, PublicAPI<T, PK, DB> api, Result<T> result) throws Exception {
		if(result.isSuccess()){//清除缓存 不更新是因为没有去保证更新时提交数据的完整性 所以直接删除 
			String key = getKey("id",result.getRows().getId().toString(), api.getClas());
			ec.remove(key,api.getTimeout());
		}
	}


	@Override
	public boolean gets(QueryInfo qi, PublicAPI<T, PK, DB> api, Result<List<T>> result) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}


	@Override
	public void getsCallback(QueryInfo qi, PublicAPI<T, PK, DB> api, Result<List<T>> result) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
