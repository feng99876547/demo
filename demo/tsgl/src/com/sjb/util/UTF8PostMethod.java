package com.sjb.util;

import org.apache.commons.httpclient.methods.PostMethod;

public class UTF8PostMethod extends PostMethod{ 
	
	public UTF8PostMethod(String url){       
		super(url);       
	}       
	
	@Override       
	public String getRequestCharSet() {       
	    //return super.getRequestCharSet();       
	    return "utf-8";       
	}    

}
