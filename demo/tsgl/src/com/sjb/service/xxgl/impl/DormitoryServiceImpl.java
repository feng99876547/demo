package com.sjb.service.xxgl.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fxc.admin.jdbc.service.PublicServiceImpl;
import com.fxc.admin.jdbc.service.entity.ServiceSection;
import com.fxc.entity.Result;
import com.fxc.exception.MyRollBackException;
import com.fxc.utils.Sequence;
import com.sjb.dao.xxgl.DormitoryDaoBean;
import com.sjb.model.xxgl.Dormitory;
import com.sjb.service.xxgl.DormitoryService;

@Service
public class DormitoryServiceImpl extends PublicServiceImpl<Dormitory,Integer,DormitoryDaoBean> implements DormitoryService{

	public DormitoryServiceImpl() {
	}

	@Resource
	private Sequence sequence;
	
	@Override
	public ServiceSection<Dormitory> getServiceAop() throws Exception {
		return null;
	}


	@Override
	public Result<Dormitory> updateRSZ(Integer id) throws Exception {
		Result<Dormitory> result = new Result<Dormitory>(true);
		int count = sequence.exSql("update xxgl_dormitory set zdrs = zdrs+1,syrs=syrs+1 where id = ?",id);
		if(count == 0){
			throw new MyRollBackException("修改总人数失败");
		}else{
			result.setMsg("修改成功");
		}
		return result;
	}

	@Override
	public Result<Dormitory> updateRSJ(Integer id) throws Exception {
		Result<Dormitory> result = new Result<Dormitory>(true);
		int count = sequence.exSql("update xxgl_dormitory set zdrs = zdrs-1,syrs=syrs-1 where id = ? and syrs>0",id);
		if(count == 0){
			throw new MyRollBackException("当前宿舍已住满请先移出一个学生到其它空宿舍");
		}else{
			result.setMsg("修改成功");
		}
		return result;
	}

}
