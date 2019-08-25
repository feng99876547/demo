package com.sjb.service.xxgl.impl;

import org.springframework.stereotype.Service;

import com.fxc.admin.jdbc.service.PublicServiceImpl;
import com.fxc.admin.jdbc.service.entity.ServiceSection;
import com.sjb.dao.xxgl.HealthInformationDaoBean;
import com.sjb.model.xxgl.HealthInformation;
import com.sjb.service.xxgl.HealthInformationService;

@Service
public class HealthInformationServiceImpl extends PublicServiceImpl<HealthInformation, Long, HealthInformationDaoBean> implements HealthInformationService{

	public HealthInformationServiceImpl() {
		super();
	}

	@Override
	public ServiceSection<HealthInformation> getServiceAop() throws Exception {
		return null;
	}

}
