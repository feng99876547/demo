package com.sjb.controller.system;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fxc.entity.QueryInfo;
import com.fxc.entity.Result;
import com.fxc.utils.MyUtils;
import com.log.LogAnn;
import com.sjb.interceptor.RequiresPermissions;
import com.sjb.model.system.Role;
import com.sjb.model.system.User;
import com.sjb.model.system.UserRoleMiddle;
import com.sjb.service.system.UserRoleMiddleService;
import com.sjb.service.system.UserService;
import com.sjb.util.Treat;
import com.sjb.util.session.SystemUtil;


/**
 * 后台用户管理
 */
@Controller
@RequestMapping("/system/user")
public class UserController {

	@Autowired
	@Qualifier("userServiceImpl")
	private  UserService carUserService;
	
	@Autowired
	@Qualifier("userRoleMiddleServiceImpl")
	private  UserRoleMiddleService UserRoleMiddleServiceImpl;
	
	
	@RequiresPermissions(value = "system:user:read" )
	@RequestMapping
	public String index(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		return "/admin/system/user";
	}
	
	@RequiresPermissions(value = "system:user:read")
	@RequestMapping("/list")
	public void list(HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception{
		qi.init(request, response);
		qi.getSearch().put("THAN_id", 1);//排除管理员
		if(qi.getParams("search_EQ_roleId")!=null){//这个功能很像屎 不符合那什么
			qi.addCancel("Role.roleId");
			qi.getParams().remove("search_LIKE_name");//按角色查取消了name条件 因为不像写sql select  count(*) 那边没做关联
			Result<List<UserRoleMiddle>> list = UserRoleMiddleServiceImpl.gets(qi);
			List<User> li = new ArrayList<User>();
			
			for(int i=0;i< list.getRows().size();i++){//密码要去掉
				li.add(list.getRows().get(i).getUserId());
			}
			Result<List<User>> result = new Result<List<User>>(li,true);
			MyUtils.print(response,result);
			return;
		}else{
			Result<List<User>> result = carUserService.gets(qi);
			MyUtils.print(response,result);
		}
	}
	
	/**
	 * 附属权限查找
	 * @param request
	 * @param response
	 * @param qi
	 * @throws Exception
	 */
	@RequiresPermissions(value = "system:user:subsidiaryList")
	@RequestMapping("/subsidiaryList")
	public void subsidiaryList(HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception{
		qi.init(request, response);
//		qi.getSearch().put("THAN_id", 1);//排除管理员
		if(qi.getParams("search_EQ_roleId")!=null){//这个功能很像屎 不符合那什么
			qi.addCancel("Role.roleId");
			qi.getParams().remove("search_LIKE_name");//按角色查取消了name条件 因为不像写sql select  count(*) 那边没做关联
			Result<List<UserRoleMiddle>> list = UserRoleMiddleServiceImpl.gets(qi);
			List<User> li = new ArrayList<User>();
			
			for(int i=0;i< list.getRows().size();i++){//密码要去掉
				li.add(list.getRows().get(i).getUserId());
			}
			Result<List<User>> result = new Result<List<User>>(li,true);
			MyUtils.print(response,result);
			return;
		}else{
			Result<List<User>> result = carUserService.gets(qi);
			MyUtils.print(response,result);
		}
	}
	
	@RequiresPermissions(value = "system:user:add" )
	@RequestMapping("/add")
	@LogAnn(name="新增用户")
	public void add( HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception {
		//到时候qi要添加一个解锁NotNull注释的配置 不是所有的NotNull都要生效
		qi.init(request, response);
		Result<User> result = carUserService.add(qi);
		MyUtils.print(response,result);
	}

	@RequiresPermissions(value = "system:user:update" )
	@RequestMapping("/update")
	@LogAnn(name="修改用户")
	public void update(HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception {
		qi.init(request, response);
		Result<User> result = carUserService.update(qi);
		MyUtils.print(response,result);
	}
	
	@RequiresPermissions(value = "system:user:delete")
	@RequestMapping("/delete")
	@LogAnn(name = "删除用户")
	public void delete(HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception {
		qi.init(request, response);
		Result<List<User>> result = carUserService.del(qi);
		MyUtils.print(response,result);
	}
	
//	/**
//	 * 获取所有角色 不包含管理员
//	 * @param request
//	 * @param response
//	 * @param qi
//	 * @throws Exception
//	 */
//	@RequiresPermissions(value = "system:user:read")
//	@RequestMapping("/getRoles")
//	public void getRoles(HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception{
//		qi.init(request, response);
//		Result<List<Role>> result = roleService.gets(qi);
//		MyUtils.print(response,result);
//	}
	
	/**
	 * 获取用户当前拥有角色
	 */
	@RequiresPermissions(value = "system:user:read")
	@RequestMapping("/getMyRoles")
	public void getMyRoles(String id,HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception{
		Result<List<Role>> result = new Result<List<Role>>(false);
		List<Role> list = null;
		if(!Treat.isEmpty(id)){
			qi.init(request, response);
			qi.addCancel("User.userId");
			qi.getSearch().put("EQ_userId", id);
			List<UserRoleMiddle> li = UserRoleMiddleServiceImpl.getDao().gets(qi);
			if(li!=null){
				list = new ArrayList<Role>();
				for(UserRoleMiddle urm : li){
					list.add(urm.getRoleId());
				}
				result.setSuccess(true);
				result.setRows(list);
			}
		}
		MyUtils.print(response,result);
	}
	
	/**
	 * 修改密码
	 */
	@RequiresPermissions(value = "public")
	@RequestMapping("/updatePassword")
	public void updatePassword(HttpServletResponse response,String oldpas,String pas) throws Exception{
		String loginName = SystemUtil.getUserAccount();
		Result<?> result = carUserService.updatePassword(loginName, oldpas, pas);
		MyUtils.print(response,result);
	}
	
//	/**
//	 * 获取用户当前拥有角色
//	 */
//	@RequiresPermissions(value = "system:user:read")
//	@RequestMapping("/getCacha")
//	public void getCacha(String id,HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception{
//		qi.init(request, response);
//		Result<User> result = new Result<User>(false);
//		result = carUserService.get(qi);
//		MyUtils.print(response,result);
//	}
}
