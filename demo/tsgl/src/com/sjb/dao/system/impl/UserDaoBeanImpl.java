package com.sjb.dao.system.impl;

import java.util.Date;
import java.util.HashMap;

import org.springframework.stereotype.Repository;

import com.fxc.admin.jdbc.dao.PublicDaoImpl;
import com.fxc.utils.ContextUtils;
import com.sjb.dao.system.UserDaoBean;
import com.sjb.model.system.User;


@Repository
public class UserDaoBeanImpl extends PublicDaoImpl<User, Long> implements UserDaoBean{
	
	public UserDaoBeanImpl(){
		super();
		super.defaultWhere = new HashMap<String,Object>();
		defaultWhere.put("THAN_createTime",new Date(100,1,1));//大于2000年的 year会加1900 是从1900开始算
		defaultWhere.put("EQ_deletedState", ContextUtils.WSC);//默认查状态未删除的
		defaultWhere.put("LESSTHAN_status", 10);//默认查新建和可用的
	}
	
	/**
	 * 根据username查找
	 */
	@Override
	public User getUser(String username) throws Exception{
		HashMap<String,Object> ma = new HashMap<String,Object>();
		ma.put("EQ_loginName", username);
		return super.get(ma);
	}
	
	
}
