package com.wjd.javacourse.week11;

import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.SessionCallback;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public interface ICacheManager {

    String get(final String key);

    void set(final String key, final String value);

    void set(final String key, final String value,long timeout);

    void set(final String key, final String value, long timeout, TimeUnit unit);

    boolean setNX(final String key, final String value);

    Long incrBy(String key,long value);

    String getSet(final String key, final String value);

    void delete(final String key);

    public Map setHashObject(String key, Object obj);

    List<Object> pipeline(RedisCallback<List<Object>> Object);

    boolean zadd(String key, String value, double score);

    Long sadd(String key, String value);

    void batchDel(String... pattern);

    String hget(String key, String value);

    Long hincrBy(String key, String field, long value);

    Long hlen(String key);

    Map<Object, Object> hgetAll(String key);

    Object hmget(String s, Class clazz);

    void hset(String s, String bidStatus, String value);

    Set<String> getAllSet(String key);

    Boolean sIsMember(String key,String value);

    Set<String> smembers(String key);

    Long sdel(String key,String... value);

    Set<String> getSet(String key, long start, long end);

    List<String> getList(String key, long start, long end);

    Long getSetNum(String key);

    Long getListNum(String key);

    Object execute(RedisCallback<Object> calback);

    Object execute(SessionCallback<Object> calback);

    boolean hasKey(String key);

    Object lPop(String key);

    boolean hExist(String key, String hash);

    void rPush(String key, String value);

    void hDel(String key, Object... value);

    Boolean expire(String key, long timeout, TimeUnit unit);

    Long getExpire(String key);

    Long getExpire(String key, TimeUnit timeUnit);

    Long increment(String key, long l);
}
