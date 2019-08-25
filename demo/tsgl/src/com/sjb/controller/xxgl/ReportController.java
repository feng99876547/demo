package com.sjb.controller.xxgl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fxc.entity.QueryInfo;
import com.fxc.entity.Result;
import com.fxc.utils.MyUtils;
import com.log.LogAnn;
import com.sjb.interceptor.RequiresPermissions;
import com.sjb.model.xxgl.Report;
import com.sjb.model.xxgl.Student;
import com.sjb.service.xxgl.ReportService;
import com.sjb.service.xxgl.StudentService;

@Controller
@RequestMapping("/xxgl/report")
public class ReportController {

	public ReportController() {
	}

	@Autowired
	private ReportService reportService;
	
	@Autowired
	private StudentService studentService;
	
	@RequiresPermissions(value = "xxgl:report:read")
	@RequestMapping("/list")
	public void list(HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception{
		qi.init(request, response);
		Result<List<Report>> result = reportService.gets(qi);
		MyUtils.print(response,result);
	}
	

	@RequiresPermissions(value = "xxgl:report:add" )
	@RequestMapping("/add")
	@LogAnn(name="新增报道")
	public void add( HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception {
		qi.init(request, response);
		Result<Report> result = reportService.add(qi);
		MyUtils.print(response,result);
	}

	
	@RequiresPermissions(value = "xxgl:report:update" )
	@RequestMapping("/update")
	@LogAnn(name="修改报道")
	public void update( HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception {
		qi.init(request, response);
		Result<Report> result = reportService.update(qi);
		MyUtils.print(response,result);
	}
	
	@RequiresPermissions(value = "xxgl:report:update" )
	@RequestMapping("/updatess")
	@LogAnn(name="报到分配宿舍")
	public void updatess( HttpServletRequest request, HttpServletResponse response,@RequestParam(value="id") Long id,
			@RequestParam(value="dormitoryId") Integer dormitoryId) throws Exception {
		Result<Student> result = studentService.updatess(id,dormitoryId);
		MyUtils.print(response,result);
	}
	
	@RequiresPermissions(value = "xxgl:report:delete")
	@RequestMapping("/delete")
	@LogAnn(name = "删除报道")
	public void delete(Long id, HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception {
		qi.init(request, response);
		Result<List<Report>> result = reportService.del(qi);
		MyUtils.print(response,result);
	}
}
