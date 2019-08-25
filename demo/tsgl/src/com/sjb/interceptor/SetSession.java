package com.sjb.interceptor;

import javax.servlet.http.HttpServletRequest;

public interface SetSession {
	public void set(HttpServletRequest request) throws Exception;
}
