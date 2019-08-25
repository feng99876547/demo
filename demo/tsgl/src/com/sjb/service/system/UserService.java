package com.sjb.service.system;

import javax.servlet.http.HttpServletRequest;

import com.fxc.admin.jdbc.service.PublicService;
import com.fxc.entity.QueryInfo;
import com.fxc.entity.Result;
import com.sjb.dao.system.UserDaoBean;

import com.sjb.model.system.User;

public interface UserService  extends PublicService<User,Long,UserDaoBean>{

	public Result<User> login(QueryInfo qi,HttpServletRequest request) throws Exception;

	public Result<?> updatePassword(String loginName,String old, String pas) throws Exception;

}
