package com.sjb.service.system.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fxc.admin.jdbc.service.PublicServiceImpl;
import com.fxc.admin.jdbc.service.entity.ServiceSectionImpl;
import com.fxc.admin.jdbc.service.entity.ServiceSection;
import com.fxc.admin.jdbc.service.listener.serviceSection.AfterUpdateListener;
import com.fxc.admin.jdbc.service.listener.serviceSection.BeforeDelListener;
import com.fxc.entity.QueryInfo;
import com.fxc.entity.Result;
import com.sjb.dao.system.PermissionDaoBean;
import com.sjb.model.system.Permission;
import com.sjb.service.system.PermissionService;
import com.sjb.service.system.ehcache.PermissionsCache;
import com.sjb.util.CombineBeans;


@Service
public class PermissionServiceImpl extends PublicServiceImpl<Permission, Long, PermissionDaoBean> 
implements PermissionService{

	@Resource
	private PermissionsCache permissionsCache;
	
	//string需要注意的是不同的key指向的是同一内存空间的值 不会因为name不同重新new一块内存存值 所以value一样会被认为是一把锁
//	private String permissionsCacheLock = "lock";
	
	private Object permissionsCacheLock = new Object();
	
	private ServiceSection<Permission> serviceAop ;
	
	@Override
	public ServiceSection<Permission> getServiceAop() throws Exception {
		if(serviceAop == null){
			serviceAop = new ServiceSectionImpl<Permission>();
			/**
			 * 修改之后需要清掉就缓存 
			 */
			serviceAop.setAfterUpdate(new AfterUpdateListener<Permission>() {
				@Override
				public boolean onAOP(Result<Permission> result, QueryInfo query) throws Exception {
					String key = result.getRows().getId().toString();
					CombineBeans.combineSydwCore(result.getRows(),permissionsCache.get(key));
//					permissionsCache.remove(result.getRows().getId().toString());//清掉缓存
					return true;
				}
			});
			/**
			 * 删除之后删缓存
			 */
			serviceAop.setBeforeDel(new BeforeDelListener<List<Permission>>() {
				
				@Override
				public boolean onAOP(Result<List<Permission>> result, QueryInfo query) throws Exception {
					String id = (String) result.getIds();
					String[] ids = id.trim().split(",");
					for(int i=0;i<ids.length;i++){
						//清空对应角色缓存
						permissionsCache.remove(ids[i]);
					}
					return true;
				}
			});
		}
		return serviceAop;
	}
	
	/**
	 * 根据permission（xx:xx:xx）获取权限 有缓存直接返回缓存数据 没有进数据库查
	 */
	@Override
	public  Permission getPermission(Long id) throws Exception{
		String perid = id.toString();
		Permission per = permissionsCache.get(perid);
		if(per!=null){
			return per;
		}else{
			synchronized (permissionsCacheLock) {
				per =  permissionsCache.get(perid);
				if(per != null){
					return per;
				}
				
				per = getDao().get(id);
				if(per==null){
//					throw new Exception("数据库中没有查到对应权限的id:"+id);
					return null;
				}else{
					permissionsCache.put(id.toString(), per);
					return per;
				}
			}
		}
	}

	
}
