package com.log;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface LogAnn {

	/**
	 * 打入哪个日志
	 */
	String logName() default "business";
	
	/**
	 * 模块名称
	 */
	String name();
	
	/**
	 * 记录的key名称
	 */
	String keyName() default "result";
	
	/**
	 * 日志策略
	 * 一般指 写入哪些内容 格式 如添加的 查询的 删除的
	 */
	String content() default "";
}
