package com.sjb.dao.system.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.fxc.admin.jdbc.dao.PublicDaoImpl;
import com.fxc.entity.QueryInfo;
import com.fxc.utils.ContextUtils;
import com.sjb.dao.system.RoleDaoBean;
import com.sjb.model.system.Permission;
import com.sjb.model.system.Role;
import com.sjb.util.Treat;

@Repository
public class RoleDaoBeanImpl extends PublicDaoImpl<Role, Long> implements RoleDaoBean{
	
	@Resource
	private MenuDaoBeanImpl menuDaoBeanImpl;
	
	
	private static HashMap<String,Permission> permissions;
	
//	private static List<Role> roles ;
//	
//	private static HashMap<String,Menu> menus;
	
	
	/**
	 * 获取角色 没做缓存 值不要被修改 注意
	 * @return
	 * @throws Exception 
	 */
	public Role getRole(Long id) throws Exception{
//		if(roles == null){
//			roles = initRoles();
//		}
		// 保证安全先 到时候再做缓存
		List<Role> roles = initRoles();
		for(Role role:roles){
			if(role.getId().equals(id)){
				return role;
			}
		}
		return null;
	}
	
	/**
	 * 获取权限  没做缓存值不要被修改 注意
	 * @param key 为角色id加权限permission
	 * @return
	 * @throws Exception 
	 */
	public Permission getPermission(String key) throws Exception{
		//先用变量代替到时候做缓存
		if(permissions == null)
			initRoles();
		return permissions.get(key);
	}
	
	/**
	 * 验证权限是否  生效 false
	 */
	public static Boolean isPermission(Permission per){
		//状态显示 系统权限
		if(per.getStatus()==ContextUtils.XS && per.getType() == 0){
			return false;
		}
		return true;
	}
	
	
	
	/**
	 * 初始化所有系统权限
	 * @return
	 * @throws Exception
	 * 
	 */
	public List<Role> initRoles() throws Exception {
		QueryInfo qi = new QueryInfo();
		//关联中间表
		qi.addCancelNotSelect("Permission.permissionList");
		List<Role> li = getGetsJdbc().executeGets(qi,this.c);
		//所有角色权限集合 按角色id+角色的permission做key
		permissions = new HashMap<String,Permission>();
		if(!Treat.isEmpty(li)){
			//初始化所有角色权限 和角色菜单
			for(Role role : li){
				if(!Treat.isEmpty(role.getPermissionList())){
					//角色的菜单集合 多角色先集成权限在生成菜单
				    HashMap<String,Permission> menuPers = new HashMap<String,Permission>();
				    if(!Treat.isEmpty(role.getPermissionList())){
				    	for(Permission per : role.getPermissionList()){
				    		if(per == null || isPermission(per))
								continue;
							menuPers.put(per.getPermission(), per);
							//菜单的  角色id加permission 做key
							String perCode = role.getId()+per.getPermission();
							permissions.put(perCode,per);
						}
				    	role.setMaPermission(menuPers);
				    }
				}
			}
		}
		return  li;
	}
}
