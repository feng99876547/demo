package com.sjb.model.system;

import java.util.Date;

import javax.persistence.Table;

import com.fxc.admin.jdbc.annotation.Custom;
import com.fxc.admin.jdbc.annotation.OTO;
import com.fxc.entity.IdEntity;
import com.fxc.utils.ContextUtils;
import com.sjb.model.EntityDeleteImpl;
import com.sjb.model.UserKey;

/**
 * 字典管理
 * @author fxc
 *
 */

@Table(name = "system_dictionary")
public class Dictionary extends IdEntity<Integer> implements EntityDeleteImpl<Integer>{

	private static final long serialVersionUID = 882161149916581034L;

	@Custom(NotNull=true,name="名称")
	private String name;//名称
	
	@Custom(name="编码",isUnique=true)
	private String code;//编码 编码唯一，所以一般不配合层级查找 层级只区分是第几层节点的数据
	
	private Integer sequence;//排序
	
	private String remarks;//备注
	
	/**删除状态: 5 删除  1未删除*/
	@Custom(DefaultIntValue = ContextUtils.WSC)
	private Integer state;//删除状态
	
	@Custom(DefaultIntValue=1)
	private Integer showPeanl;//是否为有大量数据 如果有在点击事件时在弹出一个编辑窗 1没有 2 有
	
	//层级标记 1 为根节点 根节点下一层2，下下层3 以此类推（单更改父节点时 要判断父节点的层级和自己当前层级是否吻合不吻合需要批量修改自身子节点的层级
	//那些用户多级代理不适合这样 因为表的数据量不一样如果是索引字段这样操作会非常慢 访问量也不一样，所以不适合）
	@Custom(DefaultIntValue = 0)
	private Integer level;//默认值 1
	
	private Integer parentId;//父节点id 默认值 0 表示根节点
	
	@Custom(createInjectUser=UserKey.systemUser)
	@OTO(assField="createUser",autoLoading=false)
	private User createUser;//创建人
	
	@Custom(createInjectUser=UserKey.newDate)
	private Date createTime;//创建时间
	
	@Custom(isUpdateUser=UserKey.systemUser)
	@OTO(assField="updateUser",autoLoading=false)
	private User updateUser;//修改人
	
	@Custom(isUpdateUser=UserKey.newDate)
	private Date updateDate;//修改时间
	
	public Dictionary() {
	}

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

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getShowPeanl() {
		return showPeanl;
	}

	public void setShowPeanl(Integer showPeanl) {
		this.showPeanl = showPeanl;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public User getCreateUser() {
		return createUser;
	}

	public void setCreateUser(User createUser) {
		this.createUser = createUser;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public User getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(User updateUser) {
		this.updateUser = updateUser;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	
	@Override
	public void setDeletedState(Integer deletedState) {
		this.state = deletedState;
	}
	
}
