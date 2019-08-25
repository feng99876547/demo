package com.sjb.service.tsgl;


import com.fxc.admin.jdbc.service.PublicService;
import com.fxc.entity.QueryInfo;
import com.fxc.entity.Result;
import com.sjb.dao.tsgl.ThemeEditDaoBean;
import com.sjb.model.tsgl.ThemeEdit;

public interface ThemeEditService extends PublicService<ThemeEdit,Long,ThemeEditDaoBean>{

	public Result<ThemeEdit> publish(QueryInfo qi) throws Exception;

}
