package com.sjb.interceptor;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fxc.exception.MyPermissionsException;
import com.sjb.model.PublicUser;
import com.sjb.model.system.Permission;
import com.sjb.model.system.Role;
import com.sjb.service.system.RolePermissionMiddleService;
import com.sjb.util.Treat;
import com.sjb.util.errorJson;
import com.sjb.util.redis.RedisApi;
import com.sjb.util.session.SystemUtil;

/**
 * @ProjectName:lightning
 * @ClassName: SzInterceptor
 * @Description: 
 * @UpdateUser: 
 * @UpdateDate: 
 * @UpdateRemark: 
 * @versions:1.0
 */
public class Interceptor implements HandlerInterceptor{
	/**
	 * 通用路径集合
	 */
	private List<String> excludeUrl;
	
	
	/**
	 * 静态资源路径集合
	 */
	private List<String> resourceUrl;
	
	private SetSession session = new MySession();
	
//	private SetSession session = new JWTSession();
	
	@Autowired
	@Qualifier("rolePermissionMiddleServiceImpl")
	private RolePermissionMiddleService rolePermissionMiddleService;
	
	public List<String> getExcludeUrl() {
		return excludeUrl;
	}
	
	public void setExcludeUrl(List<String> excludeUrl) {
		this.excludeUrl = excludeUrl;
	}

	/**
	 * ：预处理回调方法，实现处理器的预处理（如登录检查），第三个参数为响应的处理器（如我们上一章的Controller实现）；
     *	返回值：true表示继续流程（如调用下一个拦截器或处理器）；
     *  false表示流程中断（如登录检查失败），不会继续调用其他的拦截器或处理器
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		System.out.println(request.getRequestURL());
		HandlerMethod handlerMethod = (HandlerMethod)handler;
		String url = getMappingUrl(handlerMethod);
		System.out.println(url);
		
		session.set(request);
		
		PublicUser adminUser = SystemUtil.SessionMagger.get();
	/**************************************************************************************************************/
		//通行路径放行
		if (excludeUrl!=null && isPermissionUrl(excludeUrl,url)) {
			return true;
		}
		
	/*********下面开始需要权限*****************************************************************************************************/
		if(adminUser == null){
			errorJson.userNotLogin(response);
			return false;
//			throw new UserNotLoginException("登录失效");
		}
		//判断ip是否和登录ip一致 如果使用Nginx分流要注意配置一下 不然拿到的是Nginx的ip
		//管理员放行 这边
		if(Treat.isAdmin()){
			return true;
		}
	/**************************************************************************************************************/
		//验证admin开头的后台 第一个没加 其它的系统要加上 如果有多个系统
//		if(url.indexOf("/admin/")==0){//先不区分系统
			//验证最新session的ip是谁
		
			//权限验证
			if(adminUser.getRole()!=null && adminUser.getRole().size()>0){
				RequiresPermissions  rp = handlerMethod.getMethodAnnotation(RequiresPermissions.class);
				String rpValue = null;
//				rpValue = rp == null ? null : rp.value().trim();
				rpValue = rp == null ? null : rp.value();
				if(rpValue == null || rpValue == ""){
					throw new MyPermissionsException("路径:"+url+",方法:"+handlerMethod.getMethod().getName()+"没有找到权限配置!");
				}else if("public".equals(rpValue)){//public的权限需要登录后才可以使用属于权限内共用 不需要分配
					return true;
				}else{
					//权限操作限制每个用户每2秒只能执行一次操作 不包含查询操作也没有限制超级管理员
//					if(rpValue.indexOf(":read")==-1){
//						if(RedisApi.setNXEX(SystemUtil.getUserName(),"", 2)){
//							System.out.println("拿到锁");
//						}else{
//							System.out.println("操作太频繁被过滤");
//							errorJson.frequently(response);
//							return false;
//						}
//					}
										
					//遍历所有角色
					for(Role r: adminUser.getRole  ()){
						//从本地缓存更加角色id获取权限集合 角色太多使用id 从权限表取
						HashMap<String,Permission> permission = rolePermissionMiddleService.getPermissions(r.getId());
						if(!Treat.isEmpty(permission)){
							//不为空拥有权限
//							int len = rpValue.lastIndexOf(":");
//							String key = rpValue.substring(0,len);
							String[] rpValues = rpValue.split(",");
							for(int i=0;i<rpValues.length;i++){
								if(permission.get(rpValues[i])!= null && permission.get(rpValues[i]).getStatus().intValue()==1){//权限存在变且显示
									return true;
								}
							}
						}
					}
					throw new MyPermissionsException("路径:"+url+",方法:"+handlerMethod.getMethod().getName()+"没有匹配到对应权限请联系管理员!");
				}
			}

//		}
	/**************************************************************************************************************/	
		return false;
	}

