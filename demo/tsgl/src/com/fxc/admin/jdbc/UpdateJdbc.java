package com.fxc.admin.jdbc;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Component;

import com.fxc.admin.jdbc.util.FieldUtil;
import com.fxc.admin.jdbc.util.JDBCPublic;
import com.fxc.admin.jdbc.util.SqlCache;
import com.fxc.admin.jdbc.util.SqlUtil;
import com.fxc.admin.jdbc.util.Treat;
import com.fxc.entity.Associated;
import com.fxc.entity.CatchInfo;
import com.fxc.entity.IdEntity;
import com.fxc.utils.caseConversion;

/**
 * 所有条件都不要用map啊
 * @author Administrator
 *
 * @param <T>
 * @param <PK>
 */
@Component
@Qualifier("updateJdbc")
public class UpdateJdbc<T extends IdEntity<PK>,PK> extends JDBCPublic{
	
	//读写分离啦 配置不同的jdbcTemplate
	@Autowired
	private  JdbcTemplate jdbcTemplate;
	
	/**
	 * 修改
	 * @param object 修改的新值
	 * @param where 不使用id时的where条件 where 为空是全表删除
	 * @param clas
	 * @return
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public int update(T object,HashMap<String,Object> whereMa,Class<?> clas) throws IllegalArgumentException, IllegalAccessException{
		if(object == null)
			return 0;
		final ArrayList<ArrayList<Object>> li = getPara(object);
		if(li.size()==0)
			return 0;
		final List<Object> whereValue = new ArrayList<Object>();
		StringBuffer where = new StringBuffer();
		CatchInfo info = SqlCache.getCacheSqlInfo(clas);
		final String idName = info.getIdKey();
		StringBuffer sb = getUpdateSql(li,info.getTable(),idName);
		SqlUtil.addWhere(where,whereValue,whereMa,null);
		printsSQL(sb.toString()+where.toString());
	    int count = jdbcTemplate.update(sb.toString()+where.toString(), new PreparedStatementSetter() {  
	    	@Override  
	    	public void setValues(PreparedStatement pstmt) throws SQLException {
	    		int k = 0;
	    		for(int i=0;i<li.size();i++){
    				pstmt.setObject(i+1,li.get(i).get(1)); 
    				printsSQL("value"+i+":"+li.get(i).get(1));
	    			k++;
	    		}  
	    		if(whereValue.size()>0){
	    			for(int i=0;i<whereValue.size();i++){
	    				pstmt.setObject((k+i+1),whereValue.get(i)); 
	    				printsSQL("value"+k+i+":"+whereValue.get(i));
	    			}
	    		}
	    }});
	    return count;
	}
	
	/**
	 * 返回update的 sql语句
	 * @param li object中不为空的对象集合
	 * @param name 表名
	 * @return StringBuffer
	 */
	public  StringBuffer getUpdateSql(ArrayList<ArrayList<Object>> li,String myname,String idName){
		if(li.size()==0)
			return null;
		StringBuffer sb = new StringBuffer();
		sb.append("update "+myname+" set ");
		for(int i =0;i<li.size();i++){
			sb.append(caseConversion.camel2Underline(li.get(i).get(0).toString())+" = ?,");
		}
		sb.deleteCharAt(sb.length()-1);
		return sb;
	}
	
	/**
	 * 获取obj的id value
	 * @param obj
	 * @return
	 */
	public Object getIdValue(Object obj,String name){
		Object val = null;
		if(name != null)
			val = FieldUtil.getValue(obj,name);
		return val;
	}
	
	
	
	/**
	 * 返回Object对象中不为空的字段集合  还过滤了idField字段
	 * @param obj
	 * @return ArrayList<ArrayList<Object>>
	 * List 封装的 List是固定的长度 下标0 name 下标1 值
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public  ArrayList<ArrayList<Object>>  getPara(Object obj) throws IllegalArgumentException, IllegalAccessException{
		ArrayList<ArrayList<Object>> li=new ArrayList<ArrayList<Object>>();
//		BeanInfo info; 
//		try {
//			info = Introspector.getBeanInfo(obj.getClass());
			Class<?> cl = obj.getClass();
			for (Class<?> superClass = cl; superClass != Object.class; superClass = superClass.getSuperclass()) {
				Field[] f = superClass.getDeclaredFields();
				for(int i=0;i<f.length;i++){
					if (f[i].getModifiers() == 2 && !(Modifier.toString(f[i].getModifiers()).indexOf("static") > -1)
							&& !(Modifier.toString(f[i].getModifiers()).indexOf("final") > -1)) {
						f[i].setAccessible(true);
						Object val = f[i].get(obj);
						if(val == null)
							continue;
						
						if(f[i].getAnnotation(Transient.class) !=null)
							continue;
//						if(f[i].getAnnotation(Custom.class)!=null 
//								&& !"".equals(f[i].getAnnotation(Custom.class).IdField()))
//							continue;
						if(val.getClass().isEnum()){
							val.toString();
							val = Treat.getEnumOrdinal(f[i].getType(), val.toString());
						}else if(val.getClass().toString().indexOf("com.")>-1){
							Associated ass = SqlCache.getAssociated(obj,f[i].getName());
							if(ass == null){
								continue;
							}
							val = FieldUtil.getValue(val,ass.getPointAssField());
							if(val==null)
								continue;
						}
						ArrayList<Object> lite = new ArrayList<Object>();
						lite.add(f[i].getName());
						lite.add(val);
						li.add(lite);
					}
				}
			}
			return li;
	}
}
