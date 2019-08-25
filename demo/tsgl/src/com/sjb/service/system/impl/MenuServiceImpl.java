package com.sjb.service.system.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fxc.admin.jdbc.service.PublicServiceImpl;
import com.fxc.admin.jdbc.service.entity.ServiceSectionImpl;
import com.fxc.admin.jdbc.service.entity.ServiceSection;
import com.fxc.admin.jdbc.service.listener.serviceSection.AfterUpdateListener;
import com.fxc.admin.jdbc.service.listener.serviceSection.BeforeAddListener;
import com.fxc.admin.jdbc.service.listener.serviceSection.BeforeDelListener;
import com.fxc.entity.QueryInfo;
import com.fxc.entity.Result;
import com.fxc.utils.ContextUtils;
import com.fxc.utils.Sequence;
import com.sjb.dao.system.MenuDaoBean;
import com.sjb.model.system.Menu;
import com.sjb.model.system.Menu.MenuType;
import com.sjb.model.system.Permission;
import com.sjb.model.system.Role;
import com.sjb.service.system.MenuService;
import com.sjb.service.system.RolePermissionMiddleService;
import com.sjb.service.system.ehcache.MenuCache;
import com.sjb.util.Treat;
import com.sjb.util.session.SystemUtil;


@Service
//@Scope(value=WebApplicationContext.SCOPE_SESSION,proxyMode=ScopedProxyMode.INTERFACES)
public class MenuServiceImpl extends PublicServiceImpl<Menu,Long,MenuDaoBean> implements MenuService{

	
	@Autowired
	private RolePermissionMiddleService rolePermissionMiddleService;
	
	private final String ALLMENU = "ALLMENU";
	
	@Resource
	private MenuCache menuCache;
	
	@Resource
	private Sequence sequence;
	
	private ServiceSection<Menu> serviceAop ;//使用注入不方便阅读啊

	@Override
	public ServiceSection<Menu> getServiceAop(){
		 if(serviceAop == null){
			 serviceAop = new ServiceSectionImpl<Menu>();
			 /**
			  * 涉及排序添加之前需要把大于等当前顺序的加1
			  * 使用情况多可以封装一个bean 传入参数 注入使用
			  */
			 serviceAop.setBeforeAdd(new BeforeAddListener<Menu>() {
				 @Override
				 public boolean onAOP(Result<Menu> result, QueryInfo query) throws Exception {
					 sequence.setSequence(result.getRows().getPosition(), "system_menu", "position");
					 menuCache.remove(ALLMENU);//因为菜单缓存存的是整个集合 所以新增也要更新缓存
					 return true;
				 }
			 });
			 
			/**
			 * 删除之前
			 */
			serviceAop.setBeforeDel(new BeforeDelListener<List<Menu>>() {
				
				@Override
				public boolean onAOP(Result<List<Menu>> result, QueryInfo query) throws Exception {
					//清空菜单缓存
					menuCache.remove(ALLMENU);
					return true;
				}
			});
			
			/**
			 * 修改之后
			 */
			serviceAop.setAfterUpdate(new AfterUpdateListener<Menu>() {
				@Override
				public boolean onAOP(Result<Menu> result, QueryInfo query) throws Exception {
					//清空菜单缓存
					menuCache.remove(ALLMENU);
					return true;
				}
			});
		 }
		 return serviceAop;
	 }
	
	/**
	 * 获取用户所有角色所对应的菜单
	 * 到时候想想怎么优化
	 */
	@Override
	public List<Menu> getUserMenus() throws Exception {
		List<Menu> rulstMenus = new ArrayList<Menu>();//返回菜单集合
		HashMap<String,HashMap<String,Permission>> permissionXJ = new HashMap<String,HashMap<String,Permission>>();
		List<Role> roles = SystemUtil.getRole();
		List<Menu> listMenus = menuCache.get(ALLMENU);//菜单做缓存
		if(listMenus == null){
			listMenus = getDao().gets();
			menuCache.put(ALLMENU, listMenus);;
		}
		
		if(Treat.isAdmin()){//如果是系统管理员
			for(Menu m :listMenus){
				if(MenuType.PARENT.equals(m.getMenuType())){
					for(Menu child :listMenus){
						if(child.getStatus()==ContextUtils.XS && m.getId().equals(child.getParentId())){
							m.addChildMenu(child);
						}
					}
					rulstMenus.add(m);
				}
			}
		}else{
			if(roles!=null){
				for(Role role: roles){
					//从本地缓存获取角色对应的权限
					HashMap<String,Permission> permissions = rolePermissionMiddleService.getPermissions(role.getId());
					//将权限按相同的菜单合拢
					if(permissions!=null && permissions.size()>0){
						for(Map.Entry<String,Permission> per : permissions.entrySet()){
							if(per.getValue().getStatus()==ContextUtils.XS){
								String key = getMenuPermission(per.getValue().getPermission());
								if(permissionXJ.get(key)==null){
									permissionXJ.put(key, new HashMap<String,Permission>());
								}
								permissionXJ.get(key).put(per.getValue().getPermission(), per.getValue());
							}
						}
					}
				}
				for(Menu listMenu : listMenus){
					if(listMenu.getStatus()==ContextUtils.XS){
						//如果是子节点
						if(listMenu.getMenuType().equals(MenuType.CHILD)){
							//如果有权限添加
							String key = getMenuPermission(listMenu.getPermission());
							if(permissionXJ.get(key)!=null){
								listMenu.setListPermissions(permissionXJ.get(key));
							}else{
								//声明过滤节点
								listMenu.setFilter(true);
							}
						}else if(listMenu.getMenuType().equals(MenuType.PARENT)){
							//声明父节点
							listMenu.setFilter(true);
							rulstMenus.add(listMenu);
						}else{
							//声明过滤节点
							listMenu.setFilter(true);
						}
					}else{
						listMenu.setFilter(true);
					}
				}
				for(Menu rulstMenu : rulstMenus){
					for(Menu listMenu : listMenus){
						if(listMenu.isFilter()){
							continue;
						}
						//如果子菜单匹配到父菜单将子菜单添加到父菜单下
						if(listMenu.getParentId().intValue()==rulstMenu.getId().intValue()){
							rulstMenu.addChildMenu(listMenu);
							listMenu.setFilter(true);
						}
					}
				}
				//子菜单排序
				ArrayList<Menu> delMenu = new ArrayList<Menu>();
				for(Menu rulstMenu : rulstMenus){
					//如果父菜单空的删掉
					if(rulstMenu.getChildMenu()==null){
						delMenu.add(rulstMenu);
						continue;
					}
					Collections.sort(rulstMenu.getChildMenu(),new Menu());
				}
				if(delMenu.size()>0){
					for(int i =0 ;i<delMenu.size();i++){
						rulstMenus.remove(delMenu.get(i));
					}
				}
				//父菜单排序
				Collections.sort(rulstMenus,new Menu());
			}
		}
		return rulstMenus;
	}
	
	
	/**
	 * 权限匹配菜单 菜单权限统一为xxx:xxx:read
	 */
	public String getPermissionMenu(String permission){
		int len = permission.lastIndexOf(":");
		String perCode = permission.substring(0,len)+":read";
		return perCode;
	}
	
	/**
	 * 获取权限前缀
	 */
	public String getMenuPermission(String permission){
		int len = permission.lastIndexOf(":");
		String perCode = permission.substring(0,len);
		return perCode;
	}


}
