package com.fxc.exception;

public class MyPermissionsException extends RuntimeException{

	
	private static final long serialVersionUID = -3188341459739411100L;

	private String className;
	
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public MyPermissionsException() {
		super();
	}

	public MyPermissionsException(String arg0, Throwable arg1, boolean arg2,
			boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public MyPermissionsException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public MyPermissionsException(String arg0) {
		super(arg0);
	}

	public MyPermissionsException(String arg0,String className) {
		super(arg0);
		this.className = className;
	}
	
	public MyPermissionsException(Throwable arg0) {
		super(arg0);
	}

}