//	/**
//	 * 权限验证通过
//	 */
//	public boolean passPermission(User user){
//		MDC.put(SystemUtil.userSession, JSONObject.toJSONString(user));
//		return true;
//	}
	
	/**
	 * 
	 * 后处理回调方法，实现处理器的后处理（但在渲染视图之前），此时我们可以通过
	 * 
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
			throws Exception {
	}
	
	/**
	 * 整个请求处理完毕回调方法，即在视图渲染完毕时回调，如性能监控中我们可以在此记录结束时间并输出消耗时间，
	 * 还可以进行一些资源清理，类似于try-catch-finally中的finally，
	 * 但仅调用处理器执行链中preHandle返回true的拦截器的afterCompletion。
	 */
	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
//		MDC.remove(UserSession.TOKENKEY);
	}
	
	
//	/**
//	 * 判断是否资源路径
//	 * @return
//	 */
//	private boolean isresourceUrl(List<String> urls,String uri){
//		for(String url : urls){
//			int len = url.length();
//			if(uri.lastIndexOf(url) == (uri.length()-len)){
//				return true;
//			}
//		}
//		return false;
//	}
	/**
	 * 判断是否无权限路径
	 * @return
	 */
	private boolean isPermissionUrl(List<String> urls,String uri){
		for(String url : urls){
//			url = url.trim();
			if(url.indexOf("*")>0){
				String ul = url.substring(0,url.length()-1);
				if(uri.indexOf(ul)==0)
					return true;
			}else if(uri.contains(url))
				return true;
		}
		return false;
	}
	
	private String getMappingUrl(HandlerMethod handlerMethod){
		
		String url = "";
		//如果有对controller做aop 那么这边得到的是代理对象要使用getBeanType()
		RequestMapping rm  = handlerMethod.getBeanType().getAnnotation(RequestMapping.class);
//		RequestMapping rm = handlerMethod.getBean().getClass().getAnnotation(RequestMapping.class);
		if(rm!=null){
			String[] value = rm.value();
			if (value!=null && value.length>0) {
				url=value[0];
			}
		}
		rm = handlerMethod.getMethodAnnotation(RequestMapping.class);
		if(rm!=null){
			String[] value = rm.value();
			if(value!=null && value.length>0){
				url+=value[0];
			}
		}
		
		return url;
	}

	public List<String> getResourceUrl() {
		return resourceUrl;
	}

	public void setResourceUrl(List<String> resourceUrl) {
		this.resourceUrl = resourceUrl;
	}
	
	public static String getRequestParams(HttpServletRequest request) {
		Enumeration<?> enumeration = request.getParameterNames();
		StringBuffer sb = new StringBuffer("{");
		while (enumeration.hasMoreElements()) {
			String key = (String) enumeration.nextElement();
//			System.out.println(key);
			sb.append(key+"="+request.getParameter(key)+",");
		}
		if(sb.length()>2){
			sb.deleteCharAt(sb.length()-1);
		}
		sb.append("}");
		return sb.toString();
	}
	
}
