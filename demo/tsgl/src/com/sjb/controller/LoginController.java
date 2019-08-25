package com.sjb.controller;



import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fxc.entity.QueryInfo;
import com.fxc.entity.Result;
import com.fxc.utils.MyUtils;
import com.sjb.model.system.User;
import com.sjb.model.tsgl.OnlineArticles;
import com.sjb.model.xxgl.Announcement;
import com.sjb.service.system.UserService;
import com.sjb.service.tsgl.OnlineArticlesService;
import com.sjb.service.xxgl.AnnouncementService;


@Controller
@RequestMapping("/login")
public class LoginController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private OnlineArticlesService onlineArticlesService;
	
	@Autowired
	private AnnouncementService announcementService;
	
	@RequestMapping(value="/index",method = RequestMethod.GET)
	public String login1(ModelMap modelMap,HttpServletRequest request) {
		return "/admin/index";
	}

	/**
	 * 管理后台用户登录
	 * @param request
	 * @param response
	 * @param qi
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/signin")
	public void login(HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception {
		qi.init(request,response);
//		//管理员密码不正确看carUserService 的 beforeGet方法有打印
		Result<User> result =  userService.login(qi,request);
		if(result.isSuccess()){
			MyUtils.print(response, "yes");
		}
		else{
			MyUtils.print(response, "账号密码不正确!");
		}
	}

	/**
	 * 获取文章内容
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/getContext")
	public void getContext(HttpServletRequest request, HttpServletResponse response,String id) throws Exception {
		if(id!=null && id.trim()!=""){
			OnlineArticles themeEdit = onlineArticlesService.getDao().get(Long.valueOf(id));
			MyUtils.print(response,themeEdit == null ? "":themeEdit.getContent());
		}
	}

	/**
	 * 获取公告
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/getAnnouncement")
	public void getAnnouncement(HttpServletRequest request, HttpServletResponse response) throws Exception {
		QueryInfo qi = new QueryInfo();
		qi.setEnd(20);
		Date da = new Date();
		qi.getSearch().put("LESSTHAN_createTime", da);//发布时间小于当前时间
		qi.getSearch().put("THAN_maturity", da);//到期时间大于当前时间
		qi.setSort("top");//按置顶排序
		qi.setOrder("desc");
		Result<List<Announcement>> result = announcementService.gets(qi);
		MyUtils.print(response,result);
	}

}
