package com.shark.server.annotation;

import java.lang.annotation.*;

/**
 * @Author: SuLiang
 * @Date: 2018/8/29 0029
 * @Description: mapping annotation to Behavior
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface BehaviorMapAnnotation {

	Class<?> value() default Object.class;

	Class<? extends Annotation> annotation() default Annotation.class;

}
