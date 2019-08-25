package com.sjb.model.system;

import javax.persistence.Table;

import com.fxc.admin.jdbc.annotation.Custom;
import com.fxc.entity.IdEntity;

/**
 * 组织机构  
 * @author fxc
 *
 */
@Table(name = "system_organization")
public class Organization extends IdEntity<Long>{

	private static final long serialVersionUID = 4891541132341830051L;

	private String name; //机构名称
	
	//编码 用于like查找 假设最高机构markCode值为null 那么属于最高机构的子机构编码为 1a 2a 3a(数量加a) 层数为父机构+1 父机构数量加1 markCode为a
	//1a子机构的子机构为 code 1a-1a 1a-2a markCode 1a/ 2a子机构的子机构为 code 2a-1a 2a-2a markCode 2a 
	//这种设计层数不能太多 因为字符串长度有上限 不过现在国家规定不能超过7层 否则算传销 所以暂时不用担心
	private String code;//重新指定父节点 sql语句使用  拼接加截取 一次性批量修改 所对应的层级也进行相应的修改
	
//	private String markCode;//
	
	@Custom(DefaultIntValue=0)
	private Integer number;//机构下的子机构数量
	
	private Long parentId;//父id
	
	@Custom(DefaultIntValue=0)
	private Integer layer;//第几层
	
	private Integer sequence;//顺序
	
	private String remarks;//备注

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Integer getLayer() {
		return layer;
	}

	public void setLayer(Integer layer) {
		this.layer = layer;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Organization() {
		super();
	}
	
}
