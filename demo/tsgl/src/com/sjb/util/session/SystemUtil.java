package com.sjb.util.session;

import java.util.List;

import com.sjb.model.PublicUser;
import com.sjb.model.system.Role;

/**
 * @author Administrator
 *
 */
public final class SystemUtil {

	public static ThreadLocal<PublicUser> SessionMagger = new ThreadLocal<PublicUser>();
	
	/**
	 * 获取session
	 * @return
	 */
	public static PublicUser getUser(){
		return SessionMagger.get();
	}
	
	/**
	 * 获取角色
	 * @return
	 */
	public static List<Role> getRole(){
		PublicUser user = SessionMagger.get();
		if(user!=null){
			if(user.getRole() == null){
				//说明没有登录
			}else{
				return user.getRole();
			}
		}
		return null;
	}
	
	/**
	 * 获取用户账号名字
	 */
	public static String getUserName(){
		PublicUser user = SessionMagger.get();
		return user == null ? null : user.getAccount()+"["+user.getName()+"]";
	}
	
	/**
	 * 获取用户账号
	 */
	public static String getUserAccount(){
		PublicUser user = SessionMagger.get();
		return user == null ? null : user.getAccount();
	}
	
	/**
	 * 获取用户账号(手机号)
	 */
	public static String getPhone(){
		PublicUser user = SessionMagger.get();
		return user == null ? null : user.getPhone();
	}
	
	/**
	 * 获取用户id
	 */
	public static Long getUserId(){
		PublicUser user = SessionMagger.get();
		return user == null ? null : user.getUserId();
	}
	
	
	/**
	 * 获取用户名字
	 */
	public static String getName(){
		PublicUser user =  SessionMagger.get();
		return user == null ? null : user.getName();
	}
	
	/**
	 * 获取代理平台商户ProxyPath
	 * @throws Exception 
	 */
	public static String getProxyPath() throws Exception{
		if(SessionMagger.get().getProxy_path() == null){
			throw new Exception("没有获取到对应参数ProxyPath请确认是否登录进正确的后台");
		}
		return SessionMagger.get().getProxy_path();
	}
	
	/**
	 * 获取代理平台商户ProxyId
	 * @throws Exception 
	 */
	public static Integer getProxyId() throws Exception{
		if(SessionMagger.get().getProxyParentId() == null){
			throw new Exception("没有获取到对应参数ProxyId请确认是否登录进正确的后台");
		}
		return SessionMagger.get().getProxyParentId();
	}
	
	/**
	 * 获取代理平台商户ProxyLevel
	 * @throws Exception 
	 */
	public static Integer getProxyLevel() throws Exception{
		if(SessionMagger.get().getProxyLevel() == null){
			throw new Exception("没有获取到对应参数ProxyId请确认是否登录进正确的后台");
		}
		return SessionMagger.get().getProxyLevel();
	}
	
	
	/**
	 * 获取是哪个表的用户登录
	 */
	public static Integer getType(){
		PublicUser user =  SessionMagger.get();
		return user == null ? null : user.getType();
	}

}
