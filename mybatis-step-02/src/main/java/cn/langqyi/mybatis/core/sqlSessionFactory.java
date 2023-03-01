package cn.langqyi.mybatis.core;

import javax.sql.DataSource;
import java.util.Map;

public class sqlSessionFactory {
    /*
        属性应该有事务管理器，数据源，sql对象的映射
     */
    private Transaction transaction;
    private Map<String, MappedStatement> mappedStatements;

    public sqlSessionFactory(Transaction transaction, Map<String, MappedStatement> mappedStatements) {
        this.transaction = transaction;
        this.mappedStatements = mappedStatements;
    }
}
