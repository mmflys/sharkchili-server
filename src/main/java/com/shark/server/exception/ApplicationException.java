package com.shark.server.exception;

/**
 * @Author: SuLiang
 * @Date: 2018/9/4 0004
 * @Description: Application com.shark.job.exception will not make application stop.
 */
public class ApplicationException extends SharkRuntimeException {

	public ApplicationException() {
		super();
	}

	public ApplicationException(String message, Object...args) {
		super(message, args);
	}

	public ApplicationException(String message, Throwable cause, Object...args) {
		super(message, cause, args);
	}

	public ApplicationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Object...args) {
		super(message, cause, enableSuppression, writableStackTrace, args);
	}

}
