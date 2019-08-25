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
import com.sjb.model.system.Menu;
import com.sjb.service.system.MenuService;


/**
 * 角色信息页面 控制器
 */
@Controller
@RequestMapping("/system/menu")
public class MenuController {
	
	@Autowired
	private MenuService menuService;

	@RequiresPermissions(value = "system:menu:read" )
	@RequestMapping
	public String index(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		return "/admin/system/menu";
	}
	
	@RequiresPermissions(value = "system:menu:read")
	@RequestMapping("/list")
	public void list(HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception{
		qi.init(request, response);
		Result<List<Menu>> result = menuService.gets(qi);
		MyUtils.print(response,result);
	}

	@RequiresPermissions(value = "system:menu:add" )
	@RequestMapping("/add")
	@LogAnn(name="新增菜单")
	public void add( HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception {
		qi.init(request, response);
		Result<Menu> result = menuService.add(qi);
		MyUtils.print(response,result);
	}

	@RequiresPermissions(value = "system:menu:update" )
	@RequestMapping("/update")
	@LogAnn(name="修改菜单")
	public void update( HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception {
		qi.init(request, response);
		Result<Menu> result = menuService.update(qi);
		MyUtils.print(response,result);
	}
	
	@RequiresPermissions(value = "system:menu:delete")
	@RequestMapping("/delete")
	@LogAnn(name = "删除菜单")
	public void delete(Long id, HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception {
		qi.init(request, response);
		Result<List<Menu>> result = menuService.del(qi);
		MyUtils.print(response,result);
	}
	
	
}
