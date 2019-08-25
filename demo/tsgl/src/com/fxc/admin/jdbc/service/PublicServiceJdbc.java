package com.fxc.admin.jdbc.service;


import com.fxc.admin.jdbc.service.jdbc.JdbcAdd;
import com.fxc.admin.jdbc.service.jdbc.JdbcDel;
import com.fxc.admin.jdbc.service.jdbc.JdbcGet;
import com.fxc.admin.jdbc.service.jdbc.JdbcUpdate;
import com.fxc.admin.jdbc.service.jdbc.PublicServiceJdbcExecution;
import com.fxc.utils.ContextUtils;

/**
 * @author fxc
 * @param <T>
 * @param <PK>
 * @param <DB>
 */
@SuppressWarnings("unchecked")
public class PublicServiceJdbc<T,PK,DB> {


	private PublicServiceJdbcExecution<T,PK,DB> publicServiceJdbc;
	
	
	/**
	 * 默认的到层实现是publicServiceJdbcImpl 
	 * 重新实现PublicServiceJdbcExecution接口用于对servcie层添加一层自定义缓存封装 实现之后在重写该方法
	 * @return
	 */
	/*为什么不在dao层实现因为service层因为业务依赖（分布式事物）的问题需要回滚所以设置在service层 这是适合添加和修改 缓存一般试用与查询啊 考虑考虑*/
	public PublicServiceJdbcExecution<T,PK,DB> getPublicServiceJdbc(){
		if(this.publicServiceJdbc==null){
			this.publicServiceJdbc = (PublicServiceJdbcExecution<T,PK,DB>) ContextUtils.appContext.getBean("publicServiceJdbcImpl");
		}
		return this.publicServiceJdbc;
	}
	
	public JdbcAdd<T,PK,DB> getJcbcAdd(){
		return getPublicServiceJdbc().getJcbcAdd();
	}
	
	public JdbcDel<T,PK,DB> getJcbcDel(){
		return getPublicServiceJdbc().getJcbcDel();
	}
	
	public JdbcUpdate<T,PK,DB> getJcbcUpdate(){
		return getPublicServiceJdbc().getJcbcUpdate();
	}
	
	public JdbcGet<T,PK,DB> getJcbcGet(){
		return getPublicServiceJdbc().getJcbcGet();
	}
	
	public PublicServiceJdbc() {
	}
	
}
