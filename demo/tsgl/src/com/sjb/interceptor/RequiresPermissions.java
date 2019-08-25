package com.sjb.interceptor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 权限匹配
 */
@Target({ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
/**
 * 声明这个就是父类声明的注解 可以被子类集成 
 */
@Inherited
public @interface RequiresPermissions {
	/**
	 * 对应权限的前缀 XXX:XXX
	 * @return
	 */
	String value();
}
