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
import com.sjb.model.xxgl.HealthInformation;
import com.sjb.service.xxgl.HealthInformationService;

@Controller
@RequestMapping("/xxgl/healthinformation")
public class HealthInformationController {

	public HealthInformationController() {
	}
	@Autowired
	private HealthInformationService healthInformationService;

	@RequiresPermissions(value = "xxgl:healthinformation:read")
	@RequestMapping("/list")
	public void list(HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception{
		qi.init(request, response);
		Result<List<HealthInformation>> result = healthInformationService.gets(qi);
		MyUtils.print(response,result);
	}
	
	@RequiresPermissions(value = "xxgl:healthinformation:subsidiaryList,xxgl:report:read")
	@RequestMapping("/subsidiaryList")
	public void subsidiaryList(HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception{
		qi.init(request, response);
		Result<List<HealthInformation>> result = healthInformationService.gets(qi);
		MyUtils.print(response,result);
	}
	
	@RequiresPermissions(value = "xxgl:healthinformation:add,xxgl:report:update")
	@RequestMapping("/add")
	@LogAnn(name="新增健康信息")
	public void add( HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception {
		qi.init(request, response);
		Result<HealthInformation> result = healthInformationService.add(qi);
		MyUtils.print(response,result);
	}

	@RequiresPermissions(value = "xxgl:healthinformation:update" )
	@RequestMapping("/update")
	@LogAnn(name="修改健康信息")
	public void update( HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception {
		qi.init(request, response);
		Result<HealthInformation> result = healthInformationService.update(qi);
		MyUtils.print(response,result);
	}
	
	@RequiresPermissions(value = "xxgl:healthinformation:delete")
	@RequestMapping("/delete")
	@LogAnn(name = "删除健康信息")
	public void delete(Long id, HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception {
		qi.init(request, response);
		Result<List<HealthInformation>> result = healthInformationService.del(qi);
		MyUtils.print(response,result);
	}
}
