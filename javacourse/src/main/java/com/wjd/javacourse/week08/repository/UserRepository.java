package com.wjd.javacourse.week08.repository;

import com.wjd.javacourse.week08.bean.User;

import java.util.List;

/**
 * @author: WJD
 * @Description:
 * @Created: 2021/12/26
 */
public interface UserRepository {
    Long insert(final User user) ;

    Long update(final User user) ;

    List<User> list();

    User getUserByUid(final Integer uid);
}
