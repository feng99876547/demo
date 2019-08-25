package com.sjb.dao.system;

import com.fxc.admin.jdbc.dao.PublicDao;
import com.sjb.model.system.Role;

public interface RoleDaoBean extends PublicDao<Role,Long>{
	public Role getRole(Long id) throws Exception;
}
