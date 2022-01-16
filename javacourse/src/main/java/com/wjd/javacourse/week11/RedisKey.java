package com.wjd.javacourse.week11;

/**
 * @Author: WJD
 * @Description:
 * @Created: 2022/1/15
 */
public class RedisKey {
    public static final String UID = "uid:";

    /**
     * 车抵贷并发锁
     */
    public static final String REDIS_LOCK_PREFIX = "REDIS:LOCK:UID:";
}
