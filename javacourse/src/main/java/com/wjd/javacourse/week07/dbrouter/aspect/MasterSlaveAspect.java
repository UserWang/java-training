package com.wjd.javacourse.week07.dbrouter.aspect;

import com.wjd.javacourse.week07.dbrouter.DbContextHolder;
import com.wjd.javacourse.week07.dbrouter.annotation.MasterDB;
import com.wjd.javacourse.week07.dbrouter.annotation.SlaveDB;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class MasterSlaveAspect implements Ordered {

    public static final Logger logger = LoggerFactory.getLogger(MasterSlaveAspect.class);

    /**
     * 切换到主库
     *
     * @param proceedingJoinPoint
     * @param masterDB
     * @return
     * @throws Throwable
     */
    @Around("@annotation(masterDB)")
    public Object proceed(ProceedingJoinPoint proceedingJoinPoint, MasterDB masterDB) throws Throwable {
        try {
            logger.debug("set database connection to master only");
            DbContextHolder.setDbType(DbContextHolder.DbType.MASTER);
            Object result = proceedingJoinPoint.proceed();
            return result;
        } finally {
            DbContextHolder.clearDbType();
            logger.info("restore database connection");
        }
    }


    /**
     * 切换到从库
     *
     * @param proceedingJoinPoint
     * @param slaveDB
     * @return
     * @throws Throwable
     */
    @Around("@annotation(slaveDB)")
    public Object proceed(ProceedingJoinPoint proceedingJoinPoint, SlaveDB slaveDB) throws Throwable {
        try {
            logger.debug("set database connection to slave only");
            DbContextHolder.setDbType(DbContextHolder.DbType.SLAVE);
            Object result = proceedingJoinPoint.proceed();
            return result;
        } finally {
            DbContextHolder.clearDbType();
            logger.info("restore database connection");
        }
    }
    @Override
    public int getOrder() {
        return 0;
    }
}

