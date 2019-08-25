package com.sjb.controller.system;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fxc.entity.QueryInfo;
import com.fxc.entity.Result;
import com.fxc.utils.MyUtils;
import com.log.LogAnn;
import com.sjb.interceptor.RequiresPermissions;
import com.sjb.model.system.Permission;
import com.sjb.service.system.PermissionService;



@Controller
@RequestMapping("/system/permission")
public class PermissionController {
	
	@Autowired
	private PermissionService permissionService;
	
	@RequiresPermissions(value = "system:permission:read" )
	@RequestMapping
	public String index(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		return "/admin/system/permission";
	}
	
	@RequiresPermissions(value = "system:permission:read")
	@RequestMapping("/list")
	public void list(HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception{
		qi.init(request, response);
		Result<List<Permission>> result = permissionService.gets(qi);
		MyUtils.print(response,result);
	}

	@RequiresPermissions(value = "system:permission:add" )
	@RequestMapping("/add")
	@LogAnn(name="新增权限")
	public void add(HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception {
		qi.init(request, response);
		Result<Permission> result = permissionService.add(qi);
		MyUtils.print(response,result);
	}

	@RequiresPermissions(value = "system:permission:update" )
	@RequestMapping("/update")
	@LogAnn(name="修改权限")
	public void update(HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception {
		qi.init(request, response);
		Result<Permission> result = permissionService.update(qi);
		MyUtils.print(response,result);
	}
	
	@RequiresPermissions(value = "system:permission:delete")
	@RequestMapping("/delete")
	@LogAnn(name = "删除权限")
	public void delete(Long id, HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception {
		qi.init(request, response);
		Result<List<Permission>> result = permissionService.del(qi);
		MyUtils.print(response,result);
	}
	
}
