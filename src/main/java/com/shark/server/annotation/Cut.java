package com.shark.server.annotation;

import java.lang.annotation.*;

/**
 * @Author: SuLiang
 * @Date: 2018/8/29 0029
 * @Description: The class or method will be proxied that is annotated @Cut
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Cut {

}
