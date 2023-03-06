package cn.langqyi.mybatis.session.defaults;

import cn.langqyi.mybatis.binding.MapperRegistry;
import cn.langqyi.mybatis.mapping.MappedStatement;
import cn.langqyi.mybatis.session.SqlSession;
import cn.langqyi.mybatis.session.SqlSessionFactory;

import java.util.Map;

public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private final MapperRegistry mapperRegistry;
    private Map<String, MappedStatement> mappedStatements;

    public DefaultSqlSessionFactory(MapperRegistry mapperRegistry, Map<String, MappedStatement> mappedStatements) {
        this.mapperRegistry = mapperRegistry;
        this.mappedStatements = mappedStatements;
    }

    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(mapperRegistry, mappedStatements);
    }
}
