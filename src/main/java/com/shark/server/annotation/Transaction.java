package com.shark.server.annotation;

import java.lang.annotation.*;

/**
 * @Author: SuLiang
 * @Date: 2018/9/7 0007
 * @Description:
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Transaction {
}
