package com.wjd.javacourse.week11.annotation;

import java.lang.annotation.*;


@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LockObject {
    //不需要值
}
