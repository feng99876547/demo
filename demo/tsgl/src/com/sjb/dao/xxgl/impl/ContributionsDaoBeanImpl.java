package com.sjb.dao.xxgl.impl;

import org.springframework.stereotype.Repository;

import com.fxc.admin.jdbc.dao.PublicDaoImpl;
import com.sjb.dao.xxgl.ContributionsDaoBean;
import com.sjb.model.xxgl.Contributions;

@Repository
public class ContributionsDaoBeanImpl extends PublicDaoImpl<Contributions, Long> implements ContributionsDaoBean{

	public ContributionsDaoBeanImpl() {
	}

}
