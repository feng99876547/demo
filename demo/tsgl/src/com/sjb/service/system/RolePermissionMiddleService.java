package com.sjb.service.system;


import java.util.HashMap;
import java.util.List;

import com.fxc.admin.jdbc.service.PublicService;
import com.sjb.dao.system.RolePermissionMiddleDaoBean;
import com.sjb.model.system.Permission;
import com.sjb.model.system.RolePermissionMiddle;

public interface RolePermissionMiddleService extends PublicService<RolePermissionMiddle, Long, RolePermissionMiddleDaoBean>{

	public HashMap<String,Permission> getPermissions(Long RoleId) throws Exception;

	public List<Permission> getListPermissions(Long RoleId) throws Exception;
	
	public void removeRole(Long roleId) throws Exception;
	
	public void removeRole(String roleId) throws Exception;

}
