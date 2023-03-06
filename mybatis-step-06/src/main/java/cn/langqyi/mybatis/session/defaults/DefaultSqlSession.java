package cn.langqyi.mybatis.session.defaults;

import cn.langqyi.mybatis.binding.MapperRegistry;
import cn.langqyi.mybatis.mapping.MappedStatement;
import cn.langqyi.mybatis.session.SqlSession;

import java.util.Map;

public class DefaultSqlSession implements SqlSession {

    /**
     * 映射器注册机 -> 处理动态生成接口代码
     */
    private MapperRegistry mapperRegistry;

    /**
     * 这个是sql的实体类集合 -> 是通过pacagename+id 映射 MappedStatement(存储sql信息的对象) 执行sql使用得到
     */
    private Map<String, MappedStatement> mappedStatements;

    public DefaultSqlSession(MapperRegistry mapperRegistry, Map<String, MappedStatement> mappedStatements) {
        this.mapperRegistry = mapperRegistry;
        this.mappedStatements = mappedStatements;
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
        return mapperRegistry.getMapper(type, this);
    }

    @Override
    public String getSqltype(String sqlid) {
        return mappedStatements.get(sqlid).getSqlType();
    }

}
