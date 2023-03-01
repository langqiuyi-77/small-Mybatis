package cn.langqyi.mybatis.core;

import javax.sql.DataSource;

public class sqlSessionFactory {
    /*
        属性应该有事务管理器，数据源，sql对象的映射
     */
    private Transaction transaction;

    public sqlSessionFactory() {
    }

}
