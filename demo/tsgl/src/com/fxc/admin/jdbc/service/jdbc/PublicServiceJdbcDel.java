package com.fxc.admin.jdbc.service.jdbc;

import java.util.List;


import com.fxc.admin.jdbc.dao.PublicDaoImpl;
import com.fxc.admin.jdbc.service.PublicAPI;
import com.fxc.admin.jdbc.util.SqlUtil;
import com.fxc.entity.IdEntity;
import com.fxc.entity.QueryInfo;
import com.fxc.entity.Result;
import com.fxc.exception.MyRollBackException;

public class PublicServiceJdbcDel<T extends IdEntity<PK>,PK,DB extends PublicDaoImpl<T,PK>> implements JdbcDel<T,PK,DB>{

	public PublicServiceJdbcDel() {
	}


	/**
	 * 这个删除方法 条件只会使用到id 需要其他条件请调用带map参数的删除方法
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Result<List<T>> del(QueryInfo qi, PublicAPI<T,PK,DB> api,Result<List<T>> result) throws Exception {
		String id = SqlUtil.getDelParamsId(qi,"删除时id不能为空");
		if(id != null){
			result.setIds(id);
			boolean  b = true;
			if(api.getServiceAop() != null && api.getServiceAop().getBeforeDel()!=null){
				b = api.getServiceAop().getBeforeDel().onAOP(result, qi);
			}
			if(b){
				String[] ids = id.trim().split(",");
				int count = 0;
				if(id.length()==1){
					count = api.getDao().del((PK) ids[0].trim());
				}else{
					count = api.getDao().dels(id);
				}
				if(count == 0){//如果想要捕获这个异常可以为这个异常添加一个标识
					throw new MyRollBackException("在"+api.getClas()+result.getMsg());
				}
				result.setMsg("删除成功!");//如果BeforeDel方法中验证失败 需要更新msg 所以这边先设置成功
				result.setSuccess(true);
			}else{
				result.setSuccess(false);
			}
		}
		return result;
	}

}
