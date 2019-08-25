package com.sjb.service.system;

import java.util.List;

import com.fxc.admin.jdbc.service.PublicService;
import com.sjb.dao.system.MenuDaoBean;
import com.sjb.model.system.Menu;


public interface MenuService extends PublicService<Menu, Long, MenuDaoBean>{
	
	public List<Menu> getUserMenus() throws Exception;
	
//	public List<Menu> getMenus() throws Exception;
}
