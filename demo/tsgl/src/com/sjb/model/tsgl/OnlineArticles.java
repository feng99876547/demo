package com.sjb.model.tsgl;

import java.util.Date;

import javax.persistence.Table;

import com.fxc.admin.jdbc.annotation.Custom;
import com.fxc.entity.IdEntity;
import com.sjb.model.UserKey;

/**
 * 线上文章 因为服务器内存小项目也小就放在一台服务器中
 * @author fxc
 *
 */
@Table(name = "tsgl_onlinearticles")
public class OnlineArticles extends IdEntity<Long>{

	private static final long serialVersionUID = -5477054936163246804L;

	private Long themeEditId; 
	
	@Custom(name="标题",NotNull=true)
	private String title;//标题
	
	@Custom(createInjectUser=UserKey.newDate)
	private Date createTime;//创建时间
	
	private String remarks;//备注

	private String content;//内容

	private Date updateTime;//修改时间
	
	
	/**
	 * @return the themeEditId
	 */
	public Long getThemeEditId() {
		return themeEditId;
	}

	/**
	 * @param themeEditId the themeEditId to set
	 */
	public void setThemeEditId(long themeEditId) {
		this.themeEditId = themeEditId;
	}

	/**
	 * @return the updateTime
	 */
	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

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
	 * @return the remarks
	 */
	public String getRemarks() {
		return remarks;
	}

	/**
	 * @param remarks the remarks to set
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
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
	
	public OnlineArticles() {
	}
}
