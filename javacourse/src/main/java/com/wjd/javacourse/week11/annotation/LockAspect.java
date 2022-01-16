package com.wjd.javacourse.week11.annotation;


import com.wjd.javacourse.week11.ICacheManager;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Aspect
@Component
public class LockAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(LockAspect.class);

    @Resource
    private ICacheManager cacheManager;

    @Pointcut("@annotation(com.wjd.javacourse.week11.annotation.CacheLock)")
    public void cacheLockPointcut(){}

    @Around("cacheLockPointcut()")
    public Object Interceptor(ProceedingJoinPoint pjp) throws Throwable {

        MethodSignature signature = (MethodSignature) pjp.getSignature();
        //获取被拦截的方法
        Method method = signature.getMethod();
        CacheLock cacheLock = method.getAnnotation(CacheLock.class);
        if(null == cacheLock){
            return pjp.proceed();
        }
        String key = cacheLock.key();
        if(StringUtils.isBlank(key)) {
            //获得方法中参数的注解
            Annotation[][] annotations = method.getParameterAnnotations();
            Object[] args = pjp.getArgs();
            Object lockedObject = getLockedObject(annotations, args);
            key = lockedObject.toString();
        }
        //新建一个锁
        RedisLock lock = new RedisLock(cacheManager,cacheLock.prefix(), key);
        //加锁
        boolean result = lock.lock(cacheLock.timeOut(), cacheLock.expireTime());
        LOGGER.info("加锁结果:{},LOCK KEY:{}",result,lock.getLockKey());
        //取锁失败
        if(!result){
            throw new Exception("GET LOCK FAIL");
        }
        try{
            //加锁成功，执行方法
            return pjp.proceed();
        }finally{
            //释放锁
            lock.unlock(System.currentTimeMillis());
        }
    }

    private Object getLockedObject(Annotation[][] annotations,Object[] args) throws Exception {
        if(null == args || args.length == 0){
            throw new Exception("方法参数为空，没有被锁定的对象");
        }

        if(null == annotations || annotations.length == 0){
            throw new Exception("没有被注解的参数");
        }
        int index = -1;
        for(int i = 0;i < annotations.length;i++){
            for(int j = 0;j < annotations[i].length;j++){
                if(annotations[i][j] instanceof LockComplexObject){
                    index = i;
                    try {
                        Field field = args[i].getClass().getDeclaredField(((LockComplexObject)annotations[i][j]).field());
                        field.setAccessible(true);
                        return field.get(args[index]);
                    } catch (NoSuchFieldException | SecurityException e) {
                        throw new Exception("注解对象中没有该属性" + ((LockComplexObject)annotations[i][j]).field());
                    } catch (IllegalAccessException e) {
                        throw new Exception("获取锁对象失败" + ((LockComplexObject)annotations[i][j]).field());
                    }
                }
                if(annotations[i][j] instanceof LockObject){
                    index = i;
                    break;
                }
            }
            if(index != -1){
                break;
            }
        }

        if(index == -1){
            throw new Exception("请指定被锁定参数");
        }
        return args[index];
    }
}
