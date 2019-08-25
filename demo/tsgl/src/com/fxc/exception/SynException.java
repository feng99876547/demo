package com.fxc.exception;

/**
 * 用于同步失败抛出的异常
 * @author fxc
 */
public class SynException extends RuntimeException{

	private static final long serialVersionUID = 4289785119999721630L;

	public SynException(String msg){
		super(msg);
	}
	
}
