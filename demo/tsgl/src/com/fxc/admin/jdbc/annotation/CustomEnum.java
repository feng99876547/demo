package com.fxc.admin.jdbc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 用于注入业务规定的枚举类型 (没有设置值时使用的默认值)
 * @author fxc
 *
 */
@Target({ ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface CustomEnum{

	/**用于标识 name */
	public abstract String DefaultValue() default "";
	
	/**用于标识下标*/
	public abstract int DefaultIntValue() default -1;
}
