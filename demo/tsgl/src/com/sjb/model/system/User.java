package com.sjb.model.system;

import java.util.Date;
import java.util.List;

import javax.persistence.Table;

import com.fxc.admin.jdbc.annotation.Custom;
import com.fxc.admin.jdbc.annotation.MTM;
import com.fxc.admin.jdbc.annotation.OTO;
import com.fxc.entity.IdEntity;
import com.fxc.utils.ContextUtils;
import com.sjb.model.EntityDeleteImpl;
import com.sjb.model.UserKey;

/**
 * 系统用户表
 */
@Table(name ="system_user")
public class User extends IdEntity<Long> implements EntityDeleteImpl<Long>{
	
	private static final long serialVersionUID = -2288326354146811386L;

	/** 账号 */
	@Custom(name="账号",isUnique=true,NotNull=true)
	private String loginName;
	
	/** 密文密码 */
	@Custom(NotSelect="password",NotNull=true,DefaultStringValue="123456")
	private String password;

	/** 名字*/
	private String name;
	
	/** 学号*/
	private String number;
	
	/** 头像*/
	private String imgUrl;
	
	/** 学院*/
	private String faculty;
	
	/** 电话*/
	private String cellPhone;
	
	/** 性别 1男 2女*/
	private Integer xb;
	
	/** 地址*/
	private String dz;
	
	/** 政治面貌*/
	private String zzmm;
	
	/** 身份证*/
	private String sfz;
	
	/** 专业*/
	private String zy;
	
	/** 宿舍*/
	private Integer dormitoryId;

	@MTM(assField="userId",pointAssField="roleId",middleTable="system_user_role_middle")
	private List<Role> roleList; 
	
	/** 创建时间 */
	@Custom(createInjectUser=UserKey.newDate)
	private Date createTime;
	
	/** 创建人id */
	@Custom(createInjectUser = UserKey.systemUser)
	@OTO(assField="createUser",autoLoading=false)//默认不关联只返回pointAssField的key自段
	private User createUser;
	
	@Custom(isUpdateUser=UserKey.systemUser)
	@OTO(assField="updateUser",autoLoading=false)
	private User updateUser;//修改人id
	
	@Custom(isUpdateUser=UserKey.newDate)
	private Date updateDate;//修改时间

	private String remarks;//备注
	
	/** 用户状态: 0 新建  5可用 10不可用 */
	@Custom(DefaultIntValue = 0)//添加时没设置值 默认值为1不可用
	private Integer  status;
	
	/**删除状态: 0 删除  1未删除*/
	@Custom(DefaultIntValue = ContextUtils.WSC)
	private Integer deletedState;
	
//	@Transient
//	private String ip;
	
	public User(Long id) {
		super();
		setId(id);
	}

	/**
	 * @return the xb
	 */
	public Integer getXb() {
		return xb;
	}

	/**
	 * @param xb the xb to set
	 */
	public void setXb(Integer xb) {
		this.xb = xb;
	}

	/**
	 * @return the dz
	 */
	public String getDz() {
		return dz;
	}

	/**
	 * @param dz the dz to set
	 */
	public void setDz(String dz) {
		this.dz = dz;
	}

	/**
	 * @return the zzmm
	 */
	public String getZzmm() {
		return zzmm;
	}

	/**
	 * @param zzmm the zzmm to set
	 */
	public void setZzmm(String zzmm) {
		this.zzmm = zzmm;
	}

	/**
	 * @return the sfz
	 */
	public String getSfz() {
		return sfz;
	}

	/**
	 * @param sfz the sfz to set
	 */
	public void setSfz(String sfz) {
		this.sfz = sfz;
	}

	/**
	 * @return the zy
	 */
	public String getZy() {
		return zy;
	}

	/**
	 * @param zy the zy to set
	 */
	public void setZy(String zy) {
		this.zy = zy;
	}

	/**
	 * @return the dormitoryId
	 */
	public Integer getDormitoryId() {
		return dormitoryId;
	}

	/**
	 * @param dormitoryId the dormitoryId to set
	 */
	public void setDormitoryId(Integer dormitoryId) {
		this.dormitoryId = dormitoryId;
	}
	
	/**
	 * @return the loginName
	 */
	public String getLoginName() {
		return loginName;
	}

	/**
	 * @param loginName the loginName to set
	 */
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}
	
 	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the roleList
	 */
	public List<Role> getRoleList() {
		return roleList;
	}

	/**
	 * @param roleList the roleList to set
	 */
	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}

	/**
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the createUser
	 */
	public User getCreateUser() {
		return createUser;
	}

	/**
	 * @param createUser the createUser to set
	 */
	public void setCreateUser(User createUser) {
		this.createUser = createUser;
	}

	/**
	 * @return the updateUser
	 */
	public User getUpdateUser() {
		return updateUser;
	}

	/**
	 * @param updateUser the updateUser to set
	 */
	public void setUpdateUser(User updateUser) {
		this.updateUser = updateUser;
	}

	/**
	 * @return the updateDate
	 */
	public Date getUpdateDate() {
		return updateDate;
	}

	/**
	 * @param updateDate the updateDate to set
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	/**
	 * @return the remarks
	 */
	public String getRemarks() {
		return remarks;
	}

	/**
	 * @param remarks the remarks to set
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}


//	/**
//	 * @return the ip
//	 */
//	public String getIp() {
//		return ip;
//	}
//
//	/**
//	 * @param ip the ip to set
//	 */
//	public void setIp(String ip) {
//		this.ip = ip;
//	}
	
	/**
	 * @return the cellPhone
	 */
	public String getCellPhone() {
		return cellPhone;
	}

	/**
	 * @param cellPhone the cellPhone to set
	 */
	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}
	
	/**
	 * @return the imgUrl
	 */
	public String getImgUrl() {
		return imgUrl;
	}

	/**
	 * @param imgUrl the imgUrl to set
	 */
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	/**
	 * @return the faculty
	 */
	public String getFaculty() {
		return faculty;
	}

	/**
	 * @param faculty the faculty to set
	 */
	public void setFaculty(String faculty) {
		this.faculty = faculty;
	}

	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}

	public User() {
		super();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((loginName == null) ? 0 : loginName.hashCode());
		return result;
	}

	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof User))
			return false;
		User other = (User) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		if (loginName == null) {
			if (other.loginName != null)
				return false;
		} else if (!loginName.equals(other.loginName))
			return false;
		return true;
	}


	@Override
	public void setDeletedState(Integer deletedState) {
		this.deletedState = deletedState;
	}
	
}