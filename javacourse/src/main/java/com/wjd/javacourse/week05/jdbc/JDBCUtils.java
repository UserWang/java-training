package com.wjd.javacourse.week05.jdbc;

import java.io.File;
import java.io.FileInputStream;
import java.sql.*;
import java.util.Properties;

/**
 * @author: WJD
 * @Description:
 * @Created: 2021/12/5
 */
public class JDBCUtils {
    private static String driverName;
    private static String url;
    private static String user;
    private static String password;

    static{
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(new File("src/main/resources/jdbc.properties")));
            driverName = properties.getProperty("driver");
            url = properties.getProperty("url");
            user = properties.getProperty("user");
            password = properties.getProperty("password");
            Class.forName(driverName);
        }catch(Exception e){
            throw new RuntimeException("获取数据库连接异常");
        }
    }

    public static Connection getConnection(){
        Connection con = null;
        try {
            con = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("获取数据库连接异常");
        }
        return con;
    }

    public static void closeResource(Connection con, Statement sta, ResultSet rs) {
        try {
            if(con!=null) {
                con.close();
            }
            if(sta!=null) {
                sta.close();
            }
            if(rs!=null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
