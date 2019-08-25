package com.sjb.aop;


import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.log.LogAnn;
import com.sjb.interceptor.Interceptor;
import com.sjb.util.Log;
import com.sjb.util.session.SystemUtil;

@Aspect
@Component
public class LogAnnAspect {
	
//	@Around(value = "(execution(* com.sjb.controller..*(..))) && @annotation (logAnn)", argNames = "joinPoint , logAnn")
//	public Object aroundMethod(ProceedingJoinPoint  point,LogAnn logAnn) throws Throwable {
//
//		System.out.println(point.getSignature().getDeclaringType());
//		System.out.println("---------------LogAnnAspectLogAnnAspect------------------");
//		Object obj = null;
//		try {
//			System.out.println(logAnn.name());
//			obj = point.proceed();
//			
//		} catch (Exception e) {
////			MyUtils.print(response, obj);
//			throw new Exception(e);
//		}
//		System.out.println("---------------LogAnnAspectLogAnnAspect------------------");
//		return obj;
//
//	}
	
    @AfterReturning(returning="result" , pointcut="(execution(* com.sjb.controller..*(..))) && @annotation (logAnn)", argNames = "result,logAnn")  
    public void doAfterReturning(JoinPoint joinPoint,Object result,LogAnn logAnn){ 
    	Object[] args = joinPoint.getArgs();
    	HttpServletRequest resquest = null;
    	for(int i=0;i<args.length;i++){
    		if(args[i] instanceof HttpServletRequest){
    			resquest = (HttpServletRequest) args[i];
    			break;
    		}
    	}
    	if(result!=null){
    		//class,操作人,模块名,返回结果
    		Log.business.info(joinPoint.getTarget().getClass().getName()+",{},["+SystemUtil.getUserName()+"],提交参数:[{}],返回结果[{}]",logAnn.name(),Interceptor.getRequestParams(resquest),result.toString());
    	}else{
    		if (args != null && args.length > 0) {
            	if(resquest!=null){
            		Log.business.info(joinPoint.getTarget().getClass().getName()+",{},["+SystemUtil.getUserName()+"],提交参数[{}]",logAnn.name(),Interceptor.getRequestParams(resquest));
            	}
    		}else{
    			Log.business.info(joinPoint.getTarget().getClass().getName()+",{},["+SystemUtil.getUserName()+"],提交参数[{}]",logAnn.name(),null);
    		}
    	}
    } 
    
    
//    @AfterThrowing(value = "@annotation (com.log.LogAnn)", throwing = "e")
//    public void doAfterThrowing(JoinPoint point, Exception e) throws IOException{
//    	if(!(e instanceof UserNotLoginException)){
//    		Object[] args = point.getArgs();
//            if (args != null && args.length > 0) {
//            	HttpServletRequest resquest = null;
//            	for(int i=0;i<args.length;i++){
//            		if(args[i] instanceof HttpServletRequest){
//            			resquest = (HttpServletRequest) args[i];
//            			break;
//            		}
//            	}
//            	if(resquest != null){
//            		Log.allError.error(point.getTarget().getClass().getName()+","+SystemUtil.getUserName()+",请求参数:[{}],异常:[{}]",Interceptor.getRequestParams(resquest),e.getMessage());
//            	}else{
//            		Log.allError.error(point.getTarget().getClass().getName()+","+SystemUtil.getUserName()+",[{}]",e.getMessage());
//            	}
//            }
//    	}
//    }

//	  /**
//     * 最终通知 after advice
//     * 使用的是在上面声明的切面，并且带上个注解，意思是除了满足上面aa()方法的条件还得带上注解才OK
//     */
//	//aa() 方法是aa
//    @After(value = "aa() && @annotation(methodLog)", argNames = "joinPoint, methodLog")
//    public void methodAfter(JoinPoint joinPoint, MethodLog methodLog) throws Throwable {
//        System.out.println("After method     start.......................");
//        //获得自定义注解的参数
//        System.out.println("After method   methodLog 的参数，remark：" + methodLog.description() + " clazz：" + methodLog.clazz());
//        MethodLog remark = getMethodRemark(joinPoint);
//        System.out.println("After method        end.......................");
//    }
	
	
//    声明个切面，切哪呢？切到 com.lxk.service.StudentService 这个目录下，以save开头的方法，方法参数(..)和返回类型(*)不限
//    @Pointcut("execution(* com.lxk.service.StudentService.save*(..))")
//    private void aa() {
//    }//切入点签名
// 
//    /**
//     * 前置通知
//     */
//    @Before("aa()")
//    public void beforeMethod(JoinPoint joinPoint) {
//        System.out.println("before method   start ...");
// 
//        System.out.println("before method   end ...");
//    }
	
//	@After(value = "@annotation (com.log.LogAnn)", argNames = "joinPoint , logAnn")
//	public void doAfter(JoinPoint joinPoint,LogAnn logAnn){
//		System.out.println("=====doAfter通知开始=====");
//		joinPoint.getArgs()
//		
//	}
    
}
