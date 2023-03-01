package cn.langqyi.mybatis.core;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * JDBC事务管理器;
 * 要想实现commit()和rollback()方法就必须要和具体的connection绑定
 * 1个connection就需要一个事务进行控制
 * 所以将datasource放入到事务管理器中
 * ....ε=(´ο｀*)))唉不知道了
 */
public class JDBCTransaction implements Transaction {

    private DataSource dataSource;
    private Connection conn;    //控制事务的connection
    private boolean AutoCommit = false; //是否打开事务; 默认false不打开事务

    public JDBCTransaction(DataSource dataSource, boolean AutoCommit) {
        this.AutoCommit = AutoCommit;
        this.dataSource = dataSource;
    }

    public JDBCTransaction(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void commit() {
        if (conn != null) {
            try {
                conn.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void rollback() {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void openConnection() {
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(AutoCommit); //是否开启事务
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        try {
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Connection getConnection() {
        if (conn == null)
            openConnection();
        return conn;
    }


}
