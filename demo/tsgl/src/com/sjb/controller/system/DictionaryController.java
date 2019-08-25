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
import com.sjb.model.system.Dictionary;
import com.sjb.service.system.DictionaryService;

@Controller
@RequestMapping("/system/dictionary")
public class DictionaryController {

	public DictionaryController() {
	}
	
	@Autowired
	private DictionaryService dictionaryService;
	
	@RequiresPermissions(value = "system:dictionary:read")
	@RequestMapping("/list")
	public void list(HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception{
		qi.init(request, response);
		Result<List<Dictionary>> result = dictionaryService.gets(qi);
		MyUtils.print(response,result);
	}

	@RequiresPermissions(value = "system:dictionary:add" )
	@RequestMapping("/add")
	@LogAnn(name="新增字典")
	public void add( HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception {
		qi.init(request, response);
		Result<Dictionary> result = dictionaryService.add(qi);
		MyUtils.print(response,result);
	}

	@RequiresPermissions(value = "system:dictionary:update" )
	@RequestMapping("/update")
	@LogAnn(name="修改字典")
	public void update( HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception {
		qi.init(request, response);
		Result<Dictionary> result = dictionaryService.update(qi);
		MyUtils.print(response,result);
	}
	
	@RequiresPermissions(value = "system:dictionary:delete")
	@RequestMapping("/delete")
	@LogAnn(name = "删除字典")
	public void delete(Long id, HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception {
		qi.init(request, response);
		Result<List<Dictionary>> result = dictionaryService.del(qi);
		MyUtils.print(response,result);
	}

}
