package com.sjb.controller.tsgl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fxc.entity.QueryInfo;
import com.fxc.entity.Result;
import com.fxc.utils.MyUtils;
import com.log.LogAnn;
import com.sjb.interceptor.RequiresPermissions;
import com.sjb.model.tsgl.OnlineArticles;
import com.sjb.service.tsgl.OnlineArticlesService;

@Controller
@RequestMapping("/tsgl/onlineArticles")
public class OnlineArticlesController {

	public OnlineArticlesController() {
	}
	
	@Autowired
	private OnlineArticlesService onlineArticlesService;
	
	

	@RequiresPermissions(value = "tsgl:onlineArticles:read")
	@RequestMapping("/list")
	public void list(HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception{
		qi.init(request, response);
		Result<List<OnlineArticles>> result = onlineArticlesService.gets(qi);
		MyUtils.print(response,result);
	}
	
	@RequiresPermissions(value = "tsgl:onlineArticles:delete")
	@RequestMapping("/delete")
	@LogAnn(name = "删除上线文章")
	public void delete(Long id, HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception {
		qi.init(request, response);
		Result<List<OnlineArticles>> result = onlineArticlesService.del(qi);
		MyUtils.print(response,result);
	}
	
}
