package cn.langqyi.mybatis.session;

import cn.langqyi.mybatis.binding.MapperRegistry;
import cn.langqyi.mybatis.mapping.MappedStatement;
import cn.langqyi.mybatis.transaction.Transaction;

import java.util.Map;

/**
 * 串联整个程序的存储所有配置信息的配置类
 */
public class Configuration {

    /**
     * 事务管理器 -> 事务管理器和数据源是组合的关系
     */
    private Transaction transaction;

    /**
     * 映射器注册机 -> 处理动态生成接口代码
     */
    private MapperRegistry mapperRegistry;

    /**
     * 这个是sql的实体类集合 -> 是通过pacagename+id 映射 MappedStatement(存储sql信息的对象) 执行sql使用得到
     */
    private Map<String, MappedStatement> mappedStatements;

    public Configuration(Transaction transaction, MapperRegistry mapperRegistry, Map<String, MappedStatement> mappedStatements) {
        this.transaction = transaction;
        this.mapperRegistry = mapperRegistry;
        this.mappedStatements = mappedStatements;
    }

    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        return mapperRegistry.getMapper(type, sqlSession);
    }


    public String getSqltype(String sqlid) {
        return mappedStatements.get(sqlid).getSqlType();
    }
}
