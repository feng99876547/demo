package com.fxc.admin.jdbc;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;





import org.springframework.stereotype.Component;

import com.fxc.admin.jdbc.util.JDBCPublic;
import com.fxc.admin.jdbc.util.SqlCache;
import com.fxc.admin.jdbc.util.SqlUtil;
import com.fxc.entity.CatchInfo;
import com.fxc.entity.IdEntity;

/**
 * 删除也一样不要用map封装条件用实体 否则会被注入
 * @author Administrator
 *
 * @param <T>
 * @param <PK>
 */
@Component
@Qualifier("deleteJdbc")
public class DeleteJdbc<T extends IdEntity<PK>,PK> extends JDBCPublic{
	
	@Autowired
	private  JdbcTemplate jdbcTemplate;
	
	
	/**
	 * 按条件删除
	 * @param id
	 * @param clas
	 * @return
	 */
	public int delete(HashMap<String,Object> whereMa,Class<?> clas){
		CatchInfo info = SqlCache.getCacheSqlInfo(clas);
		final ArrayList<Object> whereValue = new ArrayList<Object>();
		StringBuffer where = new StringBuffer();
		SqlUtil.addWhere(where,whereValue,whereMa,null);
		//是否打印sql
		printsSQL("delete from "+info.getTable()+where);
		int count = jdbcTemplate.update("delete from "+info.getTable()+where,SqlUtil.getObjectValue(whereValue));
		return count;
	}
	
	
	/**
	 * 按id删除
	 * @param id
	 * @param clas
	 * @return
	 */
	public int deleteId(PK id,Class<?> clas){
		CatchInfo info = SqlCache.getCacheSqlInfo(clas);
		int count = jdbcTemplate.update("delete from "+info.getTable()+" where "+info.getIdKey()+" = ? ",new Object[]{id});
		return count;
	}
	
	/**
	 * 按id删除多条
	 * @param id
	 * @param clas
	 * @return
	 */
	public int deleteIds(String ids,Class<?> clas){
		CatchInfo info = SqlCache.getCacheSqlInfo(clas);
		String quantity = "";
		String [] id = ids.split(",");
		int len = id.length-1;
		for(int i = 0;i<=len;i++){
			quantity +=  i == len ? "?":"?,";
		}
		int count = jdbcTemplate.update("delete from "+info.getTable()+" where "+info.getIdKey()+" in("+quantity+") ",(Object[]) id);
		return count;
	}
	
}
