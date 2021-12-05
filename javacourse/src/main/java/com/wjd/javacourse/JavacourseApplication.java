package com.wjd.javacourse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({"com.wjd.javacourse.*"})
@SpringBootApplication
public class JavacourseApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavacourseApplication.class, args);
	}

}
