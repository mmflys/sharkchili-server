package com.shark.server.annotation;

import java.lang.annotation.*;

/**
 * @Author: SuLiang
 * @Date: 2018/8/30 0030
 * @Description: Indicate method need to be calculate time of execution.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Timer {
}
