package com.fxc.admin.jdbc;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import com.fxc.admin.jdbc.annotation.MTO;
import com.fxc.admin.jdbc.annotation.OTO;
import com.fxc.admin.jdbc.util.FieldUtil;
import com.fxc.admin.jdbc.util.JDBCPublic;
import com.fxc.admin.jdbc.util.SqlCache;
import com.fxc.admin.jdbc.util.SqlUtil;
import com.fxc.admin.jdbc.util.Treat;
import com.fxc.entity.Associated;
import com.fxc.entity.CatchInfo;
import com.fxc.entity.Order;
import com.fxc.entity.QueryInfo;
import com.fxc.utils.caseConversion;


/**
 * 注意所有状态int类型的默认值不要为0 为0在mysql中使用字符串（abc）作为查询条件会忽略该条件
 * 所有的Map条件都可能被注入 需要该成实体封装查询条件
 * 自定义查询条件的wher 比如大于小于 默认等于 需要验证 先把key和条件拆开 验证key是否存在
 * @author fxc
 *
 * @param <T>
 * @param <PK>
 */
@SuppressWarnings({"unchecked","rawtypes"})
@Component
@Qualifier("getsJdbc")
public class GetsJdbc<T,PK> extends JDBCPublic{
	
	@Autowired
	private  JdbcTemplate jdbcTemplate;
	
	private FieldUtil fieldUtil = new FieldUtil();
	
	public GetsJdbc() {
		super();
	}
	
	/**
	 * @param queryinfo sql查询配置
	 * @param ma where 参数 用加号分割查询条件 如 EQ+id 等于id = xxx
	 * @param clas 映射实体
	 * @return
	 */
	public final  T executeGet(QueryInfo qi,Class<?> cl) {
		qi.setBegin(0);
		qi.setEnd(1);
		List<T> li = (List<T>) executeGets(qi,cl);
		return (li==null || li.size()==0)? null : li.get(0);
	}
	
	/**
	 * 返回总行数
	 * @param where
	 * @param clas
	 * @return
	 */
	public int executeCount(QueryInfo qi,Class<?> clas){
	    return executeCount(qi.getSearch(),clas);
	}
	
	/**
	 * 返回总行数 或是判断是否存在某一条的数据
	 * @param where
	 * @param clas
	 * @return
	 */
	public int executeCount(HashMap<String,Object> ma,Class<?> clas){
		CatchInfo info = SqlCache.getCacheSqlInfo(clas);
		final HashMap<String,Object> sqlInfo = getCountSql(ma,info.getTable());
		printsSQL(sqlInfo.get("sql").toString()+sqlInfo.get("where").toString());
		int count = (int) jdbcTemplate.queryForObject(sqlInfo.get("sql").toString() + sqlInfo.get("where").toString(),
				SqlUtil.getObjectValue(((ArrayList<Object>) sqlInfo.get("whereValue"))),Integer.class); 
	    return count;
	}
	
	/**
	 * 返回查询 sql语句
	 * @param li object中不为空的对象集合
	 * @param name 表名
	 * @return StringBuffer
	 */
	public  HashMap<String,Object> getCountSql(HashMap<String,Object> whereMa,String myname){
		HashMap<String,Object> ma = new HashMap<String,Object>();
		StringBuffer sb = new StringBuffer();
		StringBuffer where = new StringBuffer("");
		List<Object> whereValue = new ArrayList<Object>();
		SqlUtil.addWhere(where,whereValue,whereMa,null);
		sb.append("select  count(*) AS cou from " + myname);
		ma.put("sql",sb.toString());
		ma.put("where",where);
		ma.put("whereValue",whereValue);
		return ma;
	}
	
