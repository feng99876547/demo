package com.sjb.service.system.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.fxc.admin.jdbc.service.PublicServiceImpl;
import com.fxc.admin.jdbc.service.entity.ServiceSection;
import com.fxc.admin.jdbc.service.jdbc.JdbcDel;
import com.sjb.dao.system.DictionaryDaoBean;
import com.sjb.model.system.Dictionary;
import com.sjb.service.EntityDeleteService;
import com.sjb.service.system.DictionaryService;

@Service
public class DictionaryServiceImpl extends PublicServiceImpl<Dictionary,Integer,DictionaryDaoBean> implements DictionaryService{

	public DictionaryServiceImpl() {
	}

	@Override
	public ServiceSection<Dictionary> getServiceAop() throws Exception {
		return null;
	}
	
	@Autowired
	@Qualifier("entityDeleteService")
	protected EntityDeleteService<Dictionary,Integer,DictionaryDaoBean> entityDeleteService;

	
	/**
	 * 使用自定义删除替换默认删除
	 */
	@Override
	public JdbcDel<Dictionary,Integer,DictionaryDaoBean> getJcbcDel(){
		return entityDeleteService;
	}

}
