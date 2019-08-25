package com.sjb.dao.system;

import com.fxc.admin.jdbc.dao.PublicDao;
import com.sjb.model.system.User;

public interface UserDaoBean extends PublicDao<User, Long>{
	
	public User getUser(String username) throws Exception;
	
}
