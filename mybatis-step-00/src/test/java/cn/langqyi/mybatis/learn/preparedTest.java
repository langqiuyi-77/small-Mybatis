package cn.langqyi.mybatis.learn;

import cn.langqyi.mybatis.learn.pojo.Car;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;


import java.sql.*;

/**
 * 这是手写Mybatis之前对原本的JDBC的流程
 * 以及要仿照实现的Mybatis的使用
 */
public class preparedTest {

    /**
     * 原始jdbc代码的书写流程
     */
    @Test
    public void jdbcTest() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // 1. 注册驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            // 2. 获得连接
            String url = "jdbc:mysql://localhost:3306/test";
            conn = DriverManager.getConnection(url, "root", "19821029");
//            Connection conn1 = DriverManager.getConnection(url, "root", "19821029");
//            System.out.println("conn: " + conn + ";" + "conn1: " + conn1);
//            conn1.close();
//            System.out.println("conn: " + conn + ";" + "conn1: " + conn1);
            // 3. 获取sql语句
            String sql = "select * from t_car where id = ?";
            // 4. 获取预处理statement
            stmt = conn.prepareStatement(sql);
            // 5. 设置参数，参数index从1开始
            stmt.setInt(1, 1);
            // 6. 执行sql语句
            rs = stmt.executeQuery();
            // 7. 处理结果集
            while (rs.next()) {
                // 获得一行数据
                System.out.println(rs.getInt("id") + ", "
                        + rs.getString("car_num") + ", " + rs.getString("brand")
                        + rs.getDouble("guide_price") + ", " + rs.getString("produce_time")
                        + rs.getString("car_type"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 8. 释放资源
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 使用Mybatis的过程; 手写框架之前可以参考一下mybatis的客户端程序，
     * 来逆推需要的类
     */
    @Test
    public void mybatisTest() {
        SqlSession sqlSession = null;
        try {
            // 1. 创建sqlsessionfactoryBuilder对象
            SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
            // 2. 创建SqlSessionFactory对象
            SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(Resources.getResourceAsStream("mybatis-config.xml"));
            // 3. 创建sqlsession对象
            sqlSession = sqlSessionFactory.openSession();
            // 4. 执行sql
            Car car = new Car(null, "111", "宝马x7", 70.3, "2010-10-11", "燃油车");
            int count = sqlSession.insert("insertCar", car);
            System.out.println("更新了几条数据：" + count);
            // 5. 提交
            sqlSession.commit();
        } catch (Exception e) {
            // 回滚
            if (sqlSession != null) {
                sqlSession.rollback();
            }
            e.printStackTrace();
        } finally {
            // 6. 关闭
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
    }
}
