package com.sjb.dao.xxgl.impl;

import org.springframework.stereotype.Repository;

import com.fxc.admin.jdbc.dao.PublicDaoImpl;
import com.sjb.dao.xxgl.AnnouncementDaoBean;
import com.sjb.model.xxgl.Announcement;

@Repository
public class AnnouncementDaoBeanImpl extends PublicDaoImpl<Announcement, Integer> implements AnnouncementDaoBean{

	public AnnouncementDaoBeanImpl() {
		// TODO Auto-generated constructor stub
	}

}
