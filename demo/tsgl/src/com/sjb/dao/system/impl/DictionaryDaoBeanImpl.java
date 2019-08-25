package com.sjb.dao.system.impl;

import java.util.HashMap;

import org.springframework.stereotype.Repository;

import com.fxc.admin.jdbc.dao.PublicDaoImpl;
import com.fxc.utils.ContextUtils;
import com.sjb.dao.system.DictionaryDaoBean;
import com.sjb.model.system.Dictionary;

@Repository
public class DictionaryDaoBeanImpl extends PublicDaoImpl<Dictionary,Integer> implements DictionaryDaoBean{

	public DictionaryDaoBeanImpl() {
		super.defaultWhere = new HashMap<String,Object>();
		defaultWhere.put("EQ_state", ContextUtils.WSC);//默认查状态未删除的
	}

}
