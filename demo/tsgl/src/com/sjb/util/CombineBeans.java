package com.sjb.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * 合并对象
 * @author fxc
 *
 */
public class CombineBeans {
	 
	/**
	 * @Title: combineSydwCore
	 * @Description: 该方法是用于相同对象不同属性值的合并，如果两个相同对象中同一属性都有值，
	 *               那么sourceBean中的值会覆盖tagetBean重点的值
	 * @author: WangLongFei
	 * @date: 2017年12月26日 下午1:53:19
	 * @param sourceBean
	 *            被提取的对象bean
	 * @param targetBean
	 *            用于合并的对象bean
	 * @return targetBean 合并后的对象
	 * @return: Object
	 */
	public static Object combineSydwCore(Object sourceBean, Object targetBean) {
		if(Treat.isEmpty(targetBean)){
			return null;
		}
		Class<?> sourceBeanClass = sourceBean.getClass();
 
		Field[] sourceFields = sourceBeanClass.getDeclaredFields();
		Field[] targetFields = sourceBeanClass.getDeclaredFields();
		for (int i = 0; i < sourceFields.length; i++) {
			
			Field sourceField = sourceFields[i];
			Field targetField = targetFields[i];
			if (sourceField.getModifiers() == 2 && !(Modifier.toString(sourceField.getModifiers()).indexOf("static") > -1)
					&& !(Modifier.toString(sourceField.getModifiers()).indexOf("final") > -1)) {
				sourceField.setAccessible(true);
				targetField.setAccessible(true);
				try {
					if (!(sourceField.get(sourceBean) == null)) {
						targetField.set(targetBean, sourceField.get(sourceBean));
					}
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		return targetBean;
	}
	 
}
