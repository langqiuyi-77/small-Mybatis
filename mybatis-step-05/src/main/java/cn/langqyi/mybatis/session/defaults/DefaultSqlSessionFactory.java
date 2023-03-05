package cn.langqyi.mybatis.session.defaults;

import cn.langqyi.mybatis.binding.MapperRegistry;
import cn.langqyi.mybatis.mapping.MappedStatement;
import cn.langqyi.mybatis.session.SqlSession;
import cn.langqyi.mybatis.session.SqlSessionFactory;

import java.util.Map;

public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private final MapperRegistry mapperRegistry;
    private Map<String, MappedStatement> mappedStatements;

    public DefaultSqlSessionFactory(MapperRegistry mapperRegistry) {
        this.mapperRegistry = mapperRegistry;
    }

    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(mapperRegistry, mappedStatements);
    }
}
