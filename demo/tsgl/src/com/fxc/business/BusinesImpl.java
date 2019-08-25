package com.fxc.business;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fxc.admin.jdbc.annotation.Custom;
import com.fxc.admin.jdbc.annotation.CustomEnum;
import com.fxc.admin.jdbc.service.DispatchTask;
import com.fxc.admin.jdbc.service.PublicAPI;
import com.fxc.entity.QueryInfo;
import com.fxc.entity.Result;
import com.fxc.exception.BusinesException;

/**
 * 默认自动业务处理事件源
 * 可以更改为策略模式这样易于扩展
 * 根据注解收集对应的策略保存到集合中 key使用className className对应自定义的字段
 * @author fxc
 *
 */
public class BusinesImpl<T,PK,DB> implements Busines<T,PK,DB>{

	private BusinesProcessor businesProcessor;
	
//	private static final HashMap<String,CustomBusines> ma = new HashMap<String,CustomBusines>();
	
	public void setBusinesProcessor(BusinesProcessor businesProcessor) {
		this.businesProcessor = businesProcessor;
	}
	
	/**
	 * 收集实体中 业务需要处理的注释字段
	 * @param ma
	 * @param clas
	 */
	public static HashMap<String,ArrayList<Field>> setBusiness(Class<?> clas){
		HashMap<String,ArrayList<Field>> ma = new HashMap<String,ArrayList<Field>>();
		Field[] fis = clas.getDeclaredFields();
		for(Field f : fis){
			if(f.getAnnotation(Custom.class)!=null){
				if(!"".equals(f.getAnnotation(Custom.class).DefaultStringValue())){
					isThere(ma,"DefaultStringValue").add(f);
				}
				if(f.getAnnotation(Custom.class).DefaultIntValue()>-1){
					isThere(ma,"DefaultIntValue").add(f);
				}
				if(f.getAnnotation(Custom.class).isUnique()==true){
					isThere(ma,"isUnique").add(f);
				}
				if(!"".equals(f.getAnnotation(Custom.class).createInjectUser())){
					isThere(ma,"createInjectUser").add(f);
				}if(!"".equals(f.getAnnotation(Custom.class).isUpdateUser())){
					isThere(ma,"isUpdateUser").add(f);
				}if(!"".equals(f.getAnnotation(Custom.class).NotSelect())){
					isThere(ma,"NotSelect").add(f);
				}
				if(f.getAnnotation(Custom.class).NotNull()==true){
					isThere(ma,"NotNull").add(f);
				}
				if(!"".equals(f.getAnnotation(Custom.class).Binding().FieldName())
						&& !"".equals(f.getAnnotation(Custom.class).Binding().FieldValue())){
					isThere(ma,"Binding").add(f);
				}
				if(!"".equals(f.getAnnotation(Custom.class).associationField())){
					isThere(ma,"associationField").add(f);
				}
				if(f.getAnnotation(Custom.class).Fieldassociation().length>0){
					isThere(ma,"Fieldassociation").add(f);
				}
				if(!"".equals(f.getAnnotation(Custom.class).FixedIntValue())){
					isThere(ma,"FixedIntValue").add(f);
				}
				
			}
			if(f.getAnnotation(CustomEnum.class)!=null){
				isThere(ma,"CustomEnum").add(f);
			}
		}
		return ma;
	}
	
	public static ArrayList<Field> isThere(HashMap<String,ArrayList<Field>> ma,String key){
		if(ma.get(key)==null)
			ma.put(key,new ArrayList<Field>());
		return ma.get(key);
	}

