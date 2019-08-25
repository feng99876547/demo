package com.sjb.model;

/**
 * 实现该接口后 使用EntityDeleteService替换JdbcDel的实现为ServiceEntityDelete 实现的是修改不删除修改状态
 * @author fxc
 *
 * @param <ID> 
 */
public interface EntityDeleteImpl<ID> {

	/** 返回id*/
	public abstract void setId(ID Id);
	
	/** 设置实体中对应的状态为 删除状态*/
	public abstract void setDeletedState(Integer deletedState);
	
}
