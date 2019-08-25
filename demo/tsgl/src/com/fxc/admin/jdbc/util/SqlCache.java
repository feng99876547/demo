package com.fxc.admin.jdbc.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Table;
import javax.persistence.Transient;

import com.fxc.admin.jdbc.annotation.Custom;
import com.fxc.admin.jdbc.annotation.MTM;
import com.fxc.admin.jdbc.annotation.MTO;
import com.fxc.admin.jdbc.annotation.OTM;
import com.fxc.admin.jdbc.annotation.OTO;
import com.fxc.entity.Associated;
import com.fxc.entity.CatchInfo;
import com.fxc.utils.caseConversion;

/**
 * 下一步优化可以把Method或Field的引用保存起来不用每次都去寻找
 * 好像时说Method更快找到Field后还是要调Method
 * 这边到时候要实现分表
 * @author fxc
 *
 */
public class SqlCache {
	
	//是否可以考虑使用本地缓存的copyOnRead  copyOnWrite模式不保存引用重新实例的哪种这样配置可以随便改
	public static HashMap<String,CatchInfo> CacheSqlInfo = new HashMap<String,CatchInfo>();

	//sqlPrefix是使用map排序的的 所以字段顺序是按字母升序排 这样会降低查询时的速度 下一般解决这个顺序问题 用ArrayList最好是完全对应数据库
	//还有就是解决key注入的问题
	public final static CatchInfo getCacheSqlInfo(Class<?> clas) {
		
		if(SqlCache.CacheSqlInfo.get(clas.getName())== null){
			createInfo(clas);
		}
		return  SqlCache.CacheSqlInfo.get(clas.getName());
	}
	
	private static void  createInfo(Class<?> clas){
		CatchInfo catchinfo = new CatchInfo();
		StringBuffer select = new StringBuffer();
		final String  alias = clas.getSimpleName();
		HashMap<String,Associated> ma = new HashMap<String,Associated>();
		String idkey = "id";//idkey
		//查找当前并向上查找
		String[] fields = null;
		Custom custom = clas.getAnnotation(Custom.class);
		if(custom!=null){
			fields = custom.Transient().trim().split(",");
			if(!"".equals(custom.IdKey())){
				idkey = custom.IdKey();
			}
		}
		for (Class<?> superClass = clas; superClass != Object.class; superClass = superClass.getSuperclass()) {
				Field[] fi = superClass.getDeclaredFields();
				setSelectAndConfiguration(fi,select,ma,alias,fields);
		}
		if(select.length()>0)
			select.deleteCharAt(select.length()-1);
		//获取表名
		Table annotation = (Table)clas.getAnnotation(Table.class);
		//表名
        if(annotation != null)
        	catchinfo.setTable(annotation.name());
        else//需要分表 到时候定义一个接口 由实体实现 分表规则在实体中完成 
        	catchinfo.setTable(caseConversion.camel2Underline(clas.getSimpleName()));
        catchinfo.setIdKey(idkey);
        catchinfo.setAlias(alias);
        catchinfo.setAss(ma);
        catchinfo.setSqlPrefix(select.toString());
        SqlCache.CacheSqlInfo.put(clas.getName(),catchinfo );//用class做key保存到全局变量中 
	}
	
	
	public  static void setSelectAndConfiguration(Field[] fi,StringBuffer select,HashMap<String,Associated> ma,String alias,String[] transients){
		if(fi!=null && fi.length>0){
			out : for(Field f : fi){
				if(transients != null){
					for(int i=0;i<transients.length;i++){
						if(f.getName().equals(transients[i])){
							continue out;
						}
					}
				}
				//私有 非静态非fianl属性
				if (f.getModifiers() == 2 && !(Modifier.toString(f.getModifiers()).indexOf("static") > -1)
						&& !(Modifier.toString(f.getModifiers()).indexOf("final") > -1)) {
					
					if (f.getAnnotation(Transient.class) != null){
						continue;
					}
					// 添加该实体所有的关联对象
					if (f.getAnnotation(OTO.class) != null) {
						ma.put(f.getName(), setAnnotation(f,OTO.class.getSimpleName(),alias));
						continue;
					} else if (f.getAnnotation(MTO.class) != null) {
						ma.put(f.getName(), setAnnotation(f,MTO.class.getSimpleName(),alias));
						continue;
					} else if (f.getAnnotation(MTM.class) != null) {
						ma.put(f.getName(), setAnnotation(f,MTM.class.getSimpleName(),alias));
						continue;
					} else if (f.getAnnotation(OTM.class) != null) {
						ma.put(f.getName(), setAnnotation(f,OTM.class.getSimpleName(),alias));
						continue;
					} else if (f.getType().getClassLoader() != null && !f.getType().isEnum()) {
						continue;
					} else if (f.getType() == List.class) {
						continue;
					} 
					select.append(alias + "." + caseConversion.camel2Underline(f.getName()) + ",");
				}
			}
		}
	}
	
