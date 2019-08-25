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
import com.sjb.model.xxgl.Dormitory;
import com.sjb.service.xxgl.DormitoryService;

@Controller
@RequestMapping("/xxgl/dormitory")
public class DormitoryController {

	@Autowired
	private DormitoryService dormitoryService;
	
	public DormitoryController() {
	}
	
	
	@RequiresPermissions(value = "xxgl:dormitory:read")
	@RequestMapping("/list")
	public void list(HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception{
		qi.init(request, response);
		Result<List<Dormitory>> result = dormitoryService.gets(qi);
		MyUtils.print(response,result);
	}
	
	@RequiresPermissions(value = "xxgl:dormitory:subsidiaryList,xxgl:report:read")
	@RequestMapping("/subsidiaryList")
	public void subsidiaryList(HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception{
		qi.init(request, response);
		Result<List<Dormitory>> result = dormitoryService.gets(qi);
		MyUtils.print(response,result);
	}

	@RequiresPermissions(value = "xxgl:dormitory:add,xxgl:report:update")
	@RequestMapping("/add")
	@LogAnn(name="新增宿舍")
	public void add( HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception {
		qi.init(request, response);
		Result<Dormitory> result = dormitoryService.add(qi);
		MyUtils.print(response,result);
	}

	@RequiresPermissions(value = "xxgl:dormitory:update" )
	@RequestMapping("/update")
	@LogAnn(name="修改宿舍")
	public void update( HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception {
		qi.init(request, response);
		Result<Dormitory> result = dormitoryService.update(qi);
		MyUtils.print(response,result);
	}
	
	@RequiresPermissions(value = "xxgl:dormitory:update" )
	@RequestMapping("/updateRSZ")
	public void updateRSZ( HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception {
		qi.init(request, response);
		Result<Dormitory> result = dormitoryService.updateRSZ(Integer.parseInt(qi.getParams("id").toString()));
		MyUtils.print(response,result);
	}
	
	@RequiresPermissions(value = "xxgl:dormitory:update" )
	@RequestMapping("/updateRSJ")
	public void updateRSJ( HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception {
		qi.init(request, response);
		Result<Dormitory> result = dormitoryService.updateRSJ(Integer.parseInt(qi.getParams("id").toString()));
		MyUtils.print(response,result);
	}
	
	@RequiresPermissions(value = "xxgl:dormitory:delete")
	@RequestMapping("/delete")
	@LogAnn(name = "删除宿舍")
	public void delete(Long id, HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception {
		qi.init(request, response);
		Result<List<Dormitory>> result = dormitoryService.del(qi);
		MyUtils.print(response,result);
	}

}
