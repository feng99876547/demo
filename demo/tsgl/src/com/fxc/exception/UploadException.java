package com.fxc.exception;

public class UploadException extends RuntimeException{


	private static final long serialVersionUID = 4836977963968581341L;


	public UploadException() {
		super();
	}

	public UploadException(String msg) {
		super(msg);
	}
}
