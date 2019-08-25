package com.sjb.service.system.impl;


import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fxc.admin.jdbc.service.DispatchTask;
import com.fxc.admin.jdbc.service.PublicServiceImpl;
import com.fxc.admin.jdbc.service.entity.ServiceSectionImpl;
import com.fxc.admin.jdbc.service.entity.ServiceSection;
import com.fxc.admin.jdbc.service.listener.serviceSection.AfertAddListener;
import com.fxc.admin.jdbc.service.listener.serviceSection.BeforeAddListener;
import com.fxc.admin.jdbc.service.listener.serviceSection.BeforeGetListener;
import com.fxc.admin.jdbc.service.listener.serviceSection.BeforeUpdateListener;
import com.fxc.entity.QueryInfo;
import com.fxc.entity.Result;
import com.fxc.utils.MD5Util;
import com.sjb.dao.system.UserDaoBean;
import com.sjb.model.PublicUser;
import com.sjb.model.system.Role;
import com.sjb.model.system.User;
import com.sjb.model.system.UserRoleMiddle;
import com.sjb.service.system.UserRoleMiddleService;
import com.sjb.service.system.UserService;
import com.sjb.util.StaticKey;
import com.sjb.util.Treat;

@Service
@Transactional
public class UserServiceImpl extends PublicServiceImpl<User, Long, UserDaoBean> implements UserService {

	@Autowired
	private UserRoleMiddleService userRoleMiddleService;
	
	public UserServiceImpl(){
		super();
		super.initDispatch(new DispatchTask[]{DispatchTask.CACHE,DispatchTask.BUSINES});
	} 
	
	
//	@Autowired
//	@Qualifier("entityDeleteService")
//	protected EntityDeleteService<User,Long,UserDaoBean> entityDeleteService;
//	/**
//	 * 使用自定义删除替换默认删除
//	 */
//	@Override
//	public JdbcDel<User,Long,UserDaoBean> getJcbcDel(){
//		return entityDeleteService;
//	}
	
	
	private ServiceSection<User> serviceAop ;//使用注入不方便阅读啊

