package com.wjd.javacourse.week05.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author: WJD
 * @Description:
 * @Created: 2021/12/5
 */
public class PreparedStatementPra {
    public static void main(String[] args) throws SQLException {
        Connection con = JDBCUtils.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        //查询
        String sql1  = "SELECT * FROM DEPT WHERE DEPTNO = ?";
        ps = con.prepareStatement(sql1);
        ps.setObject(1, "10");
        qry(ps,rs);
        //增加
        String sql2 = "INSERT INTO DEPT VALUES(?,?,?)";
        ps = con.prepareStatement(sql2);
        ps.setObject(1, "20");
        ps.setObject(2, "XYG");
        ps.setObject(3, "beijing");
        System.out.println("插入执行结果:"+update(ps,sql2));
        //更新
        String sql3 = "UPDATE DEPT SET loc=? WHERE DEPTNO = ?";
        ps = con.prepareStatement(sql3);
        ps.setObject(1, "shanghai");
        ps.setObject(2, "20");
        System.out.println("更新执行结果:"+update(ps,sql3));
        //删除
        String sql4 = "DELETE FROM DEPT WHERE DEPTNO = ?";
        ps = con.prepareStatement(sql4);
        ps.setObject(1, "20");
        System.out.println("删除执行结果:"+update(ps,sql4));
        JDBCUtils.closeResource(con, ps, rs);

    }

    /**
     * 查询
     * @param sta
     * @param rs
     * @throws SQLException
     */
    private static void qry(PreparedStatement sta, ResultSet rs) throws SQLException {
        rs = sta.executeQuery();
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
    private static int update(PreparedStatement sta, String sql) throws SQLException {
        return sta.executeUpdate();
    }


}
