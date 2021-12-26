package com.wjd.javacourse.week08;/**
 * @author: WJD
 * @Description:
 * @Created: 2021/12/26
 */

import com.wjd.javacourse.week08.bean.User;
import com.wjd.javacourse.week08.gateway.UserGateway;
import com.wjd.javacourse.week08.gateway.UserGatewayImpl;
import com.wjd.javacourse.week08.util.ShardingDatabasesAndTableConfiguration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Random;

/**
 * @author: WJD
 * @Description:
 * @Created: 2021/12/26
 */
public class MainClass {

    public static void main(String[] args) throws SQLException {
        DataSource dataSource = ShardingDatabasesAndTableConfiguration.getDataSource();
        System.out.println("----dataSource---");
        System.out.println(dataSource);
        UserGateway userGateway = new UserGatewayImpl(dataSource);
        User user = new User();
        int uid = new Random().nextInt(10000);
        System.out.println("uid:" + uid);
        user.setUid(uid);
        user.setUserName("老王");
        user.setPhone("18510001000");
        userGateway.insert(user);
    }
}
