package com.fxc.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.fxc.admin.jdbc.service.DispatchTask;
import com.fxc.admin.jdbc.service.PublicAPI;
import com.fxc.entity.QueryInfo;
import com.fxc.entity.Result;
import com.fxc.utils.GetPara;
import com.sjb.service.cache.EhCachePublicServiceJdbcGetTest;

/**
 * 负责业务链调度
 * @author fxc
 *
 * @param <T>
 * @param <PK>
 * @param <DB>
 */
@Component
@Qualifier("businessDispatchImpl")
@SuppressWarnings({"unchecked","rawtypes"})
public class BusinessDispatchImpl<T,PK,DB> implements BusinessDispatch<T,PK,DB>{
	
	public final static Map<String,Busines> map = new HashMap<String,Busines>();

	//初始化业务链
	public BusinessDispatchImpl() {
//		map.put(DispatchTask.CACHE.getKey(), (Busines) ContextUtils.appContext.getBean("ehCachePublicServiceJdbcGetTest"));
//		map.put(DispatchTask.BUSINES.getKey(), (Busines) ContextUtils.appContext.getBean("businesImpl"));
	}
	static{
		map.put(DispatchTask.CACHE.getKey(),new EhCachePublicServiceJdbcGetTest());
		BusinesImpl bus =new BusinesImpl();
		bus.setBusinesProcessor(new BusinesProcessorImpl());
		map.put(DispatchTask.BUSINES.getKey(),bus );
	}
	
	@Override
	public Result<T> AddDispatchTask(ArrayList<DispatchTask> list,QueryInfo qi,PublicAPI<T,PK,DB> api) throws Exception {
		T obj = GetPara.load(api.getClas(),qi.getParams());
		Result<T> result = new Result<T>(obj,false);
		if(list.size()>0){
			int len = list.size();
			for(int i=0;i<len;i++){
				Busines<T,PK,DB> bus = (Busines<T,PK,DB>) map.get(list.get(i).getKey());
				bus.add(qi,api,result);
			}
			api.getJcbcAdd().add(qi,api,result);
			for(int i =(len-1);i>=0;i--){
				Busines<T,PK,DB> bus = (Busines<T,PK,DB>) map.get(list.get(i).getKey());
				bus.addCallback(qi,api,result);
			}
		}else{
			api.getJcbcAdd().add(qi,api,result);
		}
		return result;
	}


	@Override
	public Result<List<T>> delDispatchTask(ArrayList<DispatchTask> list, QueryInfo qi, PublicAPI<T, PK, DB> api)
			throws Exception {
		Result<List<T>> result = new Result<List<T>>(false);
		if(list.size()>0){
			int len = list.size();
			for(int i=0;i<len;i++){
				Busines<T,PK,DB> bus = (Busines<T,PK,DB>) map.get(list.get(i).getKey());
				bus.del(qi,api,result);
			}
			api.getJcbcDel().del(qi,api,result);
			for(int i =(len-1);i>=0;i--){
				Busines<T,PK,DB> bus = (Busines<T,PK,DB>) map.get(list.get(i).getKey());
				bus.delCallback(qi,api,result);
			}
		}else{
			api.getJcbcDel().del(qi,api,result);
		}
		return result;
	}


	@Override
	public Result<T> updateDispatchTask(ArrayList<DispatchTask> list, QueryInfo qi, PublicAPI<T, PK, DB> api)
			throws Exception {
		T obj = GetPara.load(api.getClas(),qi.getParams());
		Result<T> result = new Result<T>(obj,false);
		if(list.size()>0){
			int len = list.size();
			for(int i=0;i<len;i++){
				Busines<T,PK,DB> bus = (Busines<T,PK,DB>) map.get(list.get(i).getKey());
				bus.update(qi,api,result);
			}
			api.getJcbcUpdate().update(qi,api,result);
			for(int i =(len-1);i>=0;i--){
				Busines<T,PK,DB> bus = (Busines<T,PK,DB>) map.get(list.get(i).getKey());
				bus.updateCallback(qi,api,result);
			}
		}else{
			api.getJcbcUpdate().update(qi,api,result);
		}
		return result;
	}

	//或是在配个模板继承当前模板重写该方法 把同步移到这边
	@Override
	public Result<T> getDispatchTask(ArrayList<DispatchTask> list, QueryInfo qi, PublicAPI<T, PK, DB> api)
			throws Exception {
		Result<T> result = new Result<T>(false);
		if(list.size()>0){//因为用到缓存需要加锁
			Busines<T,PK,DB> bus = (Busines<T,PK,DB>) map.get(list.get(0).getKey());
			bus.get(qi,api,result,list,0);
		}else{
			api.getJcbcGet().freeGet(qi,api,result);
		}
		return result;
	}

	@Override
	public Result<List<T>> getsDispatchTask(ArrayList<DispatchTask> list, QueryInfo qi, PublicAPI<T, PK, DB> api)
			throws Exception {
		Result<List<T>> result = new Result<List<T>>(false);
		if(list.size()>0){
			int len = list.size();
			for(int i=0;i<len;i++){
				Busines<T,PK,DB> bus = (Busines<T,PK,DB>) map.get(list.get(i).getKey());
				bus.gets(qi,api,result);
			}
			api.getJcbcGet().freeGets(qi,api,result);
			for(int i =(len-1);i>=0;i--){
				Busines<T,PK,DB> bus = (Busines<T,PK,DB>) map.get(list.get(i).getKey());
				bus.getsCallback(qi,api,result);
			}
		}else{
			api.getJcbcGet().freeGets(qi,api,result);
		}
		return result;
	}


}
