package com.sjb.service.system;

import com.fxc.admin.jdbc.service.PublicService;
import com.sjb.dao.system.PermissionDaoBean;
import com.sjb.model.system.Permission;

public interface PermissionService extends PublicService<Permission, Long, PermissionDaoBean>{
	public  Permission getPermission(Long id) throws Exception;
}
