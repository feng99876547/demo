package com.sjb.model.system;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Table;
import javax.persistence.Transient;

import com.fxc.admin.jdbc.annotation.Custom;
import com.fxc.admin.jdbc.annotation.OTM;
import com.fxc.entity.IdEntity;
import com.fxc.utils.ContextUtils;

/**
 *  系统菜单.
 * 
 * @author fxc
 */
@Table(name = "system_menu")
public class Menu extends IdEntity<Long> implements Comparator<Menu>{
	
	private static final long serialVersionUID = -7620525264893243462L;
	
	public static enum MenuType{
		
		PARENT("父节点"),CHILD("子节点");
		
		private String explain;
		
		MenuType(String explain){
			this.explain = explain;
		}
		
		public String getExplain(){
			return this.explain;
		}
//		public String toString(){
//			return explain;
//		}
	}
	/** 名称  */
	private String name;
	/** 图标 */
	private String icon;
	/** 识别那套系统的菜单 因为代理商和业务员不是同一张表和同一套系统会产生冲突*/
//	private String type;
	/** 跳转地址 */
	private String url;
	/** 父节点 */
	private Long parentId;
	/** 对应权限 唯一值  XXX:XXX:read */
	private String permission;
	/** 位置：0-99 */
	private Integer position;
	/** 类型：0-根菜单，1-子菜单 */
	private MenuType menuType;
	/** 状态：1 -可用，2-不可用 */
	@Custom(DefaultIntValue = ContextUtils.XS)
	private Integer status;
	
	@OTM(assField = "id",pointAssField="parentId")
//	private List<Menu> childMenu = Lists.newArrayList();
	private List<Menu> childMenu;
	
	@Transient
	private HashMap<String,Permission> listPermissions;
	
	/**用于菜单过滤*/
	@Transient
	private boolean filter;

	public boolean isFilter() {
		return filter;
	}
	
	public void setFilter(boolean filter) {
		this.filter = filter;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public String getPermission() {
		return permission;
	}
	public void setPermission(String permission) {
		this.permission = permission;
	}
	public Integer getPosition() {
		return position;
	}
	public void setPosition(Integer position) {
		this.position = position;
	}
	public MenuType getMenuType() {
		return menuType;
	}
	public void setMenuType(MenuType menuType) {
		this.menuType = menuType;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public List<Menu> getChildMenu() {
		return childMenu;
	}
	public void setChildMenu(List<Menu> childMenu) {
		this.childMenu = childMenu;
	}
	
//	public String getType() {
//		return type;
//	}
//
//	public void setType(String type) {
//		this.type = type;
//	}

	public void addChildMenu(Menu menu){
		if(this.childMenu == null)
			this.childMenu = new ArrayList<Menu>();
		this.childMenu.add(menu);
	}
	
	public HashMap<String,Permission> getListPermissions() {
		return listPermissions;
	}
	public void setListPermissions(HashMap<String,Permission> listPermissions) {
		this.listPermissions = listPermissions;
	}
	
	@Override
	public String toString() {
		return "Menu [id=" + super.getId() + ", icon=" + icon + ", name=" + name + ", url=" + url + ", parentId=" + parentId + ", permission=" + permission
				+ ", menuType=" + menuType + "]";
	}


	@Override
	public int compare(Menu m1, Menu m2) {
		Menu s1 = (Menu) m1;
		Menu s2 = (Menu) m2;
	    return s1.getPosition().compareTo(s2.getPosition());
	}

}
