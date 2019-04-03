package com.shark.server.annotation.consts;

/**
 * The type of message,is a object of @Pit.type(),eg: singleton manyCase
 */
public enum CreateType {
	/**only a object for specific class*/
	SINGLETON,
	/**can create many object for specific class*/
	MANY_CASE,
	/**belong to primary thread*/
	THREAD_SAFETY
}
