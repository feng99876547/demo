package com.fxc.admin.jdbc.service.jdbc;



import com.fxc.admin.jdbc.dao.PublicDaoImpl;
import com.fxc.admin.jdbc.service.PublicAPI;
import com.fxc.entity.IdEntity;
import com.fxc.entity.QueryInfo;
import com.fxc.entity.Result;

public class PublicServiceJdbcUpdate<T extends IdEntity<PK>,PK,DB extends PublicDaoImpl<T,PK>> implements JdbcUpdate<T,PK,DB>{

	public PublicServiceJdbcUpdate() {
	}

	@Override
	public Result<T> update(QueryInfo qi, PublicAPI<T,PK,DB> api,Result<T> result) throws Exception {
		
		if(api.getServiceAop() == null ? true : (api.getServiceAop().getBeforeUpdate() == null ? true :api.getServiceAop().getBeforeUpdate().onAOP(result,qi))){
			if(api.getDao().update(result.getRows())>0){
				result.setMsg("修改成功!");	
				if(api.getServiceAop()!=null && api.getServiceAop().getAfterUpdate()!=null){
					result.setSuccess(api.getServiceAop().getAfterUpdate().onAOP(result, qi));
				}else{
					result.setSuccess(true);
				}
			}else{
				result.setMsg("修改失败!");	
				result.setSuccess(false);
			}
			
		}else{
			result.setSuccess(false);
		}
		
		return result;
	}

}
