package com.wjd.javacourse.week05;

import lombok.extern.slf4j.Slf4j;
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
public class SchoolService {

    @Resource
    private SchoolProperties schoolProperties;

    public String schoolName() {
        return schoolProperties.getName();
    }

    public Integer klassCount() {
        return schoolProperties.getKlassCount();
    }

}
