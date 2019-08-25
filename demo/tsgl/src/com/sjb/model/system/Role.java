package com.sjb.model.system;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Table;
import javax.persistence.Transient;


import com.alibaba.fastjson.annotation.JSONField;
import com.fxc.admin.jdbc.annotation.Custom;
import com.fxc.admin.jdbc.annotation.MTM;
import com.fxc.entity.IdEntity;

/**
 * 角色.
 * 
 * @author fxc
 */
@Table(name = "system_role")
public class Role extends IdEntity<Long> {
	
	private static final long serialVersionUID = 2273547548165869031L;
	/**
	 * 超级管理员(*.*)数据库id
	 */
//	@Custom(Transient=true)
	public static Long SUPER_ADMIN_ID = 1L;
	
	/** 角色名称：通常为中文，用于用户识别 */
	private String name;
	
	/** 角色字符：必须为英文，用于系统识别 
	 * car4s 系统权限
	 * TC 套餐权限
	 * */
	@Custom(DefaultStringValue="car4s")
	private String role;
	
	/** 角色描述 */
	private String description;

	@Transient
	private List<Menu> menuList; // 有序的关联对象集合
	
	/** 角色对应的所有权限*/
	@Transient
	private HashMap<String,Permission> maPermission;
	
	/** 角色对应的权限*/
	@MTM(assField = "role" , pointAssField = "permission", middleTable ="role_permission_middle")
	private List<Permission> permissionList;

	public List<Permission> getPermissionList() {
		return permissionList;
	}
	public void setPermissionList(List<Permission> permissionList) {
		this.permissionList = permissionList;
	}
	public Role(){
		
	}
	public Role(Long id){
		super.setId(id);
	}
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@JSONField(serialize=false)
	public List<Menu> getMenuList() {
		return menuList;
	}
	public void setMenuList(List<Menu> menuList) {
		this.menuList = menuList;
	}
	
	public HashMap<String, Permission> getMaPermission() {
		return maPermission;
	}
	public void setMaPermission(HashMap<String, Permission> maPermission) {
		this.maPermission = maPermission;
	}
	
	@Override
	public String toString() {
		return "Role [role=" + role + ", name=" + name + ", description=" + description + ", permissionList="
				+ menuList + "]";
	}
//	public List<Permission> getPermissionList() {
//		return permissionList;
//	}
//	public void setPermissionList(List<Permission> permissionList) {
//		this.permissionList = permissionList;
//	}
}
