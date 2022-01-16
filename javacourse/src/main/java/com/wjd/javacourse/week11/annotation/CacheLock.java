package com.wjd.javacourse.week11.annotation;

import java.lang.annotation.*;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CacheLock {
    /**
     * redis 锁key的前缀
     *
     * @return
     */
    String prefix() default "";

    /**
     * 锁key
     *
     * @return
     */
    String key() default "";

    /**
     * 轮询锁的时间
     *
     * @return
     */
    long timeOut() default 1000;

    /**
     * key在redis里存在的时间，1000S
     *
     * @return
     */
    int expireTime() default 1000;
}
