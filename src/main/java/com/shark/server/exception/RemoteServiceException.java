package com.shark.server.exception;

/**
 * @Author: SuLiang
 * @Date: 2018/9/22 0022
 * @Description:
 */
public class RemoteServiceException extends SharkRuntimeException {
	public RemoteServiceException() {
		super();
	}

	public RemoteServiceException(String message, Object... args) {
		super(message, args);
	}

	public RemoteServiceException(String message, Throwable cause, Object... args) {
		super(message, cause, args);
	}

	public RemoteServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Object... args) {
		super(message, cause, enableSuppression, writableStackTrace, args);
	}
}
