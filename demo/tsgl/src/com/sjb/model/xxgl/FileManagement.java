package com.sjb.model.xxgl;

import java.util.Date;

import javax.persistence.Table;

import com.fxc.admin.jdbc.annotation.Custom;
import com.fxc.admin.jdbc.annotation.OTO;
import com.fxc.entity.IdEntity;
import com.sjb.model.UserKey;

/**
 * 档案管理
 * @author fxc
 *
 */
@Table(name="xxgl_filemanagement")
public class FileManagement extends IdEntity<Long>{

	private static final long serialVersionUID = -4218915329762322827L;

	public FileManagement() {
		super();
	}

	@OTO(assField="student",autoLoading=true)
	private Student student;//学号
	
	private String pzh;//开通凭证
	
	@Custom(createInjectUser=UserKey.newDate)
	private Date createTime;//开通时间

	@Custom(createInjectUser=UserKey.id)
	private Long createUser;

	/**
	 * @return the student
	 */
	public Student getStudent() {
		return student;
	}

	/**
	 * @param student the student to set
	 */
	public void setStudent(Student student) {
		this.student = student;
	}

	/**
	 * @return the pzh
	 */
	public String getPzh() {
		return pzh;
	}

	/**
	 * @param pzh the pzh to set
	 */
	public void setPzh(String pzh) {
		this.pzh = pzh;
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
	public Long getCreateUser() {
		return createUser;
	}

	/**
	 * @param createUser the createUser to set
	 */
	public void setCreateUser(Long createUser) {
		this.createUser = createUser;
	}
	
}
