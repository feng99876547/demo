package com.sjb.dao.xxgl.impl;

import org.springframework.stereotype.Repository;

import com.fxc.admin.jdbc.dao.PublicDaoImpl;
import com.sjb.dao.xxgl.SchoolCardDaoBean;
import com.sjb.model.xxgl.SchoolCard;

@Repository
public class SchoolCardDaoBeanImpl extends PublicDaoImpl<SchoolCard, Long> implements SchoolCardDaoBean{

	public SchoolCardDaoBeanImpl() {
		super();
	}

}
