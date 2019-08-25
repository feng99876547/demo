package com.sjb.interceptor;

import javax.servlet.http.HttpServletRequest;

import com.sjb.model.PublicUser;
import com.sjb.util.StaticKey;
import com.sjb.util.session.SystemUtil;

public class MySession implements SetSession{

	@Override
	public void set(HttpServletRequest request) throws Exception {
		//实现了一致性session 然后通过线程变量指向session的引用 没有实现就是使用本地session
		SystemUtil.SessionMagger.set((PublicUser)request.getSession().getAttribute(StaticKey.USER_SESSION_KEY));		
	}

}
