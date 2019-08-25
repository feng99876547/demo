package com.fxc.admin.jdbc.annotation;

/**
 * 用于统一返回枚举的转json使用的值 自定义json在转json时实现该接口的枚举值返回getValue（）
 * @author fxc
 *
 */
public interface JsonEnum {
	/**
	 * 当枚举类型参数没赋值时或是没有指定默认值时
	 * 使用该方法返回统一的默认枚举值
	 */
	public abstract String getValue();
	
}
