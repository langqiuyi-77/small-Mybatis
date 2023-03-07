package cn.langqyi.mybatis.transaction;

import java.sql.Connection;

/**
 * 事务管理器：是一个接口，在Mybatis中支持2种不同的事务管理器
 * 1. JDBC管理器
 * 2. MANAGED管理器
 */
public interface Transaction {

    void commit();

    void rollback();

    /*
        除了提供基本的事务控制的方法之外；
        因为数据源也封装到了事务之中
        所以还需要提供对数据源的操作方法接口
     */

    /**
     * 从数据源中获取新的connection连接
     */
    void openConnection();

    /**
     * 关闭connection
     */
    void close();

    /**
     * 因为需要在sqlSession中实现真正的对connection的使用
     * 执行sql，所以我们需要getConnection的方法
     */
    Connection getConnection();
}
