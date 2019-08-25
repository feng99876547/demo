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
import com.sjb.model.xxgl.FileManagement;
import com.sjb.service.xxgl.FileManagementService;

@Controller
@RequestMapping("/xxgl/filemanagement")
public class FileManagementController {

	public FileManagementController() {
	}
	
	@Autowired
	private FileManagementService fileManagementService;
	
	@RequiresPermissions(value = "xxgl:filemanagement:read")
	@RequestMapping("/list")
	public void list(HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception{
		qi.init(request, response);
		Result<List<FileManagement>> result = fileManagementService.gets(qi);
		MyUtils.print(response,result);
	}
	
	
	@RequiresPermissions(value = "xxgl:filemanagement:subsidiaryList,xxgl:report:read")
	@RequestMapping("/subsidiaryList")
	public void subsidiaryList(HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception{
		qi.init(request, response);
		Result<List<FileManagement>> result = fileManagementService.gets(qi);
		MyUtils.print(response,result);
	}
	
	@RequiresPermissions(value = "xxgl:filemanagement:add,xxgl:report:update")
	@RequestMapping("/add")
	@LogAnn(name="新增档案")
	public void add( HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception {
		qi.init(request, response);
		Result<FileManagement> result = fileManagementService.add(qi);
		MyUtils.print(response,result);
	}

	@RequiresPermissions(value = "xxgl:filemanagement:update" )
	@RequestMapping("/update")
	@LogAnn(name="修改档案")
	public void update( HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception {
		qi.init(request, response);
		Result<FileManagement> result = fileManagementService.update(qi);
		MyUtils.print(response,result);
	}
	
	@RequiresPermissions(value = "xxgl:filemanagement:delete")
	@RequestMapping("/delete")
	@LogAnn(name = "删除档案")
	public void delete(Long id, HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception {
		qi.init(request, response);
		Result<List<FileManagement>> result = fileManagementService.del(qi);
		MyUtils.print(response,result);
	}

}
