package com.fxc.admin.jdbc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author fxc
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoSqlAssociation {

	/** 需要关关联获取的字段  可以是对象 也可以是一个或多个字段  */
	Relationship[] fields();
	
	/** 取值key*/
	String querykey();
	
	/** 映射key*/
	String mapkey();
	
	/** 调用反射的载体类*/
	String className();
	
	/** 反射调用的方法*/
	String method();
	
	/** 反射方法的参数*/
	String[] args() default "qi";
	
	/** 是否做对象关联 false默认不是对象  */
	boolean isObj() default false;
	
}
