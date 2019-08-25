package com.sjb.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fxc.exception.UploadException;
import com.fxc.utils.MyUtils;
import com.sjb.interceptor.RequiresPermissions;
import com.sjb.util.redis.RedisApi;
import com.sjb.util.session.SystemUtil;


@Controller
@RequestMapping(value="/upload")
public class UploadController {

	public UploadController() {
	}

	private static Properties properties = null;
    
	public static final String IMGPATH;
	
	public static final String rootPath;
	
	static {
        InputStream in = RedisApi.class.getClassLoader().getResourceAsStream("rootPath.properties");
        properties = new Properties();
        try {
        	properties.load(in);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        rootPath =  properties.getProperty("path");
        IMGPATH = properties.getProperty("imgpath");
        properties.clear();
        try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
       
	}
	
	@RequiresPermissions(value = "public" )
	@RequestMapping(value="/image")
	public void uploadImage(HttpServletRequest request, HttpServletResponse response,@RequestParam(value="subfileName") String subfileName) throws Exception {
		MyUtils.print(response, saveImage(request,"/userPortrait/"+SystemUtil.getUserId().toString()+subfileName));
	}
	
	/**
	 * 
	 * @param request
	 * @param path 不包含后缀
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public String saveImage(HttpServletRequest request,String path) throws IllegalStateException, IOException{
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext()); 
		if(multipartResolver.isMultipart(request)){
			MultipartHttpServletRequest multiRequest= (MultipartHttpServletRequest)request;
			Iterator<String> iter=multiRequest.getFileNames();
			while(iter.hasNext()){
				MultipartFile file= multiRequest.getFile((String)iter.next());
				String fileName=file.getOriginalFilename();// 文件原名称
				String type = fileName.indexOf(".")!=-1 ? fileName.substring(fileName.lastIndexOf(".")+1, fileName.length()).toLowerCase():null;
				if("png".equals(type) || "jpeg".equals(type) || "jpg".equals(type)){
					//不同类型后缀的图片是会多出来 但是还好
					String sfParh = rootPath+path+"."+type;
					File sf = new File(sfParh);  
					File fileParent = sf.getParentFile();
					if(!fileParent.exists()){  
					    fileParent.mkdirs();  
					}
					file.transferTo(sf);
					return path+"."+type;
				}else{//上传类型不匹配
					throw new UploadException("上传类型:"+type+",不匹配");
				}
			}
		}
		return null;
	}
	
}
