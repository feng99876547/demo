package com.sjb.model.xxgl;

import javax.persistence.Table;

import com.fxc.entity.IdEntity;

/**
 * 宿舍管理
 * @author fxc
 *
 */
@Table(name = "xxgl_dormitory")
public class Dormitory extends IdEntity<Integer>{

	private static final long serialVersionUID = -6065372890482967355L;

	private Integer lou;//楼号
	
	private Integer ceng;//层
	
	private String code;//宿舍编号
	
	private Integer type;//宿舍类型 男女 1男 2女
	
	private Integer zdrs;//最大人数
	
	private Integer yzrs;//已住人数
	
	private Integer syrs;//剩余人数
	
	/**
	 * @return the lou
	 */
	public Integer getLou() {
		return lou;
	}



	/**
	 * @param lou the lou to set
	 */
	public void setLou(Integer lou) {
		this.lou = lou;
	}



	/**
	 * @return the ceng
	 */
	public Integer getCeng() {
		return ceng;
	}



	/**
	 * @param ceng the ceng to set
	 */
	public void setCeng(Integer ceng) {
		this.ceng = ceng;
	}



	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}



	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}



	/**
	 * @return the type
	 */
	public Integer getType() {
		return type;
	}



	/**
	 * @param type the type to set
	 */
	public void setType(Integer type) {
		this.type = type;
	}



	/**
	 * @return the zdrs
	 */
	public Integer getZdrs() {
		return zdrs;
	}



	/**
	 * @param zdrs the zdrs to set
	 */
	public void setZdrs(Integer zdrs) {
		this.zdrs = zdrs;
	}



	/**
	 * @return the yzrs
	 */
	public Integer getYzrs() {
		return yzrs;
	}



	/**
	 * @param yzrs the yzrs to set
	 */
	public void setYzrs(Integer yzrs) {
		this.yzrs = yzrs;
	}



	/**
	 * @return the syrs
	 */
	public Integer getSyrs() {
		return syrs;
	}



	/**
	 * @param syrs the syrs to set
	 */
	public void setSyrs(Integer syrs) {
		this.syrs = syrs;
	}



	public Dormitory() {
	}
}
