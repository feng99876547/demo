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
import com.sjb.model.xxgl.SchoolCard;
import com.sjb.service.xxgl.SchoolCardService;

@Controller
@RequestMapping("/xxgl/schoolcard")
public class SchoolCardController {

	public SchoolCardController() {
		// TODO Auto-generated constructor stub
	}

	@Autowired
	private SchoolCardService schoolcardService;
	
	@RequiresPermissions(value = "xxgl:schoolcard:read")
	@RequestMapping("/list")
	public void list(HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception{
		qi.init(request, response);
		Result<List<SchoolCard>> result = schoolcardService.gets(qi);
		MyUtils.print(response,result);
	}
	
	@RequiresPermissions(value = "xxgl:schoolcard:subsidiaryList,xxgl:report:read")
	@RequestMapping("/subsidiaryList")
	public void subsidiaryList(HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception{
		qi.init(request, response);
		Result<List<SchoolCard>> result = schoolcardService.gets(qi);
		MyUtils.print(response,result);
	}
	
	@RequiresPermissions(value = "xxgl:schoolcard:add,xxgl:report:update")
	@RequestMapping("/add")
	@LogAnn(name="新增校园卡")
	public void add( HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception {
		qi.init(request, response);
		Result<SchoolCard> result = schoolcardService.add(qi);
		MyUtils.print(response,result);
	}

	@RequiresPermissions(value = "xxgl:schoolcard:update" )
	@RequestMapping("/update")
	@LogAnn(name="修改校园卡")
	public void update( HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception {
		qi.init(request, response);
		Result<SchoolCard> result = schoolcardService.update(qi);
		MyUtils.print(response,result);
	}
	
	@RequiresPermissions(value = "xxgl:schoolcard:delete")
	@RequestMapping("/delete")
	@LogAnn(name = "删除校园卡")
	public void delete(Long id, HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception {
		qi.init(request, response);
		Result<List<SchoolCard>> result = schoolcardService.del(qi);
		MyUtils.print(response,result);
	}
}
