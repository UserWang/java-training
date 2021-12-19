package com.wjd.javacourse.week07.dbrouter.annotation;

import com.wjd.javacourse.week07.dbrouter.aspect.MasterSlaveAspect;
import com.wjd.javacourse.week07.dbrouter.config.DataSourceConfiguration;
import com.wjd.javacourse.week07.dbrouter.config.MvcFilterConfig;
import com.wjd.javacourse.week07.dbrouter.interceptor.GetReqDefaultDBInterceptor;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;


@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({DataSourceConfiguration.class, MvcFilterConfig.class, GetReqDefaultDBInterceptor.class, MasterSlaveAspect.class})
public @interface EnableMasterSlaveDB {
}
