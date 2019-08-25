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
import com.fxc.admin.jdbc.service.entity.ServiceSection;
import com.fxc.entity.QueryInfo;
import com.sjb.dao.system.RolePermissionMiddleDaoBean;
import com.sjb.model.system.Permission;
import com.sjb.model.system.RolePermissionMiddle;
import com.sjb.service.system.PermissionService;
import com.sjb.service.system.RolePermissionMiddleService;
import com.sjb.service.system.ehcache.RoleCache;


@Service
public class RolePermissionMiddleServiceImpl extends PublicServiceImpl<RolePermissionMiddle, Long, RolePermissionMiddleDaoBean>
implements RolePermissionMiddleService{

	@Resource
	private RoleCache roleCache;
	
//	private Object roleCacheLock = "lock";
	
	private Object roleCacheLock = new Object();
	
	@Autowired
	private PermissionService PermissionServiceImpl;
	
	@Override
	public ServiceSection<RolePermissionMiddle> getServiceAop() throws Exception {
		return null;
	}

	
	@Override
	public List<Permission> getListPermissions(Long roleId) throws Exception {
		HashMap<String,Permission> ma = getPermissions(roleId);
		List<Permission> li = null;
		if(ma!=null){
			li = new ArrayList<Permission>();
			for(Map.Entry<String, Permission> per : ma.entrySet()){
				li.add(per.getValue());
			}
			Collections.sort(li,new Permission());
		}
		return li;
	}
	
	/**
	 * 根据角色id从缓存获取角色对应的权限集合
	 */
	@Override
	public HashMap<String,Permission> getPermissions(Long roleId) throws Exception {
		String key = roleId.toString();
		HashMap<String, Permission> permissions = roleCache.get(key);
		if(permissions != null){
			return permissions;
		}else{
			synchronized (roleCacheLock) {
				permissions = roleCache.get(key);
				if(permissions != null){
					return permissions;
				}
				permissions = new HashMap<String,Permission>();
				QueryInfo qi = new QueryInfo();
				qi.getSearch().put("EQ_role", roleId);
				List<RolePermissionMiddle> rolePermissionMiddle = getDao().gets(qi);
				
				if(rolePermissionMiddle!=null){
					for(RolePermissionMiddle rpm : rolePermissionMiddle){
						Long perId = rpm.getPermission().getId();
						Permission p = PermissionServiceImpl.getPermission(perId);
						if(p!=null){
							permissions.put(p.getPermission(),p);
						}else{//如果通过权限id查找不到权限 说明权限已经被删除 清掉旧的数据
							getDao().del(rpm.getId());
						}
					}
					roleCache.put(key, permissions);
				}
				return permissions.size() == 0 ? null: permissions;
			}
		}
	}
	
	/**
	 * 角色权限跟新时清除旧缓存
	 */
	@Override
	public void removeRole(Long roleId) throws Exception {
		roleCache.remove(roleId.toString());
	}

	@Override
	public void removeRole(String roleId) throws Exception {
		roleCache.remove(roleId);
	}
	
}
