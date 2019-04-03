package com.shark.server.annotation;

import java.lang.annotation.*;

/**
 * @Author: SuLiang
 * @Date: 2018/9/22 0022
 * @Description:
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Syn {
}
