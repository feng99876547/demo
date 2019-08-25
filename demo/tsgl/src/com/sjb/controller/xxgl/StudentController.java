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
import com.sjb.model.xxgl.Student;
import com.sjb.service.xxgl.StudentService;

@Controller
@RequestMapping("/xxgl/student")
public class StudentController {

	public StudentController() {
	}

	@Autowired
	private StudentService studentService;

	@RequiresPermissions(value = "xxgl:student:read")
	@RequestMapping("/list")
	public void list(HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception{
		qi.init(request, response);
		Result<List<Student>> result = studentService.gets(qi);
		MyUtils.print(response,result);
	}
	
	
	@RequiresPermissions(value = "public")//毕业设计侧漏就侧漏了
	@RequestMapping("/subsidiaryList")
	public void subsidiaryList(HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception{
		qi.init(request, response);
		Result<List<Student>> result = studentService.gets(qi);
		MyUtils.print(response,result);
	}
	
	@RequiresPermissions(value = "xxgl:student:add" )
	@RequestMapping("/add")
	@LogAnn(name="新增学生")
	public void add( HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception {
		qi.init(request, response);
		Result<Student> result = studentService.add(qi);
		MyUtils.print(response,result);
	}

	@RequiresPermissions(value = "xxgl:student:update" )
	@RequestMapping("/update")
	@LogAnn(name="修改学生")
	public void update( HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception {
		qi.init(request, response);
		Result<Student> result = studentService.update(qi);
		MyUtils.print(response,result);
	}
	
	@RequiresPermissions(value = "xxgl:student:delete")
	@RequestMapping("/delete")
	@LogAnn(name = "删除学生")
	public void delete(Long id, HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception {
		qi.init(request, response);
		Result<List<Student>> result = studentService.del(qi);
		MyUtils.print(response,result);
	}
	
}
