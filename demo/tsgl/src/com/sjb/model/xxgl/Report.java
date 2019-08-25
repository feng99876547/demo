package com.sjb.model.xxgl;

import java.util.Date;

import javax.persistence.Table;

import com.fxc.admin.jdbc.annotation.Custom;
import com.fxc.admin.jdbc.annotation.MTO;
import com.fxc.entity.IdEntity;
import com.sjb.model.ReportStatus;
import com.sjb.model.UserKey;

@Table(name="xxgl_report")
public class Report extends IdEntity<Long>{

	private static final long serialVersionUID = 3852425612114004466L;
	
	@MTO(assField="student",autoLoading=true)
	private Student student;
	
	@Custom(createInjectUser=UserKey.newDate)
	private Date createTime;
	
	@Custom(createInjectUser=UserKey.id)
	private Long createUser;

	private ReportStatus status;
	
	public Report() {
	}

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

	/**
	 * @return the status
	 */
	public ReportStatus getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(ReportStatus status) {
		this.status = status;
	}
	
}
