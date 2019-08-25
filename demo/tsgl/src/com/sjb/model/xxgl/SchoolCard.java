package com.sjb.model.xxgl;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Table;

import com.fxc.admin.jdbc.annotation.Custom;
import com.fxc.admin.jdbc.annotation.OTO;
import com.fxc.entity.IdEntity;
import com.sjb.model.UserKey;

@Table(name="xxgl_schoolcard")
public class SchoolCard extends IdEntity<Long>{

	private static final long serialVersionUID = 2789548789578523885L;

	public SchoolCard() {
		super();
	}
	
	@OTO(assField="student",autoLoading=true)
	private Student student;//学号
	
	@Custom(name="卡号",NotNull=true)
	private String card;//校园卡号
	
	private BigDecimal je;//金额
	
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
	 * @return the card
	 */
	public String getCard() {
		return card;
	}

	/**
	 * @param card the card to set
	 */
	public void setCard(String card) {
		this.card = card;
	}

	/**
	 * @return the je
	 */
	public BigDecimal getJe() {
		return je;
	}

	/**
	 * @param je the je to set
	 */
	public void setJe(BigDecimal je) {
		this.je = je;
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
