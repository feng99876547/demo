package com.sjb.service.xxgl.impl;

import org.springframework.stereotype.Service;

import com.fxc.admin.jdbc.service.PublicServiceImpl;
import com.fxc.admin.jdbc.service.entity.ServiceSection;
import com.sjb.dao.xxgl.ReportDaobean;
import com.sjb.model.xxgl.Report;
import com.sjb.service.xxgl.ReportService;

@Service
public class ReportServiceImpl extends PublicServiceImpl<Report, Long, ReportDaobean> implements ReportService{

	public ReportServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public ServiceSection<Report> getServiceAop() throws Exception {
		return null;
	}

}
