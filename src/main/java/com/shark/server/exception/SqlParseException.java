package com.shark.server.exception;

/**
 * @Author: SuLiang
 * @Date: 2018/10/16 0016
 * @Description:
 */
public class SqlParseException extends SharkRuntimeException{
	public SqlParseException() {
		super();
	}

	public SqlParseException(String message, Object... args) {
		super(message, args);
	}

	public SqlParseException(String message, Throwable cause, Object... args) {
		super(message, cause, args);
	}

	public SqlParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Object... args) {
		super(message, cause, enableSuppression, writableStackTrace, args);
	}
}
