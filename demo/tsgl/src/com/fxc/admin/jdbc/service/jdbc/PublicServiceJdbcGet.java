package com.fxc.admin.jdbc.service.jdbc;

import java.util.List;


import com.fxc.admin.jdbc.dao.PublicDaoImpl;
import com.fxc.admin.jdbc.service.PublicAPI;
import com.fxc.entity.IdEntity;
import com.fxc.entity.QueryInfo;
import com.fxc.entity.Result;

public class PublicServiceJdbcGet<T extends IdEntity<PK>,PK,DB extends PublicDaoImpl<T,PK>> implements JdbcGet<T,PK,DB>{

	public PublicServiceJdbcGet() {
	}

	@Override
	public T get(PK id,PublicAPI<T,PK,DB> api) throws Exception {
		return  api.getDao().get(id);
	}

	@Override
	public Result<T> freeGet(QueryInfo qi, PublicAPI<T,PK,DB> api,Result<T> result) throws Exception {
		if(api.getServiceAop() == null ? true : (api.getServiceAop().getBeforeGet() == null ? true :api.getServiceAop().getBeforeGet().onAOP(result,qi))){
			result.setRows(api.getDao().get(qi));
			if(result.getRows()!=null){
				result.setSuccess(true);
				if(api.getServiceAop() != null && api.getServiceAop().getAfertGet()!= null){
					api.getServiceAop().getAfertGet().onAOP(result, qi);
				}
			}else{
				result.setSuccess(false);
				result.setMsg("没有查到数据");
			}
		}else{
			result.setSuccess(false);
		}
		return result;
	}

	@Override
	public Result<List<T>> freeGets(QueryInfo qi, PublicAPI<T,PK,DB> api,Result<List<T>> result) throws Exception {
		if(api.getServiceAop() == null ? true : (api.getServiceAop().getBeforeGets() == null ? true :api.getServiceAop().getBeforeGets().onAOP(result,qi))){
			result.setRows(api.getDao().gets(qi));
			result.setTotal(((DB)api.getDao()).getCount(qi == null ? null:qi.getSearch()));
			result.setSuccess(true);
		}else{
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 不带条件差全表
	 */
	@Override
	public Result<List<T>> gets(PublicAPI<T,PK,DB> api) throws Exception {
		Result<List<T>> result = new Result<List<T>>();
		result.setRows(api.getDao().gets());
		return result;
	}
}