	@Override
	public boolean add(QueryInfo qi, PublicAPI<T, PK, DB> api, Result<T> result) throws Exception {
		Object obj = result.getRows();
		HashMap<String,ArrayList<Field>> businessFields = api.getBusinessFields();
		for(Map.Entry<String, ArrayList<Field>> li : businessFields.entrySet()){
			String results = null;
			switch(li.getKey()){
				case "isUnique":
					results = businesProcessor.validationUnique(obj,li.getValue());
					if(results != null){
						throw new BusinesException("操作失败:"+obj.getClass().getName()+"("+results+")是唯一字段");
					}
					break;
				case "NotNull":
					results = businesProcessor.setNotNull(obj,li.getValue());//setNotNull 字段为空返回name
					if(results != null){
						throw new BusinesException("操作失败:"+obj.getClass().getName()+"("+results+")不 能为空");
					}
					break;
				case "createInjectUser"://添加时注入session中对应的字段
					businesProcessor.setisCreateUser(obj,li.getValue());
					break;
//				case "isCreateName":	
//					businesProcessor.setisUserName(obj,li.getValue());
//					break;
				case "DefaultStringValue":	
					businesProcessor.setDefaultValue(obj,li.getValue());
					break;
				case "DefaultIntValue":	
					businesProcessor.setDefaultValue(obj,li.getValue());
					break;
				case "CustomEnum":
					businesProcessor.setCustomEnumValue(obj,li.getValue());
					break;
				case "Binding":
					businesProcessor.setBinding(obj,li.getValue());
					break;
				case "associationField":
					businesProcessor.setAssociationField(obj,li.getValue());
					break;
				case "Fieldassociation":
					businesProcessor.setFieldassociation(obj,li.getValue());
					break;
				case "FixedIntValue":
					businesProcessor.setFixedIntValue(obj,li.getValue());
					break;
			}
		}
		return true;
	}

	@Override
	public void addCallback(QueryInfo qi, PublicAPI<T, PK, DB> api, Result<T> result) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean del(QueryInfo qi, PublicAPI<T, PK, DB> api, Result<List<T>> result) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void delCallback(QueryInfo qi, PublicAPI<T, PK, DB> api, Result<List<T>> result) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean update(QueryInfo qi, PublicAPI<T, PK, DB> api, Result<T> result) throws Exception {
		Object obj = result.getRows();
		HashMap<String,ArrayList<Field>> businessFields = api.getBusinessFields();
		if(businessFields.get("isUnique")!=null){
			String results = businesProcessor.validationUniqueUpDate(obj,businessFields.get("isUnique"));
			if(results != null){
				throw new BusinesException("修改失败: "+results+" 已经存在");
			}
		}
		if(businessFields.get("NotNull")!=null){
			String results = businesProcessor.setUpdateNotNull(obj,businessFields.get("NotNull"));
			if(results != null){
				throw new BusinesException("操作失败:"+obj.getClass().getName()+"("+results+")不 能为空");
			}
		}
		if(businessFields.get("isUpdateUser")!=null){
			businesProcessor.setisUpdateUser(obj,businessFields.get("isUpdateUser"));
		}
		if(businessFields.get("Binding")!=null){
			businesProcessor.setBinding(obj,businessFields.get("Binding"));
		}

		return true;
	}

	@Override
	public void updateCallback(QueryInfo qi, PublicAPI<T, PK, DB> api, Result<T> result) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean gets(QueryInfo qi, PublicAPI<T, PK, DB> api, Result<List<T>> result) throws Exception {
		HashMap<String,ArrayList<Field>> businessFields = api.getBusinessFields();
		if(businessFields.get("NotSelect")!=null){
			businesProcessor.setNotSelect(qi,businessFields.get("NotSelect"));
		}
		return true;
	}

	@Override
	public void getsCallback(QueryInfo qi, PublicAPI<T, PK, DB> api, Result<List<T>> result) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean get( QueryInfo qi, PublicAPI<T, PK, DB> api,
			Result<T> result,ArrayList<DispatchTask> list,int index) throws Exception {
		System.out.println("----busines执行验证业务了------");
		next(qi, api, result, list, index);
		getCallback(qi, api, result);
		return true;
	}

	@Override
	public void getCallback( QueryInfo qi, PublicAPI<T, PK, DB> api,
			Result<T> result) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("----busines回调了不需要验证业务------");
	}
}
