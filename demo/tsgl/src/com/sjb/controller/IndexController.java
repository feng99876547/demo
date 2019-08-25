package com.sjb.controller;

import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.fxc.exception.UserNotLoginException;
import com.fxc.utils.MyUtils;
import com.sjb.model.system.Menu;
import com.sjb.service.system.MenuService;
import com.sjb.util.StaticKey;
import com.sjb.util.Treat;
import com.sjb.util.session.SystemUtil;


/**
 * IndexController负责首页跳转
 * 
 * @author Even
 */
@Controller
@RequestMapping(value="/login")
//@Scope(value= WebApplicationContext.SCOPE_REQUEST)
public class IndexController{
	
	@Autowired
	private MenuService menuService;
	

	/** 
	 * 获取菜单
	 */
	@RequestMapping(value="/begin")
	public void index(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(SystemUtil.getUser()==null){
			throw new UserNotLoginException("没有登录");
		}
		Criteria c;
		List<Menu> menus = menuService.getUserMenus();
		String menusJson = JSON.toJSONString(menus);
		String context = (Treat.isAdmin() == true ? "1":"0")+","+SystemUtil.getName();
		Cookie cookie = new Cookie("USER",URLEncoder.encode(context, "UTF-8"));
		cookie.setHttpOnly(false);//设为true后，只能通过http访问，javascript无法访问
//		cookie.setMaxAge(3600);//不设置时间随浏览器关闭而关闭
		cookie.setPath("/");//没有设置path nginx配置不同的路径前端会js会拿不到cookie但是提交的时候一样会提交
		response.addCookie(cookie);
//		response.addCookie(cookieName);
		MyUtils.print(response,menusJson);
		
	}
	@RequestMapping(value="/logout")
	public void logout(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.getSession().removeAttribute(StaticKey.USER_SESSION_KEY);
		MyUtils.print(response,"yes");
	}
	
	
}
