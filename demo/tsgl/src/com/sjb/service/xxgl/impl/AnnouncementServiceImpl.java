package com.sjb.service.xxgl.impl;

import java.util.Calendar;
import java.util.Date;
import org.springframework.stereotype.Service;

import com.fxc.admin.jdbc.service.PublicServiceImpl;
import com.fxc.admin.jdbc.service.entity.ServiceSection;
import com.fxc.admin.jdbc.service.entity.ServiceSectionImpl;
import com.fxc.admin.jdbc.service.listener.serviceSection.BeforeAddListener;
import com.fxc.admin.jdbc.service.listener.serviceSection.BeforeUpdateListener;
import com.fxc.entity.QueryInfo;
import com.fxc.entity.Result;
import com.sjb.dao.xxgl.AnnouncementDaoBean;
import com.sjb.model.xxgl.Announcement;
import com.sjb.service.xxgl.AnnouncementService;

@Service
public class AnnouncementServiceImpl extends PublicServiceImpl<Announcement, Integer, AnnouncementDaoBean> implements AnnouncementService{

	public AnnouncementServiceImpl() {
		super();
	}

	private ServiceSection<Announcement> serviceAop ;//使用注入不方便阅读啊

	@Override
	public ServiceSection<Announcement> getServiceAop(){
		 if(serviceAop == null){
			 serviceAop = new ServiceSectionImpl<Announcement>();
			serviceAop.setBeforeAdd(new BeforeAddListener<Announcement>() {
				@Override
				public boolean onAOP(Result<Announcement> result, QueryInfo query) throws Exception {
					if("1".equals(query.getParams("istop"))){
						result.getRows().setTop(new Date());
					}
					if(result.getRows().getEffective()!=null){
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(result.getRows().getCreateTime());
						calendar.add(Calendar.DATE,result.getRows().getEffective());
						result.getRows().setMaturity(calendar.getTime());
					}
					return true;
				}
			});
			
			serviceAop.setBeforeUpdate(new BeforeUpdateListener<Announcement>() {
				@Override
				public boolean onAOP(Result<Announcement> result, QueryInfo query) throws Exception {
					if("1".equals(query.getParams("istop"))){
						result.getRows().setTop(new Date());
					}
					if(result.getRows().getEffective()!=null){
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(result.getRows().getCreateTime());
						calendar.add(Calendar.DATE,result.getRows().getEffective());
						result.getRows().setMaturity(calendar.getTime());
					}
					return true;
				}
			});
		 }
		 return serviceAop;
	 }
	
	
}
