package com.sjb.service.xxgl.impl;

import org.springframework.stereotype.Service;

import com.fxc.admin.jdbc.service.PublicServiceImpl;
import com.fxc.admin.jdbc.service.entity.ServiceSection;
import com.sjb.dao.xxgl.FileManagementDaoBean;
import com.sjb.model.xxgl.FileManagement;
import com.sjb.service.xxgl.FileManagementService;

@Service
public class FileManagementServiceImpl extends PublicServiceImpl<FileManagement, Long, FileManagementDaoBean> implements FileManagementService{

	public FileManagementServiceImpl() {
	}

	@Override
	public ServiceSection<FileManagement> getServiceAop() throws Exception {
		return null;
	}

}
