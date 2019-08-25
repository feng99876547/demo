package com.sjb.model.system;

import javax.persistence.Table;

import com.fxc.admin.jdbc.annotation.OTO;
import com.fxc.entity.IdEntity;


/**
 * 角色和权限的中间表
 */
@Table(name="role_permission_middle")
public class RolePermissionMiddle extends IdEntity<Long>{

	private static final long serialVersionUID = 3643015765766643238L;

	@OTO(assField="role")
	private Role role;
	
	@OTO(assField="permission")
	private Permission permission;

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Permission getPermission() {
		return permission;
	}

	public void setPermission(Permission permission) {
		this.permission = permission;
	}

	public RolePermissionMiddle() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RolePermissionMiddle(Long roleId, Long parseLong) {
		Permission p = new Permission();
		Role r = new Role();
		p.setId(parseLong);
		r.setId(roleId);
		this.permission = p;
		this.role = r;
	}
	
}
