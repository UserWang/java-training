package com.wjd.javacourse.week05;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author: WJD
 * @Description:
 * @Created: 2021/12/4
 */
@Data
@Component
public class Student {
    private String name;
    private Integer age;

    public String desc(){
        return "我的名字叫" + name + ";我今年" + age + "岁";
    }
}
