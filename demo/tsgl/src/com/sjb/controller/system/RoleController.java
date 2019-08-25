package com.sjb.controller.system;

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
import com.sjb.model.system.Permission;
import com.sjb.model.system.Role;
import com.sjb.service.system.RoleService;
import com.sjb.util.Treat;



@Controller
@RequestMapping("/system/role")
public class RoleController {

	@Autowired
	private RoleService roleService;
	

	@RequiresPermissions(value = "system:role:read")
	@RequestMapping("/list")
	public void list(HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception{
		qi.init(request, response);
		Result<List<Role>> result = roleService.gets(qi);
		MyUtils.print(response,result);
	}
	
	@RequiresPermissions(value = "system:role:subsidiaryList")
	@RequestMapping("/subsidiaryList")
	public void subsidiaryList(HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception{
		qi.init(request, response);
		Result<List<Role>> result = roleService.gets(qi);
		MyUtils.print(response,result);
	}
	
	@RequiresPermissions(value = "system:role:add" )
	@RequestMapping("/add")
	@LogAnn(name="新增角色")
	public void add(HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception {
		qi.init(request, response);
		Result<Role> result = roleService.add(qi);
		MyUtils.print(response,result);
	}

	/**
	 * 修改操作后需要把缓存清掉
	 * @param request
	 * @param response
	 * @param qi
	 * @throws Exception
	 */
	@RequiresPermissions(value = "system:role:update" )
	@RequestMapping("/update")
	@LogAnn(name="修改角色")
	public void update(HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception {
		qi.init(request, response);
		Result<Role> result = roleService.update(qi);
		MyUtils.print(response,result);
	}
	
	@RequiresPermissions(value = "system:role:delete")
	@RequestMapping("/delete")
	@LogAnn(name = "删除角色")
	public void delete(HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception {
		qi.init(request, response);
		Result<List<Role>> result = roleService.del(qi);
		MyUtils.print(response,result);
	}
	
	/**
	 * 获取角色权限已选权限
	 * 
	 * @param roleId
	 * @param request
	 * @param response
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	@RequiresPermissions(value ="system:role:read")
	@RequestMapping("/getRolePermission")
	public void getRolePermission(HttpServletRequest request, HttpServletResponse response) throws NumberFormatException, Exception {
		String roleId = request.getParameter("roleId");
		Result<List<Permission>> result = new Result<List<Permission>>();
		if(roleId != null){
			result = roleService.getRolePermission(Long.parseLong(roleId));
			result.setSuccess(true);
		}
		MyUtils.print(response,result);
	}


	/**
	 * 获取角色剩余可选权限page(不包含已选)
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	@RequiresPermissions(value = "system:role:read")
	@RequestMapping("/getChoosablePermission")
	public void getChoosablePermission(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String roleId = request.getParameter("roleId");
		Result<List<Permission>> result = roleService.getChoosablePermission(request,Treat.isEmpty(roleId) ? null:Long.parseLong(roleId));
		MyUtils.print(response,result);
	}
}
