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
 * 多对多的关系配置
 * @author fxc
 */
public @interface MTM {
	
	/**
	 * 该注释为  指定默认的关联关系
	 */
	String associated() default "inner join";
	
	/**
	 * 该注释为  指定中间表用于关联本表id的查询的字段
	 */
	String assField() default "id";
	
	/**
	 * 该注释为  指定该实体用于关联的查询的字段是否为当前字段
	 * 多对多自身字段不存在所以false
	 */
	boolean heirOwn() default false;
	
	/**
	 *  中间表
	 */
	String middleTable() default "";
	
//	/**
//	 *  返回的字段 如果存在返回的字段应该是多对多的关系 所以存在中间表要继承关联
//	 */
//	String returnField() default "";
	
	/**
	 * 该注释为  指定关联的实体的id关联中间表的字段的字段
	 */
	String pointAssField() default "id";
	
	
	/**
	 * 该注释为  指定字段排序
	 */
	String orderBy() default "";
	
	/**
	 * 是否自动关联
	 * @return
	 */
	boolean autoLoading() default false;
}
