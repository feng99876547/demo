package com.fxc.admin.jdbc;

import com.fxc.entity.IdEntity;
import com.fxc.utils.ContextUtils;

/**
 * 需要分库在屡屡怎么弄
 * @author fxc
 *
 * @param <T>
 * @param <PK>
 */
@SuppressWarnings("unchecked")
public class PublicJdbc<T extends IdEntity<PK>,PK> {
	
	private AddJdbc<T,PK> addjdbc;
	    
    private DeleteJdbc<T ,PK> deleteJdbc;
    
    private UpdateJdbc<T,PK> updateJdbc;
    
    private GetsJdbc<T,PK> getsJdbc;
    
   
    protected AddJdbc<T,PK> getAddJdbc(){
    	if(addjdbc == null)
    		this.addjdbc = (AddJdbc<T,PK>) ContextUtils.appContext.getBean("addJdbc");
		return addjdbc;
	}
	
    protected DeleteJdbc<T,PK> getDeleteJdbc(){
		if(deleteJdbc == null)
			deleteJdbc = (DeleteJdbc<T,PK>) ContextUtils.appContext.getBean("deleteJdbc");
		return deleteJdbc;
	}
	
    protected GetsJdbc<T,PK> getGetsJdbc(){
		if(getsJdbc == null)
			getsJdbc = (GetsJdbc<T,PK>) ContextUtils.appContext.getBean("getsJdbc");
		return getsJdbc;
	}
	
    protected UpdateJdbc<T,PK> getUpdateJdbc(){
		if(updateJdbc == null)
			updateJdbc = (UpdateJdbc<T,PK>) ContextUtils.appContext.getBean("updateJdbc");
		return updateJdbc;
	}
}
