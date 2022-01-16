package com.wjd.javacourse.week11.annotation;

import com.wjd.javacourse.week11.ICacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedisLock {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisLock.class);

    private static final int DEFAULT_ACQUIRY_RESOLUTION_MILLIS = 100;

    private volatile boolean locked = false;

    private String prefix;

    private String objectValue;

    private String lockKey;

    private long expireTime;

    private ICacheManager cacheManager;

    public RedisLock(ICacheManager cacheManager, String prefix, String objectValue) {
        this.cacheManager = cacheManager;
        this.objectValue = objectValue;
        this.prefix = prefix;
        this.lockKey = prefix+objectValue;
    }

    public boolean lock(long timeout, int expireTime) {
        while (timeout >= 0) {
            long expires = System.currentTimeMillis() + expireTime + 1;
            this.expireTime = expires;
            String expiresStr = String.valueOf(expires); //锁到期时间
            if (cacheManager.setNX(lockKey, expiresStr)) {
                locked = true;
                return true;
            }
            String currentValueStr = cacheManager.get(lockKey); //redis里的时间
            if (currentValueStr != null && Long.parseLong(currentValueStr) < System.currentTimeMillis()) {
                String oldValueStr = cacheManager.getSet(lockKey, expiresStr);
                if (oldValueStr != null && oldValueStr.equals(currentValueStr)) {
                    locked = true;
                    return true;
                }
            }
            timeout -= DEFAULT_ACQUIRY_RESOLUTION_MILLIS;
            try {
                Thread.sleep(DEFAULT_ACQUIRY_RESOLUTION_MILLIS);
            } catch (InterruptedException e) {
                LOGGER.warn("锁等待失败");
            }
        }
        return false;
    }

    public void unlock(long currentTime) {
        if(locked&&currentTime<expireTime) {
            cacheManager.delete(lockKey);
            locked = false;
            LOGGER.info("释放锁结果:{},LOCK KEY:{}",true,lockKey);
        }
    }


    public String getLockKey() {
        return lockKey;
    }
}
