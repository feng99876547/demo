package com.sjb.service.xxgl.impl;

import org.springframework.stereotype.Repository;

import com.fxc.admin.jdbc.service.PublicServiceImpl;
import com.fxc.admin.jdbc.service.entity.ServiceSection;
import com.sjb.dao.xxgl.SchoolCardDaoBean;
import com.sjb.model.xxgl.SchoolCard;
import com.sjb.service.xxgl.SchoolCardService;

@Repository
public class SchoolCardServiceImpl extends PublicServiceImpl<SchoolCard, Long, SchoolCardDaoBean> implements SchoolCardService{

	public SchoolCardServiceImpl() {
	}

	@Override
	public ServiceSection<SchoolCard> getServiceAop() throws Exception {
		return null;
	}

}
