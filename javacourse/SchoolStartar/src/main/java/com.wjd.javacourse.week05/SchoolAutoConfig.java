package com.wjd.javacourse.week05;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author: WJD
 * @Description:
 * @Created: 2021/12/4
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(SchoolProperties.class)
public class SchoolAutoConfig {

    @Resource
    private SchoolProperties schoolProperties;

    @ConditionalOnMissingBean(SchoolService.class) //当容器中没有 SchoolService 时生效
    @Bean
    public SchoolService school() {
        SchoolService schoolService = new SchoolService();
        return schoolService;
    }

}
