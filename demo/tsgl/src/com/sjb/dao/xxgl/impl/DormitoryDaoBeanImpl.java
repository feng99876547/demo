package com.sjb.dao.xxgl.impl;

import org.springframework.stereotype.Repository;

import com.fxc.admin.jdbc.dao.PublicDaoImpl;
import com.sjb.dao.xxgl.DormitoryDaoBean;
import com.sjb.model.xxgl.Dormitory;

@Repository
public class DormitoryDaoBeanImpl extends PublicDaoImpl<Dormitory, Integer> implements DormitoryDaoBean{

	public DormitoryDaoBeanImpl() {
	}

}
