package com.fxc.admin.jdbc;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.mysql.jdbc.Statement;
import com.fxc.admin.jdbc.util.FieldUtil;
import com.fxc.admin.jdbc.util.JDBCPublic;
import com.fxc.admin.jdbc.util.SqlCache;
import com.fxc.admin.jdbc.util.Treat;
import com.fxc.entity.Associated;
import com.fxc.entity.CatchInfo;
import com.fxc.utils.caseConversion;

@Component
@Qualifier("addJdbc")
public class AddJdbc<T,PK> extends JDBCPublic {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * clasA 是否继承clasB 
	 * @param clasA
	 * @param clasB
	 * @return
	 */
//	public boolean isExtendsClass(Class<?> clasA,Class<?> clasB) {
//		for (Class<?> superClass = clasA.getSuperclass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
//			if(superClass == clasB){
//				return true;
//			}
//		}
//		return false;
//	}
	
	
	/**
	 * 插入
	 * @param object
	 * @param clas
	 * @return
	 */
	public  Integer executeAdd(T object,Class<?> clas){
		if(object == null)
			return null;
		CatchInfo info = SqlCache.getCacheSqlInfo(clas);
		final ArrayList<ArrayList<Object>> li = getPara(object);
		
		StringBuffer sb = getInserySql(li,info.getTable());
//		System.out.println(sb.toString());
	    int count = jdbcTemplate.update(sb.toString(), new PreparedStatementSetter() {  
	    	@Override  
	    	public void setValues(PreparedStatement pstmt) throws SQLException {  
	    		for(int i=0;i<li.size();i++){
	    			pstmt.setObject(i+1,li.get(i).get(1)); 
//	    			System.out.println("value"+i+":"+li.get(i).get(1));
	    		}      
	    }});
	    return count;
	}
	
	
	/**
	 * 插入并返回id
	 * @param object
	 * @param clas
	 * @return
	 */
	public Object addGetId(T object,Class<?> clas){
        final ArrayList<ArrayList<Object>> li = getPara(object);
		CatchInfo info = SqlCache.getCacheSqlInfo(clas);
		final StringBuffer sb = getInserySql(li,info.getTable());
		printsSQL(sb.toString());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                new PreparedStatementCreator(){
                    public java.sql.PreparedStatement createPreparedStatement(Connection conn) throws SQLException{
                        PreparedStatement pstmt = null;
                        pstmt = conn.prepareStatement(sb.toString(), Statement.RETURN_GENERATED_KEYS);
                        for(int i=0;i<li.size();i++){
        	    			pstmt.setObject(i+1,li.get(i).get(1)); 
        	    			printsSQL("value"+i+":"+li.get(i).get(1));
        	    		}
                        return pstmt;
                    }
                },
                keyHolder);
        if(keyHolder.getKey() == null)
        	return li.get(0).get(1);
        return keyHolder.getKey();
    }
	
	/**
	 * 返回insert sql语句
	 * @param li object中不为空的对象集合
	 * @param name 表名
	 *  @param name useUUID 是否使用uuid true 使用
	 * @return StringBuffer
	 */
	public  StringBuffer getInserySql(ArrayList<ArrayList<Object>> li,String myname){
		if(li.size()==0)
			return null;
		StringBuffer sb = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		sb.append("insert into "+myname+" ( ");
//		if(useUUID){
//			sb.append("id ,");
//			sb2.append(" ?,");
//		}
		for(int i =0;i<li.size();i++){
			sb.append(caseConversion.camel2Underline(li.get(i).get(0).toString())+" ,");
			sb2.append(" ?,");
		}
		sb.deleteCharAt(sb.length()-1);
		sb2.deleteCharAt(sb2.length()-1);
		sb.append(" )  values ( "+sb2.toString()+" )");
//		if(useUUID){
//			ArrayList<Object> list = new ArrayList<Object>();
//			list.add("id");
//			list.add(UUID.randomUUID().toString());
//			li.add(0,list);
//		}
		return sb;
	}
	
	/**
	 * 返回Object对象中不为空的字段集合  还过滤了idField字段
	 * @param obj
	 * @return ArrayList<ArrayList<Object>>
	 * List 封装的 List是固定的长度 下标0 name 下标1 值
	 */
	public  ArrayList<ArrayList<Object>>  getPara(Object obj){
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
						Object val =null;
						try {
							val = f[i].get(obj);
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if(val == null)
							continue;
						if(f[i].getAnnotation(Transient.class) !=null)
							continue;
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
