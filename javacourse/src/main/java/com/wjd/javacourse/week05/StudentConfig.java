package com.wjd.javacourse.week05;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: WJD
 * @Description:
 * @Created: 2021/12/4
 */
@Configuration
public class StudentConfig {

    @Bean
    public Student oldwang() {
        Student student = new Student();
        student.setName("老王");
        student.setAge(18);
        return student;
    }
}
