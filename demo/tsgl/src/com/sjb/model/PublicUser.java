package com.sjb.model;

import java.io.Serializable;
import java.util.List;

import com.sjb.model.system.Role;
import com.sjb.model.system.User;

public class PublicUser implements Serializable{

	private static final long serialVersionUID = -4672729133593478474L;

	private List<Role> role;//角色
	
	private String account;//账号
	
	private String name;//名字
	
	private String phone;//手机
	
	private Long userId;
	
	private String proxy_path;//代理平台需要使用的下级查询路径
	
	private Integer proxyParentId;//代理平台用户的父id
	
	private Integer proxyLevel;//代理平台用户当前层级
	
	private Integer type;//属于哪个平台的商户  1 代理  2 业务员 3 member

	public PublicUser(){
		super();
	}
	
	public PublicUser(User user){
		this.role = user.getRoleList();
		this.account = user.getLoginName();
		this.name = user.getName();
		this.phone = user.getCellPhone();
		this.userId = user.getId();
		this.type = 3;
	}
	
//	public PublicUser(Salesman salesman){
//		this.role = new ArrayList<Role>();
//		this.role.add(salesman.getRole_id()) ;
//		this.account = salesman.getAccount();
//		this.name = salesman.getSalesman_name();
//		this.phone = salesman.getSalesman_tel();
//		this.userId = salesman.getSalesman_id();
//		this.type = 2;
//	}
	
//	public PublicUser(Proxy proxy){
//		this.role = new ArrayList<Role>();
//		this.role.add(proxy.getRole_id()) ;
//		this.account = proxy.getAccount();
//		this.name = proxy.getCo_name();
//		this.phone = proxy.getContact_man_tel();
//		this.userId = proxy.getProxy_id();
//		this.proxy_path = proxy.getProxy_path();
//		this.proxyParentId = proxy.getParent_proxy_id();
//		this.proxyLevel = proxy.getProxy_level();
//		this.type = 1;
//	}
	
	public Integer getProxyLevel() {
		return proxyLevel;
	}

	public void setProxyLevel(Integer proxyLevel) {
		this.proxyLevel = proxyLevel;
	}

	public Integer getProxyParentId() {
		return proxyParentId;
	}

	public void setProxyParentId(Integer proxyParentId) {
		this.proxyParentId = proxyParentId;
	}

	public String getProxy_path() {
		return proxy_path;
	}

	public void setProxy_path(String proxy_path) {
		this.proxy_path = proxy_path;
	}

	public List<Role> getRole() {
		return role;
	}

	public void setRole(List<Role> role) {
		this.role = role;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
}
