package com.fxc.admin.jdbc.service;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.fxc.admin.jdbc.dao.PublicDao;
import com.fxc.admin.jdbc.service.magger.FieldMagger;
import com.fxc.business.BusinessDispatch;
import com.fxc.entity.IdEntity;
import com.fxc.entity.QueryInfo;
import com.fxc.entity.Result;
import com.fxc.utils.ContextUtils;
import com.fxc.utils.GetPara;
import com.sjb.util.Treat;

/**
 * 对于自己实现的jdbc 解放了hibernate的配置 解放jpa的接口配置 解放一定的mybatis 融合各家所长
 * ArrayList<DispatchTask> 可以自由配置业务挡板 非常适用于那些频繁复杂的业务 无论是开发还是维护都有质的提升
 * @author fxc
 * @param <T>
 * @param <PK>
 * @param <DB>
 */
//这边开启@Transactional事物会占用一个DataSource链接线程
@SuppressWarnings("unchecked")
public abstract class PublicServiceImpl<T extends IdEntity<PK>,PK,DB extends PublicDao<T,PK>> extends PublicServiceJdbc<T,PK,DB> implements PublicAPI<T,PK,DB>{
	
	private  Class<T> c;
	
	private String daoName;
	
	protected DB dao;
	
	protected ArrayList<DispatchTask> dispatch = new ArrayList<DispatchTask>();
	
	private  HashMap<String,ArrayList<Field>> businessFields;
	
	@Autowired
	@Qualifier("businessDispatchImpl")
	private BusinessDispatch<T,PK,DB> businessDispatch;
	
	private static FieldMagger fieldMagger = new FieldMagger();
	
	@Override
	public DB getDao(){
		if(dao == null){
			dao = (DB) ContextUtils.appContext.getBean(this.daoName);
		}
		return dao;
	}
	
	@Override
	public Class<T> getClas() throws Exception {
		return this.c;
	}
	
	public HashMap<String,ArrayList<Field>> getBusinessFields() throws Exception {
		if(this.businessFields == null){
			this.businessFields = fieldMagger.getFields(this.c);
		}
		return this.businessFields;
	}
	
	/**
	 * 重新初始化dispatch
	 * @param dt
	 */
	public void initDispatch(DispatchTask[] dt){
		dispatch = new ArrayList<DispatchTask>();
		for(DispatchTask d : dt){
			dispatch.add(d);
		}
	}
	
	public PublicServiceImpl(){
		if((this.getClass().getGenericSuperclass() instanceof ParameterizedType)){
			this.c = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		}
		if(this.c!=null){
			this.daoName = this.c.getSimpleName().substring(0, 1).toLowerCase()
					+ c.getSimpleName().substring(1) + "DaoBeanImpl";
		}
		//默认只有BUSINES
		dispatch.add(DispatchTask.BUSINES);
	}
	
	
	public Result<T> add(QueryInfo qi) throws Exception {
		return businessDispatch.AddDispatchTask(dispatch,qi,this);
	}
	
	/**
	 * 这个删除方法 条件只会使用到id 需要其他条件请调用带map参数的删除方法 这边好像没实现这个方法 
	 * 自己手动实现吧 因为这个方法对于现在的项目来说使用概率小不值得在继承的公共方法中添加 这样会浪费内存
	 */
	public Result<List<T>> del(QueryInfo qi) throws Exception {
		return businessDispatch.delDispatchTask(dispatch,qi,this);
	}

	public Result<T> update(QueryInfo qi) throws Exception {
		return businessDispatch.updateDispatchTask(dispatch,qi,this);
	}
	
	public T get(PK ID) throws Exception {
		return getJcbcGet().get(ID,this);
	}

	public Result<T> get(QueryInfo qi) throws Exception {
		setSearch(qi);
		return businessDispatch.getDispatchTask(dispatch,qi,this);
	}
	
	/**
	 *  freeGet不抓取前端提交的searc条件 只会使用后台定义的
	 */
	public Result<T> freeGet(QueryInfo qi) throws Exception {
		return businessDispatch.getDispatchTask(dispatch,qi,this);
	}
	
	/**
	 * 查全表 不带条件 一般别用
	 */
	public Result<List<T>> gets() throws Exception {
		return getJcbcGet().gets(this);
	}

	public Result<List<T>> gets(QueryInfo qi) throws Exception {
		setSearch(qi);
		return businessDispatch.getsDispatchTask(dispatch,qi,this);
	}
	
	/**
	 *  freeGets不抓取前端提交的searc条件 只会使用后台定义的
	 */
	public Result<List<T>> freeGets(QueryInfo qi) throws Exception {
		return businessDispatch.getsDispatchTask(dispatch,qi,this);
	}
	
	public static void setSearch(QueryInfo qi) throws Exception{
		if(qi==null) return;
		HashMap<String,Object> ma = GetPara.getSearch(qi.getParams());//加载查询条件
		Treat.combineMap(ma, qi.getSearch());
	}
}
