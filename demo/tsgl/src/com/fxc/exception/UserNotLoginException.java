package com.fxc.exception;

public class UserNotLoginException extends RuntimeException{

	private static final long serialVersionUID = 2397110142934559931L;

	public UserNotLoginException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserNotLoginException(String s, Throwable throwable, boolean flag,
			boolean flag1) {
		super(s, throwable, flag, flag1);
		// TODO Auto-generated constructor stub
	}

	public UserNotLoginException(String s, Throwable throwable) {
		super(s, throwable);
		// TODO Auto-generated constructor stub
	}

	public UserNotLoginException(String s) {
		super(s);
		// TODO Auto-generated constructor stub
	}

	public UserNotLoginException(Throwable throwable) {
		super(throwable);
		// TODO Auto-generated constructor stub
	}

}