	@Override
	public ServiceSection<User> getServiceAop(){
		 if(serviceAop == null){
			 serviceAop = new ServiceSectionImpl<User>();
			 /**
			 * 保存User之前业务
			 */
			serviceAop.setBeforeAdd(new BeforeAddListener<User>() {
				
				@Override
				public boolean onAOP(Result<User> result, QueryInfo query) throws Exception {
					//密码加密
					setPassword(result.getRows());
					return true;
				}
			});
			
			/**
			 * 保存之后再保存
			 */
			serviceAop.setAfertAdd(new AfertAddListener<User>() {
				@Override
				public boolean onAOP(Result<User> result, QueryInfo query) throws Exception {
//					if(lock.putIfAbsent(result.getRows().getDormitoryId().toString(), "1") == null){
//						if(!Treat.isEmpty(result.getRows().getDormitoryId())){
//							//mysql 默认事物是'REPEATABLE-READ',也就是可重复读 开启事物执行该sql时 该事物未提交 其他事物读到的是快照也就是修改之前的数据 要修改需要等到该事物提交后才可修改
//							//所以这样是不需要用锁来控制并发问题
//							int count = sequence.exSql("update xxgl_dormitory set yzrs = yzrs+1,syrs=zdrs-yzrs where id = ?  and yzrs < zdrs",result.getRows().getDormitoryId());
//							if(count == 0){
//								throw new MyRollBackException("该宿舍已经满了");
//							}
//						}
//					}
					
					if(Role.SUPER_ADMIN_ID.equals(result.getRows().getId())){//超级管理员不需要分配角色
						return true;
					}else{
						String[] roleIds = (String[]) query.getParams().get("ids[]");
						addRole(roleIds,Long.parseLong(result.getIds().toString()));
//						throw new Exception("主动回滚测试");
						return true;
					}
				}
			});
			/**
			 * 修改之前
			 */
			serviceAop.setBeforeUpdate(new BeforeUpdateListener<User>() {
				@Override
				public boolean onAOP(Result<User> result, QueryInfo query) throws Exception {
//					query.getParams().put("isUpdate", "false");
//					if(result.getRows().getDormitoryId()!=null){
//						User u = getDao().get(result.getRows().getId());
//						if(u.getDormitoryId()!=null){
//							if(u.getDormitoryId().intValue() != result.getRows().getDormitoryId()){
//								sequence.exSql("update xxgl_dormitory set yzrs = yzrs-1,syrs=zdrs-yzrs where id = ?  and yzrs > 0 ",u.getDormitoryId());
//							}else{//没有变动不需要修改
//								query.getParams().put("isUpdate", "true");
//							}
//						}
//					}
					
					if(Role.SUPER_ADMIN_ID.equals(result.getRows().getId())){
						return true;
					}else{
						//先删除全部
						HashMap<String,Object> ma = new HashMap<String,Object>();
						ma.put("EQ_userId", result.getRows().getId());
						
						userRoleMiddleService.getDao().del(ma);
						//在添加
						String[] roleIds = (String[]) query.getParams().get("ids[]");
						addRole(roleIds,result.getRows().getId());
						return true;
					}
				}
			});
			/**
			 * 修改之后
			 */
//			serviceAop.setAfterUpdate(new AfterUpdateListener<User>() {
//				@Override
//				public boolean onAOP(Result<User> result, QueryInfo query) throws Exception {
//					if(result.getRows().getDormitoryId()!=null){
//						if(!"true".equals(query.getParams().get("isUpdate").toString())){
//							int count = sequence.exSql("update xxgl_dormitory set yzrs = yzrs+1,syrs=zdrs-yzrs where id = ?  and yzrs < zdrs",result.getRows().getDormitoryId());
//							if(count == 0){
//								throw new MyRollBackException("该宿舍已经满了");
//							}
//						}
//					}
//					return true;
//				}
//			});
			/**
			 * get查询之前
			 */
			serviceAop.setBeforeGet(new BeforeGetListener<User>() {
				@Override
				public boolean onAOP(Result<User> result, QueryInfo query) throws Exception {
					//是否有提交 密码等于xxx的查询条件 
					if(query.getParams("search_EQ_password")!=null){
						String password =setPassword(query.getSearch().get("password").toString());
						query.getParams().put("search_EQ_password", password);
					}
					return true;
				}
			});
			
//			serviceAop.setBeforeDel(new BeforeDelListener<List<User>>() {
//				@Override
//				public boolean onAOP(Result<List<User>> result, QueryInfo query) throws Exception {
//					User u = getDao().get(Long.parseLong(result.getIds().toString()));
//					if(u.getDormitoryId()!=null){
//						sequence.exSql("update xxgl_dormitory set yzrs = yzrs-1,syrs=zdrs-yzrs where id = ?  and yzrs > 0 ",u.getDormitoryId());
//					}
//					return true;
//				}
//			});
			
		 }
		 return serviceAop;
	 }
	
	
	/**
	 * 添加角色
	 * @throws Exception 
	 */
	public void addRole(String[] roleIds,Long userId) throws Exception{
		if(roleIds!=null){
			for (String roleId : roleIds) {
				UserRoleMiddle urm = new UserRoleMiddle(Long.parseLong(roleId),userId);
				userRoleMiddleService.getDao().add(urm);
			}
		}
	}
	
	
	/**
	 * 用户登录
	 * @param qi
	 * @return
	 * @throws Exception
	 */
	@Override
	public Result<User> login(QueryInfo qi,HttpServletRequest request) throws Exception {
		//添加角色查询关联
		qi.addCancelNotSelect("Role.roleList");
		
		//后台覆盖前端防止url自定义参数 默认查询条件为等于
//		qi.getSearch().put("EQ_loginName", qi.getNotNullParams("username"));
		
		qi.setKeyName("EQ_loginName,and_password");
		
		qi.setKeyValue(qi.getNotNullParams("username").toString());
		
		String password = (String) qi.getNotNullParams("password");
		
		qi.addCancelNotSelect("password");//登录时需要查询密码字段
		
		User user = super.get(qi).getRows();

		if(Treat.isEmpty(user)){//验证账号
			return new Result<User>(user,false,"账号密码不正确!");//账号不存在
		}
		else if(!MD5Util.validPassword(password, user.getPassword())){//验证密码
			return new Result<User>(user,false,"账号密码不正确!");//密码不正确
		}else{
			//设置session
//			user.setIp(Treat.getRemoteHost(qi.getRequest()));
			request.getSession().setAttribute(StaticKey.USER_SESSION_KEY,new PublicUser(user));
		}
		return new Result<User>(user,true);
	}
	

	/**
	 * 修改密码
	 */
	@Override
	public Result<?> updatePassword(String loginName,String old,String pas)throws Exception{
		Result<?> result = new Result<User>(false);
		User user = getDao().getUser(loginName);
		if(MD5Util.validPassword(old, user.getPassword())){
			User u = new User();
			u.setId(user.getId());
			u.setPassword(setPassword(pas));
			getDao().update(u);
			result.setSuccess(true);
			result.setMsg("修改成功");
		}else{
			result.setMsg("密码不正确!");
		}
		return result;
	}
	
	/**
	 * MD5密码加密
	 * @param user
	 * @throws UnsupportedEncodingException 
	 * @throws NoSuchAlgorithmException 
	 */
	public void setPassword(User user) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		if(user.getPassword()!=null)
			user.setPassword(MD5Util.getEncryptedPwd(user.getPassword()));
	}
	
	public static String setPassword(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		return password==null?null : MD5Util.getEncryptedPwd(password);
	}
	
	public static String setPasswordSocket(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		return password==null?null : MD5Util.getEncryptedPwd(password);
	}

}
