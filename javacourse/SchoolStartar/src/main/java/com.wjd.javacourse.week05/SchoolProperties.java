package com.wjd.javacourse.week05;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: WJD
 * @Description:
 * @Created: 2021/12/5
 */
@Data
@ConfigurationProperties(prefix = "week05.school")
public class SchoolProperties {
    private String name;

    private int klassCount;
}
