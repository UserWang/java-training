package com.wjd.javacourse.week08.util;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.sql.*;
import java.util.Properties;

/**
 * @author: WJD
 * @Description:
 * @Created: 2021/12/5
 */
public class NewJDBCUtils {
    private static String driverName;
    private static String url01;
    private static String url00;
    private static String user;
    private static String password;

    static {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(new File("src/main/resources/jdbc.properties")));
            driverName = properties.getProperty("driver");
            url01 = properties.getProperty("url01");
            url00 = properties.getProperty("url00");
            user = properties.getProperty("user");
            password = properties.getProperty("password");
            Class.forName(driverName);
        } catch (Exception e) {
            throw new RuntimeException("获取数据库连接异常");
        }
    }

    public static DataSource createDataSource(final String dataSourceName) {
        HikariDataSource result = new HikariDataSource();
        result.setDriverClassName("com.mysql.jdbc.Driver");
        result.setJdbcUrl(dataSourceName.equals("url01") ? url01 : url00);
        result.setUsername(user);
        result.setPassword(password);
        return result;
    }

    public static Connection getConnection() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(url01, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("获取数据库连接异常");
        }
        return con;
    }

    public static void closeResource(Connection con, Statement sta, ResultSet rs) {
        try {
            if (con != null) {
                con.close();
            }
            if (sta != null) {
                sta.close();
            }
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
