package com.wjd.javacourse.week07;/**
 * @author: WJD
 * @Description:
 * @Created: 2021/12/18
 */

import com.wjd.javacourse.week05.jdbc.JDBCUtils;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author: WJD
 * @Description:
 * @Created: 2021/12/18
 */
public class Week07 {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        insert();
        long end = System.currentTimeMillis();
        System.out.println("插入数据完成，执行时长:"+(end - start));

    }

    public static void insert()  {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "insert into customer_login (login_name,password)value (?,?)";
            pstmt = conn.prepareStatement(sql);
            for (int i = 0; i < 1000000; i++) {
                pstmt.setString(1,"login_name"+i);
                pstmt.setString(2,"123456");
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            System.out.println("1000000条数据插入完成");
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            if (pstmt != null){
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
