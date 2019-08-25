package com.fxc.admin.jdbc.dao;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.fxc.admin.jdbc.PublicJdbc;
import com.fxc.admin.jdbc.util.SqlCache;
import com.fxc.admin.jdbc.util.Treat;
import com.fxc.entity.IdEntity;
import com.fxc.entity.Order;
import com.fxc.entity.QueryInfo;
import com.fxc.utils.ContextUtils;


/**
 * daobean的公共实现类
 * @author fxc
 */
@SuppressWarnings("unchecked")
public class PublicDaoImpl<T extends IdEntity<PK>,PK> extends PublicJdbc<T,PK> implements PublicDao<T, PK>{
	
	protected ArrayList<Order> sort; // 排序的列
	
    private static JdbcTemplate jdbcTemplate;
    
	protected  ArrayList<String> cancel;//QueryInfo 的cancelNotSelect属性可以抵消cancel的取消查询
	
	protected HashMap<String,Object> defaultWhere;//默认使用的查询条件 会被QueryInfo的查询条件覆盖
	
	protected  Class<T> c;
	
	protected String idKey = null;
	
	public PublicDaoImpl() {
		if((this.getClass().getGenericSuperclass() instanceof ParameterizedType)){
			this.c = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		}
	}
	
	private String getIdKey(){
		if(this.idKey == null){
			this.idKey =  SqlCache.getCacheSqlInfo(this.c).getIdKey();
		}
		return this.idKey ;
	}
	
	public JdbcTemplate getJdbc(){
		if(jdbcTemplate == null){
			jdbcTemplate = (JdbcTemplate) ContextUtils.appContext.getBean("jdbcTemplate");
		}
		return jdbcTemplate;
	}
	
	/**
	 * Query 没有值使用默认值
	 * 因为直接调用daobean不一定会经过业务处理  所以使用默认值的前提是没有值就使用默认值
	 */
	protected  QueryInfo setQuery(QueryInfo qi){
		if(qi.getSort()==null && this.sort!=null)
			qi.setOr(this.sort);
		if(this.cancel!=null){
			//没有取消同名 不过 同名概率低 反而比每次都验证同名效率高 出现同名影响不大
			for(String can : this.cancel){
				qi.addCancel(can);
			}
		}
		
		if(!Treat.isEmpty(qi.getCancelNotSelect()) && !Treat.isEmpty(qi.getCancel())){
			for(int i=0;i<qi.getCancel().size();i++){
				for(Map.Entry<String,String> ma:qi.getCancelNotSelect().entrySet()){
					if(qi.getCancel().get(i).equals(ma.getValue())){
						qi.getCancel().remove(i);
						break;
					}
				}
			}
		}
		//在封装参数时已经对涉及到查询条件的可以进行验证是否有被注入了 在入口处解决
		if(defaultWhere!=null){
			out : for(Map.Entry<String,Object> ma:defaultWhere.entrySet()){
				if(qi.getSearch().get(ma.getKey())==null ){
					for(Map.Entry<String,Object> qima:qi.getSearch().entrySet()){
						if(IsEquals(qima.getKey(), ma.getKey())){
							break out;
						}
					}
//					qi.getSearch().put("EQ_"+ma.getKey(), ma.getValue());
					qi.getSearch().put(ma.getKey(), ma.getValue());
				}
			}
		}
		return qi;
	}
	
	/**
	 * 返回所有总行数
	 * @return
	 */
	@Override
	public int getCount() throws Exception{
		return getCount(null);
	}
	
	/**
	 * 按条件查询 返回总行数
	 * @param searchs
	 * @return
	 */
	@Override
	public int getCount(HashMap<String, Object> searchs)throws Exception {
		return getGetsJdbc().executeCount(searchs,this.c);
	}
	
	/**
	 * 比较qi带进来的查询条件和daobean默认的查询条件是否冲突
	 * 冲突返回true 不冲突返回 false
	 * @param key
	 * @param comparisonKey
	 * @return
	 */
	public static boolean IsEquals(String key,String comparisonKey){
		String[] k = key.split("_");
		String[] ck = comparisonKey.split("_");
		String k1 = key.substring(k[0].length()+1);
		String ck1 = comparisonKey.substring(ck[0].length()+1);
		if(k1.equals(ck1)){
			return true;
		}
		return false;
	}
	

	@Override
	public T get(PK id,boolean b) throws Exception {
		QueryInfo qi = new QueryInfo();
		qi.setCancelConnection(b);
		qi.getSearch().put("EQ_"+getIdKey(), id);
		return get(qi);
	}
	
	@Override
	public int add(T obj) throws Exception {
		return getAddJdbc().executeAdd(obj, this.c);
	}

	@Override
	public PK addAndId(T obj) throws Exception {
		return (PK) getAddJdbc().addGetId(obj, this.c);
	}

	@Override
	public int dels(String id) throws Exception {
		return getDeleteJdbc().deleteIds(id,this.c);
	}
	
	@Override
	public int del(PK id) throws Exception {
		return getDeleteJdbc().deleteId(id,this.c);
	}

	@Override
	public int del(HashMap<String, Object> ma) throws Exception {
		return getDeleteJdbc().delete(ma, this.c);
	}

	@Override
	public int update(T obj) throws Exception {
		Object val = getUpdateJdbc().getIdValue(obj,getIdKey());
		if(val == null){
			throw new Exception("id的值不能为空!");
		}
		HashMap<String,Object> where = new HashMap<String,Object>();
		where.put("EQ_"+getIdKey(), val);
		return getUpdateJdbc().update(obj,where,this.c);
	}

	@Override
	public int update(T obj, HashMap<String, Object> where) throws Exception {
		return getUpdateJdbc().update(obj,where,this.c);
	}

	@Override
	public T get(PK ID) throws Exception {
		QueryInfo qi = new QueryInfo();
		qi.getSearch().put("EQ_"+getIdKey(), ID);
		List<T> li = (List<T>) getGetsJdbc().executeGets(qi,this.c);
		return (li!=null && li.size()>0) ? li.get(0) : null;
	}

	@Override
	public T get(QueryInfo qi) throws Exception {
		setQuery(qi);
		List<T> li = (List<T>) getGetsJdbc().executeGets(qi,this.c);
		return (li!=null && li.size()>0) ? li.get(0) : null;
	}
	
	/**
	 * 单纯的按HashMap<String,Object> ma 不做任何多余操作
	 * @param ma
	 * @return
	 * @throws Exception
	 */
	@Override
	public T get(HashMap<String,Object> ma) throws Exception {
		QueryInfo qi = new QueryInfo();
		qi.setSearch(ma);
		List<T> li = (List<T>) getGetsJdbc().executeGets(qi,this.c);
		return (li!=null && li.size()>0) ? li.get(0) : null;
	}

	@Override
	public List<T> gets() throws Exception {
		QueryInfo qi = new QueryInfo();
		return  (List<T>) getGetsJdbc().executeGets(qi,this.c);
	}

	@Override
	public List<T> gets(QueryInfo qi) throws Exception {
		setQuery(qi);
		return  (List<T>) getGetsJdbc().executeGets(qi,this.c);
	}
	
	/**
	 * 单纯的按HashMap<String,Object> ma 不做任何多余操作
	 * @param ma
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<T> gets(HashMap<String,Object> ma) throws Exception {
		QueryInfo qi = new QueryInfo();
		qi.setSearch(ma);
		return  (List<T>) getGetsJdbc().executeGets(qi,this.c);
	}
	
}
