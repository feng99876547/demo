package com.sjb.util;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.fxc.entity.Result;
import com.sjb.model.system.User;
import com.sjb.util.session.SystemUtil;



//import org.apache.commons.beanutils.PropertyUtilsBean;
//import com.octo.captcha.component.word.wordgenerator.RandomWordGenerator;
//import com.octo.captcha.component.word.wordgenerator.WordGenerator;


public class Treat {
//	String msg = "{0}{1}{2}{3}{4}{5}{6}{7}{8}";  
//	 Object [] array = new Object[]{"A","B","C","D","E","F","G","H","I",};         
//	 String value = MessageFormat.format(msg, array);  
//	   
//	 System.out.println(value);  // 输出：ABCDEFGHI  
//	/**
//	 * 固定随机编码
//	 */
//	public final static WordGenerator wordGenerator = new RandomWordGenerator("1234567890abcdefghijkmnpqrstuvwxyz");
//	
	public static Random random = new Random();
	
	/**
	 * 判断Object 对象是否为空
	 * true 为空
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
		return o == null || o.equals("") ;
	}
	
	//获取ip
	public static String getRemoteHost(HttpServletRequest request){
	    String ip = request.getHeader("x-forwarded-for");
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getHeader("Proxy-Client-IP");
	    }
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getHeader("WL-Proxy-Client-IP");
	    }
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getRemoteAddr();
	    }
	    return ip.equals("0:0:0:0:0:0:0:1")?"127.0.0.1":ip;
	}

	
	/**
	 * 判断当前登录角色是否为管理员
	 * true 为超级管理员
	 */
	public static boolean isAdmin(){
		Long id = SystemUtil.getUserId();
		if( id != null){
			return isAdmin(Integer.parseInt(id.toString()));
		}
		return false;
	}
	
	/**
	 * 判断当前登录角色是否为管理员
	 * true 为超级管理员
	 */
	public static boolean isAdmin(int id){
		if(1==id)
			return true;
		return false;
	}
	
	

	/**
	 * 把前端提交的map合并到后台定义的map 出现同名key将使用后台定义的key和value
	 * @param accq 前端提交的map（自动捕获的ma）
	 * @param ma 后台定义的map(不能为空)
	 * @return Map<String,Object>
	 * @throws Exception 
	 */
	public static void combineMap(Map<String,Object> accq,Map<String,Object> ma) throws Exception{
		if(ma == null)
			throw new Exception("接收对象ma不能为空");
		if(accq!=null && accq.size()>0){
			out: for (Map.Entry<String, Object> ac : accq.entrySet()) {
				for (Map.Entry<String, Object> map : ma.entrySet()) {
					String[] accnames = ac.getKey().split("\\_");
					String[] manames = map.getKey().split("\\_");
					String accname = accnames.length == 2 ? accnames[1] : accnames[0];
					String maname = manames.length == 2 ? manames[1] : manames[0];
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
	
	public static String getEnumName(Class<?> clas){
		return clas.getSimpleName();
	}
	
	/**
	 * 通过下标实例枚举
	 */
	public static Enum<?> getEnum(Class<?> clas,int i){
		Object[] ob = clas.getEnumConstants();
		Enum<?> em = (Enum<?>) ob[i];
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
	 * 生成流水号
	 * 要保证唯一使用公共redis对相同的salt上锁 保证并发时相同的salt只有一个能生效没拿到锁视为操作失败
	 */
	public static String getSerialNum(String salt){
		String str = null;
		str = System.currentTimeMillis()+random.nextInt(1000)+salt;
		String rlust = str.substring(3);
		return rlust.length()>20 ? rlust.substring(0,20) : rlust;
	}
	
	public static String getSerialNum(){
		return getSerialNum(SystemUtil.getUserId().toString());
	}
	
	public static int random(){
		return (int)(Math.random() * (1000000 - 100000)+100000);
	}

	/**
	 * 合并数组
	 */
	public static Object[] packedArray(Object[] arr1,Object[] arr2){
		Object[] arr3 =new Object[arr1.length+arr2.length]; 
	    System.arraycopy(arr1, 0, arr3,0,arr1.length);  
	    System.arraycopy(arr2, 0, arr3,arr1.length,arr2.length);  
//	    System.out.print(arr3);
	    return arr3;
	}
	
	/**
	 * 用于获取json参数中不能为空的值
	 * json 为空抛出异常 key值为空也抛出异常
	 * @param json
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static Object JsonIsNotNull(JSONObject json,String key) throws Exception{
		Object obj = json.getString(key);
		if(isEmpty(obj)){
			throw new Exception("必须参数不能为空!");
		}
		return obj;
	}
	
	/** 获取当前 年 月 日  小于10 补0*/
	public static String getToDay(){
		Calendar c = Calendar.getInstance();
		return getYear(c)+getMonth(c)+getDay(c);
	}
	
	/** 获取当前 年 */
	public static String getYear(Calendar c){
		return String.valueOf(c.get(Calendar.YEAR));
	}
	
	/** 获取当前 月  格式2位 xx*/
	public static String getMonth(Calendar c){
		int m = c.get(Calendar.MONTH)+1;
		if(m<10){
			return "0"+String.valueOf(m);
		}else{
			return String.valueOf(m);
		}
		
	}
	
	/** 获取当前 日  格式2位 xx*/
	public static String getDay(Calendar c){
		int d = c.get(Calendar.DATE);
		if(d<10){
			return "0"+String.valueOf(d);
		}else{
			return String.valueOf(d);
		}
	}
	
	/**
	 * 验证session中的验证码是否过期
	 * @param request
	 * @param code
	 * @param key
	 * @return
	 */
	public static Result<?> checkedYZM(HttpServletRequest request,String code,String key){
		Result<?> result = new Result<User>(false);
		if(request.getSession()==null || request.getSession().getAttribute(key) == null){
			result.setMsg("验证码过期");
		}else if(!code.equals(request.getSession().getAttribute(key))){
			result.setMsg("验证码不正确");
		}else{
			result.setSuccess(true);
		}
		return result;
	}
	
	public static ModelAndView setModelAndView(Object obj){
		ModelAndView m = new ModelAndView();
		m.getModelMap().addAttribute(obj);
		return m;
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
	
	
}