	// 这边参数是对象 传的是引用 不要修改全局参数
	public final  List<T> executeGets(final QueryInfo qi,Class<?> cl) {
		
		final Class<?> clas = cl;
		final CatchInfo info = SqlCache.getCacheSqlInfo(clas);
		
		final HashMap<String,Object> sqlDebris =  createSQLDebris(info,qi);
		
		final boolean cancelConnectionb = (qi==null ? true : qi.isCancelConnection());//qi==null 默认取消所有关联
		printsSQL("select "+sqlDebris.get("sqlPrefix")+" "+sqlDebris.get("from")+" "+(sqlDebris.get("where")==null ? "" : sqlDebris.get("where")));
		
		List result = jdbcTemplate.query("select "+sqlDebris.get("sqlPrefix")+" "+sqlDebris.get("from")+" "+(sqlDebris.get("where")==null ? "" : sqlDebris.get("where")), 
		new PreparedStatementSetter() {  
			@Override
			public void setValues(PreparedStatement pstmt) throws SQLException {
				List<Object> li = (List<Object>) sqlDebris.get("whereVlaue");
				setWhereValue(pstmt,li);
			}
		},	
		new ResultSetExtractor<List>() {
			@Override
			public List extractData(ResultSet rs) throws SQLException, DataAccessException {
				List result = new ArrayList();
				String[] fields = sqlDebris.get("sqlPrefix").toString().split(",");
				try {
					while (rs.next()) {
						Object obj = sealResults(rs, clas, fields);
						if(!cancelConnectionb){
							outer:
								for (Map.Entry<String, Associated> map : info.getAss().entrySet()) {
									if(sqlDebris.get("cancelObj")!=null){
										for(int i=0;i<((List<String>) sqlDebris.get("cancelObj")).size();i++){
											if(map.getKey().equals(((List<String>) sqlDebris.get("cancelObj")).get(i).trim().split("\\.")[1])){
												 continue outer;
											}
										}
									}
									if(isCancel(qi,map)){
										if("OTM".equals(map.getValue().getType())){
											QueryInfo qi = new QueryInfo();
											qi.addCancel(map.getValue().getPointAssField());
											qi.setCancelConnection(true);
											Field fidx = clas.getDeclaredField(map.getKey());
											fidx.setAccessible(true);
											qi.getSearch().put("EQ_"+map.getValue().getPointAssField(), rs.getObject(caseConversion.camel2Underline(map.getValue().getAssField())));
											if(qi.getSearch().get(map.getValue().getPointAssField()) == null){
												throw new Exception(map.getKey()+"PointAssField == null");
											}
											fidx.set(obj,executeList(qi,map.getValue().getClas(),null));
										}else if("MTM".equals(map.getValue().getType())){
											String idkey =  SqlCache.getCacheSqlInfo(obj.getClass()).getIdKey();
											Object myid = rs.getObject(caseConversion.camel2Underline(idkey));
											if(myid == null){
												continue;
											}
											List<Object> li = MaToMaGetList(map.getValue(),myid);
											QueryInfo qi = new QueryInfo();
											qi.setBegin(0);
											qi.setEnd(1);
											qi.setCancelConnection(true);
											List<Object> valList = new ArrayList<Object>();
											String childid = SqlCache.getCacheSqlInfo(map.getValue().getClas()).getIdKey();
											for(Object id : li){
												qi.getSearch().put("EQ_"+childid, id);
												Object liobj = executeGet(qi,map.getValue().getClas());
												valList.add(liobj);
											}
											Field fidx = clas.getDeclaredField(map.getKey());
											fidx.setAccessible(true);
											fidx.set(obj,valList);
										}
									}
								}
						}
						result.add(obj);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return result;
			}
		});
		return result;
	}
	
	
	
	/**
	 * 对象list的关联进行查询
	 * @param where
	 * @param qi
	 * @param cl
	 * @return
	 */
	public final List<Object> executeList(QueryInfo qi,Class<?> cl,String sql) {
		final Class<?> clas = cl;
		final CatchInfo info = SqlCache.getCacheSqlInfo(clas);
		final HashMap<String,Object> sqlDebris = createSQLDebris(info,qi);
		String executesql = null;
		if(sql!=null){
			executesql = sql;
		}else
			executesql = "select "+sqlDebris.get("sqlPrefix")+" "+sqlDebris.get("from")+" "+(sqlDebris.get("where")==null ? "" : sqlDebris.get("where"));
		printsSQL(executesql);
		List result = jdbcTemplate.query(executesql, 
		new PreparedStatementSetter() {  
			@Override
			public void setValues(PreparedStatement pstmt) throws SQLException {
				//添加参数
				setWhereValue(pstmt,(List<Object>)sqlDebris.get("whereVlaue"));
			}
		},	
		new ResultSetExtractor<List>() {
			@Override
			public List extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<Object> result = new ArrayList<Object>();
				String[] fields = sqlDebris.get("sqlPrefix").toString().split(",");
				try {
					while (rs.next()) {
						Object obj = sealResults(rs, clas,fields);
						result.add(obj);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return result;
			}
		});
		return result;
	}
	
	/**
	 * 添加参数
	 * @param pstmt
	 * @param whereValue
	 * @throws SQLException
	 */
	public  void setWhereValue(PreparedStatement pstmt,List<Object> whereValue) throws SQLException{
		if(whereValue!=null){
			int i = 1;
		    for(Object obj: whereValue){ 
		    	pstmt.setObject(i++,obj);  
		    }  
		}
	}
	
	/**
	 * 获取多对多中间表 返回映射id
	 * @param sql
	 * @param assField
	 * @param pointAssField
	 * @return
	 */
	public  List<Object> MaToMaGetList(Associated ass,Object id){
		StringBuffer sql = new StringBuffer();
		sql.append("select "+ caseConversion.camel2Underline(ass.getAssField())+", "+caseConversion.camel2Underline(ass.getPointAssField())+" from "+ ass.getTabName()+" where "+caseConversion.camel2Underline(ass.getAssField())+"= ? ");
		final String pointAssField = caseConversion.camel2Underline(ass.getPointAssField());
		List<Object> list =  jdbcTemplate.query(sql.toString(), new ResultSetExtractor<List>() {
			@Override
			public List extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<Object> li = new ArrayList<Object>();
				while (rs.next()) {
					Object o = rs.getObject(pointAssField);
					li.add(o);
				}
				return li;
			}
		},new Object[]{id});
		return list;
	}
	
	/**
	 * 取消关联时为子对象赋值
	 */
	public  Object createChild(String fieldName,Class<?> faClass,Object val){
		try {
			Field f = faClass.getDeclaredField(caseConversion.underline2Camel(fieldName,true) );
			if(f.getType().isEnum()){
				Object[] ob = f.getType().getEnumConstants();
				return ob[(int)val];
			}
			Class<?> childClass = f.getType();
			String name = null;
			if(f.getAnnotation(OTO.class)!=null){
				name = f.getAnnotation(OTO.class).pointAssField();
			}else if(f.getAnnotation(MTO.class)!=null){
				name = f.getAnnotation(MTO.class).pointAssField();
			}else{
				throw new Exception(faClass+"."+f.getName()+"没有多对一或一对一的注释");
			}
			Field chif = fieldUtil.getAccessibleField(childClass,name);
			Object chiObj = childClass.newInstance();
			chif.setAccessible(true);
			chif.set(chiObj, val);
			return chiObj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 封装对象
	 * @param rs
	 * @param clas
	 * @param sqlDebris
	 * @param fields
	 * @param m
	 * @param method
	 * @return
	 * @throws Exception
	 */
	public final  Object sealResults(ResultSet rs,Class<?> clas,String[] fields) 
			throws Exception{
		Object obj = clas.newInstance();
		HashMap<String,Object> objdx = new HashMap<String,Object>();
		HashMap<String,Object> objdxField = new HashMap<String,Object>();
		for (int i = 0; i < fields.length; i++) {
			Object o = rs.getObject(i+1);
			if(o != null){
				String[] name = fields[i].split("\\.");
				if((clas.getSimpleName()).equals(name[0].trim())){
					Field field = fieldUtil.getAccessibleField(clas, caseConversion.underline2Camel(name[1],true));
					if(!Treat.isJavaClass(field.getType())){
						o = createChild(name[1],clas,o);
					}
					field.set(obj, o);
				}else{
					if(objdx.get(name[0])==null){
						Field fidx = clas.getDeclaredField(name[0]);
						objdx.put(name[0], fidx.getType().newInstance());//初始化
						objdxField.put(name[0], SqlCache.getCacheSqlInfo(fidx.getType()).getAss());
						fidx.setAccessible(true);
						fidx.set(obj, objdx.get(name[0]));
					}
					Class<?> childc = objdx.get(name[0]).getClass();
					Field field = fieldUtil.getAccessibleField(childc, caseConversion.underline2Camel(name[1],true));
					if(!Treat.isJavaClass(field.getType())){
						o = createChild(name[1],childc,o);
					}
					field.setAccessible(true);
					field.set(objdx.get(name[0]), o);
				}
			}
		}
		return obj;
	}
	
	
	/**
	 * 返回sql语句 片段
	 * @param info 该实体的sql配置
	 * @param ma where 参数
	 * @param qi 自定义sql配置
	 * @return
	 */
	public final HashMap<String,Object> createSQLDebris(CatchInfo info,final QueryInfo qi){
		HashMap<String,Object> rema= new HashMap<String,Object>();
		StringBuffer sqlPrefix =  new StringBuffer();;
		List<Object> whereVlaue = new ArrayList<Object>();
		StringBuffer where = new StringBuffer();
		final String  myForm  = info.getTable();
		SqlUtil.addWhere(where,whereVlaue,qi.getSearch(),info.getAlias());
		String stort = setSort(qi,info.getAlias());
		if(stort!=null)
			where.append(stort);
		if(qi.getBegin()!=null && qi.getEnd()!=null){
			where.append(" limit ?,? ");
			whereVlaue.add(qi.getBegin());
			whereVlaue.add(qi.getEnd());
		}
		StringBuffer from = new  StringBuffer("from "+myForm+" "+info.getAlias()+" ");
		List<String> cancelField = null;
		List<String> cancelObj = null;
		if(qi.getCancel()!=null && qi.getCancel().size()>0){
			cancelField = new ArrayList<String>();
			cancelObj = new ArrayList<String>();
			for(int i=0;i<qi.getCancel().size();i++){
				String[] spper = qi.getCancel().get(i).split("\\.");
				if(spper.length>1)// 可以 className.obj
					cancelObj.add(qi.getCancel().get(i));
				else
					cancelField.add(qi.getCancel().get(i));
			}
		}
		if(qi.getMeSelect()!=null){
			sqlPrefix = new StringBuffer(qi.getMeSelect().trim().replaceAll("\\*", info.getSqlPrefix())+" ");
			if(!",".equals(sqlPrefix.substring(sqlPrefix.length()-1))){
				sqlPrefix.append(",");
			}
		}
		if(!qi.isCancelConnection()){
			if(info.getSqlPrefix().trim().length()>0 && qi.getMeSelect()==null)
				sqlPrefix = new StringBuffer(info.getSqlPrefix()+",");
			if(info.getAss()!=null){
				outer:
				for (Map.Entry<String, Associated> map : info.getAss().entrySet()) {
					if(OTO.class.getSimpleName().equals(map.getValue().getType()) || MTO.class.getSimpleName().equals(map.getValue().getType())){
						CatchInfo dx = SqlCache.getCacheSqlInfo(map.getValue().getClas());
						if(qi.getMeSelect()==null){
							if(cancelObj!=null){
								for(int i=0;i<cancelObj.size();i++){
									if(map.getKey().equals(cancelObj.get(i).split("\\.")[1]) && map.getValue().getHeirOwn()){
										sqlPrefix.append(info.getAlias()+"."+caseConversion.camel2Underline(map.getValue().getAssField().trim())+",");
										continue outer;
									}
								}
							}
							//如果在 cancelNotSelect属性中有添加关联        没在 cancelNotSelect添加采用 autoLoading默认判断
							if(isCancel(qi,map)){
								sqlPrefix.append(dx.getSqlPrefix().replaceAll(map.getValue().getClas().getSimpleName()+".",map.getKey()+".")+",");
								for (Map.Entry<String, Associated> dxma : dx.getAss().entrySet()) {
									if(OTO.class.getSimpleName().equals(dxma.getValue().getType()) 
											|| MTO.class.getSimpleName().equals(dxma.getValue().getType())){
										if(map.getValue().getHeirOwn())
											sqlPrefix.append(map.getKey()+"."+caseConversion.camel2Underline(dxma.getValue().getAssField())+",");
									}
								}
							}else{
								if(map.getValue().getHeirOwn())
									sqlPrefix.append(info.getAlias()+"."+caseConversion.camel2Underline(map.getValue().getAssField().trim())+",");
							}
						}
						if(isCancel(qi,map)){
							if(qi.getConnection() != null && qi.getConnection().get(map.getKey())!=null)
								from.append(" "+qi.getConnection().get(map.getKey()));
							else
								from.append(" "+map.getValue().getConnection());
							from.append(" "+dx.getTable()+" "+map.getKey()+"");
							from.append(" on "+map.getValue().getRelationship());
						}
					}
				}
			}
			sqlPrefix.deleteCharAt(sqlPrefix.length()-1);
		}else{
			sqlPrefix = new StringBuffer(info.getSqlPrefix());
		}
		
		
		if(cancelField != null && cancelField.size()>0){
			String pre = sqlPrefix.toString();
			for(int i=0;i<cancelField.size();i++){
				pre = pre.replaceAll(info.getAlias()+"."+cancelField.get(i)+",","");
			}
			sqlPrefix = new StringBuffer(pre);
		}
		rema.put("sqlPrefix", sqlPrefix.toString());
		rema.put("from", from.toString());
		rema.put("where", where);
		rema.put("cancelObj", cancelObj);
		rema.put("whereVlaue", whereVlaue);
		return rema;
	}
	
	/**
	 * 是否要取消关联
	 * false 取消
	 */
	public boolean isCancel(QueryInfo qi,Map.Entry<String, Associated> map){
		//如果在 cancelNotSelect属性中有添加关联        没在 cancelNotSelect添加采用 autoLoading默认判断
		if(iscancelNotSelect(qi.getCancelNotSelect(),map.getKey()) || map.getValue().isAutoLoading()){
			return true;
		}
		return false;
	}
	
	
	/**
	 * 判断QueryInfo的cancelNotSelect字段是否有fieldName
	 * 有返回ture
	 * @param s
	 * @return
	 */
	public boolean iscancelNotSelect(HashMap<String,String> cancelNotSelect,String fieldName){
		if(!Treat.isEmpty(cancelNotSelect)){
			for(Map.Entry<String, String> ma:cancelNotSelect.entrySet()){
				String[] key = ma.getValue().split("\\.");
				if(key.length>1 && fieldName.equals(key[1].trim())){
					return true;
				}else if(fieldName.equals(ma.getValue().trim())){
					return true;
				}
			}
		}
		return false;
	}


	/**
	 * 设置排序
	 */
	public final String setSort(QueryInfo qi,String alias){
		
		StringBuffer s = new StringBuffer();
		if(qi.getSort()!=null){
			s.append(" ORDER BY ");
			s.append(alias+"."+caseConversion.camel2Underline(qi.getSort())+" "+qi.getOrder());
		}else if(qi.getOr()!=null && qi.getOr().size()>0){
			s.append(setSort(qi.getOr(),alias));
		}
		return s.length()>0 ? s.toString() : null;
	}
	
	/**
	 * 设置排序
	 */
	public final String setSort(ArrayList<Order> or,String alias){
		StringBuffer s = new StringBuffer();
		if(or!=null && or.size()>0){
			s.append(" ORDER BY ");
			for(Order order : or){
				s.append((order.getAlias()==null?alias:order.getAlias())+"."+order.getName()+" "+order.getSort()+",");
			}
			s.deleteCharAt(s.length()-1);
		}
		return s.length()>0 ? s.toString() : "";
	}

}
