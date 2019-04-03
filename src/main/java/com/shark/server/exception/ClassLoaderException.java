package com.shark.server.exception;

/**
 * @Author: SuLiang
 * @Date: 2018/9/7 0007
 * @Description:
 */
public class ClassLoaderException extends SystemException {
	public ClassLoaderException() {
	}

	public ClassLoaderException(String message, Object... args) {
		super(message, args);
	}

	public ClassLoaderException(String message, Throwable cause, Object... args) {
		super(message, cause, args);
	}

	public ClassLoaderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Object... args) {
		super(message, cause, enableSuppression, writableStackTrace, args);
	}
}
