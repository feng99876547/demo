package com.sjb.controller.xxgl;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fxc.entity.QueryInfo;
import com.fxc.entity.Result;
import com.fxc.utils.MyUtils;
import com.log.LogAnn;
import com.sjb.controller.UploadController;
import com.sjb.controller.tsgl.ThemeEditController;
import com.sjb.interceptor.RequiresPermissions;
import com.sjb.model.xxgl.Announcement;
import com.sjb.service.xxgl.AnnouncementService;

@Controller
@RequestMapping("/xxgl/announcement")
public class AnnouncementController {

	public AnnouncementController() {
	}

	@Autowired
	private AnnouncementService announcementService;

	@RequiresPermissions(value = "xxgl:announcement:read")
	@RequestMapping("/list")
	public void list(HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception{
		qi.init(request, response);
		Result<List<Announcement>> result = announcementService.gets(qi);
		MyUtils.print(response,result);
	}
	
	
	@RequiresPermissions(value = "xxgl:announcement:subsidiaryList")
	@RequestMapping("/subsidiaryList")
	public void subsidiaryList(HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception{
		qi.init(request, response);
		Result<List<Announcement>> result = announcementService.gets(qi);
		MyUtils.print(response,result);
	}
	
	@RequiresPermissions(value = "xxgl:announcement:add")
	@RequestMapping("/add")
	@LogAnn(name="新增公告")
	public void add( HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception {
		qi.init(request, response);
		Result<Announcement> result = announcementService.add(qi);
		MyUtils.print(response,result);
	}

	@RequiresPermissions(value = "xxgl:announcement:update" )
	@RequestMapping("/update")
	@LogAnn(name="修改公告")
	public void update( HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception {
		qi.init(request, response);
		Result<Announcement> result = announcementService.update(qi);
		MyUtils.print(response,result);
	}
	
	@RequiresPermissions(value = "xxgl:announcement:delete")
	@RequestMapping("/delete")
	@LogAnn(name = "删除公告")
	public void delete(Long id, HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception {
		qi.init(request, response);
		Result<List<Announcement>> result = announcementService.del(qi);
		MyUtils.print(response,result);
	}
	
	//私有权限
	@RequiresPermissions(value = "tsgl:announcement:upload" )
	@RequestMapping(value="/subImage")
	public void uploadImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		MyUtils.print(response, saveImage(request,"/themeEdit/"+SystemUtil.getUserId()+id));
		//如果属于私人在加上用户id限制 编辑图片共用吧 如果要分个人或是不同的类型可以在url后面挂上参数id来分包 需要前端更新路径
		MyUtils.print(response, saveImage(request,ThemeEditController.themeEdit,String.valueOf(Calendar.getInstance().getTimeInMillis())));
	}
	
	//私有权限
	/**
	 * 返回上传包下的图片名称
	 * @param request
	 * @param response
	 * @param start
	 * @throws Exception
	 */
	@RequiresPermissions(value = "tsgl:announcement:upload" )
	@RequestMapping(value="/getImage")
	public void getImage(HttpServletRequest request, HttpServletResponse response,String start) throws Exception {
		MyUtils.print(response, getImageName(request,ThemeEditController.themeEdit,start));
	}
	
	
	/**
	 *获取对应的编辑图片 如果是分布式图片同一保存了 可以将上传的图片记录到数据库
	 */
	public String getImageName(HttpServletRequest request,String path,String start) throws IllegalStateException, IOException{
		String rootpath = UploadController.rootPath;
		File packeageDir = new File(rootpath+path);
		JSONObject json = new JSONObject();
        if(packeageDir.isDirectory()){
            String[] allClassName = packeageDir.list();
            json.put("state", "SUCCESS");
            json.put("total", allClassName.length);
            json.put("start", start);
            if(allClassName!=null && allClassName.length>0){
            	JSONArray list = new JSONArray();
            	for(int i = Integer.parseInt(start);i<allClassName.length;i++){
            		JSONObject obj= new JSONObject();
            		obj.put("state", "SUCCESS");
            		//前端用于显示的url
            		obj.put("url",path+allClassName[i]);
            		list.add(obj);
            	}
            	json.put("list", list);
            }
        }
		return json.toJSONString();
	}
	
	/**
	 * 
	 * @param request
	 * @param path 不包含后缀
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public String saveImage(HttpServletRequest request,String path,String name) throws IllegalStateException, IOException{
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext()); 
		if(multipartResolver.isMultipart(request)){
			MultipartHttpServletRequest multiRequest= (MultipartHttpServletRequest)request;
			Iterator<String> iter=multiRequest.getFileNames();
			while(iter.hasNext()){
				MultipartFile file= multiRequest.getFile((String)iter.next());
				String fileName=file.getOriginalFilename();// 文件原名称
				String type = fileName.indexOf(".")!=-1 ? fileName.substring(fileName.lastIndexOf(".")+1, fileName.length()).toLowerCase():null;
				
				String sfParh = UploadController.rootPath+path+name+"."+type;
				File sf = new File(sfParh);  
				File fileParent = sf.getParentFile();
				if(!fileParent.exists()){  
				    fileParent.mkdirs();  
				}
				file.transferTo(sf);
				JSONObject json = new JSONObject();
				json.put("state", "SUCCESS");
				json.put("original", name);
				json.put("size", file.getSize());
				json.put("title", name+"."+type); 
				json.put("type","."+type);
				json.put("url", path+name+"."+type); 
				return json.toJSONString();
			}
		}
		return null;
	}
	
}
