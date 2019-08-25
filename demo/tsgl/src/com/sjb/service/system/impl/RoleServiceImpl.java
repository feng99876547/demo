package com.sjb.service.system.impl;

import java.util.HashMap;
import java.util.List;




import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fxc.admin.jdbc.service.PublicServiceImpl;
import com.fxc.admin.jdbc.service.entity.ServiceSectionImpl;
import com.fxc.admin.jdbc.service.entity.ServiceSection;
import com.fxc.admin.jdbc.service.listener.serviceSection.AfertAddListener;
import com.fxc.admin.jdbc.service.listener.serviceSection.AfterUpdateListener;
import com.fxc.admin.jdbc.service.listener.serviceSection.BeforeDelListener;
import com.fxc.admin.jdbc.service.listener.serviceSection.BeforeGetsListener;
import com.fxc.admin.jdbc.service.listener.serviceSection.BeforeUpdateListener;
import com.fxc.entity.QueryInfo;
import com.fxc.entity.Result;
import com.fxc.utils.GetPara;
import com.sjb.dao.system.RoleDaoBean;
import com.sjb.model.system.Permission;
import com.sjb.model.system.Role;
import com.sjb.model.system.RolePermissionMiddle;
import com.sjb.service.system.PermissionService;
import com.sjb.service.system.RolePermissionMiddleService;
import com.sjb.service.system.RoleService;
import com.sjb.util.Treat;


@Service
public class RoleServiceImpl  extends PublicServiceImpl<Role,Long,RoleDaoBean>
implements RoleService{

	@Autowired
	private PermissionService permissionService;
	
	@Autowired
	private RolePermissionMiddleService rolePermissionMiddleService;
	
	private ServiceSection<Role> serviceAop ;
	
	@Override
	public ServiceSection<Role> getServiceAop(){
		if(serviceAop == null){
			serviceAop = new ServiceSectionImpl<Role>();
			/**
			 * 修改成功之前
			 */
			serviceAop.setBeforeUpdate(new BeforeUpdateListener<Role>(){
				@Override
				public boolean onAOP(Result<Role> result, QueryInfo query) throws Exception {
					//超级管理员
					if(Role.SUPER_ADMIN_ID.equals(result.getRows().getId())){
						return true;
					}else{
						//先删除全部
						HashMap<String,Object> ma = new HashMap<String,Object>();
						ma.put("EQ_role", result.getRows().getId());
						rolePermissionMiddleService.getDao().del(ma);
						
						//在添加
						String[] permissionIds = (String[]) query.getParams().get("ids[]");
						addPermission(permissionIds,result.getRows().getId());
					}
					return true;
				}
			});
			
			serviceAop.setAfterUpdate(new AfterUpdateListener<Role>() {
				
				@Override
				public boolean onAOP(Result<Role> result, QueryInfo query) throws Exception {
					//清空缓存
					rolePermissionMiddleService.removeRole(result.getRows().getId());
					return true;
				}
			});
			
			/**
			 * 获取角色之前
			 */
			serviceAop.setBeforeGets(new BeforeGetsListener<List<Role>>() {
				@Override
				public boolean onAOP(Result<List<Role>> result, QueryInfo query) throws Exception {
					//排除超级管理员
					query.getSearch().put("UNEQ_id", Role.SUPER_ADMIN_ID);
					return true;
				}
			});
			
			/**
			 * 角色添加成功之后添加权限
			 */
			serviceAop.setAfertAdd(new AfertAddListener<Role>() {
				@Override
				public boolean onAOP(Result<Role> result, QueryInfo query) throws Exception {
					//超级管理员不需要权限
					if(Role.SUPER_ADMIN_ID.equals(result.getRows().getId())){
						return true;
					}else{
						String[] permissionIds = (String[]) query.getParams().get("ids[]");
						addPermission(permissionIds,Long.parseLong(result.getIds().toString()));
					}
					return  true;
				}
			});
			
			/**
			 * 删除角色后删除缓存
			 */
			serviceAop.setBeforeDel(new BeforeDelListener<List<Role>>() {
				
				@Override
				public boolean onAOP(Result<List<Role>> result, QueryInfo query) throws Exception {
					String id = (String) result.getIds();
					String[] ids = id.trim().split(",");
					for(int i=0;i<ids.length;i++){
						//清空对应角色缓存
						rolePermissionMiddleService.removeRole(ids[i]);
					}
					return true;
				}
			});
		}
		return serviceAop;
	}
	
	/**
	 * 添加权限
	 * @throws Exception 
	 */
	public void addPermission(String[] permissionIds,Long roleId) throws Exception{
		if(permissionIds!=null){
			for (String permissionId : permissionIds) {
				RolePermissionMiddle permission = new RolePermissionMiddle(roleId,Long.parseLong(permissionId));
				rolePermissionMiddleService.getDao().add(permission);
			}
		}
	}
	
	/**
	 * 获取角色对应权限
	 */
	@Override
	public Result<List<Permission>> getRolePermission(Long roleId) throws Exception {
		List<Permission> list = rolePermissionMiddleService.getListPermissions(roleId);
		Result<List<Permission>> result = new Result<List<Permission>>();
		result.setSuccess(true);
		result.setRows(list);
		return result;
	}

	
	@Override
	public Result<List<Permission>> getChoosablePermission(HttpServletRequest request,Long roleId) throws Exception {
		HashMap<String,Object> search = GetPara.getSearch(GetPara.getParams(request));
		search.put("THAN_type", 0);//权限分配不暂时不分配系统权限
		List<Permission> pers = (List<Permission>) permissionService.getDao().gets(search);//所有权限
		List<Permission> getPer =null;//拥有的权限
		
		if(roleId != null){
			Role r = getDao().getRole(roleId);
			getPer = r.getPermissionList();
		}
		
		Result<List<Permission>> result = new Result<List<Permission>>();
		if(!Treat.isEmpty(getPer)){
			for(Permission per : getPer){
				if(per==null)
					continue;
				for(int i=0;i<pers.size();i++){
					if(pers.get(i).getId().equals(per.getId())){
						pers.remove(i);
						break;
					}
				}
			}
		}
		result.setSuccess(true);
		result.setRows(pers);
		return result;
	}


}
