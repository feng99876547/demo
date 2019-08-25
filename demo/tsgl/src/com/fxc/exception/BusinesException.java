package com.fxc.exception;

public class BusinesException extends RuntimeException{

	private static final long serialVersionUID = 8312906901427688522L;
	
	
	public BusinesException(String msg){
		super(msg);
	}
	
	public BusinesException(String s, Throwable throwable) {
		super(s, throwable);
	}
	
}
