package com.wjd.javacourse;/**
 * @author: WJD
 * @Description:
 * @Created: 2021/12/26
 */

import com.wjd.javacourse.week08.bean.User;
import com.wjd.javacourse.week08.gateway.UserGateway;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author: WJD
 * @Description:
 * @Created: 2021/12/26
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = JavacourseApplicationTests.class)
public class UserGatewayTest {

    @Resource
    private UserGateway userGatewayImpl;

    @Test
    public void testInsert() throws Exception {

        User user = new User();
        user.setUid(123);
        user.setUserName("老王");
        user.setPhone("18510001000");
        userGatewayImpl.insert(user);
    }
}
