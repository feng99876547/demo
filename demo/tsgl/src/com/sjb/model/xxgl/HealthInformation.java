package com.sjb.model.xxgl;

import java.util.Date;

import javax.persistence.Table;

import com.fxc.admin.jdbc.annotation.Custom;
import com.fxc.admin.jdbc.annotation.OTO;
import com.fxc.entity.IdEntity;
import com.sjb.model.UserKey;

@Table(name="xxgl_healthinformation")
public class HealthInformation extends IdEntity<Long>{

	private static final long serialVersionUID = -7280402324697995305L;

	public HealthInformation() {
		super();
	}
	
	@OTO(assField="student",autoLoading=true)
	private Student student;//学号
	
	private String tjbg;//体检报告
	
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
	 * @return the tjbg
	 */
	public String getTjbg() {
		return tjbg;
	}

	/**
	 * @param tjbg the tjbg to set
	 */
	public void setTjbg(String tjbg) {
		this.tjbg = tjbg;
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
