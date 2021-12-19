package com.wjd.javacourse.week07.dbrouter.config;

import com.wjd.javacourse.week07.dbrouter.interceptor.GetReqDefaultDBInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;

@Configuration
public class MvcFilterConfig extends WebMvcConfigurerAdapter {
    @Value("${spring.datasource.druid.getFromSlave:false}")
    private Boolean getFromSlave;

    @Resource
    private GetReqDefaultDBInterceptor getReqDefaultDBInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (getFromSlave != null && getFromSlave) {
            registry.addInterceptor(getReqDefaultDBInterceptor);
        }
    }


}