	/**
	 * 记录下每个实体的关联关系
	 * @param f
	 * @param annClass Annotation的getSimpleName
	 * @return
	 */
	private static Associated  setAnnotation(Field f,String annClass,String alias){
		Associated ass = new Associated();
		Class<?> c = null;
		if(annClass.equals(OTM.class.getSimpleName())){
			OTM oto = f.getAnnotation(OTM.class);
			c = (Class<?>) ((ParameterizedType) f.getGenericType()).getActualTypeArguments()[0];
			ass.setConnection(oto.associated());
			ass.setAssField(oto.assField());
			ass.setPointAssField(oto.pointAssField());
			ass.setHeirOwn(oto.heirOwn());
			ass.setAutoLoading(oto.autoLoading());
		}else if(annClass.equals(MTM.class.getSimpleName())){
			c = (Class<?>) ((ParameterizedType) f.getGenericType()).getActualTypeArguments()[0];
			ass.setTabName(f.getAnnotation(MTM.class).middleTable());
			ass.setConnection(f.getAnnotation(MTM.class).associated());
			ass.setAssField(f.getAnnotation(MTM.class).assField());
			ass.setPointAssField(f.getAnnotation(MTM.class).pointAssField());
			ass.setHeirOwn(f.getAnnotation(MTM.class).heirOwn());
			ass.setAutoLoading(f.getAnnotation(MTM.class).autoLoading());
		}else if(annClass.equals(MTO.class.getSimpleName())){
			c  = f.getType();
			ass.setConnection(f.getAnnotation(MTO.class).associated());
			ass.setAssField(f.getAnnotation(MTO.class).assField());
			ass.setPointAssField(f.getAnnotation(MTO.class).pointAssField());
			ass.setHeirOwn(f.getAnnotation(MTO.class).heirOwn());
			ass.setAutoLoading(f.getAnnotation(MTO.class).autoLoading());
		}else if(annClass.equals(OTO.class.getSimpleName())){
			c  = f.getType();
			ass.setConnection(f.getAnnotation(OTO.class).associated());
			ass.setAssField(f.getAnnotation(OTO.class).assField());
			ass.setPointAssField(f.getAnnotation(OTO.class).pointAssField());
			ass.setHeirOwn(f.getAnnotation(OTO.class).heirOwn());
			ass.setAutoLoading(f.getAnnotation(OTO.class).autoLoading());
		}
		ass.setClas(c);
		ass.setType(annClass);
		//生成该实体和主实体的关联关系
		ass.setRelationship(alias + "." + caseConversion.camel2Underline((ass.getAssField())) + " = "
				+ f.getName() + "." + caseConversion.camel2Underline(ass.getPointAssField()));
		return ass;
	}
	
	/**
	 * 获取关联对象的Associated
	 * @return
	 */
	public final static Associated getAssociated(Object obj,String name){
		CatchInfo caif = SqlCache.getCacheSqlInfo(obj.getClass());
		Associated ass = caif.getAss().get(name);
		return ass;
	}
}
