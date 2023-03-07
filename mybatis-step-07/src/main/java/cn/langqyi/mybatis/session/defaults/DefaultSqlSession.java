package cn.langqyi.mybatis.session.defaults;

import cn.langqyi.mybatis.binding.MapperRegistry;
import cn.langqyi.mybatis.mapping.MappedStatement;
import cn.langqyi.mybatis.session.Configuration;
import cn.langqyi.mybatis.session.SqlSession;

import java.util.Map;

public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public int insert(String sqlid, Object parameter) {
        System.out.println("DefaultSqlSession的insert方法" + " sqlId:" + sqlid);
        return 1;
    }

    @Override
    public int delete(String sqlid, Object parameter) {
        System.out.println("DefaultSqlSession的delete方法" + " sqlId:" + sqlid);
        return 1;
    }

    @Override
    public <T> T selectOne(String sqlid) {
        System.out.println("DefaultSqlSession的selectOne方法" + " sqlId:" + sqlid);
        return null;
    }

    @Override
    public <T> T selectOne(String sqlid, Object parameter) {
        System.out.println("DefaultSqlSession的selectOne2方法" + " sqlId:" + sqlid);
        return null;
    }


    @Override
    public int update(String sqlid, Object parameter) {
        System.out.println("DefaultSqlSession的selectOne2方法" + " sqlId:" + sqlid);
        return 1;
    }

    @Override
    public <T> T getMapper(Class<T> type) {
        return configuration.getMapper(type, this);
    }

    @Override
    public String getSqltype(String sqlid) {
        return configuration.getSqltype(sqlid);
    }

}
