package com.shark.server.exception;


import com.shark.util.util.StringUtil;

/**
 * @Author: SuLiang
 * @Date: 2018/9/4 0004
 * @Description:
 */
public abstract class SharkRuntimeException extends RuntimeException{

	SharkRuntimeException() {
		super();
	}

	SharkRuntimeException(String message, Object... args) {
		super(StringUtil.format(message,args));
	}

	SharkRuntimeException(String message, Throwable cause, Object... args) {
		super(StringUtil.format(message,args), cause);
	}

	SharkRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Object... args) {
		super(StringUtil.format(message,args), cause, enableSuppression, writableStackTrace);
	}
}
