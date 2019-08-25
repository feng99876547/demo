package com.sjb.service.system;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.fxc.admin.jdbc.service.PublicService;
import com.fxc.entity.Result;
import com.sjb.dao.system.RoleDaoBean;
import com.sjb.model.system.Permission;
import com.sjb.model.system.Role;


public interface RoleService extends PublicService<Role, Long,RoleDaoBean>{
	
	public Result<List<Permission>> getRolePermission(Long roleId) throws Exception;
	
	public Result<List<Permission>> getChoosablePermission(HttpServletRequest request,Long roleId) throws Exception;
	
}
