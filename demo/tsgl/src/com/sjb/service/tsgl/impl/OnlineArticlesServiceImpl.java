package com.sjb.service.tsgl.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fxc.admin.jdbc.service.PublicServiceImpl;
import com.fxc.admin.jdbc.service.entity.ServiceSection;
import com.fxc.admin.jdbc.service.entity.ServiceSectionImpl;
import com.fxc.admin.jdbc.service.listener.serviceSection.BeforeDelListener;

import com.fxc.entity.QueryInfo;
import com.fxc.entity.Result;
import com.fxc.utils.ContextUtils;
import com.sjb.dao.tsgl.OnlineArticlesDaoBean;
import com.sjb.model.tsgl.OnlineArticles;
import com.sjb.model.tsgl.ThemeEdit;
import com.sjb.service.tsgl.OnlineArticlesService;
import com.sjb.service.tsgl.ThemeEditService;

@Service
public class OnlineArticlesServiceImpl extends PublicServiceImpl<OnlineArticles,Long,OnlineArticlesDaoBean> implements OnlineArticlesService{

	@Autowired
	private ThemeEditService themeEditService;
	
	private ServiceSection<OnlineArticles> serviceAop;
	
	@Override
	public ServiceSection<OnlineArticles> getServiceAop() throws Exception {
		if(serviceAop == null){
			serviceAop = new ServiceSectionImpl<OnlineArticles>();
			
			serviceAop.setBeforeDel(new BeforeDelListener<List<OnlineArticles>>() {
				@Override
				public boolean onAOP(Result<List<OnlineArticles>> result, QueryInfo query) throws Exception {
					ThemeEdit te = new ThemeEdit();
					te.setId(Long.parseLong((String)query.getParams("themeEditId")));
					te.setReleaseStatus(ContextUtils.WFB);
					themeEditService.getDao().update(te);
					return true;
				}
			});
		}
		return serviceAop;
	}

}
