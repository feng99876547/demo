package com.sjb.controller;

import java.util.concurrent.CountDownLatch;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fxc.entity.QueryInfo;
import com.sjb.service.system.UserService;

@Controller
@RequestMapping("/login")
public class testController {
	
	private int len = 1000;
	
	private final CountDownLatch cl = new CountDownLatch(len);
	
	@Autowired
	private UserService userService;


//	@RequestMapping(value="/test")
	public void test(HttpServletRequest request, HttpServletResponse response,QueryInfo qi) throws Exception{
		for(int i=0;i<len;i++){
			new Thread(new Runnable() {
				@Override
				public void run() {
					QueryInfo qi = new QueryInfo();
					qi.setKeyName("EQ_id");
					qi.setKeyValue("1");
					cl.countDown();
					try {
						userService.get(qi);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
		cl.await();
	}
	
}
