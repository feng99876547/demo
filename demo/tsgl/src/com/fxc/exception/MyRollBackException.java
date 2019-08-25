package com.fxc.exception;

/**
 * 回滚异常 只是用于回滚
 * @author fxc
 */
public class MyRollBackException extends RuntimeException{
	
	private static final long serialVersionUID = -1936969969163474710L;

	public MyRollBackException(String msg) {
		super(msg);
	}
	
	public MyRollBackException() {
		super();
	}

	public MyRollBackException(Throwable throwable) {
		super(throwable);
	}
	
	public MyRollBackException(String s, Throwable throwable, boolean flag,
			boolean flag1) {
		super(s, throwable, flag, flag1);
	}

	public MyRollBackException(String s, Throwable throwable) {
		super(s, throwable);
	}
}
