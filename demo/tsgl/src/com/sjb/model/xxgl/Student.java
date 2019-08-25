package com.sjb.model.xxgl;

import java.util.Date;

import javax.persistence.Table;

import com.fxc.admin.jdbc.annotation.Custom;
import com.fxc.admin.jdbc.annotation.OTO;
import com.fxc.entity.IdEntity;
import com.fxc.utils.ContextUtils;
import com.sjb.model.UserKey;
import com.sjb.model.system.User;

/**
 * 系统用户表
 */
@Table(name ="xxgl_student")
public class Student extends IdEntity<Long>{
	
	private static final long serialVersionUID = -9146376949478924256L;

	/** 名字*/
	private String name;
	
	/** 身份证*/
	@Custom(name="身份证",isUnique=true)
	private String sfz;
	
	/** 学号*/
	@Custom(name="学号",isUnique=true)
	private String number;
	
	/** 头像*/
	private String imgUrl;
	
	/** 性别 1男 2女*/
	private Integer xb;
	
	/** 名族*/
	private String mz;
	
	/** 政治面貌*/
	private String zzmm;
	
	/** 学院*/
	private String faculty;
	
	/** 专业*/
	private String zy;
	
	/** 班级*/
	private String className;
	
	/** 电话*/
	private String cellPhone;
	
	/** 地址*/
	private String dz;
	
	/** 宿舍*/
	private Integer dormitoryId; 
	
	/** 创建时间 */
	@Custom(createInjectUser=UserKey.newDate)
	private Date createTime;
	
	/** 创建人id */
	@Custom(createInjectUser = UserKey.systemUser)
	@OTO(assField="createUser",autoLoading=false)//默认不关联只返回pointAssField的key自段
	private User createUser;

	/** 用户状态: 0 新建  5可用 10不可用 */
	@Custom(DefaultIntValue = 5)//添加时没设置值 默认值为1不可用
	private Integer  status;
	
	/**删除状态: 0 删除  1未删除*/
	@Custom(DefaultIntValue = ContextUtils.WSC)
	private Integer deletedState;
	
	public Student(Long id) {
		super();
		setId(id);
	}
	
	public Student() {
		super();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
	 * @return the mz
	 */
	public String getMz() {
		return mz;
	}

	/**
	 * @param mz the mz to set
	 */
	public void setMz(String mz) {
		this.mz = mz;
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
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}

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

	/**
	 * @return the deletedState
	 */
	public Integer getDeletedState() {
		return deletedState;
	}

	/**
	 * @param deletedState the deletedState to set
	 */
	public void setDeletedState(Integer deletedState) {
		this.deletedState = deletedState;
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
	
}