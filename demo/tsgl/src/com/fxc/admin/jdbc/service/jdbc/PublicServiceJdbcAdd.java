package com.fxc.admin.jdbc.service.jdbc;


import com.fxc.admin.jdbc.dao.PublicDaoImpl;
import com.fxc.admin.jdbc.service.PublicAPI;
import com.fxc.entity.IdEntity;
import com.fxc.entity.QueryInfo;
import com.fxc.entity.Result;
import com.fxc.exception.MyRollBackException;

public class PublicServiceJdbcAdd<T extends IdEntity<PK>,PK,DB extends PublicDaoImpl<T,PK>> implements JdbcAdd<T,PK,DB>{

	public PublicServiceJdbcAdd() {
	}

	
	@Override
	public Result<T> add(QueryInfo qi,PublicAPI<T,PK,DB> api,Result<T> result) throws Exception {
		T obj = result.getRows();
		//到时候在看看 切面是否要往外调整
		if(api.getServiceAop() == null ? true : (api.getServiceAop().getBeforeAdd() == null ? true :api.getServiceAop().getBeforeAdd().onAOP(result,qi))){
			PK id = ((DB)api.getDao()).addAndId(result.getRows());
			result.setIds(id);
			obj.setId(id);
			if(api.getServiceAop() == null ? true : (api.getServiceAop().getAfertAdd() == null ? true :api.getServiceAop().getAfertAdd().onAOP(result,qi))){
				result.setMsg("新增成功!");
				result.setSuccess(true);
			}else{
				//业务回滚
				result.setSuccess(false);
				throw new MyRollBackException(result.getMsg());
			}
		}else{
			result.setSuccess(false);
		}
		return result;
	}
	
}
