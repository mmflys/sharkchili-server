package com.shark.server.exception;

/**
 * @Author: SuLiang
 * @Date: 2018/9/4 0004
 * @Description: System com.shark.job.exception will make application stop.
 */
public class SystemException extends SharkRuntimeException {

	public SystemException() {
		super();
	}

	public SystemException(String message, Object...args) {
		super(message, args);
	}

	public SystemException(String message, Throwable cause, Object...args) {
		super(message, cause, args);
	}

	public SystemException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Object...args) {
		super(message, cause, enableSuppression, writableStackTrace, args);
	}

}
