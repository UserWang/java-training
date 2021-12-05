package com.wjd.javacourse.week05.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author: WJD
 * @Description:
 * @Created: 2021/12/5
 */
public class JDBCTest {

    public static void main(String[] args) throws SQLException {
        Connection con = JDBCUtils.getConnection();
        Statement statement = con.createStatement();
        ResultSet resultSet = null;
        String sql1 = "SELECT * FROM DEPT";
        qry(statement,sql1,resultSet);
        String sql2 = "INSERT INTO DEPT VALUES('20','XYG','shanghai')";
        System.out.println("插入执行结果:"+update(statement,sql2));
//更新
        String sql3 = "UPDATE DEPT SET loc='beijing' WHERE DEPTNO = '20'";
        System.out.println("更新执行结果:"+update(statement,sql3));
        //删除
        String sql4 = "DELETE FROM DEPT WHERE DEPTNO = '20'";
        System.out.println("删除执行结果:"+update(statement,sql4));
        JDBCUtils.closeResource(con, statement, resultSet);
    }

    /**
     * 查询
     * @param sta
     * @param sql
     * @param rs
     * @throws SQLException
     */
    private static void qry(Statement sta,String sql,ResultSet rs) throws SQLException {
        rs = sta.executeQuery(sql);
        while(rs.next()) {
            System.out.println(rs.getObject("deptno"));
        }
    }
    /**
     * 增删改
     * @param sta
     * @param sql
     * @return
     * @throws SQLException
     */
    private static int update(Statement sta,String sql) throws SQLException {
        return sta.executeUpdate(sql);
    }

}
