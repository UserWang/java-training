package com.wjd.javacourse.week11;

import com.wjd.javacourse.week11.annotation.CacheLock;
import com.wjd.javacourse.week11.annotation.LockComplexObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: WJD
 * @Description:
 * @Created: 2022/1/15
 */
@RestController
@RequestMapping("/redistest")
public class RedisController {

    @Resource
    private ICacheManager icacheManager;

    @RequestMapping("/set")
    @CacheLock(prefix = RedisKey.REDIS_LOCK_PREFIX, timeOut = 0, expireTime = 3000)
    public void set(@LockComplexObject(field = "uid")String uid) {
        icacheManager.set(RedisKey.UID, uid);
    }

    @RequestMapping("/get")
    public String get(String key) {
        if (StringUtils.isEmpty(key)){
            key = RedisKey.UID;
        }
        return icacheManager.get(key);
    }
}
