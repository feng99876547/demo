package com.sjb.model.xxgl;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Table;

import com.fxc.admin.jdbc.annotation.Custom;
import com.fxc.admin.jdbc.annotation.OTO;
import com.fxc.entity.IdEntity;
import com.sjb.model.UserKey;

/**
 * 缴费管理
 * @author fxc
 *
 */
@Table(name = "xxgl_contributions")
public class Contributions extends IdEntity<Long>{

	private static final long serialVersionUID = -2111195510526665121L;

	@OTO(assField="student",autoLoading=true)
	private Student student;//缴费人
	
	private BigDecimal je;//金额
	
	@Custom(createInjectUser = UserKey.newDate)
	private Date date;//时间
	
	private String zfddh;//支付订单号(缴费凭证)

	/**
	 * @return the user
	 */
	public Student getStudent() {
		return student;
	}

	/**
	 * @param user the user to set
	 */
	public void setStudent(Student student) {
		this.student = student;
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
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the zfddh
	 */
	public String getZfddh() {
		return zfddh;
	}

	/**
	 * @param zfddh the zfddh to set
	 */
	public void setZfddh(String zfddh) {
		this.zfddh = zfddh;
	}
	
	
}
