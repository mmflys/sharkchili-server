package com.shark.server.exception;

/**
 * @Author: SuLiang
 * @Date: 2018/10/12 0012
 * @Description:
 */
public class QueryException extends SystemException{
	public QueryException() {
		super();
	}

	public QueryException(String message, Object... args) {
		super(message, args);
	}

	public QueryException(String message, Throwable cause, Object... args) {
		super(message, cause, args);
	}

	public QueryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Object... args) {
		super(message, cause, enableSuppression, writableStackTrace, args);
	}
}
