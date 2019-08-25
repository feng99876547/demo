package com.fxc.admin.jdbc.util;

import java.lang.reflect.Field;


import com.fxc.admin.jdbc.annotation.Custom;


/**
 * jdbc 工具类
 * @author fxc
 */
public class FieldUtil {
	
	
	/**
	 * 向上查找到Object类  获取Field直到找到或没有
	 * 并释放私有属性
	 * @param clas
	 * @param fieldName
	 * @return
	 */
	public Field getAccessibleField(final Class<?> clas,String fieldName) {
		for (Class<?> superClass = clas; superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				Field field = superClass.getDeclaredField(fieldName);
				field.setAccessible(true);
				return field;
			} catch (NoSuchFieldException e) {
			}
		}
		return null;
	}
	
//	public  Method setAccessibleMethodByName(final Object obj,String methodName,Object val) {
//		for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType.getSuperclass()) {
//			Method[] methods = searchType.getDeclaredMethods();
//			for (Method method : methods) {
//				if (method.getName().equals(methodName)) {
//					method.setAccessible(true);
//					try {
//						method.invoke(obj, val);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//					break;
//				}
//			}
//		}
//		return null;
//	}
	
	/**
	 * 向上查找到Object类  获取Field直到找到或没有
	 * 并释放私有属性
	 * @param obj
	 * @param fieldName
	 * @return
	 */
	public Field getAccessibleField(final Object obj,String fieldName) {
		for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				Field field = superClass.getDeclaredField(fieldName);
				field.setAccessible(true);
				return field;
			} catch (NoSuchFieldException e) {
			}
		}
		return null;
	}
	
	/**
	 * 查找实体标注的id字段
	 * @param clas
	 * @return
	 */
//	public Field getFieldId(final Class<?> cl){
//		Field field = null;
//		outer:
//			for (Class<?> superClass = cl; superClass != Object.class; superClass = superClass.getSuperclass()) {
//				Field[] f = superClass.getDeclaredFields();
//				for(int i=0;i<f.length;i++){
//					Custom custom = f[i].getAnnotation(Custom.class);
//					if(custom !=null && !"".equals(custom.IdField())){
//						field = f[i];
//						break outer;
//					}
//				}
//			}
//		if(field == null){
//			try {
//				throw new Exception("==========该实体没有找到idField注解==========");
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		return field;
//	}
	
	/**
	 * 获取id字段key
	 * @param cl
	 * @return
	 */
//	public String getFieldIdName(final Class<?> cl) {
//		Field f = getFieldId(cl);
//		return f==null ? "" : f.getName();
//	}
	
	/**
	 * 获取对象Value
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public static Object getValue(Object obj,String name){
		Object val = null;
		for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				Field field = superClass.getDeclaredField(name);
				field.setAccessible(true);
				try {
					val = field.get(obj);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			} catch (NoSuchFieldException e) {
			}
		}
		return val;
	}
	
	
}
