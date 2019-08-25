package com.fxc.admin.jdbc.util;

import java.util.Collection;

public class Treat {
	
	/**
	 * 判断对象是否为实体
	 * 返回true为非自定义类型 false为自定义类型
	 * @param clz
	 * @return
	 */
	public static boolean isJavaClass(Class<?> clz) {  
	    return clz != null && clz.getClassLoader() == null;  
	}
	
	/**
	 * 判断Object 对象是否为空
	 * true 空 false 非空
	 * @param o
	 * @return
	 */
	public static boolean isEmpty(Object o) {
		if (o instanceof String) {
			return o == null || o.toString().trim().equals("");
		} else if (o instanceof Collection) {
			@SuppressWarnings("rawtypes")
			Collection collection = (Collection) o;
			return o == null || collection.size() < 1;
		} else if (o instanceof Integer) {
			Integer integer = (Integer) o;
			return o == null || integer.equals("0");
		} else if (o instanceof Long) {
			Long l = (Long) o;
			return o == null || l.equals("0");
		}
		return o == null || o.equals("");
	}
	
	/**
	 * 实例枚举
	 */
	public static Enum<?> getEnum(Class<?> clas,String name){
		Object[] ob = clas.getEnumConstants();
		Enum<?> em = null;
		for(Object o : ob){
			if(name.equals(o.toString())){
				em =(Enum<?>) o;
				break;
			}
		}
		return em;
	}
	

	/**
	 * 返回枚举序列
	 */
	public static int getEnumOrdinal(Class<?> clas,String name){
		Enum<?> em = (Enum<?>) getEnum(clas,name);
		return em.ordinal();
	}
	
	/**
	 * 把前端提交的map合并到后台定义的map 出现同名key将使用后台定义的key和value
	 * @param accq 前端提交的map（自动捕获的ma）
	 * @param ma 后台定义的map(不能为空)
	 * @return Map<String,Object>
	 * @throws Exception 
	 */
//	public static void combineMap(Map<String,Object> accq,Map<String,Object> ma) throws Exception{
//		if(ma == null)
//			throw new Exception("接收对象ma不能为空");
//		if(accq!=null && accq.size()>0){
//			out: for (Map.Entry<String, Object> ac : accq.entrySet()) {
//				for (Map.Entry<String, Object> map : ma.entrySet()) {
//					String[] accnames = ac.getKey().split("\\-");
//					String[] manames = map.getKey().split("\\-");
//					String accname = accnames.length == 2 ? accnames[1] : accnames[0];
//					String maname = manames.length == 2 ? manames[1] : manames[0];
//					if (accname.trim().equals(maname.trim())) {
//						// 出现同名取消添加 以ma为主
//						continue out;
//					}
//				}
//				ma.put(ac.getKey(), ac.getValue());
//			}
//		}
//	}
}
