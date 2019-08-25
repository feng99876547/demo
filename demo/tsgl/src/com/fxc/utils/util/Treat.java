package com.fxc.utils.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;






//import org.apache.commons.beanutils.PropertyUtilsBean;
//import com.octo.captcha.component.word.wordgenerator.RandomWordGenerator;
//import com.octo.captcha.component.word.wordgenerator.WordGenerator;


public class Treat {
	
//	/**
//	 * 固定随机编码
//	 */
//	public final static WordGenerator wordGenerator = new RandomWordGenerator("1234567890abcdefghijkmnpqrstuvwxyz");
//	
	
	/**
	 * 判断Object 对象是否为空
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
		} else if (o instanceof List || o instanceof List){
			return isListEmpty((List<Object>) o);
		} else if (o instanceof Map || o instanceof HashMap){
			return isMapEmpty((Map<String, Object>) o);
		}
		return o == null || o.equals("") ;
	}
	
	/**
	 * 判断List 对象是否为空
	 * @param o
	 * @return
	 */
	public static boolean isListEmpty(List<Object> obj) {
		if(obj == null || obj.size() == 0){
			return true;
		}
		return false;
	}
	
	/**
	 * 判断Map 对象是否为空
	 * @param o
	 * @return
	 */
	public static boolean isMapEmpty(Map<String,Object> obj) {
		if(obj == null || obj.size() == 0){
			return true;
		}
		return false;
	}
	
	
//	/**
//	 * 递归查找子类
//	 * @param sellPermissions
//	 * @param obj
//	 */
//	public static void setChildPermissions(List<SellPermissions> sellPermissions,SellPermissions obj){
//		for(SellPermissions sp : sellPermissions){
//			if(sp.getGlzt() != null)//已经归类过了直接跳过
//				continue;
//			//如果是父子
//			//sp.getFapermissions()!=null排除为归类的父类
//			if(sp.getFapermissions()!=null && sp.getFapermissions().getPermissions_id() == obj.getPermissions_id()){
//				sp.setGlzt(1);
//				obj.addChildPermissionsid(sp);
//				setChildPermissions(sellPermissions,sp);
//				
//			}
//		}
//	}
	
//	/**
//	 * 权限归类
//	 */
//	public static List<SellPermissions> SetPermissions(List<SellPermissions> sellPermissions){
//		List<SellPermissions> permissions = null;
//		if(sellPermissions!=null){
//			permissions = new ArrayList<SellPermissions>();
//			
//			for(SellPermissions sp : sellPermissions){
//				//找出父类
//				if(sp.getFapermissions() == null){
//					sp.setGlzt(1);
//					setChildPermissions(sellPermissions,sp);
//					permissions.add(sp);
//				}
//			}
//		}
//		return permissions;
//	}

	
//	/**
//	 * 判断当前登录角色是否为管理员
//	 */
//	public static boolean isAdmin(){
//		if(SystemUtil.getUser() !=null){
//			return isAdmin(SystemUtil.getUser());
//		}
//		return false;
//	}
//	
	/**
	 * 判断当前登录角色是否为管理员
	 */
//	public static boolean isAdmin(CarUser user){
//		if("1".endsWith(user.getId()))
//			return true;
//		return false;
//	}
	
//	/**
//	 * 判断当前登录角色是否为管理员
//	 */
//	public static boolean isAdmin(HttpServletRequest request){
//		if(request.getSession().getAttribute("carUser") != null){
//			return isAdmin((CarUser) request.getSession().getAttribute("carUser"));
//		}
//		return false;
//	}
	

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
//					String[] accnames = ac.getKey().split("\\_");
//					String[] manames = map.getKey().split("\\_");
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
	
	public static void combineMap(Map<String,Object> accq,Map<String,Object> ma) throws Exception{
		if(ma == null)
			throw new Exception("接收对象ma不能为空");
		if(accq!=null && accq.size()>0){
			out: for (Map.Entry<String, Object> ac : accq.entrySet()) {
				for (Map.Entry<String, Object> map : ma.entrySet()) {
					String[] accnames = ac.getKey().split("\\_");
					String[] manames = map.getKey().split("\\_");
					String accname = ac.getKey().substring(accnames[0].length()+1);
					String maname = map.getKey().substring(manames[0].length()+1);
					if (accname.trim().equals(maname.trim())) {
						// 出现同名取消添加 以ma为主
						continue out;
					}
				}
				ma.put(ac.getKey(), ac.getValue());
			}
		}
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
	 * 合并数组
	 */
	public static Object[] packedArray(Object[] arr1,Object[] arr2){
		Object[] arr3 =new Object[arr1.length+arr2.length]; 
	    System.arraycopy(arr1, 0, arr3,0,arr1.length);  
	    System.arraycopy(arr2, 0, arr3,arr1.length,arr2.length);  
	    return arr3;
	}
	
//	/**
//	 * 实体转map
//	 * @param obj
//	 * @return
//	 */
//	public static Map<String, Object> beanToMap(Object obj) { 
//	     Map<String, Object> params = new HashMap<String, Object>(0); 
//	     try { 
//	    	 PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean(); 
//	         PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(obj); 
//	         for (int i = 0; i < descriptors.length; i++) { 
//	             String name = descriptors[i].getName(); 
//	             if (!"class".equals(name)) { 
//                     params.put(name, propertyUtilsBean.getNestedProperty(obj, name)); 
//                 } 
//             } 
//         } catch (Exception e) { 
//             e.printStackTrace(); 
//         } 
//         return params; 
//	}
	
	
	
	
	
//	/**
//	 * 获取会员等级
//	 * @throws Exception 
//	 */
//	public static PlatformMembers getMembersGrade(Double historicalConsumption) throws Exception{
//		if(ContextUtils.platformMembers==null || ContextUtils.platformMembers.size()==0){
//			throw new Exception("没有设置会员等级");
//		}
//		PlatformMembers p = null;
//		if(historicalConsumption==null)
//			historicalConsumption = 0.0;
//		//如果消费积分大于等于最高级别的结束分段返回最高级别
//		if(historicalConsumption>=ContextUtils.platformMembers.get(ContextUtils.platformMembers.size()-1).getBeginSubsection()){
//			p = (PlatformMembers)ContextUtils.platformMembers.get(ContextUtils.platformMembers.size()-1).clone();
//		}else{
//			for(PlatformMembers pm: ContextUtils.platformMembers){
//				if(historicalConsumption>= pm.getBeginSubsection() && historicalConsumption<pm.getEndSubsection()){
//					p = (PlatformMembers)pm.clone();
//					break;
//				}
//			}
//		}
//		return p;
//	}
	
	
	
	
}
