package com.fxc.admin.jdbc.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fxc.entity.QueryInfo;
import com.fxc.entity.SqlWhereType;
import com.fxc.exception.BusinesException;
import com.fxc.utils.caseConversion;

public class SqlUtil {

	/**
	 * 将list whereValue 的查询值生成数组对象
	 */
	public static Object[] getObjectValue(ArrayList<Object> lis){
		Object[] s = null;
		if(lis != null){
			s = new Object[lis.size()];
			for(int i=0;i<lis.size();i++){
				s[i] = lis.get(i);
//				System.out.println("value"+i+":"+lis.get(i));
			}
		}
		return s;
	}
	
	/**
	 * 获取id,当id为空时抛出异常
	 * @throws Exception 
	 */
	public static String getDelParamsId(QueryInfo qi,String errMsg) throws Exception{
		String id = null;
		id = (String) qi.getParams().get("id");
		if(id == null){
			throw new BusinesException(errMsg);
		}
		return id;
	}
	
	
	/**
	 * 拼接where 取消默认等于
	 * @param where
	 */
	public static void addWhere(StringBuffer where,List<Object> whereVlaue,HashMap<String,Object> whereMa,String alias){
		if(whereMa != null && whereMa.size()>0){
			where.append(" where ");
			String usealias = null;
			if(alias != null)
				usealias = alias+".";
			else
				usealias = "";
			String fieldName = null;
			for (Map.Entry<String, Object> map : whereMa.entrySet()) {
				//用加号区分查询条件 

				String[] name = map.getKey().split("\\_");
				
				String key = map.getKey().substring(name[0].length()+1);
				
				String whe = name[0];//取消默认 必须要带条件
				
				fieldName = caseConversion.camel2Underline(key);
				
//				if(name.length>1){
//					whe = name[0];
//					fieldName = caseConversion.camel2Underline(name[1]);
//				}else{
//					fieldName = caseConversion.camel2Underline(name[0]);
//				}
				
				if(fieldName.indexOf(".") == -1){
					where.append(usealias+fieldName+SqlWhereType.getKey(whe).getExpression()+" and ");
				}else{
					where.append(fieldName+SqlWhereType.getKey(whe).getExpression()+" and ");
				}
//				where.append(usealias+(name.length>1 ? caseConversion.camel2Underline(name[1]) : caseConversion.camel2Underline(name[0]))+SqlWhereType.getKey(whe).getExpression()+" and ");
				//isNull 不需要值
				if(!Treat.isEmpty(map.getValue())){
					whereVlaue.add(setWhereValue(whe,map.getValue()));
				}
			}
			where.delete(where.length()-4,where.length());
		}
	}
	
	
	public static Object setWhereValue(String key,Object value){
		Object v = value;
		if(SqlWhereType.EQ.toString().endsWith(key)){
			return v;
		}else if(SqlWhereType.LIKE.toString().equals(key)){
			v ="%"+v+"%";
		}else if(SqlWhereType.BEFORELIKE.toString().equals(key)){
			v ="%"+v;
		}else if(SqlWhereType.AFTERLIKE.toString().equals(key)){
			v =v+"%";
		}
		return v;
	}
	
}
