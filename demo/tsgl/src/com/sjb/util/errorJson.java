package com.sjb.util;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.dao.DataIntegrityViolationException;

import com.fxc.entity.Result;
import com.fxc.exception.BusinesException;
import com.fxc.exception.MyCacheException;
import com.fxc.exception.MyPermissionsException;
import com.fxc.exception.MyRollBackException;
import com.fxc.exception.ParamsException;
import com.fxc.exception.SynException;
import com.fxc.utils.MyUtils;

/**
 * 接口出现错误时返回的json
 * @author fxc
 *
 */
public class errorJson {
	
	/**
	 * 没有登录 返回json
	 */
	public static void userNotLogin(HttpServletResponse response){
		Result<String> result = new Result<String>(false);
		result.setMsg("1001");//没有登录
		try {
			MyUtils.print(response, result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 *  因为业务处理过程中发现不能满足需求 需要主动回滚之前的操作
	 * @param response
	 */
	public static void rollBack(HttpServletResponse response,MyRollBackException exception){
		Result<String> result = new Result<String>(false);
		result.setMsg("1002,"+(exception.getMessage()!=null?exception.getMessage():"业务条件不满足"));//业务处理失败主动回滚
		try {
			MyUtils.print(response, result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 *  权限匹配异常
	 * @param response
	 */
	public static void permission(HttpServletResponse response,MyPermissionsException exception){
		Result<String> result = new Result<String>(false);
		result.setMsg("1003,"+exception.getMessage());
		try {
			MyUtils.print(response, result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 缓存异常
	 * @param response
	 * @param exception
	 */
	public static void cache(HttpServletResponse response, MyCacheException exception) {
		Result<String> result = new Result<String>(false);
		result.setMsg("1004,"+exception.getMessage());
		try {
			MyUtils.print(response, result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 同步失败
	 * @param response
	 * @param exception
	 */
	public static void synfail(HttpServletResponse response,SynException exception) {
		Result<String> result = new Result<String>(false);
		result.setMsg("1005,"+exception.getMessage());
		try {
			MyUtils.print(response, result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 自定义业务异常
	 * @param response
	 * @param exception
	 */
	public static void busines(HttpServletResponse response, BusinesException exception) {
		Result<String> result = new Result<String>(false);
		result.setMsg("1006,"+exception.getMessage());
		try {
			MyUtils.print(response, result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 操作太频繁
	 */
	public static void frequently(HttpServletResponse response) {
		Result<String> result = new Result<String>(false);
		result.setMsg("操作太频繁,本次请求失效");
		try {
			MyUtils.print(response, result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 参数异常
	 */
	public static void params(HttpServletResponse response,ParamsException exception) {
		Result<String> result = new Result<String>(false);
		result.setMsg("1007,"+exception.getMessage());
		try {
			MyUtils.print(response, result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 在使用Spring的时候，经常碰到的一个异常是：DataIntegrityViolationExceptio。
	 * 这个异常是当插入、删除和修改数据的时候，违背的数据完整性约束抛出的异常。
	 * 例如：主键重复异常等。下面给大家讲解一下这个异常的使用
	 * sql 异常
	 * @param response
	 * @param exception
	 */
	public static void sql(HttpServletResponse response, DataIntegrityViolationException exception) {
		Result<String> result = new Result<String>(false);
		result.setMsg("1008,DataIntegrityViolationException异常");
		try {
			MyUtils.print(response, result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
