package com.wjd.javacourse.week08.gateway;/**
 * @author: WJD
 * @Description:
 * @Created: 2021/12/26
 */

import com.wjd.javacourse.week08.bean.User;
import com.wjd.javacourse.week08.repository.UserRepository;
import com.wjd.javacourse.week08.repository.UserRepositoryImpl;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

/**
 * @author: WJD
 * @Description:
 * @Created: 2021/12/26
 */
@Component
public class UserGatewayImpl implements UserGateway {

    private final UserRepository userRepository;

    public UserGatewayImpl(DataSource dataSource) {
        userRepository = new UserRepositoryImpl(dataSource);
    }

    @Override
    public Long insert(User user) {
        return userRepository.insert(user);
    }

    @Override
    public Long update(User user) {
        return null;
    }

    @Override
    public List<User> list() {
        return null;
    }

    @Override
    public User getUserByUid(Integer uid) {
        return null;
    }
}
