package com.wjd.javacourse.week05;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author: WJD
 * @Description:
 * @Created: 2021/12/5
 */
public class ConfigTest {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(StudentConfig.class);
        Student student = context.getBean("oldwang",Student.class);
        System.out.println("desc:" + student.desc());
    }
}
