package com.fxc.admin.jdbc.service.jdbc;

import org.springframework.stereotype.Component;

import com.fxc.utils.ContextUtils;

@Component
@SuppressWarnings("unchecked")
public class PublicServiceJdbcImpl<T,PK,DB> implements PublicServiceJdbcExecution<T,PK,DB>{

	private JdbcAdd<T,PK,DB> jcbcAdd ;
	
	private JdbcDel<T,PK,DB> jcbcDel;
	
	private JdbcUpdate<T,PK,DB> jcbcUpdate;
	
	private JdbcGet<T,PK,DB> jcbcGet;
	
	public JdbcAdd<T,PK,DB> getJcbcAdd(){
		if(this.jcbcAdd==null){
			this.jcbcAdd = (JdbcAdd<T,PK,DB>) ContextUtils.appContext.getBean("publicServiceJdbcAdd");
		}
		return this.jcbcAdd;
	}
	
	public JdbcDel<T,PK,DB> getJcbcDel(){
		if(this.jcbcDel==null){
			this.jcbcDel = (JdbcDel<T,PK,DB>) ContextUtils.appContext.getBean("publicServiceJdbcDel");
		}
		return this.jcbcDel;
	}
	
	public JdbcUpdate<T,PK,DB> getJcbcUpdate(){
		if(this.jcbcUpdate==null){
			this.jcbcUpdate = (JdbcUpdate<T,PK,DB>) ContextUtils.appContext.getBean("publicServiceJdbcUpdate");
		}
		return this.jcbcUpdate;
	}
	
	public JdbcGet<T,PK,DB> getJcbcGet(){
		if(this.jcbcGet==null){
			this.jcbcGet = (JdbcGet<T,PK,DB>) ContextUtils.appContext.getBean("publicServiceJdbcGet");
		}
		return this.jcbcGet;
	}
	
}
