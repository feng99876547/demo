package com.sjb.model.xxgl;

import java.util.Date;

import javax.persistence.Table;

import com.fxc.admin.jdbc.annotation.Custom;
import com.fxc.admin.jdbc.annotation.OTO;
import com.fxc.entity.IdEntity;
import com.sjb.model.UserKey;
import com.sjb.model.system.User;

/**
 * 公告管理
 * @author fxc
 *
 */
@Table(name="xxgl_announcement")
public class Announcement extends IdEntity<Integer>{

	private static final long serialVersionUID = -9109152942426110304L;

	public Announcement() {
		super();
	}
	
	@Custom(name="标题",NotNull=true)
	private String title;//标题
	
	private String content;//内容
	
	@OTO(assField="createUser",autoLoading=true)
	private User createUser;
	
	@Custom(createInjectUser=UserKey.newDate)
	private Date createTime;//发布时间
	
	private Integer effective;//有效期
	
	private Date maturity;//到期时间
	
	private Date top;//置顶

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
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
	 * @return the effective
	 */
	public Integer getEffective() {
		return effective;
	}

	/**
	 * @param effective the effective to set
	 */
	public void setEffective(Integer effective) {
		this.effective = effective;
	}

	/**
	 * @return the maturity
	 */
	public Date getMaturity() {
		return maturity;
	}

	/**
	 * @param maturity the maturity to set
	 */
	public void setMaturity(Date maturity) {
		this.maturity = maturity;
	}

	/**
	 * @return the top
	 */
	public Date getTop() {
		return top;
	}

	/**
	 * @param top the top to set
	 */
	public void setTop(Date top) {
		this.top = top;
	}
	
	
}
