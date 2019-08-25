package com.sjb.interceptor;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.fxc.exception.BusinesException;
import com.fxc.exception.MyCacheException;
import com.fxc.exception.MyPermissionsException;
import com.fxc.exception.MyRollBackException;
import com.fxc.exception.ParamsException;
import com.fxc.exception.SynException;
import com.fxc.exception.UserNotLoginException;
import com.sjb.util.Log;
import com.sjb.util.errorJson;
import com.sjb.util.session.SystemUtil;

/**
 * 全局异常捕获和处理
 * @ProjectName:lightning
 * @ClassName: SzExceptionHandler
 * @Description: 
 * @UpdateUser: 
 * @UpdateDate: 
 * @UpdateRemark: 
 * @versions:1.0
 */
public class ExceptionHandler implements HandlerExceptionResolver {
	
	
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception exception) {
		ModelAndView mv = new ModelAndView();
		if(exception instanceof UserNotLoginException){
			errorJson.userNotLogin(response);
			return null;//登录异常不做记录
		}else if(exception instanceof ParamsException){
			errorJson.params(response,(ParamsException)exception);
			return null;//参数异常不做记录
		}
		else if(exception instanceof MyRollBackException){
			errorJson.rollBack(response,(MyRollBackException) exception);
		}
		else if(exception instanceof MyPermissionsException){
			//没有进入controller
			errorJson.permission(response,(MyPermissionsException) exception);
			Log.allError.error("(注意)权限异常:用户"+SystemUtil.getUserName()+",访问"+exception.getMessage()+"");
			return mv;//如果返回null异常会继续往外抛给前端（前端出现异常可能会影响到脚本） 这边返回ModelAndView空对象可以间接理解为已经捕获异常
		}else if(exception instanceof SynException){
			errorJson.synfail(response,(SynException)exception);
		}else if(exception instanceof MyCacheException){
			errorJson.cache(response,(MyCacheException) exception);
		}else if(exception instanceof BusinesException){
			errorJson.busines(response,(BusinesException) exception);
		}else if(exception instanceof DataIntegrityViolationException){
			errorJson.sql(response,(DataIntegrityViolationException) exception);
		}
		//所有异常写入日志
		HandlerMethod handlerMethod = (HandlerMethod)handler;
		String classname = handlerMethod.getBeanType().getName() +"-"+ handlerMethod.getMethod().getName();
		Log.allError.error(classname+","+SystemUtil.getUserName()+",请求参数:[{}],异常:[{}]",Interceptor.getRequestParams(request),exception.getMessage());
		//控制台打印
		exception.printStackTrace();
		return mv;
	}

}
