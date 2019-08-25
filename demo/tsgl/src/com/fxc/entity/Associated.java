package com.fxc.entity;

public class Associated {
	
	private Class<?> clas;//实体类class
	
	private String tabName;//多对多中间表名字
	
	private String connection;//内联外联  用于一对一 和多对一
	
//	private String OrderBy; //是否指定一对多的排序字段  一对多的配置才生效
	
	private String relationship;//映射关系 比如id关联 还是外键关联 作为两表关联的匹配条件 由实体注释决定

	private String assField;//自身字段
	
	private Boolean heirOwn;
	
	private boolean autoLoading;//在过滤完后面代码添加的过滤条件后在使用默认的配置判断是否要关联
	
	private String pointAssField;//自身映射到的字段
	
	private String type;//关系类型 (如一对多 多对多等等)
	
	public Class<?> getClas() {
		return clas;
	}

	public void setClas(Class<?> clas) {
		this.clas = clas;
	}

	public String getConnection() {
		return connection;
	}

	public void setConnection(String connection) {
		this.connection = connection;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Associated() {
		super();
		// TODO Auto-generated constructor stub
	}
//	public String getOrderBy() {
//		return OrderBy;
//	}
//	public void setOrderBy(String orderBy) {
//		OrderBy = orderBy;
//	}
	public String getPointAssField() {
		return pointAssField;
	}

	public void setPointAssField(String pointAssField) {
		this.pointAssField = pointAssField;
	}

	public String getAssField() {
		return assField;
	}

	public void setAssField(String assField) {
		this.assField = assField;
	}

	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}

	public String getTabName() {
		return tabName;
	}

	public void setTabName(String tabName) {
		this.tabName = tabName;
	}

	public boolean isAutoLoading() {
		return autoLoading;
	}

	public void setAutoLoading(boolean autoLoading) {
		this.autoLoading = autoLoading;
	}

	public Boolean getHeirOwn() {
		return heirOwn;
	}

	public void setHeirOwn(Boolean heirOwn) {
		this.heirOwn = heirOwn;
	}

//	public Boolean getHeirOwn() {
//		return heirOwn;
//	}
//
//	public void setHeirOwn(Boolean heirOwn) {
//		this.heirOwn = heirOwn;
//	}
}