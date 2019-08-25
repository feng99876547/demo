package com.sjb.model.system;

import java.util.Comparator;

import javax.persistence.Table;
import javax.persistence.Transient;

import com.fxc.admin.jdbc.annotation.Custom;
import com.fxc.entity.IdEntity;
import com.fxc.utils.ContextUtils;


/**
 * 权限.
 * 
 * @author fxc
 */
@Table(name = "system_permission")
public class Permission extends IdEntity<Long> implements Comparator<Permission>{
	
	private static final long serialVersionUID = 8957814361318745652L;
	/** 超级权限(*.*)数据库id */
	public static Long SUPER_ID=1L;
	/** 操作权限 */
	public static Integer TYPE_OPERATE=0;
	/** 资源权限 */
	public static Integer TYPE_RESOURCE=1;
	
	/** 权限名称：建议为中文，用于用户识别 */
	private String name;
	
	/** 权限标识：必须为英文，用于系统识别，格式：XX:XX:XX   唯一值 */
	@Custom(name = "权限标识",NotNull=true)
	private String permission;
	
	
	/** 描述 */
	private String description;
	
	/** 权限类型：0-系统权限，1普通权限 2附属权限(会造成冗余好像也没什么办法) 3私有权限（只有我能用的） */
	private Integer type;
	
	/** 状态  1显示 2 隐藏*/
	@Custom(DefaultIntValue=ContextUtils.XS)
	private Integer status;
	
	/** 排序*/
	private Integer sequence;
	
	private Long parentId;
	
	/** Transient 字段 是否分配过 false 未分配 true 已分配*/
	@Transient
	private Boolean isDistribution;
	
	public Permission() {
		
	}
	public Permission(Long id){
		super.setId(id);
	}
	public String getPermission() {
		return permission;
	}
	public void setPermission(String permission) {
		this.permission = permission;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Boolean getIsDistribution() {
		return isDistribution == null ? false : isDistribution;
	}
	public void setIsDistribution(Boolean isDistribution) {
		this.isDistribution = isDistribution;
	}
	
	public Long getParentId() {
		return parentId;
	}
 
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
 
	public Integer getSequence() {
		return sequence;
	}
 
	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
	@Override
	public String toString() {
		return "Permission [name=" + name + ", permission=" + permission + ", description=" + description + ", type="
				+ type + ", status=" + status + ", parentId=" + parentId + ", isDistribution=" + isDistribution + "]";
	}
	@Override
	public int compare(Permission o1, Permission o2) {
		Permission s1 =o1;
		Permission s2 =o2;
	    return s1.getSequence().compareTo(s2.getSequence());
	}
	
}