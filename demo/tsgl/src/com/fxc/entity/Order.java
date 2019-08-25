package com.fxc.entity;

public class Order {
	
	public static enum OrderType {
		ASC, DESC
	}
	
	//alias 为子字段时多为字段名
	private String alias;
	private String name;
	private OrderType sort;
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public OrderType getSort() {
		return sort;
	}
	public void setSort(OrderType sort) {
		this.sort = sort;
	}
	
	public Order(String name, OrderType sort) {
		super();
		this.name = name;
		this.sort = sort;
	}
	
	public Order(String alias, String name, OrderType sort) {
		super();
		this.alias = alias;
		this.name = name;
		this.sort = sort;
	}
	
	public Order() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
