package com.sjb.controller.xxgl;

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
import com.sjb.model.xxgl.Contributions;
import com.sjb.service.xxgl.ContributionsService;

@Controller
@RequestMapping("/xxgl/contributions")
public class ContributionsController {

	public ContributionsController() {
	}
	
	@Autowired
	private ContributionsService contributionsService;
	
	@RequiresPermissions(value = "xxgl:contributions:read")
	@RequestMapping("/list")
	public void list(HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception{
		qi.init(request, response);
		Result<List<Contributions>> result = contributionsService.gets(qi);
		MyUtils.print(response,result);
	}
	
	@RequiresPermissions(value = "xxgl:contributions:subsidiaryList,xxgl:report:read")
	@RequestMapping("/subsidiaryList")
	public void subsidiaryList(HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception{
		qi.init(request, response);
		Result<List<Contributions>> result = contributionsService.gets(qi);
		MyUtils.print(response,result);
	}

	@RequiresPermissions(value = "xxgl:contributions:add,xxgl:report:update" )
	@RequestMapping("/add")
	@LogAnn(name="新增缴费")
	public void add( HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception {
		qi.init(request, response);
		Result<Contributions> result = contributionsService.add(qi);
		MyUtils.print(response,result);
	}

	@RequiresPermissions(value = "xxgl:contributions:update" )
	@RequestMapping("/update")
	@LogAnn(name="修改缴费")
	public void update( HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception {
		qi.init(request, response);
		Result<Contributions> result = contributionsService.update(qi);
		MyUtils.print(response,result);
	}
	
	@RequiresPermissions(value = "xxgl:contributions:delete")
	@RequestMapping("/delete")
	@LogAnn(name = "删除缴费")
	public void delete(Long id, HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception {
		qi.init(request, response);
		Result<List<Contributions>> result = contributionsService.del(qi);
		MyUtils.print(response,result);
	}


}
