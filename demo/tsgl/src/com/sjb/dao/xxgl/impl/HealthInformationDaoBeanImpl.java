package com.sjb.dao.xxgl.impl;

import org.springframework.stereotype.Repository;

import com.fxc.admin.jdbc.dao.PublicDaoImpl;
import com.sjb.dao.xxgl.HealthInformationDaoBean;
import com.sjb.model.xxgl.HealthInformation;

@Repository
public class HealthInformationDaoBeanImpl extends PublicDaoImpl<HealthInformation, Long> implements HealthInformationDaoBean{

	public HealthInformationDaoBeanImpl() {
		// TODO Auto-generated constructor stub
	}

}
