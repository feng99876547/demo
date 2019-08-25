package com.sjb.dao.tsgl.impl;

import org.springframework.stereotype.Repository;

import com.fxc.admin.jdbc.dao.PublicDaoImpl;
import com.sjb.dao.tsgl.OnlineArticlesDaoBean;
import com.sjb.model.tsgl.OnlineArticles;

@Repository
public class OnlineArticlesDaoBeanImpl extends PublicDaoImpl<OnlineArticles, Long> implements OnlineArticlesDaoBean{

	public OnlineArticlesDaoBeanImpl() {
	}

}
