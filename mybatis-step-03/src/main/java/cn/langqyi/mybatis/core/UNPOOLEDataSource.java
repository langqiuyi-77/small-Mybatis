package cn.langqyi.mybatis.core;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * 在Mybatis中支持3种类型的数据源：
 * 1. UNPOOLED
 * 2. POOLED
 * 3. ...
 * 在这里我们只实现简单的UNPOOLED
 */
public class UNPOOLEDataSource implements DataSource {

    private String url;
    private String username;
    private String password;

    public UNPOOLEDataSource(String dirver, String url, String username, String password) {
        try {
            Class.forName(dirver);  //注册驱动，开始就注册好，之后不需要driver
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Connection getConnection() throws SQLException {
        // 2. 获得连接
        return DriverManager.getConnection(url, username, password);

    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}
