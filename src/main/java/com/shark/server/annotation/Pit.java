package com.shark.server.annotation;

import com.shark.server.annotation.consts.CreateType;

import java.lang.annotation.*;

/**
 * Indicate the variable needed to be inject.
 */
@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Pit {
	/**object is equal to name*/
	String value() default "";
	/**message name*/
	String name() default "";
	/**type*/
	CreateType type() default  CreateType.MANY_CASE;
}
