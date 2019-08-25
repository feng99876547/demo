package com.sjb.service.system.impl;

import org.springframework.stereotype.Service;

import com.fxc.admin.jdbc.service.PublicServiceImpl;
import com.fxc.admin.jdbc.service.entity.ServiceSection;
import com.sjb.dao.system.UserRoleMiddleDaoBean;
import com.sjb.model.system.UserRoleMiddle;
import com.sjb.service.system.UserRoleMiddleService;


@Service
public class UserRoleMiddleServiceImpl extends PublicServiceImpl<UserRoleMiddle, String, UserRoleMiddleDaoBean> 
implements UserRoleMiddleService{

	@Override
	public ServiceSection<UserRoleMiddle> getServiceAop() throws Exception {
		return null;
	}
	
}
