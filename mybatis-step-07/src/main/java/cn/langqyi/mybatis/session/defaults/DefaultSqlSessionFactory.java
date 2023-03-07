package cn.langqyi.mybatis.session.defaults;

import cn.langqyi.mybatis.binding.MapperRegistry;
import cn.langqyi.mybatis.mapping.MappedStatement;
import cn.langqyi.mybatis.session.Configuration;
import cn.langqyi.mybatis.session.SqlSession;
import cn.langqyi.mybatis.session.SqlSessionFactory;

import java.util.Map;

public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private final Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(configuration);
    }
}
