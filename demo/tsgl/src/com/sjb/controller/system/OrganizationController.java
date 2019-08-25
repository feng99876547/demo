package com.sjb.controller.system;

/**
 * 组织机构管理
 */
public class OrganizationController {
	
//	@Autowired
//	private OrganizationService organizationService;
//
//	@RequiresPermissions(value = "system:organization:read" )
//	@RequestMapping
//	public String index(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
//		return "/admin/system/organization";
//	}
//	
//	@RequiresPermissions(value = "system:organization:read")
//	@RequestMapping("/list")
//	public void list(HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception{
//		qi.init(request, response);
//		//打开页面默认只展示第一级
//		if(Treat.isEmpty(request.getParameter("id"))){
//			qi.getSearch().put("EQ_parentId", "1");
//		}else{
//			qi.getSearch().put("EQ_parentId", request.getParameter("id"));
//		}
//		Result<List<Organization>> result = organizationService.gets(qi);
//		MyUtils.print(response,result);
//	}
//
//	@RequiresPermissions(value = "system:organization:add" )
//	@RequestMapping("/add")
//	@LogAnn(name="新增组织机构")
//	public void add( HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception {
//		qi.init(request, response);
//		Result<Organization> result = organizationService.add(qi);
//		MyUtils.print(response,result);
//	}
//
//	@RequiresPermissions(value = "system:organization:update" )
//	@RequestMapping("/update")
//	@LogAnn(name="修改组织机构")
//	public void update( HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception {
//		qi.init(request, response);
//		Result<Organization> result = organizationService.update(qi);
//		MyUtils.print(response,result);
//	}
	
//	@RequiresPermissions(value = "system:organization:delete")
//	@RequestMapping("/delete")
//	@LogAnn(name = "删除菜单")
//	public void delete(Long id, HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception {
//		qi.init(request, response);
//		Result<List<Menu>> result = menuService.del(qi);
//		MyUtils.print(response,result);
//	}
}
