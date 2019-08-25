package com.sjb.model.system;

import javax.persistence.Table;

import com.fxc.admin.jdbc.annotation.OTO;
import com.fxc.entity.IdEntity;


/**
 * 用户与角色的中间表
 */
@Table(name ="system_user_role_middle")
public class UserRoleMiddle extends IdEntity<String>{

	private static final long serialVersionUID = -3855825309605458391L;

	@OTO(assField="userId", autoLoading=true)
	private User userId;
	
	@OTO(assField="roleId",autoLoading=true)
	private Role roleId;

	
	public User getUserId() {
		return userId;
	}

	public void setUserId(User userId) {
		this.userId = userId;
	}

	public Role getRoleId() {
		return roleId;
	}

	public void setRoleId(Role roleId) {
		this.roleId = roleId;
	}

	public UserRoleMiddle(Long role, Long user) {
		super();
		this.userId = new User();
		this.roleId = new Role();
		this.userId.setId(user);
		this.roleId.setId(role);
	}

	public UserRoleMiddle() {
		super();
	}
	
	
};
