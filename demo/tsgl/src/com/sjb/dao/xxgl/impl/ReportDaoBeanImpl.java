package com.sjb.dao.xxgl.impl;

import org.springframework.stereotype.Repository;

import com.fxc.admin.jdbc.dao.PublicDaoImpl;
import com.sjb.dao.xxgl.ReportDaobean;
import com.sjb.model.xxgl.Report;

@Repository
public class ReportDaoBeanImpl extends PublicDaoImpl<Report, Long> implements  ReportDaobean{

	public ReportDaoBeanImpl() {
	}

}
