package com.sjb.service.system.ehcache;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.sjb.service.ObjectEHCache;

@Component
public class AmountCache extends ObjectEHCache<JSONObject>{

	public AmountCache() {
		super("amount");
	}

}
