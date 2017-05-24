package com.tjliqy.exception;

/**
 * Created by yanz on 2017/3/26.
 */
public class BizException extends RuntimeException {

	private int errorCode;
	private String msg;

	public BizException() {
		super();
	}

	public BizException(String message, int errorCode) {
		super(message);
		this.errorCode = errorCode;
		this.msg = message;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public String getMsg() {
		return msg;
	}
}
