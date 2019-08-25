package com.sjb.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * 过滤哪些名单可以实现跨域
 */
//@WebFilter(filterName="domainFilter",urlPatterns = "/*")
public class DomainFilter implements Filter {

    /**
     * Default constructor. 
     */
    public DomainFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
//		String origin = ((HttpServletRequest) request).getHeader("Origin");
//		System.out.println(origin);
		((HttpServletResponse) response).setHeader("Access-Control-Allow-Origin","*");//暂时不设置白名单
		//OPTIONS请求，浏览器在某些请求中，在正式通信前会增加一次HTTP查询请求，称为"预检"请求
		((HttpServletResponse) response).setHeader("Access-Control-Allow-Methods","POST,GET,OPTIONS,DELETE");
		((HttpServletResponse) response).setHeader("Access-Control-Max-Age","1800");
		((HttpServletResponse) response).setHeader("Access-Control-Allow-Credentials","true");
		((HttpServletResponse) response).setHeader("Access-Control-Allow-Headers","Origin, X-Requested-With, Content-Type, Accept");
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
