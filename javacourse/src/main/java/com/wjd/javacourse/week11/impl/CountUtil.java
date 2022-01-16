package com.wjd.javacourse.week11.impl;

import com.wjd.javacourse.week11.RedisKey;
import com.wjd.javacourse.week11.annotation.CacheLock;
import com.wjd.javacourse.week11.annotation.LockComplexObject;

/**
 * @Author: WJD
 * @Description:
 * @Created: 2022/1/16
 */
public class CountUtil extends CacheUtil{

    @CacheLock(prefix = RedisKey.REDIS_LOCK_ORDER_PREFIX, timeOut = 0, expireTime = 3000)
    public Long incrBy(@LockComplexObject(field = "key")String key, int count){
        return super.incrBy(key,count);
    }
}
