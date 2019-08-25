package com.sjb.service.xxgl;

import com.fxc.admin.jdbc.service.PublicService;
import com.fxc.entity.Result;
import com.sjb.dao.xxgl.DormitoryDaoBean;
import com.sjb.model.xxgl.Dormitory;

public interface DormitoryService extends PublicService<Dormitory, Integer, DormitoryDaoBean>{

	public Result<Dormitory> updateRSZ(Integer id)throws Exception;
	
	public Result<Dormitory> updateRSJ(Integer id)throws Exception;

}
