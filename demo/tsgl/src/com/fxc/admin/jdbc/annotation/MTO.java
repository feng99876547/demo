package com.fxc.admin.jdbc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
/**
 * 多对一 的关系配置  
 * @author Administrator
 *
 */
public @interface MTO {
	
	/**
	 * 该注释为  指定默认的关联关系
	 */
	String associated() default "inner join";
	
	/**
	 * 该注释为  指定该实体用于关联的查询的字段
	 */
	String assField() default "id";

	/**
	 * 该注释为  指定该实体用于关联的查询的字段是否为当前字段
	 * 一对多（ 关联子对象时候的对象时 指自身字段不存在所以false）而这边是true
	 * 默认是
	 */
	boolean heirOwn() default true;
	
	/**
	 * 该注释为  指定关联的实体用于关联的查询的字段
	 */
	String pointAssField() default "id";
	
	/**
	 * 是否自动关联
	 * @return
	 */
	boolean autoLoading() default false;
}
