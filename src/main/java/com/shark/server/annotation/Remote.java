package com.shark.server.annotation;

import java.lang.annotation.*;

/**
 * @Author: SuLiang
 * @Date: 2018/9/20 0020
 * @Description: Indicate local address and port as a server
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Remote {
	String address() default "localhost";
	String publicAddress() default "localhost";
	int port();
	String name();
}
