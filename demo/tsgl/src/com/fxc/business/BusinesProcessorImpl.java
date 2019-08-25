package com.fxc.business;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


import com.fxc.admin.jdbc.GetsJdbc;
import com.fxc.admin.jdbc.annotation.Custom;
import com.fxc.admin.jdbc.annotation.CustomBinding;
import com.fxc.admin.jdbc.annotation.CustomEnum;
import com.fxc.admin.jdbc.annotation.Enums;
import com.fxc.admin.jdbc.util.FieldUtil;
import com.fxc.admin.jdbc.util.SqlCache;
import com.fxc.admin.jdbc.util.Treat;
import com.fxc.entity.QueryInfo;
import com.fxc.exception.BusinesException;
import com.fxc.utils.ContextUtils;
import com.fxc.utils.GetPara;
import com.fxc.utils.Reflections;
import com.sjb.model.UserKey;
import com.sjb.model.system.User;
import com.sjb.util.session.SystemUtil;


@SuppressWarnings({"rawtypes","unchecked"})
public class BusinesProcessorImpl implements  BusinesProcessor{

	public static GetsJdbc GetsJdbc(){
		return (GetsJdbc) ContextUtils.appContext.getBean("getsJdbc");
	}
	
	public BusinesProcessorImpl() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 验证字段是否存在 存在返回true 不存在返回false
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 */
	@Override
	public  String validationUnique(Object obj,ArrayList<Field> fields) throws  Exception{
		String na = null;
		Class<?> clas = obj.getClass();
		for(Field f : fields){
			HashMap<String,Object> ma = new HashMap<String,Object>();
			f.setAccessible(true);
			Object val = f.get(obj);
			if(!Treat.isJavaClass(f.getType())){
				for(Annotation ann : f.getAnnotations()){
					try {
						if(ann.annotationType().toString().indexOf("com.fxc.admin.jdbc.annotation.custom")>-1){
							Method m = Reflections.getAccessibleMethod(ann,"pointAssField");
							if(m!=null){
								String name = m.invoke(ann).toString();
								if(name == null){
									throw new Exception(ann.getClass().toString()+"的pointAssField的值为空");
								}else{
									Field pf = Reflections.getAccessibleField(val,name);
									val = pf.get(val);
								}
							}
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			if(val!=null){
				ma.put("EQ_"+f.getName(), val);
				if(GetsJdbc().executeCount(ma,clas)>0){
					na = f.getAnnotation(Custom.class).name();
					break;
				}
			}
		}
		return na;
	}
	
	/**
	 * 修改时验证字段是否存在 存在返回true 不存在返回false
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 */
	@Override
	public  String validationUniqueUpDate(Object obj,ArrayList<Field> fields) throws Exception{
		Class<?> clas = obj.getClass();
		String idName = SqlCache.getCacheSqlInfo(clas).getIdKey();
		Object val = FieldUtil.getValue(obj, idName);
		if(idName == null || val == null){
			throw new BusinesException(clas.toString()+"没有找到idField注释  或id值为空 ");
		}
		ArrayList<Field> lis = new ArrayList<Field>();
		Field fid = Reflections.getAccessibleField(obj,idName);
		Object idVal = fid.get(obj);
		//查修改之前的数据 匹配是否和修改提交的一样
		QueryInfo q = new QueryInfo();
		q.getSearch().put("EQ_"+idName, idVal);
		q.setCancelConnection(true);
		Object searchObj = GetsJdbc().executeGet(q,clas);
		for(Field f : fields){
			f.setAccessible(true);
			//唯一字段和修改之前不一样继续匹配 一样不修改
			if(f.get(obj)!=null && !f.get(obj).equals(f.get(searchObj))){
				lis.add(f);
			}
		}
		return validationUnique(obj,lis);
	}
	
	/**
	 * 查询时更具注释业务添加不主动查询字段
	 * 也可以考虑根据角色过滤字段
	 * @param obj
	 * @param fields
	 */
	@Override
	public  void setNotSelect(QueryInfo qi,ArrayList<Field> fields) throws Exception{
		if(qi != null){
			for(Field f : fields){
				qi.addCancel(f.getAnnotation(Custom.class).NotSelect());
			}
		}
	}
	
	/**
	 * 注入默认枚举值
	 */
	@Override
	public void setCustomEnumValue(Object obj, ArrayList<Field> fields) throws Exception {
		for(Field field : fields){
			field.setAccessible(true);
			Object val = field.get(obj);
			if(val==null){
				setDefaultValue(obj,field);
			}
		}
		
	}
	
	/**
	 * 如果值为空注入注释默认值
	 * @param obj
	 * @param fields
	 * @throws Exception 
	 */
	@Override
	public  void setDefaultValue(Object obj,ArrayList<Field> fields) throws Exception {
		for(Field field : fields){
			field.setAccessible(true);
			Object val = field.get(obj);
			if(val==null){
				setDefaultValue(obj,field);
			}else if(field.getType().isPrimitive()){//使用基本类型要注意初始化时的值就是0 所以使用的状态中不能用0 提交0会被认为无值会被附上默认值
				if(Integer.parseInt(val.toString())==0){//基本类型中默认值只支持数字 不包含bolean char byte short等
					setDefaultValue(obj,field);
				}
			}
		}
	}
	
	public  void setDefaultValue(Object obj,Field f) throws Exception{
		Object val =null;
		if(f.getType() == String.class){//如果val是默认值""说明没有设置
			val = f.getAnnotation(Custom.class).DefaultStringValue();
		}else if(f.getType().isEnum()){
			if(f.getAnnotation(CustomEnum.class)!=null){
				if(f.getAnnotation(CustomEnum.class).DefaultIntValue()>=0){//如果设置了下标优先使用下标
					int i = f.getAnnotation(CustomEnum.class).DefaultIntValue();
					val = Enums.getEnum(f.getType(),i);
				}else{
					String key = f.getAnnotation(CustomEnum.class).DefaultValue();
					val = Enums.getEnum(f.getType(),key);
				}
			}
		}else{
			val = (int) f.getAnnotation(Custom.class).DefaultIntValue();
		}
		f.set(obj,val);
	}
	
	
	/**
	 * 新增时注入当前登录帐号用户
	 * @param obj
	 * @param fields
	 * @param clas
	 * @param req
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	@Override
	public  void setisCreateUser(Object obj,ArrayList<Field> fields) throws Exception{
		if(SystemUtil.getUser()!=null){
			for(Field f : fields){
				f.setAccessible(true);
				Custom  cu = f.getAnnotation(Custom.class);
				String keyName = cu.createInjectUser();
//				if("".equals(keyName)){//一个字段 应该在创建是修改 修改时也修改吗
//					keyName = cu.isUpdateUser();
//				}
				setUser(keyName,f,obj);
				
//				if(Treat.isJavaClass(f.getType())){
//					f.set(obj,SystemUtil.getUser().getId());
//				}else{
//					f.set(obj,new User(SystemUtil.getUser().getId()));
//				}
				
			}
		}
	}
	
	/**
	 * 修改时注入当前登录帐号用户
	 * @param obj
	 * @param fields
	 * @param clas
	 * @param req
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	@Override
	public  void setisUpdateUser(Object obj,ArrayList<Field> fields) throws Exception{
		if(SystemUtil.getUser()!=null){
			for(Field f : fields){
				f.setAccessible(true);
				Custom  cu = f.getAnnotation(Custom.class);
				String keyName = cu.isUpdateUser();
				setUser(keyName,f,obj);
			}
		}
	}
	
	
	public void setUser(String keyName,Field f,Object obj) throws Exception{
		switch (keyName) {
			case UserKey.id:
				f.set(obj, SystemUtil.getUserId());
				break;
			case UserKey.systemUser:
				f.set(obj,new User(SystemUtil.getUserId()));
				break;
			case UserKey.newDate:
				if(f.getType() == Integer.class){//System.currentTimeMillis() 13 位时间挫 强转int丢失了
					String time = String.valueOf(System.currentTimeMillis());
					f.set(obj,Integer.valueOf(time.substring(0,10)));//使用时要乘1000
				}else if(f.getType() == Long.class){
					f.set(obj,System.currentTimeMillis());
				}else{
					f.set(obj, new Date());
				}
				break;
			default:
				throw new BusinesException("程序有错,大兄弟setisUser的"+keyName+"没有配置");
	//			break;
		}
	}
	
	
	/**
	 * 验证字段是否为空
	 * @param obj
	 * @param fields
	 * @param query
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@Override
	public  String setNotNull(Object obj,ArrayList<Field> fields) throws Exception{
		for(Field f : fields){
			f.setAccessible(true);
			Object o = f.get(obj);
			if(Treat.isEmpty(o)){
				return f.getAnnotation(Custom.class).name();
			}
		}
		return null;
	}

	/**
	 * 修改时验证非空字段提交的值是否为空
	 * @param obj
	 * @param fields
	 * @param query
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@Override
	public  String setUpdateNotNull(Object obj,ArrayList<Field> fields) throws Exception{
		for(Field f : fields){
			if(f.getType() == String.class){
				f.setAccessible(true);
				String o = (String) f.get(obj);
				//非空字段如果是字符串 并且是空的 " "（加载参数时不过滤空格了）报错
				if (o!=null && o.trim().equals("")) {
					return f.getAnnotation(Custom.class).name();
				}
			}
		}
		return null;
	}
	
	/**
	 * 在我不为空时还要对指定的字段赋值
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	@Override
	public boolean setBinding(Object obj, ArrayList<Field> fields) throws Exception {
		for(Field f : fields){
			f.setAccessible(true);
			if(!Treat.isEmpty(f.get(obj))){
				CustomBinding bind = null;
				try {
					bind = f.getAnnotation(Custom.class).Binding();
					String name = bind.FieldName();
					String value = bind.FieldValue();
					Field fi = obj.getClass().getDeclaredField(name);
					fi.setAccessible(true);
					Object val = GetPara.setValue(obj.getClass(),value ,fi);
					if(val == null){
						throw new BusinesException(f.getName()+"字段bind注解的fieldValue找不到对应的枚举");
					}
					fi.set(obj, val);
				} catch (Exception e) {
					throw new BusinesException(obj.getClass().getName()+"在执行bunines业务时执行setBinding方法赋值异常,异常字段field:"+f.getName(),e);
				}
				
			}
		}
		return true;
	}

	/**
	 * 如果它不为空 我也不能为空
	 * 如果它为空 我也必须为空
	 */
	@Override
	public void setAssociationField(Object obj, ArrayList<Field> fields) throws Exception {
		for(Field f : fields){
			f.setAccessible(true);
			if(Treat.isEmpty(f.get(obj))){//如果我为空要判断关联的是否为空 如果我不为空就不需要判断
				String assname = f.getAnnotation(Custom.class).associationField();
				Field fi = obj.getClass().getDeclaredField(assname);
				fi.setAccessible(true);
				if(!Treat.isEmpty(fi.get(obj))){//如果他不为空我为空时需要抛出异常 他为空我也为空是正常的
					throw new BusinesException("如果"+f.getName()+"为空,那么"+fi.getName()+"不为空是不对的,因为"+fi.getName()+"不为空"+f.getName()+"不能为空");
				}
			}else{
				String assname = f.getAnnotation(Custom.class).associationField();
				Field fi = obj.getClass().getDeclaredField(assname);
				fi.setAccessible(true);
				if(Treat.isEmpty(fi.get(obj))){//如果我不为空 那么它为空 这是不对的
					throw new BusinesException("如果"+f.getName()+"不为空,那么"+fi.getName()+"为空是不对的,因为"+fi.getName()+"为空"+f.getName()+"不要为空");
				}
			}
		}
	}

	/**
	 * 值是由后台定义写死的
	 */
	@Override
	public void setFixedIntValue(Object obj, ArrayList<Field> fields) throws Exception {
		for(Field f : fields){
			f.setAccessible(true);
			String fixedIntValue = f.getAnnotation(Custom.class).FixedIntValue();
			f.set(obj, fixedIntValue);
		}
	}

	/**
	 * 我不为空时 我指向的那几个字段也不能为空
	 */
	@Override
	public void setFieldassociation(Object obj, ArrayList<Field> fields) throws Exception {
		for(Field f : fields){
			f.setAccessible(true);
			Object val = f.get(obj);
			if(!Treat.isEmpty(val)){//如果我不为空
				String[] fieldassociation = f.getAnnotation(Custom.class).Fieldassociation();
				if(fieldassociation[0].equals(val.toString())){//并且值符合条件时
					for(int i = 1;i<fieldassociation.length;i++){
						Field fi = obj.getClass().getDeclaredField(fieldassociation[i]);
						fi.setAccessible(true);
						if(Treat.isEmpty(fi.get(obj))){
							throw new BusinesException(f.getName()+"不为空时,"+fi.getName()+"为空,这是不对的");
						}
					}
				}
			}
		}
	}
	
}
