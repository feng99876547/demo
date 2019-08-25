package com.fxc.utils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.fxc.admin.jdbc.annotation.Custom;
import com.fxc.admin.jdbc.annotation.Enums;
import com.fxc.admin.jdbc.util.Treat;
import com.fxc.exception.BusinesException;



/**
 * 封装对象
 * @author fxc
 */
public class GetPara {
	
	public static final String REFLECT_INT  = "int";
	
	public static final String REFLECT_LONG  = "long";
	
	public static final String REFLECT_FLOAT  = "float";
	
	public static final String REFLECT_DOUBLE  = "double";
	
	public static final String REFLECT_BOOLEAN  = "boolean";

	public static void load(Object o, HashMap<String,Object> params) throws Exception {
//		//迭代request中提交的参数
//		Enumeration enumeration = request.getParameterNames();
//		while (enumeration.hasMoreElements()) {
//			String key = (String) enumeration.nextElement();
//			
//		}
		//迭代request中提交的参数
		if(params == null){
			System.out.println("==================================================提交参数为空被加载了====================================================");
			return;
		}
		if(params!=null){
			for(Map.Entry<String,Object> map : params.entrySet()){
				load(o, map.getKey(), map.getValue());
			}
		}
	}
	
	public static HashMap<String,Object> getParams(HttpServletRequest request) throws Exception {
		//迭代request中提交的参数
		Enumeration enumeration = request.getParameterNames();
//		HashMap<String,String> ma = null;//没找到判断参数个数的方法只能先创建
		HashMap<String,Object> ma = new HashMap<String,Object>();
		while (enumeration.hasMoreElements()) {
			String key = (String) enumeration.nextElement();
//			System.out.println(key);
			if(key.lastIndexOf("[]")>-1){
				ma.put(key, request.getParameterValues(key));
			}else{
				ma.put(key, request.getParameter(key));
			}
		}
		return ma.size()==0 ? null:ma;
	}
	
	public static <T> T load(Class<T> c, HashMap<String,Object> params)throws BusinesException {
		T o = null;
		try {
			o = c.newInstance();
			GetPara.load(o, params);
			return o;
		} catch (Exception e) {
			 throw new BusinesException("加载"+o.getClass().getSimpleName()+"异常",e);
		} 
	}
	
	/**
	 * 将字符串转成value的类型
	 * @param o
	 * @param value
	 * @param f
	 * @return
	 * @throws Exception
	 */
	public static Object setValue(Object o,Object value , Field f) throws Exception  {
		if(value == null)
			return null;
		if(f.getType() == String.class){
			return value;
		}else if(f.getType().isPrimitive()){
			if(f.getType().getName().equals(REFLECT_INT)){
				value = Integer.parseInt((String)value);
			}else if(f.getType().getName().equals(REFLECT_LONG)){
				value = Long.parseLong((String)value);
			}else if(f.getType().getName().equals(REFLECT_FLOAT)){
				value = Float.parseFloat((String)value);
			}else if(f.getType().getName().equals(REFLECT_DOUBLE)){
				value = Double.parseDouble((String)value);
			}else if(f.getType().getName().equals(REFLECT_BOOLEAN)){
				value = Boolean.parseBoolean((String)value);
			}
		}else{
			if(f.getType() == Date.class){
				try {
					value = MyUtils.sdfDateTime.parse((String) value);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}else if(f.getType().isEnum()){//先查找静态枚举没有在去实例
				Enum<?> e = Enums.getEnum(f.getType(), (String)value);
				if(e == null){
					value = Treat.getEnum(f.getType(), (String) value);
				}else{
					value = e;
				}
			}else if(f.getType() == Object.class){//id使用的时泛型 类型时Object上面的类型都匹配不到
				Class<?> entityClass = o.getClass();
				Class<?> idClass =  (Class<?>) ((ParameterizedType) entityClass.getGenericSuperclass()).getActualTypeArguments()[0];
				//id 一般Integer 和 Long类型 还有就时 string这边也就只对这3种类型进行了处理
				if(idClass == Long.class){//继承的id不支持基本数据类型需要的话优化掉
					value = Long.parseLong((String) value);
				}else if(idClass.getName().equals(REFLECT_LONG)){
					value = Long.parseLong((String)value);
				}else if(idClass == Integer.class){
					value = Integer.parseInt((String) value);
				}else if(idClass.getName().equals(REFLECT_INT)){
					value = Integer.parseInt((String)value);
				}else if(idClass != String.class){
					throw new Exception("装填对象时没有找到id的类型");
				}
			}else if(f.getType() == Long.class){
				value = Long.parseLong((String) value);
			}
			else if(f.getType() == Integer.class){
				value = Integer.parseInt((String) value);
			}
			else if(f.getType() == Double.class){
				value = Double.parseDouble((String) value);
			}else if(f.getType() == BigDecimal.class){
				value = new BigDecimal((String) value);
			}
		}
		return value;
	}
	
	
	
	public static void load(Object o, String names, Object value) throws Exception  {
//		if (Treat.isEmpty(names) || Treat.isEmpty(value)){
		if (Treat.isEmpty(names) || value.equals("")){// " "将认为非空 在load之前都是前端提交的字符串还没转换类型
			return;
		}
		//不是对象
		if (names.indexOf(".") == -1) {
			Field f = Reflections.getAccessibleField(o, names);
			//noUpload 和 roleUpload不加载  roleUpload注释字段在业务中进行处理
			if (f!=null && 
					(f.getAnnotation(Custom.class)==null ? true : (f.getAnnotation(Custom.class).noUpload()==false))) {
				value = setValue(o,value,f);
				f.set(o, value);
			}
		} else {
			//是对象 
			String[] name = names.split("\\.");
			if(name.length!=2) return;
			Field dxobj = Reflections.getAccessibleField(o, name[0]);
			//格式是实体中的字段name加name中的属性 如 private ShopStype ShopStype 就是 ShopStype.ShopStypeId
			if (dxobj!=null && 
				(dxobj.getAnnotation(Custom.class)==null ? true : (dxobj.getAnnotation(Custom.class).noUpload()==false))) {
				Class<?> cl = dxobj.getType();
				if(Treat.isEmpty(dxobj.get(o)))
					dxobj.set(o, cl.newInstance());
				Field fid =Reflections.getAccessibleField(dxobj.get(o), name[1]);
				if(fid!=null&& 
						(fid.getAnnotation(Custom.class)==null ? true : (fid.getAnnotation(Custom.class).noUpload()==false))){
					fid.set(dxobj.get(o), setValue(o,value,fid));
				} 
			}
		}
	}
	
	/**
	 * 获取前端提交的查询条件 （这边还没有对key进行验证是否被注入到时候记得加上）
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static HashMap<String,Object> getSearch(HashMap<String,Object> request) throws Exception{
		if(request == null){
			return null;
		}
		HashMap<String,Object> searchs = new HashMap<String,Object>();
		for(Map.Entry<String,Object> ma : request.entrySet()){
			if(ma.getKey().indexOf("search_")==0){
				if(!Treat.isEmpty(ma.getValue())){
					String setkey = ma.getKey().substring(7);
					searchs.put(setkey, ma.getValue());
				}
			}
		}
		return searchs;
	}
	
}
