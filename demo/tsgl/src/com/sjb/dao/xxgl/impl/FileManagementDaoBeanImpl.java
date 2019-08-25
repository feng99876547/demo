package com.sjb.dao.xxgl.impl;

import org.springframework.stereotype.Repository;

import com.fxc.admin.jdbc.dao.PublicDaoImpl;
import com.sjb.dao.xxgl.FileManagementDaoBean;
import com.sjb.model.xxgl.FileManagement;

@Repository
public class FileManagementDaoBeanImpl extends PublicDaoImpl<FileManagement, Long> implements FileManagementDaoBean{

	public FileManagementDaoBeanImpl() {
	}

}
