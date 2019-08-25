package com.sjb.service.xxgl.impl;

import org.springframework.stereotype.Service;

import com.fxc.admin.jdbc.service.PublicServiceImpl;
import com.fxc.admin.jdbc.service.entity.ServiceSection;
import com.sjb.dao.xxgl.ContributionsDaoBean;
import com.sjb.model.xxgl.Contributions;
import com.sjb.service.xxgl.ContributionsService;

@Service
public class ContributionsServiceImpl extends PublicServiceImpl<Contributions, Long, ContributionsDaoBean> implements ContributionsService{

	public ContributionsServiceImpl() {
	}

	@Override
	public ServiceSection<Contributions> getServiceAop() throws Exception {
		return null;
	}

}
