package com.wjd.javacourse.week05;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author: WJD
 * @Description:
 * @Created: 2021/12/5
 */
public class XMLTest {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("week05.xml");
        Klass klass = context.getBean("Klass", Klass.class);
        System.out.println("klass-id:" + klass.getId());
        System.out.println("klass-num:" + klass.getNum());
        System.out.println("klass-studentCount:" + klass.getStudentCount());
    }
}
