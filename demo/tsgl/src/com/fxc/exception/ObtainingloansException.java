package com.fxc.exception;

/**
 * 借款失败
 * @author fxc
 */
public class ObtainingloansException extends RuntimeException{

	private static final long serialVersionUID = -5172810091902737541L;

	public ObtainingloansException(String msg){
		super(msg);
	}
	
}
