package com.wjd.javacourse.week08.repository;/**
 * @author: WJD
 * @Description:
 * @Created: 2021/12/26
 */

import com.wjd.javacourse.week08.bean.User;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

/**
 * @author: WJD
 * @Description:
 * @Created: 2021/12/26
 */
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final DataSource dataSource;

    public UserRepositoryImpl(final DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public Long insert(User user) {
        String sql = "INSERT INTO user_info (uid, user_name, phone) VALUES (?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, user.getUid());
            preparedStatement.setString(2, user.getUserName());
            preparedStatement.setString(3, user.getPhone());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            System.out.println("insert success,ResultSet:"+resultSet);
            while (resultSet.next()) {
                return 1l;
            }
        } catch (Exception e) {
            System.out.println("error");
            e.printStackTrace();
        }
        return 0l;
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
