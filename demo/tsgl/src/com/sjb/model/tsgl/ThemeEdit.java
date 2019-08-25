package com.sjb.model.tsgl;

import java.util.Date;

import javax.persistence.Table;

import com.fxc.admin.jdbc.annotation.Custom;
import com.fxc.entity.IdEntity;
import com.fxc.utils.ContextUtils;

/**
 * 文章编辑
 * @author fxc
 *
 */
@Table(name = "tsgl_themeedit")
public class ThemeEdit extends IdEntity<Long>{

	private static final long serialVersionUID = 1597467681314355111L;

	@Custom(name="标题",NotNull=true)
	private String title;//标题
	
	private Date createTime;//创建时间
	
	private String remarks;//备注
	
	@Custom(name="标题",DefaultIntValue = ContextUtils.WFB)
	private Integer releaseStatus;//发布状态
	
	private String content;//内容

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
	 * @return the releaseStatus
	 */
	public Integer getReleaseStatus() {
		return releaseStatus;
	}

	/**
	 * @param releaseStatus the releaseStatus to set
	 */
	public void setReleaseStatus(Integer releaseStatus) {
		this.releaseStatus = releaseStatus;
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
	
	public ThemeEdit() {
	}
}
