package com.wjd.javacourse.week08.gateway;

import com.wjd.javacourse.week08.bean.User;

import java.util.List;

/**
 * @author: WJD
 * @Description:
 * @Created: 2021/12/26
 */
public interface UserGateway {
    Long insert(final User user) ;

    Long update(final User user) ;

    List<User> list();

    User getUserByUid(final Integer uid);
}
