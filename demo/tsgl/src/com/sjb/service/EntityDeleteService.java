package com.sjb.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.fxc.admin.jdbc.dao.PublicDao;
import com.fxc.admin.jdbc.service.PublicAPI;
import com.fxc.admin.jdbc.service.jdbc.JdbcDel;
import com.fxc.admin.jdbc.util.SqlUtil;
import com.fxc.entity.IdEntity;
import com.fxc.entity.QueryInfo;
import com.fxc.entity.Result;
import com.fxc.utils.ContextUtils;
import com.sjb.model.EntityDeleteImpl;

/**
 * 实现EntityDeleteImpl的接口只修改状态 不真正删除
 * @author fxc
 * @param <T>
 * @param <PK>
 * @param <DB>
 */
@Component
@Qualifier("entityDeleteService")
public class EntityDeleteService <T extends IdEntity<PK>,PK,DB extends PublicDao<T,PK>> implements JdbcDel<T,PK,DB>{

	public EntityDeleteService() {
		// TODO Auto-generated constructor stub
	}
	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Result<List<T>> del(QueryInfo qi, PublicAPI<T, PK, DB> api, Result<List<T>> result) throws Exception {
		T obj = api.getClas().newInstance();
		if(obj instanceof  EntityDeleteImpl){
			String id = SqlUtil.getDelParamsId(qi,"删除时id不能为空");
			List<T> li = new ArrayList<T>();
			if(id != null){
				String[] ids = id.trim().split(",");
				((EntityDeleteImpl) obj).setDeletedState(ContextUtils.YSC);
				for(int i=0;i<ids.length;i++){
					//记录下删除的数据
					((EntityDeleteImpl) obj).setId(ids[i]);
					if(api.getDao().update(obj)==0){
						result.setMsg("删除"+ids[i]+"失败!");
						result.setSuccess(false);
						//业务回滚
						if(ids.length>0)
							throw new Exception(result.getMsg());
						return result;
					}
				}
				result.setIds(id);
				result.setRows(li);
				result.setSuccess(true);
				result.setMsg("删除成功!");
			}
			return result;
		}else{
			throw new Exception("实体没有实现EntityDeleteImpl接口");
		}
	}
}
