package com.init;

import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


import com.fxc.utils.ContextUtils;
import com.sjb.model.system.Permission;
import com.sjb.service.system.PermissionService;
import com.sjb.service.system.ehcache.PermissionsCache;
import com.sjb.util.Treat;



public class initServlet implements ServletContextListener{

	public initServlet(){
		super();
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		System.out.println("----------销毁了Context created on----------");
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("----------------------------初始化服务器参数--------------------------------");
		PermissionService permissionService = (PermissionService) ContextUtils.appContext.getBean("permissionServiceImpl");
		PermissionsCache permissionsCache =  (PermissionsCache) ContextUtils.appContext.getBean("permissionsCache");
		try {
			//初始化所有权限缓存
			List<Permission> list = permissionService.getDao().gets();
			if(!Treat.isEmpty(list)){
				for(Permission p : list){
					permissionsCache.put(p.getId().toString(), p);
				}
			}
//			MyUtils.da.set(2019, 6, 6);
//			Thread t = new Thread(new MyUtils.go());
//			t.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
