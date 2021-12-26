package com.wjd.javacourse.week08.bean;/**
 * @author: WJD
 * @Description:
 * @Created: 2021/12/26
 */

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: WJD
 * @Description:
 * @Created: 2021/12/26
 */
@Data
public class User {

    private Long id;
    private Integer uid;
    private String userName;
    /**
     * 证件类型：1 身份证，2 军官证，3 护照
     */
    private Integer idcardType;

    private String idcard;

    private String phone;

    private String email;

    private Integer gender;

    private BigDecimal userMoney;

    private Date registerTime;
}
