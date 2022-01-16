package com.wjd.javacourse.week11.impl;

import com.wjd.javacourse.common.util.BeanToMapUtil;
import com.wjd.javacourse.week11.ICacheManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.connection.convert.Converters;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.params.SetParams;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class CacheUtil implements ICacheManager {
    @Resource
    private JedisCluster jedisCluster;

    @Override
    public String get(String key) {
        Object obj = null;
        try {
            obj = jedisCluster.get(key);
            log.info("Jedis get success :{}", key);
        } catch (Exception e) {
            log.error("get redis error, key : {}", key);
        }
        return obj != null ? obj.toString() : null;
    }

    @Override
    public void set(String key, String value) {
        try {
            // 超时时间单位:秒
            jedisCluster.set(key, value);
            log.info("Jedis set success :{}", key);
        } catch (Exception e) {
            log.error("set redis with timeout error, key : {}", key);
        }
    }

    @Override
    public void set(String key, String value, long timeout) {
        try {
            // 超时时间单位:秒
            jedisCluster.set(key, value, SetParams.setParams().ex((int) timeout));
            log.info("Jedis set success :{}",key);
        } catch (Exception e) {
            log.error("set redis with timeout error, key : {}", key);
        }
    }

    @Override
    public void set(String key, String value, long timeout, TimeUnit unit) {
        try {
            if (TimeUnit.SECONDS.equals(unit)) {
                jedisCluster.set(key, value, SetParams.setParams().ex((int) timeout));
            } else if (TimeUnit.MILLISECONDS.equals(unit)) {
                jedisCluster.set(key, value, SetParams.setParams().px(timeout));
            } else {
                jedisCluster.set(key,value,SetParams.setParams().ex((int)TimeoutUtils.toSeconds(timeout,unit)));
            }
            log.info("Jedis set success key:{}", key);
        } catch (Exception e) {
            log.error("set redis with unit error, key : {}", key);
        }
    }

    @Override
    public boolean setNX(String key, String value) {
        try {
            return jedisCluster.setnx(key, value) == 1? true :false;
        } catch (Exception e) {
            log.error("setNX redis error, key : {}", key);
        }
        return false;
    }

    @Override
    public Long incrBy(String key, long value) {
        try {
            return jedisCluster.incrBy(key,value);
        } catch (Exception e) {
            log.error("incr redis error, key : {}", key);
        }
        return null;
    }

    @Override
    public String getSet(String key, String value) {
        try {
            return jedisCluster.getSet(key, value);
        } catch (Exception e) {
            log.error("getSet redis error, key : {}", key);
        }
        return null;
    }

    @Override
    public void delete(String key) {
        try {
            jedisCluster.del(key);
        } catch (Exception e) {
            log.error("delete redis error, key : {}", key);
        }
    }

    @Override
    public Map setHashObject(String key, Object obj) {
        try {
            Map map = BeanToMapUtil.convertBean(obj);
            jedisCluster.hmset(key, map);
            return map;
        } catch (Exception e) {
            log.error("setHashObject redis error, key : {}", key);
        }
        return null;
    }

    @Override
    public List<Object> pipeline(RedisCallback<List<Object>> Object) {
        return null;
    }

    @Override
    public boolean zadd(String key, String value, double score) {
        try {
            return jedisCluster.zadd(key, score, value) == 1 ? true : false;
        } catch (Exception e) {
            log.error("setNX redis error, key : {}", key);
        }
        return false;
    }

    @Override
    public void batchDel(String... keys) {
        try {
            jedisCluster.del(keys);
        } catch (Exception e) {
            log.error("batchDel redis error, key : {}", keys);
        }
    }

    @Override
    public String hget(String key, String value) {
        try {
            return jedisCluster.hget(key, value);
        } catch (Exception e) {
            log.error("hget redis error, key : {}", key);
        }
        return null;
    }

    @Override
    public Long hincrBy(String key, String field, long value) {
        try {
            return jedisCluster.hincrBy(key, field,value);
        } catch (Exception e) {
            log.error("hincrBy redis error, key : {}", key);
        }
        return null;
    }

    @Override
    public Long hlen(String key) {
        try {
            return jedisCluster.hlen(key);
        } catch (Exception e) {
            log.error("hget redis error, key : {}", key);
        }
        return null;
    }

    @Override
    public Map hgetAll(String key) {
        try {
            return jedisCluster.hgetAll(key);
        } catch (Exception e) {
            log.error("hget redis error, key : {}", key);
        }
        return null;
    }

    @Override
    public Object hmget(String s, Class clazz) {
        try {
            Field[] fields = clazz.getDeclaredFields();
            List<String> fieldNames = new ArrayList<>();
            for (Field field : fields) {
                fieldNames.add(field.getName());
            }
            List<String> values = jedisCluster.hmget(s, fieldNames.toArray(new String[fieldNames.size()]));
            if (values == null || values.isEmpty()) {
                return null;
            }
            Object obj = clazz.newInstance();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                field.setAccessible(true);
                String type = field.getGenericType().toString();
                String value = values.get(i);
                if (StringUtils.isBlank(value)) {
                    if (type.equals("class java.lang.String")) {
                        field.set(obj, value);
                    }
                    continue;
                }
                if (type.equals("int")) {
                    field.set(obj, Integer.valueOf(value));
                } else if (type.equals("class java.math.BigDecimal")) {
                    field.set(obj, new BigDecimal(value));
                } else if (type.equals("class java.util.Date")) {
                    field.set(obj, new Date(Long.parseLong(value)));
                } else {
                    field.set(obj, values.get(i));
                }
            }
            return obj;
        } catch (Exception e) {
            log.error("hmget redis error, key : {}", s);
        }
        return null;
    }

    @Override
    public void hset(String key, String bidStatus, String value) {
        try {
            jedisCluster.hset(key, bidStatus,value);
        } catch (Exception e) {
            log.error("hset redis error, key : {}", key);
        }
    }

    @Override
    public Set<String> getAllSet(String key) {
        try {
            return jedisCluster.zrange(key, 0, -1);
        }catch (Exception e){
            log.error("getAllSet redis error, key : {}", key);
        }
        return null;
    }

    @Override
    public Boolean sIsMember(String key, String value) {
        try {
            return jedisCluster.sismember(key, value);
        }catch (Exception e){
            log.error("sIsMember redis error, key : {}", key);
        }
        return false;
    }

    @Override
    public Set<String> smembers(String key) {
        try {
            return jedisCluster.smembers(key);
        }catch (Exception e){
            log.error("smembers redis error, key : {}", key);
        }
        return null;
    }

    @Override
    public Long sadd(String key, String value) {
        try {
            return jedisCluster.sadd(key, value);
        }catch (Exception e){
            log.error("sadd redis error, key : {}", key);
        }
        return null;
    }

    @Override
    public Long sdel(String key, String... value) {
        try {
            return jedisCluster.srem(key,value);
        }catch (Exception e){
            log.error("sdel redis error, key : {}", key);
        }
        return null;
    }

    @Override
    public Set<String> getSet(String key, long start, long end) {
        try {
            return jedisCluster.zrange(key, start, end);
        }catch (Exception e){
            log.error("getSet redis error, key : {}", key);
        }
        return null;
    }

    @Override
    public List<String> getList(String key, long start, long end) {
        try {
            return jedisCluster.lrange(key, start, end);
        }catch (Exception e){
            log.error("getList redis error, key : {}", key);
        }
        return null;
    }

    @Override
    public Long getSetNum(String key) {
        try {
            return jedisCluster.zcard(key);
        }catch (Exception e){
            log.error("getSetNum redis error, key : {}", key);
        }
        return null;
    }

    @Override
    public Long getListNum(String key) {
        try {
            return jedisCluster.llen(key);
        }catch (Exception e){
            log.error("getListNum redis error, key : {}", key);
        }
        return null;
    }

    @Override
    public Object execute(RedisCallback<Object> calback) {
        return null;
    }

    @Override
    public Object execute(SessionCallback<Object> calback) {
        return null;
    }

    @Override
    public boolean hasKey(String key) {
        try {
            return jedisCluster.exists(key);
        }catch (Exception e){
            log.error("hasKey redis error, key : {}", key);
        }
        return false;
    }

    @Override
    public Object lPop(String key) {
        try {
            return jedisCluster.lpop(key);
        }catch (Exception e){
            log.error("lPop redis error, key : {}", key);
        }
        return null;
    }

    @Override
    public boolean hExist(String key, String hash) {
        try {
            return jedisCluster.hexists(key,hash);
        }catch (Exception e){
            log.error("hExist redis error, key : {}", key);
        }
        return false;
    }

    @Override
    public void rPush(String key, String value) {
        try {
            jedisCluster.rpush(key,value);
        }catch (Exception e){
            log.error("rPush redis error, key : {}", key);
        }
    }

    @Override
    public void hDel(String key, Object... value) {
        try {
            jedisCluster.hdel(key,value.toString());
        }catch (Exception e){
            log.error("hDel redis error, key : {}", key);
        }
    }

    @Override
    public Boolean expire(String key, long timeout, TimeUnit unit) {
        Long flag = null;
        try {
            if (TimeUnit.SECONDS.equals(unit)) {
                flag = jedisCluster.expire(key,(int)timeout);
            } else if (TimeUnit.MILLISECONDS.equals(unit)) {
                flag = jedisCluster.pexpire(key,timeout);
            } else {
                flag = jedisCluster.expire(key,(int) TimeoutUtils.toSeconds(timeout,unit));
            }
        }catch (Exception e){
            log.error("expire redis error, key : {}", key);
        }
        return flag == null ? false : flag == 1 ? true : false;
    }

    @Override
    public Long getExpire(String key) {
        try {
            return jedisCluster.ttl(key);
        }catch (Exception e){
            log.error("getExpire redis error, key : {}", key);
        }
        return null;
    }

    @Override
    public Long getExpire(String key, TimeUnit timeUnit) {
        try {
            return Converters.secondsToTimeUnit(this.jedisCluster.ttl(key), timeUnit);
        }catch (Exception e){
            log.error("getExpire redis error, key : {}", key);
        }
        return null;
    }

    @Override
    public Long increment(String key, long l) {
        try {
            return jedisCluster.incrBy(key,l);
        } catch (Exception e) {
            log.error("incr redis error, key : {}", key);
        }
        return null;
    }
}
