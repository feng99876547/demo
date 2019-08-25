package com.sjb.service.tsgl.impl;

import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fxc.admin.jdbc.service.PublicServiceImpl;
import com.fxc.admin.jdbc.service.entity.ServiceSection;
import com.fxc.entity.QueryInfo;
import com.fxc.entity.Result;
import com.fxc.utils.ContextUtils;
import com.fxc.utils.GetPara;
import com.fxc.utils.util.Treat;
import com.sjb.dao.tsgl.ThemeEditDaoBean;
import com.sjb.model.tsgl.OnlineArticles;
import com.sjb.model.tsgl.ThemeEdit;
import com.sjb.service.tsgl.OnlineArticlesService;
import com.sjb.service.tsgl.ThemeEditService;

@Service
public class ThemeEditServiceImpl extends PublicServiceImpl<ThemeEdit,Long,ThemeEditDaoBean> implements ThemeEditService{

	public ThemeEditServiceImpl() {
	}

	@Autowired
	private OnlineArticlesService onlineArticlesService;
	
	@Override
	public ServiceSection<ThemeEdit> getServiceAop() throws Exception {
		return null;
	}

	@Override
	public Result<ThemeEdit> publish(QueryInfo qi) throws Exception {
		ThemeEdit t = GetPara.load(ThemeEdit.class, qi.getParams());
		t.setReleaseStatus(ContextUtils.FB);
		OnlineArticles oa = getOnlineArticles(t);
		
		//将消息发送至同步队列 状态变更为等待同步 等待同步状态不能在次发布 同步成功后在发送一条消息到同步成功队列 这边像同步成功队列拿消息修改状态为同步成功
		//因为在一台服务器只需要一个事物解决
		if(Treat.isEmpty(t.getId())){
			getDao().add(t);
		}else{
			getDao().update(t);
		}
		HashMap<String,Object> ma = new HashMap<String,Object>();
		ma.put("EQ_themeEditId", t.getId());
		if(onlineArticlesService.getDao().getCount(ma)>0){
			oa.setUpdateTime(new Date());
			onlineArticlesService.getDao().update(oa,ma);
		}else{
			oa.setCreateTime(new Date());
			onlineArticlesService.getDao().add(oa);
		}
		return new Result<ThemeEdit>(true,"发布成功!");
	}

	
	public OnlineArticles getOnlineArticles(ThemeEdit t) throws Exception{
		OnlineArticles oa = new OnlineArticles();
		oa.setThemeEditId(t.getId());
		oa.setRemarks(t.getRemarks());
		oa.setTitle(t.getTitle());
		oa.setContent(t.getContent());
		return oa;
	}
}
